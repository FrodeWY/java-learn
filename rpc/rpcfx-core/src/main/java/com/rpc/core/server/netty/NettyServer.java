package com.rpc.core.server.netty;

import com.rpc.core.api.Codec;
import com.rpc.core.api.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName NettyServer
 * @Description TODO
 * @Date 2021/8/19 下午11:13
 * @Version 1.0
 */
@Slf4j
public class NettyServer implements Server {
    private Codec codec;
    private final NettyServerHandler serviceHandler;
    private final int port;
    /**
     * Netty server channel
     */
    private Channel channel;


    public NettyServer(Integer port, ChannelHandler channelHandler, Codec codec) {
        this.port = port;
        this.codec = codec;
        serviceHandler = new NettyServerHandler(channelHandler);
    }

    @Override
    public void init() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(63325, 0, 2, 0, 2))
                                .addLast("frameEncoder", new LengthFieldPrepender(2))
                                .addLast("decoder", new RpcfxRequestDecodeHandler(codec))
                                .addLast("encoder", new EncodeHandler(codec))
                                .addLast("serviceHandler", serviceHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(port);
        log.info("netty server start with port:{}", port);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
        channel.closeFuture().sync();
    }

    @Override
    public void close() {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Channel clientChannel : serviceHandler.getChannels()) {
            if (clientChannel != null) {
                clientChannel.close();
            }
        }
    }
}
