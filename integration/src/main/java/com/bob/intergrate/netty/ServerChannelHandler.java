package com.bob.intergrate.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wb-jjb318191
 * @create 2018-04-12 15:44
 */
public class ServerChannelHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ServerChannelHandler.class.getSimpleName() + "对应的Ctx名称为:" + ctx.name());
        NettyEntity entity = (NettyEntity)msg;
        System.out.println("NettyEntity name: " + entity.getName());
        //写给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("我是反馈的信息$".getBytes()));
        //ctx.fireChannelRead(msg);
        //ctx.fireChannelReadComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
