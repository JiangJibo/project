package com.bob.root.concrete.designmode.clone.unsafe;

import com.bob.root.concrete.designmode.clone.CloneEntity;
import org.junit.Test;
import sun.misc.Unsafe;

import static com.bob.root.concrete.designmode.clone.unsafe.ClassIntrospector.unsafe;

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

}
