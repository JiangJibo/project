package com.bob.common.memory;

/**
 * @author wb-jjb318191
 * @create 2019-01-17 17:08
 */
public class JavaVisualMemoryTest {

    /** VM Options :
     * 1. -Xmx20m -Xms20m -Xmn1m -XX:+PrintGCDetails
     * 2. -Xmx20m -Xms20m -Xmn15m -XX:+PrintGCDetails
     * @param args
     */
    public static void main(String[] args) {
        byte[] b = null;
        for (int i = 0; i < 20; i++)
            b = new byte[1 * 1024 * 1024];
    }

}
