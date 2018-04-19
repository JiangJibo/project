package com.bob.intergrate.netty.client;

import java.io.UnsupportedEncodingException;

import com.bob.intergrate.netty.NettyEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Administrator
 * @create 2018-04-13 22:55
 */
public class DefaultClientChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * 最后一个读取的Handler,不要在往下传了，否则会传递到tail元素上
     *
     * @param ctx
     * @param msg
     * @throws UnsupportedEncodingException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String json = new String(data, "utf-8");
        System.out.println(json);
    }

}
