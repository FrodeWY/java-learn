package com.mq.broker.processor;

import com.alibaba.fastjson.JSON;
import com.mq.broker.store.MessageStore;
import com.mq.pojo.GetRemoteOffsetInfoResult;
import com.mq.pojo.RemoteCommand;
import com.mq.pojo.enums.CommandCodeEnum;
import com.mq.pojo.request.GetRemoteOffsetInfoRequest;
import com.mq.pojo.response.GetRemoteOffsetInfoResponse;
import io.netty.channel.Channel;

/**
 * @author wangyang
 * @ClassName SendMessageProcessor
 * @Description TODO
 * @Date 2021/9/20 下午1:44
 * @Version 1.0
 */
public class GetRemoteOffsetInfoProcessor implements Processor {

  private MessageStore messageStore;

  public GetRemoteOffsetInfoProcessor(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  @Override
  public RemoteCommand process(RemoteCommand request, Channel channel) {
    GetRemoteOffsetInfoRequest getRemoteOffsetInfoRequest = JSON.parseObject(request.getBody(), GetRemoteOffsetInfoRequest.class);
    GetRemoteOffsetInfoResult getRemoteOffsetInfoResult = messageStore
        .getConsumerOffset(getRemoteOffsetInfoRequest.getTopic(), getRemoteOffsetInfoRequest.getConsumeGroupName());
    GetRemoteOffsetInfoResponse getRemoteOffsetInfoResponse = GetRemoteOffsetInfoResponse.builder()
        .errorMessage(getRemoteOffsetInfoResult.getErrorMessage())
        .code(getRemoteOffsetInfoResult.getCode())
        .maxOffset(getRemoteOffsetInfoResult.getMaxOffset())
        .offset(getRemoteOffsetInfoResult.getOffset())
        .build();

    return RemoteCommand.builder()
        .requestId(request.getRequestId())
        .body(JSON.toJSONString(getRemoteOffsetInfoResponse))
        .commandCode(CommandCodeEnum.GET_CONSUME_GROUP_OFFSET_RESPONSE.getCode())
        .build();
  }

  @Override
  public boolean matcher(RemoteCommand request) {
    return CommandCodeEnum.GET_CONSUME_GROUP_OFFSET_REQUEST.getCode().equals(request.getCommandCode());
  }
}
