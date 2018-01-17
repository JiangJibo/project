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

    public static final ThreadLocal<DataManipulationType> DATA_OPERATION_TYPE = new ThreadLocal<DataManipulationType>();

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        if (definition.isReadOnly()) {
            DATA_OPERATION_TYPE.set(DataManipulationType.READ);
        } else {
            DATA_OPERATION_TYPE.set(DataManipulationType.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        DATA_OPERATION_TYPE.remove();
        super.prepareForCommit(status);
    }

    /**
     * 查看当前线程的数据操作类型
     *
     * @return
     */
    public static DataManipulationType getCurrentManipulationType() {
        return DATA_OPERATION_TYPE.get();
    }

}
