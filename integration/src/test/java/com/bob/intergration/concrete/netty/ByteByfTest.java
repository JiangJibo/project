package com.bob.intergration.concrete.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-04-15 10:12
 */
public class ByteByfTest {

    @Test
    public void testInArray() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("lanboal".getBytes());
        if (byteBuf.hasArray()) {
            byte[] bytes = byteBuf.array();
        }
    }

    @Test
    public void duplicate() {
        ByteBuf byteBuf = Unpooled.directBuffer(100);
        byteBuf.readBytes("测试信息".getBytes());
        ByteBuf dip = byteBuf.duplicate();
        ByteBuf slice = byteBuf.slice(1, 2);
        ByteBuf copy = byteBuf.copy(1, 2);
    }

}
