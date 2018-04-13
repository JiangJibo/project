package com.bob.intergrate.netty;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * 对象编码处理器
 *
 * @author wb-jjb318191
 * @create 2018-04-13 15:10
 */
public class EncodeHandler extends ChannelHandlerAdapter {

    private static final Gson GSON = new Gson();
    private static final String DELIMITER = "$";

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(wrapByteBuffer(msg), promise);
    }

    private static ByteBuf wrapByteBuffer(Object data) {
        return Unpooled.copiedBuffer((GSON.toJson(data) + DELIMITER).getBytes());
    }
}
