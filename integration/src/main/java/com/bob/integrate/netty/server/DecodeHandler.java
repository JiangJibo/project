package com.bob.integrate.netty.server;

import java.nio.charset.Charset;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Administrator
 * @create 2018-04-12 20:04
 */
public class DecodeHandler<T> extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Gson GSON = new Gson();
    private Class<T> clazz;

    public DecodeHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(DecodeHandler.class.getSimpleName() + "对应的Ctx名称为:" + ctx.name());

        printByte(msg);

        byte[] data;
        if (msg.hasArray()) {
            data = msg.array();
        } else {
            data = new byte[msg.readableBytes()];
            msg.readBytes(data);
        }
        ctx.fireChannelRead(fromJson(new String(data, "utf-8")));
    }

    /**
     * @param json
     * @return
     */
    private T fromJson(String json) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * {@link ByteBuf#slice()}和{@link ByteBuf#duplicate()}生成副本和原始数据共享内存数组,因此他们的操作类似引用对象,耗费很低
     * {@link ByteBuf#copy()} 生成的数据类似于深拷贝形式,重新生成一个内存数组,能够独立操作不对其他变量有影响,但耗费较其他高些
     *
     * @param msg
     */
    private void printByte(ByteBuf msg) {
        Charset charset = Charset.forName("UTF-8");
        int position = msg.readableBytes();

        ByteBuf dip = msg.duplicate();
        ByteBuf slice = msg.slice(0, position);
        ByteBuf copy = msg.copy(0, position);

        System.out.println("dip:" + dip.toString(charset));
        System.out.println("slice:" + slice.toString(charset));
        System.out.println("copy:" + copy.toString(charset));

        msg.setByte(position - 3, 'D');

        System.out.println("dip:" + dip.toString(charset));
        System.out.println("slice:" + slice.toString(charset));
        System.out.println("copy:" + copy.toString(charset));
    }

}
