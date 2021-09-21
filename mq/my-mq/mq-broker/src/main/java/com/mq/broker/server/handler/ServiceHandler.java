package com.mq.broker.server.handler;

import com.alibaba.fastjson.JSON;
import com.mq.broker.processor.Processor;
import com.mq.pojo.RemoteCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName MessageStoreHandler
 * @Description TODO
 * @Date 2021/9/20 上午9:48
 * @Version 1.0
 */
@Slf4j
@Sharable
public class ServiceHandler extends ChannelDuplexHandler {

  private List<Processor> processors;
  private List<Channel> channels = new ArrayList<>();

  public ServiceHandler(List<Processor> processors) {
    this.processors = processors;
  }

  public List<Channel> getChannels() {
    return channels;
  }

  public void addProcessor(Processor processor) {
    if (processors == null) {
      processors = new ArrayList<>();
    }
    processors.add(processor);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    log.info("server channel read {}", JSON.toJSONString(msg));

    for (Processor processor : processors) {
      RemoteCommand remoteCommand = (RemoteCommand) msg;
      if (processor.matcher(remoteCommand)) {
        Channel channel = ctx.channel();
        RemoteCommand process = processor.process(remoteCommand, channel);
        ChannelFuture channelFuture = ctx.writeAndFlush(process);
        channelFuture.awaitUninterruptibly();
        break;
      }
    }

  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    log.info("server channel write {}", JSON.toJSONString(msg));

    Channel channel = ctx.channel();
    ChannelFuture channelFuture = channel.writeAndFlush(msg);
    channelFuture.awaitUninterruptibly();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channels.add(channel);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channels.remove(channel);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }
}
