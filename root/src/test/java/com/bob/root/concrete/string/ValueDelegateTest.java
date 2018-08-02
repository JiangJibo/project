package com.bob.root.concrete.string;

import java.lang.reflect.Field;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

/**
 * 值传递和引用传递测试
 *
 * @author wb-jjb318191
 * @create 2018-08-02 15:42
 */
public class ValueDelegateTest {

    @Test
    public void testReferences() throws IllegalAccessException {
        String s = "123456";
        changeReference(s);
        System.out.println(s);
    }

    /**
     * 栈中修改变量副本的引用指针,但对原始变量无效
     * 每个线程都拥有所使用的原始成员变量的副本值,如果修改此副本的引用指针,不会对原始变量造成影响
     * 但是如果想修改副本内部属性的值,那么对原始变量还是有效果的
     *
     * @param string
     */
    private void changeReference(String string) throws IllegalAccessException {
        Field field = ReflectionUtils.findField(String.class, "value");
        field.setAccessible(true);
        char[] value = (char[])field.get(string);
        System.out.println(value);
        value[5] = 'c';
    }

}
