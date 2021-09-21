package com.mq.client;

import com.alibaba.fastjson.JSON;
import com.mq.client.handler.CodecHandler;
import com.mq.client.handler.ServiceHandler;
import com.mq.pojo.DefaultFuture;
import com.mq.pojo.RemoteCommand;
import com.mq.processor.GetRemoteOffsetInfoResponseProcessor;
import com.mq.processor.Processor;
import com.mq.processor.PullResponseProcessor;
import com.mq.processor.SendResponseProcessor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName NettyClient
 * @Description TODO
 * @Date 2021/9/20 下午3:02
 * @Version 1.0
 */
@Slf4j
public class NettyClient implements Client {

  private Bootstrap bootstrap;
  private NioEventLoopGroup work;
  private Channel channel;
  private List<Processor> processors;
  private static List<Processor> defaultProcessor;

  public NettyClient(List<Processor> processors) {
    this.processors = processors;
  }

  public NettyClient() {
    this.processors = defaultProcessor;
  }


  static {
    defaultProcessor = new ArrayList<>();
    PullResponseProcessor pullResponseProcessor = new PullResponseProcessor();
    SendResponseProcessor sendResponseProcessor = new SendResponseProcessor();
    GetRemoteOffsetInfoResponseProcessor getRemoteOffsetInfoResponseProcessor = new GetRemoteOffsetInfoResponseProcessor();

    defaultProcessor.add(pullResponseProcessor);
    defaultProcessor.add(sendResponseProcessor);
    defaultProcessor.add(getRemoteOffsetInfoResponseProcessor);
  }

  @Override
  public void start(String addr, Integer port) throws InterruptedException {
    bootstrap = new Bootstrap();
    work = new NioEventLoopGroup();
    bootstrap.group(work)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.SO_SNDBUF, 32 * 1024)
//        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.SO_RCVBUF, 32 * 1024)
        .handler(new ChannelInitializer<NioSocketChannel>() {
          @Override
          protected void initChannel(NioSocketChannel ch) throws Exception {
            ch.pipeline()
                .addLast(new CodecHandler())
                .addLast(new ServiceHandler(processors))
                .addLast(new LengthFieldBasedFrameDecoder(6 * 1024, 0, 2, 0, 2))
                .addLast(new LengthFieldPrepender(2));
          }
        });
    ChannelFuture channelFuture = bootstrap.connect(addr, port).sync();
    boolean success = channelFuture.awaitUninterruptibly(2000, TimeUnit.MILLISECONDS);
    if (success && channelFuture.isSuccess()) {
      if (channel != null) {
        channel.close();
      }
      channel = channelFuture.channel();
    }
  }

  @Override
  public DefaultFuture send(RemoteCommand remoteCommand) {
    DefaultFuture defaultFuture = new DefaultFuture(remoteCommand);
    log.info("send {}", JSON.toJSONString(remoteCommand));
    channel.writeAndFlush(remoteCommand);
    return defaultFuture;
  }

  @Override
  public void close() {
    if (channel != null) {
      channel.close();
    }
    if (work != null) {
      work.shutdownGracefully();
    }
  }
}
