package com.bob.project.test.concrete.redis;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接池测试
 *
 * @author wb-jjb318191
 * @create 2018-01-10 11:23
 */
public class JedisConnectionTest {

    private JedisConnectionFactory factory;
    private RedisConnection jedisConnection;
    private String script = "return redis.call('sRandMember',KEYS[1])"; //脚本
    private String scriptSha = "e404056737d0f990e2a0517a95aa6d8f035c85a4";  //Redis加载脚本后生成的sha码

    @Before
    public void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(2000);
        //哨兵配置
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
        factory = new JedisConnectionFactory(sentinelConfiguration, poolConfig);
        factory.afterPropertiesSet();
        jedisConnection = factory.getConnection();
    }

    /**
     * 管道测试
     */
    @Test
    public void testPipeline() {
        jedisConnection.openPipeline();
        jedisConnection.hGet("user".getBytes(), "age".getBytes());
        jedisConnection.sRandMember("userNames".getBytes());
        List<Object> result = jedisConnection.closePipeline();
        for (Object object : result) {
            System.out.println(new String((byte[])object));
        }
    }

    /**
     * 脚本测试
     */
    @Test
    public void testScript() {
        String key = "userNames";
        byte[] result = jedisConnection.eval(script.getBytes(), ReturnType.VALUE, 1, key.getBytes());
        System.out.println(new String(result));
    }

    /**
     * 脚本加载
     */
    @Test
    public void testLoadScript() {
        String scriptSha = jedisConnection.scriptLoad(script.getBytes());
        System.out.println(scriptSha);
    }

    /**
     * 使用脚本Sha码执行脚本
     */
    @Test
    public void exeScriptBySha() {
        String key = "userNames";
        byte[] result = jedisConnection.evalSha(scriptSha, ReturnType.VALUE, 1, key.getBytes());
        System.out.println(new String(result));
    }

    /**
     * 实现事务
     */
    @Test
    public void testMulti() {
        jedisConnection.multi();
        jedisConnection.hSet("user:id".getBytes(), "loginTime".getBytes(), String.valueOf(System.currentTimeMillis()).getBytes());
        jedisConnection.set("user:id:login".getBytes(), "true".getBytes());
        jedisConnection.exec();
    }

    @Test
    public void testGet() {
        byte[] bytes = jedisConnection.hGet("user:id".getBytes(), "loginTime".getBytes());
        System.out.println(new String(bytes));
    }

    @After
    public void close() {
        jedisConnection.close();
    }

}
