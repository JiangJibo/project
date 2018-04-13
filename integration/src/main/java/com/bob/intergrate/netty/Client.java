package com.bob.intergrate.netty;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultChannelPromise;
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

    private static final Gson GSON = new Gson();
    private static final String DELIMITER = "$";

    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(256, Unpooled.copiedBuffer("$".getBytes())), new ClientChannelHandler());
                }
            });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8379).sync();
        SocketChannel channel = (SocketChannel)future.channel();
        ChannelHandler handler = channel.pipeline().get("ClientChannelHandler#0");
        ChannelHandlerContext context = channel.pipeline().context("ClientChannelHandler#0");
        handler.write(context, wrapByteBuffer(GSON.toJson(new NettyEntity("lanboal"))), new DefaultChannelPromise(channel));

        //channel.writeAndFlush(wrapByteBuffer(GSON.toJson(new NettyEntity("lanboal"))));
        //channel.writeAndFlush(wrapByteBuffer(GSON.toJson(new NettyEntity("jojo"))));
        channel.closeFuture().sync();
        workerGroup.shutdownGracefully();
    }

    private static ByteBuf wrapByteBuffer(String data) {
        return Unpooled.copiedBuffer((data + DELIMITER).getBytes());
    }

}
