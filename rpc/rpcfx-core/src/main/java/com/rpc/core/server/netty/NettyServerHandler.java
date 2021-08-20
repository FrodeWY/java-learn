package com.rpc.core.server.netty;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {
    private ChannelHandler channelHandler;
    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    public List<Channel> getChannels() {
        return new ArrayList<>(channelMap.values());
    }
    public NettyServerHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelHandler.received(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        log.info("server channelActive {}, ", getAddressString(inetSocketAddress));
        channelMap.put(getAddressString(inetSocketAddress), channel);
        channelHandler.connected(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        log.info("server channelInactive {}, ", getAddressString(inetSocketAddress));
        channelMap.remove(getAddressString(inetSocketAddress));
        channelHandler.disconnected(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        channelHandler.sent(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelHandler.sent(ctx, cause);
    }


    private String getAddressString(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
    }
}
