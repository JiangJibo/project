package com.bob.intergrate.netty;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Administrator
 * @create 2018-04-12 20:04
 */
public class DecodeHandler<T> extends ChannelHandlerAdapter {

    private static final Gson GSON = new Gson();
    private Class<T> clazz;

    public DecodeHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(DecodeHandler.class.getSimpleName() + "对应的Ctx名称为:" + ctx.name());
        //do something msg
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String json = new String(data, "utf-8");
        // 将当前Handler的读取结果传递给下一个Handler
        ctx.fireChannelRead(fromJson(json));
    }

    /**
     * @param json
     * @return
     */
    private T fromJson(String json) {
        return GSON.fromJson(json, clazz);
    }

}
