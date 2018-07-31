package com.bob.root.concrete.cache;

import com.bob.root.config.cache.LocalCache;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-06-30 8:49
 */
public class LocalCacheTest {

    private LocalCache localCache;

    @Before
    public void init() {
        localCache = new LocalCache();
    }

    @Test
    public void testGet() throws InterruptedException {
        localCache.get("lanboal");
        Thread.sleep(11 * 1000);
        localCache.get("lanboal");
    }

    @Test
    public void testIntegerEquals() {
        Integer a = 3;
        Integer b = 3;
        System.out.println(a == b);
    }

}
