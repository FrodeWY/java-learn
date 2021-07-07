package third_week.com.simple.gateway.server;

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
import third_week.com.simple.gateway.filter.ElapsedTimeStatisticsFilter;
import third_week.com.simple.gateway.filter.FilterChain;
import third_week.com.simple.gateway.filter.HeaderAppendUniqueIdFilter;
import third_week.com.simple.gateway.handler.inbound.HttpInBoundHandler;
import third_week.com.simple.gateway.invoker.HttpClientInvoker;
import third_week.com.simple.gateway.loadbalance.RandomLoadBalance;
import third_week.com.simple.gateway.router.LocalStaticRouter;

public class NettyServer implements Server {

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private boolean isInit;

    @Override
    public void init() {
        FilterChain filterChain = new FilterChain();
        filterChain.addFilters(new HeaderAppendUniqueIdFilter(), new ElapsedTimeStatisticsFilter());
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
                                .addLast(new HttpInBoundHandler(new RandomLoadBalance(), new LocalStaticRouter(), new HttpClientInvoker(), filterChain));
                    }
                });
        isInit = true;
    }

    @Override
    public void run(int port) {
        if (!isInit) {
            throw new IllegalStateException("The service has not been initialized. Please initialize it first");
        }
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

    public static void main(String[] args) {
        final NettyServer nettyServer = new NettyServer();
        nettyServer.init();
        nettyServer.run(8088);
    }
}
