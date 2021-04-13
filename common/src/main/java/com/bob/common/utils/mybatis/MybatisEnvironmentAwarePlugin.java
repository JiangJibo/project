package com.bob.common.utils.mybatis;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * mybatis�����ʵ�ִ��������Ԥ���ͻ������л���
 *
 * @author wb-jjb318191
 * @create 2020-06-23 10:23
 */
@Intercepts
    ({
        @Signature(type = Executor.class, method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
    })
public class MybatisEnvironmentAwarePlugin implements Interceptor {

    private String kfcEnvironment;

    /**
     * �ͻ�����صı���������
     */
    private static final Map<String, Map<String, String>> ENVIRONMENT_AWARE_TABLE_MAPPING = new HashMap<>();

    static {
        // �ճ�������ӳ��
        // TODO , ����ܶ�ʱ����Ҫ����Ե��Ż��������ַ����ıȶԴ���
        Map<String, String> dailyEnvTableMapping = new HashMap<>();
        dailyEnvTableMapping.put("kfc_package_config", "kfc_package_config_test");
        dailyEnvTableMapping.put("kfc_package_engine_config", "kfc_package_engine_config_test");

        ENVIRONMENT_AWARE_TABLE_MAPPING.put("daily", dailyEnvTableMapping);

        // Ԥ��������ӳ��
        Map<String, String> preEnvTableMapping = new HashMap<>();
        preEnvTableMapping.put("kfc_package_config", "kfc_package_config_test");
        preEnvTableMapping.put("kfc_package_engine_config", "kfc_package_engine_config_test");

        ENVIRONMENT_AWARE_TABLE_MAPPING.put("prepub", preEnvTableMapping);

        // ��ʽ������ӳ��
        Map<String, String> productEnvTableMapping = new HashMap<>();
        productEnvTableMapping.put("kfc_package_config", "kfc_package_config");
        productEnvTableMapping.put("kfc_package_engine_config", "kfc_package_engine_config");

        //ENVIRONMENT_AWARE_TABLE_MAPPING.put("production", productEnvTableMapping);
    }

    public MybatisEnvironmentAwarePlugin(String kfcEnvironment) {
        this.kfcEnvironment = kfcEnvironment;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // ��ȡsql
        String sql = getSqlByInvocation(invocation);
        if (StringUtils.isEmpty(sql)) {
            return invocation.proceed();
        }
        // sql���ɴ����ദ��  ��sql�����д���  �˴��Ƿ���  �����κδ���
        String sql2Reset = rewriteTableWithEnvironment(sql);

        if(sql2Reset.equals(sql)){
            return invocation.proceed();
        }

        // ��װsql�����õ�invocation��
        resetSql2Invocation(invocation, sql2Reset);

        // ���أ�����ִ��
        return invocation.proceed();
    }

    /**
     * ���ݻ�����дsql�ı�
     *
     * @param rawSql
     * @return
     */
    private String rewriteTableWithEnvironment(String rawSql) {

        Map<String, String> tableMapping = ENVIRONMENT_AWARE_TABLE_MAPPING.get(kfcEnvironment);
        if (tableMapping == null) {
            return rawSql;
        }

        for (Entry<String, String> entry : tableMapping.entrySet()) {
            rawSql = rawSql.replace(entry.getKey(), entry.getValue());
        }

        return rawSql;
    }

    /**
     * ��ȡsql���
     *
     * @param invocation
     * @return
     */
    private String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        return ((MappedStatement)args[0]).getBoundSql(args[1]).getSql();
    }

    /**
     * ��װsql�����õ�invocation��
     *
     * @param invocation
     * @param sql
     */
    private void resetSql2Invocation(Invocation invocation, String sql) {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement)args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(),
            new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
            new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties arg0) {

    }

    //    ����һ���ڲ������࣬�����ǰ�װsq
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }

}
