package com.bob.intergrate.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wb-jjb318191
 * @create 2018-04-12 15:44
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8379).sync();
        Channel channel = future.channel();
        channel.writeAndFlush(wrapByteBuffer("123456$"));
        channel.writeAndFlush(wrapByteBuffer("abcdefg$"));
        channel.closeFuture().sync();
        workerGroup.shutdownGracefully();
    }

    private static ByteBuf wrapByteBuffer(String data) {
        return Unpooled.copiedBuffer(data.getBytes());
    }
}
