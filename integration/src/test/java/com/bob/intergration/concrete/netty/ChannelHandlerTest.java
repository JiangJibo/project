package com.bob.intergration.concrete.netty;

import com.bob.intergrate.netty.NettyEntity;
import com.bob.intergrate.netty.client.EncodeHandler;
import com.bob.intergrate.netty.server.DecodeHandler;
import com.bob.intergrate.netty.server.ServerChannelHandler;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Before;
import org.junit.Test;

/**
 * ChannelHandler测试
 *
 * @author Administrator
 * @create 2018-04-15 10:22
 */
public class ChannelHandlerTest {

    private EmbeddedChannel channel;
    private static final Gson GSON = new Gson();

    @Before
    public void init() {
        channel = new EmbeddedChannel(
            new DecodeHandler(NettyEntity.class),
            new ServerChannelHandler(),
            new EncodeHandler()
        );
    }

    @Test
    public void read() {
        ChannelFuture future = channel.writeOneInbound(byteMsg("InBound"));
        future.addListener((ChannelFutureListener)future1 -> {
            System.out.println("读入消息完成");
        });
    }

    @Test
    public void write() {
        ChannelFuture future = channel.writeOneOutbound(byteMsg("OutBound"));
        future.addListener((ChannelFutureListener)future1 -> {
            System.out.println("写出消息完成");
        });
    }

    private ByteBuf byteMsg(String msg) {
        NettyEntity entity = new NettyEntity(msg);
        return Unpooled.copiedBuffer((GSON.toJson(entity)).getBytes());
    }

}
