package com.rpc.core.codec.msgpack;

import com.rpc.core.api.Codec;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import org.msgpack.MessagePack;

/**
 * @author wangyang
 * @ClassName MsgPackCodec
 * @Description TODO
 * @Date 2021/8/18 下午3:21
 * @Version 1.0
 */
public class MsgPackCodec implements Codec {

    public static final String NAME = "msgPack";

    @Override
    public byte[] encode(Object object) throws IOException {
        MessagePack messagePack = new MessagePack();
        return messagePack.write(object);
    }

    @Override
    public <T> T decode(ByteBuf in, Class<T> type) throws IOException {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        return decode(bytes, type);
    }

    @Override
    public <T> T decode(String str, Class<T> type) throws IOException {
        MessagePack messagePack = new MessagePack();
        return messagePack.read(str.getBytes(), type);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> type) throws IOException {
        MessagePack messagePack = new MessagePack();
        return messagePack.read(bytes, type);
    }
}
