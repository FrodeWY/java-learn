package com.mq.broker.store;

import com.mq.pojo.GetRemoteOffsetInfoResult;
import com.mq.pojo.PullResult;
import com.mq.pojo.PutResult;
import com.mq.pojo.request.PullMessageRequest;
import com.mq.pojo.request.SendMessageRequest;

/**
 * 消息存储
 */
public interface MessageStore {

  /**
   * 存储消息
   */
  PutResult putMessage(SendMessageRequest request);

  /**
   * 拉取消息
   *
   * @return 消息列表
   */
  PullResult pullMessage(PullMessageRequest pullMessageRequest);

  /**
   * 获取指定topic,指定消费组的偏移量信息
   *
   * @param topic 主题
   * @param consumerGroupName 消费组
   */
  GetRemoteOffsetInfoResult getConsumerOffset(String topic, String consumerGroupName);
}
