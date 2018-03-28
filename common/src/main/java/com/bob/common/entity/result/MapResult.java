package com.bob.common.entity.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 类MapResult.java的实现描述：返回结果是一个Map的结果类
 * 
 * @author lixj 2015年1月26日 下午3:53:55
 */
public class MapResult<K, V> extends BaseResult {

    private static final long serialVersionUID = -8282377194666806425L;

    private Map<K, V>         content;

    /**
     * 增加一个k/v
     * 
     * @param key
     * @param val
     * @return MapResult<K, V>
     */
    public MapResult<K, V> add(K key, V val) {
        if (content == null) {
            setContent(new HashMap<K, V>());
        }
        getContent().put(key, val);
        return this;
    }

    /**
     * @return the content
     */
    public Map<K, V> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Map<K, V> content) {
        this.content = content;
    }

}
