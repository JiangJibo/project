package com.bob.integrate.netty.server;

import com.bob.integrate.netty.NettyEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author wb-jjb318191
 * @create 2018-04-12 15:44
 */
@Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(ServerChannelHandler.class.getSimpleName() + "对应的Ctx名称为:" + ctx.name());
        NettyEntity entity = (NettyEntity)msg;
        System.out.println("NettyEntity name: " + entity.getData());

        ByteBufAllocator allocator = ctx.channel().alloc();
        ByteBuf byteBuf = allocator.buffer();
        byteBuf.writeBytes("我是反馈的信息$".getBytes());

        //写给客户端
        ChannelFuture future = ctx.writeAndFlush(byteBuf);
        future.addListener((ChannelFutureListener)f -> {
            if (!f.isSuccess()) {
                f.cause().printStackTrace();
                f.channel().close();
            }
        });
        //ReferenceCountUtil.release(byteBuf);

        //ctx.fireChannelRead(msg);
        //ctx.fireChannelReadComplete();
        //InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        //System.out.println("HostName: " + socketAddress.getHostName());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
