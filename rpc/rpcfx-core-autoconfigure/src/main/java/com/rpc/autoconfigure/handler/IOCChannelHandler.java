package com.rpc.autoconfigure.handler;

import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import com.rpc.core.server.RpcfxInvoker;
import com.rpc.core.server.netty.ChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOCChannelHandler implements ChannelHandler {

    private RpcfxInvoker invoke;

    public IOCChannelHandler(RpcfxInvoker invoke) {
        this.invoke = invoke;
    }

    @Override
    public void connected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info(((NioSocketChannel) channel).remoteAddress().getHostName() + "connect server");
    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info(((NioSocketChannel) channel).remoteAddress().getHostName() + "disconnect server");
    }

    @Override
    public void sent(ChannelHandlerContext ctx, Object message) {
        Channel channel = ctx.channel();
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.awaitUninterruptibly();
    }

    @Override
    public void received(ChannelHandlerContext ctx, Object message) {
        RpcfxRequest request = (RpcfxRequest) message;
        final RpcfxResponse rpcfxResponse = this.invoke.invoke(request);
        ChannelFuture channelFuture = ctx.writeAndFlush(rpcfxResponse);
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void caught(ChannelHandlerContext ctxl, Throwable exception) {
        exception.printStackTrace();
    }
}
