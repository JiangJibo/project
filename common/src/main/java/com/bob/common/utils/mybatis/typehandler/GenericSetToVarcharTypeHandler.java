package com.bob.common.utils.mybatis.typehandler;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;

/**
 * 通用的泛型Set到Varchar的类型处理器
 *
 * @author wb-jjb318191
 * @create 2019-11-29 11:24
 */
public abstract class GenericSetToVarcharTypeHandler<T> extends CommonToVarcharTypeHandler<Set<T>> {

    /**
     * Set泛型
     */
    private Class componentType;

    protected GenericSetToVarcharTypeHandler() {
        super();
        this.componentType = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Set<T> toResult(String text) {
        return new HashSet(JSON.parseArray(text, componentType));
    }

}
