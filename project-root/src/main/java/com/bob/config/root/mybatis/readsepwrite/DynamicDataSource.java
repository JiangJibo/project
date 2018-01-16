package com.bob.config.root.mybatis.readsepwrite;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源配置
 *
 * @author wb-jjb318191
 * @create 2018-01-16 9:56
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Autowired
    private Map<String, DataSource> readDataSource;

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    public void afterPropertiesSet() {

        super.afterPropertiesSet();
    }
}
