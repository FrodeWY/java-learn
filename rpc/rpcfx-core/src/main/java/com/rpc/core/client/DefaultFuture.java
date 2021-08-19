package com.rpc.core.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFuture extends CompletableFuture {
    public static Map<String, DefaultFuture> futureCache = new ConcurrentHashMap<>();
    private final Channel channel;

    public DefaultFuture(Channel channel) {
        this.channel = channel;
        futureCache.put(channel.id().asLongText(), this);
    }


    public static DefaultFuture getFuture(String channelId) {
        return futureCache.get(channelId);
    }

    public static void remove(String channelId) {
        futureCache.remove(channelId);
    }
}
