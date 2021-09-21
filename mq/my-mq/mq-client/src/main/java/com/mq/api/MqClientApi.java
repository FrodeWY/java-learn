package com.mq.api;

import com.alibaba.fastjson.JSON;
import com.mq.client.Client;
import com.mq.pojo.DefaultFuture;
import com.mq.pojo.RemoteCommand;
import com.mq.pojo.enums.CommandCodeEnum;
import com.mq.pojo.message.Message;
import com.mq.pojo.request.GetRemoteOffsetInfoRequest;
import com.mq.pojo.request.PullMessageRequest;
import com.mq.pojo.request.SendMessageRequest;
import java.util.List;
import java.util.UUID;

/**
 * @author wangyang
 * @ClassName MqClientApi
 * @Description TODO
 * @Date 2021/9/20 下午8:27
 * @Version 1.0
 */
public class MqClientApi {

  private Client client;

  public MqClientApi(Client client) {
    this.client = client;
  }

  /**
   * 投递消息
   *
   * @param messages 投的的消息列表
   * @param topic 主题
   */
  public DefaultFuture sendMessage(List<Message> messages, String topic) {
    SendMessageRequest sendMessageRequest = new SendMessageRequest(topic, messages);
    RemoteCommand remoteCommand = RemoteCommand.builder().requestId(UUID.randomUUID().toString())
        .body(JSON.toJSONString(sendMessageRequest))
        .commandCode(CommandCodeEnum.SEND_MESSAGE_REQUEST.getCode())
        .build();
    return client.send(remoteCommand);
  }

  /**
   * 拉取消息
   */
  public DefaultFuture pullMessage(PullMessageRequest pullMessageRequest) {

    RemoteCommand remoteCommand = RemoteCommand.builder().requestId(UUID.randomUUID().toString())
        .body(JSON.toJSONString(pullMessageRequest))
        .commandCode(CommandCodeEnum.PULL_MESSAGE_REQUEST.getCode())
        .build();
    return client.send(remoteCommand);
  }

  /**
   * 获取broker中记录的消费组的offset信息
   *
   * @param topic 主题
   * @param consumeGroupName 消费组
   */
  public DefaultFuture getRemoteOffsetInfo(String topic, String consumeGroupName) {
    GetRemoteOffsetInfoRequest getRemoteOffsetInfoRequest = new GetRemoteOffsetInfoRequest(topic, consumeGroupName);
    RemoteCommand remoteCommand = RemoteCommand.builder().requestId(UUID.randomUUID().toString())
        .body(JSON.toJSONString(getRemoteOffsetInfoRequest))
        .commandCode(CommandCodeEnum.GET_CONSUME_GROUP_OFFSET_REQUEST.getCode())
        .build();
    return client.send(remoteCommand);
  }
}
