package com.rpc.core.client;

import com.rpc.core.api.Client;
import com.rpc.core.api.Codec;
import com.rpc.core.api.RpcfxRequest;
import com.rpc.core.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClient implements Client {
    public static final String NAME = "netty";
    private final String serverUrl;
    private final Codec codec;
    private final String scheme = "http://";

    public NettyClient(String serverUrl, Codec codec) {
        this.serverUrl = serverUrl;
        this.codec = codec;
    }

    @Override
    public RpcfxResponse send(RpcfxRequest request) {
        InternalNettyClient client;
        RpcfxResponse result;
        Channel channel = null;
        try {
            URL url = new URL(scheme+serverUrl);
            System.out.println("request:" + serverUrl);
            String host = url.getHost();
            int port = url.getPort();
            client = InternalNettyClient.getClient(host, port);
            channel = client.getChannel();
            DefaultFuture defaultFuture = send(channel, request);
            result = getResponse(defaultFuture, request);
        } catch (Exception e) {
            e.printStackTrace();
            result = new RpcfxResponse(e);
        } finally {
            if (channel != null) {
                DefaultFuture.remove(channel.id().asLongText());
                channel.close();
            }
        }
        return result;
    }

    private static class InternalNettyClient {

        private static final Map<String, InternalNettyClient> CLIENT_CACHE = new ConcurrentHashMap<>();

        private final EventLoopGroup workerGroup;
        private final String uniqueKey;
        private final Bootstrap bootstrap;
        private final String host;
        private final Integer port;

        InternalNettyClient(String host, int port) {
            this.host = host;
            this.port = port;
            uniqueKey = generateUniqueKey(host, port);
            workerGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
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

        }

        public static InternalNettyClient getClient(String host, int port) {
            String uniqueKey = generateUniqueKey(host, port);
            if (CLIENT_CACHE.containsKey(uniqueKey)) {
                return CLIENT_CACHE.get(uniqueKey);
            }
            InternalNettyClient nettyClient = new InternalNettyClient(host, port);
            CLIENT_CACHE.put(uniqueKey, nettyClient);
            return nettyClient;
        }

        private static String generateUniqueKey(String host, int port) {
            return host + "#" + port;
        }

        public Channel getChannel() {
            try {
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                return channelFuture.channel();
            } catch (InterruptedException e) {
                System.out.println("get channel failed" + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        public void close() {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            CLIENT_CACHE.remove(uniqueKey);
        }
    }


    public DefaultFuture send(Channel channel, Object object) {
        if (object == null) {
            object = "";
        }
        channel.write(object);
        channel.flush();
        return new DefaultFuture(channel);
    }

    @NotNull
    private RpcfxResponse getResponse(DefaultFuture defaultFuture, RpcfxRequest request) throws Exception {
        RpcfxResponse result;
        FullHttpResponse response = (FullHttpResponse) defaultFuture.get();
        ByteBuf content = response.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        result = codec.decode(bytes, RpcfxResponse.class);
        result.setResult(codec.decode((String) result.getResult(), request.getReturnType()));
        return result;
    }

}
