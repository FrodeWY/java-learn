package third_week.com.simple.gateway.future;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFuture extends CompletableFuture {
    public static Map<String, DefaultFuture> futureCache = new ConcurrentHashMap<>();
    private final ChannelHandlerContext ctx;
    private final Channel channel;

    public DefaultFuture(ChannelHandlerContext ctx, Channel channel) {
        this.ctx = ctx;
        this.channel = channel;
        futureCache.put(channel.id().asLongText(), this);
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public Channel getChannel() {
        return channel;
    }

    public static DefaultFuture getFuture(String channelId) {
        return futureCache.get(channelId);
    }

    public static void remove(String channelId) {
        futureCache.remove(channelId);
    }
}
