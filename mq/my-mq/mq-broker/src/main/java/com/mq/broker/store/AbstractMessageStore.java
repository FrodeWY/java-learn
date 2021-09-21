package com.mq.broker.store;

import com.alibaba.fastjson.JSON;
import com.mq.pojo.PullResult;
import com.mq.pojo.PutResult;
import com.mq.pojo.message.Message;
import com.mq.pojo.request.PullMessageRequest;
import com.mq.pojo.request.SendMessageRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName AbstractMessageStore
 * @Description TODO
 * @Date 2021/9/17 下午11:26
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractMessageStore implements MessageStore {

  @Override
  public PutResult putMessage(SendMessageRequest request) {
    PutResult putResult = doPutMessage(request.getTopic(), request.getMessageList());

    log.info("put message:{}", JSON.toJSONString(request));
    afterPutMessageHook(putResult);
    return putResult;
  }

  @Override
  public PullResult pullMessage(PullMessageRequest pullMessageRequest) {
    log.info("pull message:{}", JSON.toJSONString(pullMessageRequest));
    PullResult pullResult = doPullMessage(pullMessageRequest.getTopic(), pullMessageRequest.getBatchSize(), pullMessageRequest.getOffset(),
        pullMessageRequest.getConsumeModel(), pullMessageRequest.getConsumeGroupName());
    afterPullMessageHook(pullResult);

    return pullResult;
  }

  /**
   * 拉取消息核心逻辑
   *
   * @param topic 主题
   * @param batchSize 消息个数
   * @param offset 起始偏移量
   * @return 消息列表
   */
  protected abstract PullResult doPullMessage(String topic, Integer batchSize, Integer offset, Integer consumeMode, String consumeGroupName);

  /**
   * 存储消息的核心逻辑
   *
   * @param topic 主题
   * @param messageList 消息列表
   * @return 存储结果
   */
  protected abstract PutResult doPutMessage(String topic, List<Message> messageList);

  /**
   * 存储消息后的hook
   */
  protected void afterPutMessageHook(PutResult putResult) {
  }

  /**
   * 存储消息后的hook
   */
  protected void afterPullMessageHook(PullResult pullResult) {
  }

}
