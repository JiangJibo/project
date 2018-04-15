package com.bob.intergrate.netty.client;

import com.bob.intergrate.netty.NettyEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @author wb-jjb318191
 * @create 2018-04-12 15:44
 */
public class Client {

    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(
                        new DelimiterBasedFrameDecoder(256, Unpooled.copiedBuffer("$".getBytes())),
                        new EncodeHandler(),
                        new DefaultClientChannelHandler());
                }
            });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8379).sync();
        future.addListener((ChannelFutureListener)future1 -> {
            future1.channel().writeAndFlush(new NettyEntity("连接成功!"));
        });
        SocketChannel channel = (SocketChannel)future.channel();
        write(channel);
        channel.closeFuture().sync();
        workerGroup.shutdownGracefully();
    }

    private static void write(SocketChannel channel) {
        ChannelHandlerContext context = channel.pipeline().lastContext();
        context.writeAndFlush(new NettyEntity("lanboal"));
        context.writeAndFlush(new NettyEntity("123456"));
    }

}
