package com.mq.processor;

import com.alibaba.fastjson.JSON;
import com.mq.pojo.DefaultFuture;
import com.mq.pojo.RemoteCommand;
import com.mq.pojo.enums.CommandCodeEnum;
import com.mq.pojo.response.PullMessageResponse;
import io.netty.channel.Channel;

/**
 * @author wangyang
 * @ClassName PullResponseProcessor
 * @Description TODO
 * @Date 2021/9/20 下午8:06
 * @Version 1.0
 */
public class PullResponseProcessor implements Processor {

  @Override
  public RemoteCommand process(RemoteCommand command, Channel channel) {
    DefaultFuture future = DefaultFuture.getFuture(command.getRequestId());
    PullMessageResponse pullMessageResponse = JSON.parseObject(command.getBody(), PullMessageResponse.class);
    future.complete(pullMessageResponse);
    return command;
  }

  @Override
  public boolean matcher(RemoteCommand request) {
    return CommandCodeEnum.PULL_MESSAGE_RESPONSE.getCode().equals(request.getCommandCode());
  }
}
