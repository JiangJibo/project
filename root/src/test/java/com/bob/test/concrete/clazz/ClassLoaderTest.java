/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.test.concrete.clazz;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * 使用不同的ClassLoader加载同一个Class,验证Class的唯一性
 *
 * @since 2017年8月20日 下午12:08:54
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ClassLoaderTest {

    public ClassLoader myLoader;

    @Before
    public void onBefore() {
        myLoader = new ClassLoader() {

            /* (non-Javadoc)
             * @see java.lang.ClassLoader#loadClass(java.lang.String)
             */
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                try {
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException();
                }
            }
        };

    }

    @Test
    public void testLoadClass() throws Exception {
        Class<?> clazz = myLoader.loadClass("com.bob.test.concrete.clazz.ClassLoaderTest");
        Object obj = clazz.newInstance();
        System.out.println(clazz + "" + clazz.getClassLoader());
        System.out.println(clazz.equals(this.getClass()));
        System.out.println(obj instanceof ClassLoaderTest);
        System.out.println(this.getClass().getClassLoader());
    }

}
