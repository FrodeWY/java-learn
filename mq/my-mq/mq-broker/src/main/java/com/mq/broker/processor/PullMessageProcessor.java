package com.mq.broker.processor;

import com.alibaba.fastjson.JSON;
import com.mq.broker.store.MessageStore;
import com.mq.pojo.PullResult;
import com.mq.pojo.RemoteCommand;
import com.mq.pojo.enums.CommandCodeEnum;
import com.mq.pojo.request.PullMessageRequest;
import com.mq.pojo.response.PullMessageResponse;
import io.netty.channel.Channel;

/**
 * @author wangyang
 * @ClassName PullMessageProcessor
 * @Description TODO
 * @Date 2021/9/20 下午1:44
 * @Version 1.0
 */
public class PullMessageProcessor implements Processor {

  private MessageStore messageStore;

  public PullMessageProcessor(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  @Override
  public RemoteCommand process(RemoteCommand request, Channel channel) {

    PullMessageRequest pullMessageRequest = JSON.parseObject(request.getBody(), PullMessageRequest.class);
    PullResult pullResult = messageStore.pullMessage(pullMessageRequest);
    PullMessageResponse pullMessageResponse = PullMessageResponse.builder()
        .messageList(pullResult.getMessageList())
        .code(pullResult.getPullResultCode())
        .errorMessage(pullResult.getErrorMessage())
        .maxOffset(pullResult.getMaxOffset())
        .nextBeginOffset(pullResult.getNextBeginOffset())
        .build();

    return RemoteCommand.builder()
        .requestId(request.getRequestId())
        .body(JSON.toJSONString(pullMessageResponse))
        .commandCode(CommandCodeEnum.PULL_MESSAGE_RESPONSE.getCode())
        .build();
  }

  @Override
  public boolean matcher(RemoteCommand request) {
    return CommandCodeEnum.PULL_MESSAGE_REQUEST.getCode().equals(request.getCommandCode());
  }
}
