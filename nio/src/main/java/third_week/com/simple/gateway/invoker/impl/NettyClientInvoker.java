package third_week.com.simple.gateway.invoker.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import third_week.com.simple.gateway.feture.DefaultFuture;
import third_week.com.simple.gateway.handler.outbound.NettyClientInvokeHandler;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.result.SyncResult;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @ClassName NettyClientInvoker
 * @Description Netty invoker 实现
 * @Date 2021/7/7 下午9:32
 * @Version 1.0
 */
public class NettyClientInvoker implements Invoker {


    private static class NettyClient {

        private final Channel channel;
        private final EventLoopGroup workerGroup;

        NettyClient(String host, int port) {
            try {
                workerGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        //                    .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline()
                                        //                                    .addLast(new HttpRequestEncoder())
                                        //                                    .addLast(new HttpResponseDecoder())
                                        .addLast(new HttpClientCodec())
                                        .addLast(new HttpObjectAggregator(1024 * 1024))
                                        .addLast(new NettyClientInvokeHandler());
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                channel = channelFuture.channel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


    @Override
    public Result get(String urlString, ChannelHandlerContext ctx) {
        NettyClient client = null;
        SyncResult result = null;
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            int port = url.getPort();
            client = new NettyClient(host, port);
            Channel channel = client.channel;
            channel.write("你好");
            channel.flush();
            DefaultFuture defaultFuture = new DefaultFuture(ctx, channel);
            channel.closeFuture().sync();
            result = getSyncResult(defaultFuture);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SyncResult(e);
        } finally {
            if (client != null) {
                client.workerGroup.shutdownGracefully();
            }
        }
        return result;
    }

    @NotNull
    private SyncResult getSyncResult(DefaultFuture defaultFuture) throws Exception {
        SyncResult result;
        FullHttpResponse response = (FullHttpResponse) defaultFuture.get();
        ByteBuf content = response.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        HttpHeaders headers = response.headers();
        result = new SyncResult(bytes, getHeaderMap(headers));
        return result;
    }

    private Map<String, String> getHeaderMap(HttpHeaders headers) {
        if (headers == null) {
            return new HashMap<>();
        }
        Map<String, String> headerMap = new HashMap<>();
        List<Map.Entry<String, String>> entries = headers.entries();
        for (Map.Entry<String, String> entry : entries) {
            headerMap.put(entry.getKey(), entry.getValue());
        }
        return headerMap;
    }
}
