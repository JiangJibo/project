package com.bob.common.utils.tablesplit;

import java.util.Date;
import java.util.List;

/**
 * 分表业务接口
 *
 * @author wb-jjb318191
 * @create 2018-04-17 13:00
 */
public interface TableSplittingService {

    /**
     * 指定拆分时间间隔,单位:月
     *
     * @return
     */
    int getSplitIntervalInMonth();

    /**
     * 指定时间格式化形式
     *
     * @return
     */
    String getDateFormatPattern();

    /**
     * 以时间为分割依据时,查询分表内时间的最大值和最小值
     *
     * @param order 当前分表的序号
     * @return
     */
    List<Date> selectTimeEndpoint(int order);

    /**
     * 根据当前分表的序号生成分表名称,从0开始
     *
     * @param order
     * @return
     */
    String generateTableNameByOrder(int order);

    /**
     * 创建分表
     *
     * @param order 当前分表的序号,从0开始
     */
    void createSplitTable(int order);

    /**
     * 提取当前插入记录的插入依据,以此作为插入哪张表
     *
     * @param obj
     * @return
     */
    Date extractInsertBasis(Object obj);

    /**
     * 获取查询对象的时间区间
     * 第一个元素存放起始时间
     * 第二个元素存放终止时间
     *
     * @param obj
     * @return
     */
    List<Date> getQueryTimeInterval(Object obj);

}