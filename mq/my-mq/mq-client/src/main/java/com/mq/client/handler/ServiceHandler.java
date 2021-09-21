package com.mq.client.handler;

import com.alibaba.fastjson.JSON;
import com.mq.pojo.RemoteCommand;
import com.mq.processor.Processor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName ServiceHanlder
 * @Description TODO
 * @Date 2021/9/20 下午3:19
 * @Version 1.0
 */
@Slf4j
public class ServiceHandler extends ChannelInboundHandlerAdapter {

  private List<Processor> processorList;

  public ServiceHandler(List<Processor> processorList) {
    this.processorList = processorList;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    log.info("client channel read {}", JSON.toJSONString(msg));
    try {
      RemoteCommand command = (RemoteCommand) msg;
      for (Processor processor : processorList) {
        if (processor.matcher(command)) {
          processor.process(command, ctx.channel());
        }
      }
    } finally {
      ctx.flush();
    }

  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }
}
