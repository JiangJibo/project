package com.bob.root.concrete.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 直接内存测试
 *
 * @author Administrator
 * @create 2018-07-24 20:51
 */
public class DirectBufferTest {

    @Test
    public void testDirectBuffer() {
        loopAllocateDirect();
    }

    private void allocateDirect() {
        ByteBuffer.allocateDirect(1024 * 1024 * 200); // 100MB
    }

    private void loopAllocateDirect() {
        for (int i = 0; i < 5000; i++) {
            ByteBuffer.allocateDirect(1024 * 100);  //100K
        }
    }

    private void loopAllocate() {
        for (int i = 0; i < 5000; i++) {
            ByteBuffer.allocate(1024 * 100);  //100K
        }
    }

}
