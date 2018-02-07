package com.bob.test.concrete.genericType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 手动获取泛型测试类
 *
 * @author wb-jjb318191
 * @create 2017-12-20 16:07
 */
public class GenericTypeAccessor {

    /**
     * 获取{@linkplain java.util.List<String>}的泛型
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testGetGenericTypeOfList() throws NoSuchMethodException {
        Method method = GenericTypeEntity.class.getMethod("getList");
        ParameterizedTypeImpl list = (ParameterizedTypeImpl)method.getGenericReturnType();
        System.out.println(list.getActualTypeArguments()[0].getTypeName());
        System.out.println(list.getRawType().getName());
    }

    /**
     * 获取{@linkplain java.util.Map<String,Integer>}的泛型
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testGetGenericTypeOfMap() throws NoSuchMethodException {
        Method method = GenericTypeEntity.class.getMethod("getMap");
        ParameterizedTypeImpl map = (ParameterizedTypeImpl)method.getGenericReturnType();
        Type[] types = map.getActualTypeArguments();
        System.out.println(types[0].getTypeName());
        System.out.println(types[1].getTypeName());
        System.out.println(map.getRawType().getName());
    }

    /**
     * 嵌套获取{@linkplain java.util.List<java.util.Map<String,Integer>}的泛型信息
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testGetGenericTypeOfListMap() throws NoSuchMethodException {
        Method method = GenericTypeEntity.class.getMethod("getListMap");
        ParameterizedTypeImpl listMap = (ParameterizedTypeImpl)method.getGenericReturnType();

        Type[] types = listMap.getActualTypeArguments();
        System.out.println(types[0].getTypeName());
        System.out.println(listMap.getRawType().getName());

        ParameterizedTypeImpl map = (ParameterizedTypeImpl)types[0];
        Type[] mapTypes = map.getActualTypeArguments();
        System.out.println(mapTypes[0].getTypeName());
        System.out.println(mapTypes[1].getTypeName());
        System.out.println(map.getRawType().getName());
    }

}
