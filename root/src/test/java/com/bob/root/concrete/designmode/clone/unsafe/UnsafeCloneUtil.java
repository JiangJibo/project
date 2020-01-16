package com.bob.root.concrete.designmode.clone.unsafe;

import com.bob.root.concrete.designmode.observer.Student;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author wb-jjb318191
 * @create 2018-10-11 9:55
 */
public class UnsafeCloneUtil {

    private static Object helperArray[] = new Object[1];

    private final static ClassIntrospector ci = new ClassIntrospector();

    /**
     * 获取对象起始位置偏移量
     *
     * @param unsafe
     * @param object
     * @return
     */
    public static long getObjectAddress(Unsafe unsafe, Object object) {
        helperArray[0] = object;
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return unsafe.getLong(helperArray, baseOffset);
    }

    /**
     * 获取Object的大小
     *
     * @param object
     * @return
     */
    public static long getObjectSize(Object object) {
        ObjectInfo res = null;
        try {
            res = ci.introspect(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return res.getDeepSize();
    }

    @Test
    public void testObjectSize(){
        System.out.println(ObjectSizeCalculator.getObjectSize(new Demo()));
    }

    public static class Demo{
        String name;
        //String age;
    }

}
