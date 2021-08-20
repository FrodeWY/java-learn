package com.rpc.core.server.netty;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelHandler {


    void connected(ChannelHandlerContext ctx);


    void disconnected(ChannelHandlerContext ctx);


    void sent(ChannelHandlerContext ctx, Object message);


    void received(ChannelHandlerContext ctx, Object message);


    void caught(ChannelHandlerContext ctxl, Throwable exception);
}
