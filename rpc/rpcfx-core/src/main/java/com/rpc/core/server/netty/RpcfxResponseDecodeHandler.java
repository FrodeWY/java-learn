package com.rpc.core.server.netty;

import com.rpc.core.api.Codec;
import com.rpc.core.common.RpcfxResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcfxResponseDecodeHandler extends ByteToMessageDecoder {

    private final Codec decoder;

    public RpcfxResponseDecodeHandler(Codec decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decode = decoder.decode(in, RpcfxResponse.class);
        out.add(decode);
    }
}
