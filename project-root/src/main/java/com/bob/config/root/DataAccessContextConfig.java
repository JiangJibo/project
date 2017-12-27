package com.bob.config.root;

import javax.sql.DataSource;

import com.bob.config.root.mapper.BaseMapper;
import com.bob.config.root.tx.TransactionConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月5日 下午5:24:24
 */
@Configuration
@MapperScan(value = "com.bob.mvc.mapper", markerInterface = BaseMapper.class)
public class DataAccessContextConfig {

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "lanboal";

    /**
     * MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
     * 避免中文乱码要指定useUnicode和characterEncoding
     * 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
     * 下面语句之前就要先创建project数据库
     */
    private static final String URL = "jdbc:mysql://localhost:3306/project?useUnicode=true&characterEncoding=UTF8&useSSL=false";

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        //针对mysql获取字段注释
        dataSource.addConnectionProperty("useInformationSchema", "true");
        //dataSource.addConnectionProperty("remarksReporting","true");  针对oracle获取字段注释
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaxTotal(50);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // 配置MapperConfig
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        //当数据库集群时,配置多个数据源,通过设置不同的DatebaseId来区分数据源,同时sql语句中通过DatabaseId来指定匹配哪个数据源
        //configuration.setDatabaseId("Mysql-1");

        // 这个配置使全局的映射器启用或禁用缓存
        configuration.setCacheEnabled(true);

        // 允许 JDBC 支持生成的键，需要适合的驱动（如MySQL，SQL Server，Sybase ASE）。
        // 如果设置为 true 则这个设置强制生成的键被使用，尽管一些驱动拒绝兼容但仍然有效（比如 Derby）。
        // 但是在 Oracle 中一般不需要它，而且容易带来其它问题，比如对创建同义词DBLINK表插入时发生以下错误：
        // "ORA-22816: unsupported feature with RETURNING clause" 在 Oracle
        // 中应明确使用 selectKey 方法
        //configuration.setUseGeneratedKeys(false);

        // 配置默认的执行器:
        // SIMPLE :> SimpleExecutor  执行器没有什么特别之处;
        // REUSE :> ReuseExecutor 执行器重用预处理语句,在一个Service方法中多次执行SQL字符串一致的操作时,会复用Statement及Connection,
        // 也就是说不需要再预编译Statement,不需要重新通过DataSource生成Connection及释放Connection,能大大提高操纵数据库效率;
        // BATCH :> BatchExecutor 执行器重用语句和批量更新
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        // 全局启用或禁用延迟加载，禁用时所有关联对象都会即时加载
        configuration.setLazyLoadingEnabled(false);
        // 设置SQL语句执行超时时间缺省值，具体SQL语句仍可以单独设置
        configuration.setDefaultStatementTimeout(5000);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(configuration);
        // 匹配多个 MapperConfig.xml, 使用mappingLocation属性
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/bob/mvc/mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }
}
