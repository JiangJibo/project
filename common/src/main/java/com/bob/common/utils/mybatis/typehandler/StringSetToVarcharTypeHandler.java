package com.bob.common.utils.mybatis.typehandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author wb-jjb318191
 * @create 2019-11-29 11:46
 */
public class StringSetToVarcharTypeHandler extends GenericSetToVarcharTypeHandler<String> {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        StringSetToVarcharTypeHandler typeHandler = StringSetToVarcharTypeHandler.class.newInstance();

        System.out.println(typeHandler);

        Configuration configuration = new Configuration();
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

        Type type = new TypeReference<List<String>>() {}.getType();
        System.out.println(type.getTypeName());

        Class<?> clazz = new ArrayList<String>().getClass();
        System.out.println(((Type)clazz).getTypeName());
    }

}
