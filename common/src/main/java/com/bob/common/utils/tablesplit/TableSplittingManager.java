package com.bob.common.utils.tablesplit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 分表业务实现抽象
 *
 * @author wb-jjb318191
 * @create 2018-04-16 9:48
 */
@Component
public class TableSplittingManager implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableSplittingManager.class);

    private Gson gson;

    private int splitInterval;

    private TableSplittingService splittingService;

    private volatile int latestOrder = 0;

    private static final ConcurrentMap<Integer, SplitTable> ORDER_TO_TABLE_MAPPINGS = new ConcurrentHashMap<Integer, SplitTable>();
    /**
     * 插入数据的属性名称 >> 属性类型的映射,目的是为了方便对字符串字段值前后加引号
     */
    private static final ConcurrentMap<Class, Map<String, Class<?>>> FIELD_TO_TYPE_MAPPINGS = new ConcurrentHashMap<Class, Map<String, Class<?>>>();

    /**
     * 查询所有分表,记录表名和数据起始,结束时间的映射
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(splittingService, "[splittingService] 属性不能为空");

        splitInterval = splittingService.getSplitIntervalInMonth();
        gson = new GsonBuilder().setDateFormat(splittingService.getDateFormatPattern()).create();

        do {
            try {
                List<Date> endpoint = splittingService.selectTimeEndpoint(latestOrder);
                String tableName = splittingService.generateTableNameByOrder(latestOrder);
                ORDER_TO_TABLE_MAPPINGS.put(latestOrder++, buildSplitTable(tableName, endpoint));
            } catch (Exception e) {
                latestOrder--;
                break;
            }
        } while (true);
    }

    /**
     * 构建Insert参数集合,table将存放表名
     * 其他的以属性名称为key,属性值为value
     * 非数字类型的value两端加上单引号
     *
     * @param record
     * @return
     */
    public Map<String, Object> buildInsertParamMap(Object record) {
        Map<String, Object> paramMap = buildParamMap(record);
        paramMap.put("table", getInsertTable(record));
        return paramMap;
    }

    /**
     * 创建查询时的参数Map集合
     * 查询可能涉及多张表,用tables属性存储表名集合
     *
     * @param query
     * @return
     */
    public Map<String, Object> buildQueryParamMap(Object query) {
        Map<String, Object> paramMap = buildParamMap(query);
        paramMap.put("tables", getSelectedTables(query));
        return paramMap;
    }

    /**
     * 获取当前待插入数据的表名
     *
     * @param record
     * @return
     */
    private String getInsertTable(Object record) {
        int order = latestOrder;
        Date gmtModified = splittingService.extractInsertBasis(record);
        SplitTable latest = ORDER_TO_TABLE_MAPPINGS.get(order);
        if (gmtModified.before(latest.getEndTime())) {
            return latest.getTableName();
        }
        // 创建新的分表
        if (order == latestOrder) {
            synchronized (this) {
                if (order == latestOrder) {
                    createNewTable(latest.getEndTime());
                }
            }
        }
        return getInsertTable(record);
    }

    /**
     * 获取当前查询时间对应的分表
     * 若未指定查询时间区间,则默认查询所有分表
     *
     * @param query
     * @return
     */
    private List<String> getSelectedTables(Object query) {
        List<Date> timeEndpoint = splittingService.getQueryTimeInterval(query);
        Date startDate = timeEndpoint.get(0);
        Date endDate = timeEndpoint.get(1);
        List<String> tables = new ArrayList<String>(ORDER_TO_TABLE_MAPPINGS.size());
        for (Entry<Integer, SplitTable> entry : ORDER_TO_TABLE_MAPPINGS.entrySet()) {
            SplitTable table = entry.getValue();
            boolean tooLater = startDate != null && startDate.after(table.getEndTime());
            boolean tooEarly = endDate != null && endDate.before(table.getStartTime());
            if (!tooEarly && !tooLater) {
                tables.add(splittingService.generateTableNameByOrder(entry.getKey()));
            }
        }
        return tables;
    }

    /**
     * 创建新的分表
     *
     * @param start 起始时间
     */
    private void createNewTable(Date start) {

        Thread thread = new Thread() {

            // 当Service层环绕事务时,在Service方法内创建表,事务未提交,新表还未实际生成。
            // 所以以异步形式跳出当前事务,非事务状态下创建表及设置索引等操作。
            @Override
            public void run() {
                splittingService.createSplitTable(++latestOrder);
            }
        };

        thread.start();
        try {
            thread.join();
            SplitTable splitTable = new SplitTable(splittingService.generateTableNameByOrder(latestOrder), start, computeEndTime(start));
            ORDER_TO_TABLE_MAPPINGS.put(latestOrder, splitTable);
        } catch (InterruptedException e) {

        }
    }

    /**
     * 生成分表内置对象
     *
     * @param tableName
     * @param endpoint
     * @return
     */
    private SplitTable buildSplitTable(String tableName, List<Date> endpoint) {
        if (endpoint.isEmpty()) {
            return new SplitTable(tableName, new Date(), computeEndTime(new Date()));
        } else {
            Date expectEnd = computeEndTime(endpoint.get(0));
            Date actualEnd = endpoint.get(1);
            // 如果分表内最近更新时间在时间区间之内,调整分表的结束时间
            if (actualEnd.before(expectEnd)) {
                actualEnd = expectEnd;
            }
            return new SplitTable(tableName, endpoint.get(0), actualEnd);
        }
    }

    /**
     * 根据起始时间计算结束时间
     *
     * @param startTime
     * @return
     */
    private Date computeEndTime(Date startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, splitInterval);
        return calendar.getTime();
    }

    /**
     * 构建待插入数据的参数列表,主要是需要对字符串类型,时间类型的字段前后加上引号
     *
     * @param object
     * @return
     */
    private Map<String, Object> buildParamMap(Object object) {
        Map<String, Object> paramMap = new JSONObject(gson.toJson(object)).toMap();
        Class<?> clazz = object.getClass();
        Map<String, Class<?>> name2TypeTable = FIELD_TO_TYPE_MAPPINGS.get(clazz);
        if (name2TypeTable == null) {
            name2TypeTable = buildFieldTypeMapping(clazz);
            FIELD_TO_TYPE_MAPPINGS.putIfAbsent(clazz, name2TypeTable);
        }
        for (Entry<String, Object> entry : paramMap.entrySet()) {
            String fieldName = entry.getKey();
            Class<?> fieldType = name2TypeTable.get(fieldName);
            // 对String和Date格式化后的字符串,在首尾两端添加引号
            if (String.class == fieldType || Date.class == fieldType) {
                entry.setValue("\'" + entry.getValue() + "\'");
            }
            //对Boolean类型转换为数值
            if (Boolean.class == fieldType) {
                entry.setValue((Boolean)entry.getValue() ? 1 : 0);
            }
            //如果当前属性是集合或者数组,取回属性值,因为此属性可能需要在XML内做forEach操作
            if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType) || fieldType.isArray()) {
                try {
                    Field field = ReflectionUtils.findField(clazz, fieldName);
                    field.setAccessible(true);
                    entry.setValue(field.get(object));
                } catch (Exception e) {

                }
            }
        }
        return paramMap;
    }

    /**
     * 构建指定类型的属性名称和属性类型的映射
     *
     * @param clazz
     * @return
     */
    private Map<String, Class<?>> buildFieldTypeMapping(Class<?> clazz) {
        Set<Field> fields = new HashSet<Field>();
        do {
            fields.addAll(new HashSet<Field>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        Map<String, Class<?>> name2TypeTable = new HashMap<String, Class<?>>();
        for (Field field : fields) {
            name2TypeTable.put(field.getName(), field.getType());
        }
        return name2TypeTable;
    }

    public void setSplittingService(TableSplittingService splittingService) {
        this.splittingService = splittingService;
    }

    /**
     * 分表的基础数据信息
     */
    static class SplitTable {

        private String tableName;
        private Date startTime;
        private Date endTime;

        public SplitTable(String tableName, Date startTime, Date endTime) {
            this.tableName = tableName;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }

}
