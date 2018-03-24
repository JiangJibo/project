package com.bob.intergrate.mybatis;

import java.util.Map;

import javax.sql.DataSource;

import com.bob.intergrate.mybatis.readasepwrite.DataSourceTransactionManagerAdapter;
import com.bob.intergrate.mybatis.readasepwrite.DynamicDataSource;
import com.bob.root.utils.BaseMapper;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据库配置类
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月5日 下午5:24:24
 */
@Configuration
@PropertySource("classpath:database-config.properties")
@MapperScan(basePackages = "com.bob.web.mvc.mapper", markerInterface = BaseMapper.class)
public class MybatisContextConfig {

    @Value("${mysql.driverClassName}")
    private String driverClassName;

    @Value("${mysql.userName}")
    private String userName;

    @Value("${mysql.password}")
    private String password;

    /**
     * MySQL的JDBC URL编写方式：jdbc:mybatis://主机名称：连接端口/数据库的名称?参数=值
     * 避免中文乱码要指定useUnicode和characterEncoding
     * 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
     * 下面语句之前就要先创建project数据库
     */
    @Value("${mysql.readUrl}")
    private String readUrl;
    @Value("${mysql.writeUrl}")
    private String writeUrl;

    /**
     * 读数据源
     *
     * @return
     */
    //@Bean(destroyMethod = "close")
    public DataSource readDataSource() {
        return generateDataSource(readUrl);
    }

    /**
     * 写数据源
     *
     * @return
     */
    @Bean(destroyMethod = "close")
    public DataSource writeDataSource() {
        return generateDataSource(writeUrl);
    }

    /**
     * 动态数据源
     *
     * @param dataSources
     * @return
     */
    @Bean
    public DataSource dataSource(Map<String, DataSource> dataSources) {
        return new DynamicDataSource(dataSources);
    }

    /**
     * 事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManagerAdapter(dataSource);
    }

    /**
     * SqlSession工厂
     *
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
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/bob/web/mvc/mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    private DataSource generateDataSource(String url) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        //针对mysql获取字段注释
        dataSource.addConnectionProperty("useInformationSchema", "true");
        //dataSource.addConnectionProperty("remarksReporting","true");  针对oracle获取字段注释
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setMaxTotal(50);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        return dataSource;
    }

}
