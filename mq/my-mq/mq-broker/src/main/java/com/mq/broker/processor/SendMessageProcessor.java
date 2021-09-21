package com.mq.broker.processor;

import com.alibaba.fastjson.JSON;
import com.mq.broker.store.MessageStore;
import com.mq.pojo.PutResult;
import com.mq.pojo.RemoteCommand;
import com.mq.pojo.enums.CommandCodeEnum;
import com.mq.pojo.request.SendMessageRequest;
import com.mq.pojo.response.SendMessageResponse;
import io.netty.channel.Channel;

/**
 * @author wangyang
 * @ClassName SendMessageProcessor
 * @Description TODO
 * @Date 2021/9/20 下午1:44
 * @Version 1.0
 */
public class SendMessageProcessor implements Processor {

  private MessageStore messageStore;

  public SendMessageProcessor(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  @Override
  public RemoteCommand process(RemoteCommand request, Channel channel) {
    SendMessageRequest sendMessageRequest = JSON.parseObject(request.getBody(), SendMessageRequest.class);
    PutResult putResult = messageStore.putMessage(sendMessageRequest);
    SendMessageResponse sendMessageResponse = SendMessageResponse.builder().errorMessage(putResult.getErrorMessage())
        .code(putResult.getPutResultCode())
        .maxOffset(putResult.getMaxOffset())
        .topic(putResult.getTopic())
        .build();

    RemoteCommand response = RemoteCommand.builder()
        .requestId(request.getRequestId())
        .body(JSON.toJSONString(sendMessageResponse))
        .commandCode(CommandCodeEnum.SEND_MESSAGE_RESPONSE.getCode())
        .build();
    return response;
  }

  @Override
  public boolean matcher(RemoteCommand request) {
    return CommandCodeEnum.SEND_MESSAGE_REQUEST.getCode().equals(request.getCommandCode());
  }
}
