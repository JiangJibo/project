package com.bob.common.utils.mybatis.typehandler;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * @author wb-jjb318191
 * @create 2019-11-29 17:41
 */
public class GenericListToVarcharTypeHandler<T> extends CommonToVarcharTypeHandler<List<T>> {

    /**
     * Set泛型
     */
    private Class componentType;

    protected GenericListToVarcharTypeHandler() {
        super();
        this.componentType = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected List<T> toResult(String text) {
        return JSON.parseArray(text, componentType);
    }

}
