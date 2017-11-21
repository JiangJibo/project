package com.bob.test.concrete.enumtest;

import org.junit.Test;

/**
 * 枚举类型测试用例
 * 每一个枚举实例相当于继承了外壳{@link Level}类,而Level则继承了{@link Enum}类
 * Enum中很多方法都是final的,不能被重写,只有一个toString()可以被重写
 *
 * @author wb-jjb318191
 * @create 2017-11-20 9:58
 */
public class EnumTest {

    /**
     * Enum内置了一个valueOf(String)的函数,通过枚举的名称来映射枚举实例
     * 同时我们定义了一个valueOf(Integer)的函数，通过code来映射枚举实例
     * {@link Level#valueOf(String)}
     * {@linkplain Level#valueOf(Integer)}
     */
    @Test
    public void testValueOf() {
        String common = Level.COMMON.toString();
        Level level0 = Level.valueOf(common);
        Level level1 = Level.valueOf(0);
        System.out.println(common);
        System.out.println(level0 == level1);
    }

    /**
     * 每一个枚举都有name和ordinal两个属性，name是枚举的名称，ordinal是枚举的顺序，按照定义顺序来，第一个是0，依次加1
     */
    @Test
    public void testGetName(){
        for(Level level : Level.values()){
            System.out.println(level.ordinal());
            System.out.println(level.name());
        }
    }

    /**
     * Enum的toString()方法输出的是枚举的名称
     * {@link Enum#toString()}
     */
    @Test
    public void testToString(){
        for(Level level : Level.values()){
            System.out.println(level.toString());
        }
    }

    /**
     * 每一个枚举实例可以重写定义外壳的方法
     */
    @Test
    public void testGetDiscount(){
        for(Level level : Level.values()){
            System.out.println(level.getDiscount());
        }
    }

}
