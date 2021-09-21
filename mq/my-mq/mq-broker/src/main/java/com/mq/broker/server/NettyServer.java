package com.mq.broker.server;

import com.mq.broker.processor.GetRemoteOffsetInfoProcessor;
import com.mq.broker.processor.Processor;
import com.mq.broker.processor.PullMessageProcessor;
import com.mq.broker.processor.SendMessageProcessor;
import com.mq.broker.server.handler.CodecHandler;
import com.mq.broker.server.handler.ServiceHandler;
import com.mq.broker.store.MemoryMessageStore;
import com.mq.broker.store.MessageStore;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 * @ClassName NettyServer
 * @Description TODO
 * @Date 2021/9/19 下午9:56
 * @Version 1.0
 */

public class NettyServer implements Server {

  private ServerBootstrap serverBootstrap;
  private NioEventLoopGroup boss;
  private NioEventLoopGroup work;
  private final Integer port;
  private Channel channel;
  private ServiceHandler serviceHandler;
  private static List<Processor> defaultProcessor;

  static {
    defaultProcessor = new ArrayList<>();
    MessageStore messageStore = new MemoryMessageStore();
    PullMessageProcessor pullMessageProcessor = new PullMessageProcessor(messageStore);
    SendMessageProcessor sendMessageProcessor = new SendMessageProcessor(messageStore);
    GetRemoteOffsetInfoProcessor getRemoteOffsetInfoProcessor = new GetRemoteOffsetInfoProcessor(messageStore);
    defaultProcessor.add(pullMessageProcessor);
    defaultProcessor.add(sendMessageProcessor);
    defaultProcessor.add(getRemoteOffsetInfoProcessor);
  }

  public NettyServer(List<Processor> processors, Integer port) {
    this.port = port;
    this.serviceHandler = new ServiceHandler(processors);
  }

  public NettyServer(Integer port) {
    this.port = port;
    this.serviceHandler = new ServiceHandler(defaultProcessor);
  }

  @Override
  public void start() throws InterruptedException {
    boss = new NioEventLoopGroup(1);
    work = new NioEventLoopGroup();
    serverBootstrap = new ServerBootstrap().group(boss, work)
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_REUSEADDR, true)
        .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
        .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline()
                .addLast(new CodecHandler())
                .addLast(serviceHandler)
                .addLast(new LengthFieldBasedFrameDecoder(6 * 1024, 0, 2, 0, 2))
                .addLast(new LengthFieldPrepender(2));
          }
        });
    ChannelFuture channelFuture = serverBootstrap.bind(port);
    channelFuture.syncUninterruptibly();
    channel = channelFuture.channel();
    System.out.println("server started...");
    channel.closeFuture().sync();
  }

  @Override
  public void close() {
    if (channel != null) {
      channel.close();
    }
    if (work != null) {
      work.shutdownGracefully();
    }
    if (boss != null) {
      boss.shutdownGracefully();
    }
    serviceHandler.getChannels().forEach(ChannelOutboundInvoker::close);
  }
}
