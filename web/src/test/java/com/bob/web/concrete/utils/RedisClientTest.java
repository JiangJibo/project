package com.bob.web.concrete.utils;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author wb-jjb318191
 * @create 2020-11-11 14:25
 */
public class RedisClientTest {

    private Jedis jedis;

    @Before
    public void init() {
        jedis = new Jedis("r-bp1c0gwzetgxhej25qpd.redis.rds.aliyuncs.com", 36379);
        jedis.auth("yDmp1mLyf2UveOOUzJL3gKSk4mIw");
        jedis.select(5);
    }

    @Test
    public void cacheTemporaryKV() throws InterruptedException {
        String key = "udf_distinct_key";
        String set = jedis.set(key, "udf_distinct_value", "NX", "EX", 3);
        System.out.println(set);
        String set1 = jedis.set(key, "udf_distinct_value", "NX", "EX", 3);
        System.out.println(set1);
        String s = jedis.get(key);
        System.out.println(s);

        Thread.sleep(3000);
        s = jedis.get(key);
        System.out.println(s);
    }

}
