package com.bob.test.concrete.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import static redis.clients.jedis.Protocol.DEFAULT_HOST;
import static redis.clients.jedis.Protocol.DEFAULT_PORT;
import static redis.clients.jedis.Protocol.DEFAULT_TIMEOUT;

/**
 * Jedis客户端测试
 *
 * @author wb-jjb318191
 * @create 2018-01-10 10:43
 */
public class JedisClientTest {

    private Jedis jedis;

    @Before
    public void init() {
        jedis = new Jedis(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }

    @Test
    public void testSet() {
        String result = jedis.set("userName", "lanboal");
        System.out.println(result);
    }

    @Test
    public void testGet() {
        String result = jedis.get("userName");
        System.out.println(result);
    }

    @Test
    public void testSadd() {
        Long result = jedis.sadd("userNames", "lanboal", "bob", "lucy", "lulu");
        System.out.println(result);
    }

    @Test
    public void testSrandommember() {
        Set<String> result = jedis.smembers("userNames");
        System.out.println(result.toString());
    }

    @Test
    public void testHmset() {
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("name", "bob");
        userInfo.put("age", "30");
        userInfo.put("weight", "67");
        String result = jedis.hmset("user", userInfo);
        System.out.println(result);
    }

    @Test
    public void testPipeline() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.hget("user", "name");
        pipeline.srandmember("userNames");
        List<Object> response = pipeline.syncAndReturnAll();
        for (Object object : response) {
            System.out.println(object);
        }
    }

    @After
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }

}
