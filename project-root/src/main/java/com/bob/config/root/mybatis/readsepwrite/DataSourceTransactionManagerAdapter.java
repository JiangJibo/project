package com.bob.config.root.mybatis.readsepwrite;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * 数据源事务管理器拓展
 *
 * @author Administrator
 * @create 2018-01-16 19:33
 */
public class DataSourceTransactionManagerAdapter extends DataSourceTransactionManager {

    public static final ThreadLocal<DataSourceType> DATA_SOURCE_TYPE = new ThreadLocal<DataSourceType>();

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        if (definition.isReadOnly()) {
            DATA_SOURCE_TYPE.set(DataSourceType.READ);
        } else {
            DATA_SOURCE_TYPE.set(DataSourceType.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        DATA_SOURCE_TYPE.remove();
        super.prepareForCommit(status);
    }
}
