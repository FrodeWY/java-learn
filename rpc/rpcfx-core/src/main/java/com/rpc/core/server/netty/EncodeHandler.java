package com.rpc.core.server.netty;

import com.rpc.core.api.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
public class EncodeHandler extends MessageToByteEncoder<Object> {

    private final Codec encoder;

    public EncodeHandler(Codec encoder) {
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] bytes = encoder.encode(msg);
        out.writeBytes(bytes);
    }
}
