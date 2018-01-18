package com.bob.config.root.mybatis.readsepwrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

/**
 * 动态数据源配置
 *
 * @author wb-jjb318191
 * @create 2018-01-16 9:56
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    private final AtomicLong readRequestedTime = new AtomicLong(0);

    private DataSource writeDataSource;
    private List<DataSource> readDataSources = new ArrayList<DataSource>();

    public DynamicDataSource(Map<String, DataSource> dataSources) {
        Assert.notEmpty(dataSources, "至少需要定义一个数据源");
        //若只有一个数据源，则指定其为写数据源
        if (dataSources.size() == 1) {
            LOGGER.info("当前容器内只定义了一个数据源，指定其为写数据源");
            writeDataSource = dataSources.values().iterator().next();
            return;
        }
        for (Entry<String, DataSource> entry : dataSources.entrySet()) {
            if (entry.getKey().toLowerCase().contains("write")) {
                if (writeDataSource != null) {
                    throw new IllegalStateException("写数据源只能定义一个");
                }
                this.writeDataSource = entry.getValue();
                continue;
            }
            //若数据源名称不含write,则默认为读数据源
            readDataSources.add(entry.getValue());
        }
        if (writeDataSource == null) {
            throw new IllegalStateException("至少需要定义一个写数据源");
        }
    }

    /**
     * 当前数据操作类型时写时或只有一个写数据源时,直接返回此数据源;
     * 当有多个读数据源时,轮训读数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        DataManipulationType manipulationType = DataSourceTransactionManagerAdapter.getCurrentManipulationType();
        if (manipulationType == DataManipulationType.WRITE || readDataSources.isEmpty()) {
            return (long)-1;
        }
        long times = readRequestedTime.getAndIncrement();
        return times % readDataSources.size();
    }

    @Override
    public void afterPropertiesSet() {
        HashMap<Integer, DataSource> dataSources = new HashMap<Integer, DataSource>();
        //按序号设置数据源，0为写数据源,之后为读数据源,方便多个读数据源时负载均衡
        dataSources.put(0, writeDataSource);
        for (int i = 0; i < readDataSources.size(); i++) {
            dataSources.put(i + 1, readDataSources.get(i));
        }
        setDefaultTargetDataSource(writeDataSource);
        setTargetDataSources(new HashMap<Object, Object>(dataSources));
        super.afterPropertiesSet();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource dataSource;
        int key = ((Long)determineCurrentLookupKey()).intValue();
        if (key == -1) {
            LOGGER.info("获取写数据源");
            dataSource = writeDataSource;
        } else {
            LOGGER.info("获取读数据源");
            dataSource = readDataSources.get(key);
        }
        return dataSource;
    }
}
