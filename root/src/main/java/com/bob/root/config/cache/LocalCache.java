package com.bob.root.config.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author Administrator
 * @create 2018-06-30 8:28
 */
public class LocalCache {

    private final LoadingCache<String, Object> readWriteCacheMap;

    public LocalCache() {
        this.readWriteCacheMap =
            CacheBuilder.newBuilder().initialCapacity(1000)
                // 设置缓存过期时间, 在写后180S过期
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> notification) {
                        String removedKey = notification.getKey();
                        System.out.println(String.format("[%s]被删除了", removedKey));
                    }
                })
                .build(new CacheLoader<String, Object>() {
                    @Override
                    // 定义缓存加载器, 当get(key)返回null时使用加载器去generatePayload(key), 生成负载value
                    public Object load(String key) throws Exception {
                        // 通过key生成负载
                        Object value = generatePayload(key);
                        return value;
                    }
                });
    }

    private Object generatePayload(String key) {
        System.out.println(String.format("加载[key]对应的值"));
        return "$" + key + "$";
    }

    public void put(String key, Object value) {
        readWriteCacheMap.put(key, value);
    }

    public Object get(String key) {
        try {
            return readWriteCacheMap.get(key);
        } catch (ExecutionException e) {
            return null;
        }
    }

}
