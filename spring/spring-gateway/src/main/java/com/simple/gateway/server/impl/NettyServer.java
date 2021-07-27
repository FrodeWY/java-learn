package com.simple.gateway.server.impl;

import com.simple.gateway.filter.Filter;
import com.simple.gateway.filter.FilterChain;
import com.simple.gateway.handler.inbound.NettyServerChannelInBoundHandler;
import com.simple.gateway.handler.outbound.NettyServerChannelOutBoundHandler;
import com.simple.gateway.invoker.Invoker;
import com.simple.gateway.loadbalance.LoadBalance;
import com.simple.gateway.router.Router;
import com.simple.gateway.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
@Component
public class NettyServer implements Server {

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Resource(name = "randomLoadBalance")
    private LoadBalance loadBalance;
    @Resource(name = "localStaticRouter")
    private Router router;
    @Resource(name = "nettyClientInvoker")
    private Invoker invoker;
    @Resource(name = "filterChain")
    private Filter filter;
    @Resource(name = "asyncThreadPoolExecutor")
    private Executor executor;
    /**
     * 是否正在运行
     */
    private volatile boolean isRunning;

    public void init() {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024))
                                .addLast(new NettyServerChannelOutBoundHandler())
                                .addLast(new NettyServerChannelInBoundHandler(loadBalance, router, invoker, filter, executor));
                    }
                });
    }

    @Override
    public void run(int port) {
        init();
        if (isRunning) {
            throw new IllegalStateException("The service is running");
        }
        isRunning = true;
        try {
            final Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
