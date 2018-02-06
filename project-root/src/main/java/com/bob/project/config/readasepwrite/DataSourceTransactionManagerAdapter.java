package com.bob.project.config.readasepwrite;

import javax.sql.DataSource;

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

    public static final ThreadLocal<DataManipulationType> DATA_MANIPULATION_TYPE = new ThreadLocal<DataManipulationType>();

    public DataSourceTransactionManagerAdapter(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        if (definition.isReadOnly()) {
            DATA_MANIPULATION_TYPE.set(DataManipulationType.READ);
        } else {
            DATA_MANIPULATION_TYPE.set(DataManipulationType.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        DATA_MANIPULATION_TYPE.remove();
        super.prepareForCommit(status);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        DATA_MANIPULATION_TYPE.remove();
        super.doRollback(status);
    }

    /**
     * 查看当前线程的数据操作类型
     *
     * @return
     */
    public static DataManipulationType getCurrentManipulationType() {
        return DATA_MANIPULATION_TYPE.get();
    }

}
