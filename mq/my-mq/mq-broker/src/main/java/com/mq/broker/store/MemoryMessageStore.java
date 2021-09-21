package com.mq.broker.store;

import com.mq.pojo.GetRemoteOffsetInfoResult;
import com.mq.pojo.PullResult;
import com.mq.pojo.PutResult;
import com.mq.pojo.enums.ConsumeModelEnum;
import com.mq.pojo.enums.ResponseCodeEnum;
import com.mq.pojo.message.Message;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyang
 * @ClassName MemoryMessageStore
 * @Description 使用内存存储消息
 * @Date 2021/9/17 下午9:53
 * @Version 1.0
 */
public class MemoryMessageStore extends AbstractMessageStore {

  private final ConcurrentHashMap<String/*topic*/, EvictEarliestNodeLinkedHashMap<Integer, Message>/*offset:message*/> memoryStore =
      new ConcurrentHashMap<>();

  private final ConcurrentHashMap<String /*topic*/, Object> topicLock = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String /*topic*/, Object> consumeGroupLock = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String /*topic*/, Integer/*max Offset*/> topicMaxOffsetCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String/*topic*/, ConcurrentHashMap<String/*consumerGroupName*/, Integer/*nextBeginOffset*/>> offsetCache
      = new ConcurrentHashMap<>();

  @Override
  public GetRemoteOffsetInfoResult getConsumerOffset(String topic, String consumerGroupName) {
    try {
      if (memoryStore.get(topic) == null) {
        throw new IllegalArgumentException("topic not exist");
      }
      if (StringUtil.isNullOrEmpty(consumerGroupName)) {
        throw new IllegalArgumentException("consumerGroupName Can't be empty");
      }
      ConcurrentHashMap<String, Integer> topicOffsetInfo = offsetCache.computeIfAbsent(topic, k -> new ConcurrentHashMap<>());
      Integer offset = topicOffsetInfo.computeIfAbsent(consumerGroupName, k -> 1);
      Integer maxOffset = topicMaxOffsetCache.get(topic);
      return GetRemoteOffsetInfoResult.builder().code(ResponseCodeEnum.SUCCESS.getCode()).maxOffset(maxOffset).offset(offset).build();
    } catch (IllegalArgumentException e) {
      return GetRemoteOffsetInfoResult.builder().code(ResponseCodeEnum.FAIL.getCode()).errorMessage(e.getMessage()).build();
    }
  }

  @Override
  public PutResult doPutMessage(String topic, List<Message> messageList) {
    try {
      if (StringUtil.isNullOrEmpty(topic)) {
        throw new IllegalArgumentException("topic Can't be empty");
      }
      EvictEarliestNodeLinkedHashMap<Integer, Message> queue = memoryStore.computeIfAbsent(topic, k -> new EvictEarliestNodeLinkedHashMap<>());
      int currentOffset = topicMaxOffsetCache.computeIfAbsent(topic, k -> 1);
      synchronized (getTopicLock(topic)) {
        for (Message message : messageList) {
          queue.put(currentOffset++, message);
        }
      }
      topicMaxOffsetCache.put(topic, currentOffset);
    } catch (Exception e) {
      PutResult.builder().putResultCode(ResponseCodeEnum.FAIL.getCode()).maxOffset(topicMaxOffsetCache.get(topic)).topic(topic)
          .errorMessage(e.getMessage()).build();
    }
    return PutResult.builder().putResultCode(ResponseCodeEnum.SUCCESS.getCode()).maxOffset(topicMaxOffsetCache.get(topic)).topic(topic).build();
  }

  @Override
  protected PullResult doPullMessage(String topic, Integer batchSize, Integer offset, Integer consumeMode, String consumeGroupName) {
    List<Message> messageList = null;
    Integer startOffset = null;
    Integer endOffset = null;
    boolean isClusterMode = ConsumeModelEnum.CLUSTERING.getCode().equals(consumeMode);
    Integer topicMaxOffset = topicMaxOffsetCache.get(topic);
    try {
      if (StringUtil.isNullOrEmpty(topic)) {
        throw new IllegalArgumentException("topic Can't be empty");
      }
      if (StringUtil.isNullOrEmpty(consumeGroupName)) {
        throw new IllegalArgumentException("consumeGroupName Can't be empty");
      }
      if (batchSize == null || batchSize < 0 || batchSize > 10000) {
        throw new IllegalArgumentException("batchSize must be >0 and <10000");
      }
      if (offset == null) {
        throw new IllegalArgumentException("offset Can't be empty");
      }
      if (consumeMode == null) {
        throw new IllegalArgumentException("consumeMode Can't be empty");
      }
      if (ConsumeModelEnum.getByValue(consumeMode) == null) {
        throw new IllegalArgumentException("consumeMode Does not support");
      }
      EvictEarliestNodeLinkedHashMap<Integer, Message> queue = memoryStore.get(topic);
      if (queue == null) {
        throw new IllegalArgumentException("topic not exist");
      }
      synchronized (getConsumeGroupLock(topic, consumeGroupName)) {
        if (isClusterMode) {
          //集群模式下,如果请求的offset已经过时了,就将当前消费组的消费起始偏移量作为offset
          Integer nextStartOffset = offsetCache.get(topic).get(consumeGroupName);
          if (offset < nextStartOffset) {
            offset = nextStartOffset;
          }
        }
        messageList = new ArrayList<>(batchSize);
        startOffset = offset;
        endOffset = startOffset + batchSize;
        if (startOffset >= topicMaxOffset) {
          return PullResult.builder()
              .maxOffset(topicMaxOffset)
              .nextBeginOffset(startOffset)
              .pullResultCode(ResponseCodeEnum.SUCCESS.getCode())
              .build();
        }
        if (endOffset >= topicMaxOffset) {
          endOffset = topicMaxOffset;
        }
        for (int i = startOffset; i < endOffset; i++) {
          messageList.add(queue.get(i));
        }
        ConcurrentHashMap<String, Integer> consumeGroupOffsetCacheMap = offsetCache.get(topic);
        if (isClusterMode) {
          consumeGroupOffsetCacheMap.put(consumeGroupName, endOffset);
        }
      }
    } catch (Exception e) {
      return PullResult.builder()
          .errorMessage(e.getMessage())
          .maxOffset(topicMaxOffset)
          .nextBeginOffset(startOffset)
          .pullResultCode(ResponseCodeEnum.FAIL.getCode())
          .build();
    }

    return PullResult.builder()
        .messageList(messageList)
        .maxOffset(topicMaxOffset)
        .nextBeginOffset(endOffset)
        .pullResultCode(ResponseCodeEnum.SUCCESS.getCode())
        .build();

  }

  private Object getTopicLock(String topic) {
    return topicLock.computeIfAbsent(topic, k -> new Object());
  }

  private Object getConsumeGroupLock(String topic, String consumeGroupName) {
    return consumeGroupLock.computeIfAbsent(topic + "@" + consumeGroupName, k -> new Object());
  }

  public static class EvictEarliestNodeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;
    private static final int DEFAULT_SIZE = 1 << 20;

    public EvictEarliestNodeLinkedHashMap() {
      this(DEFAULT_SIZE);
    }

    public EvictEarliestNodeLinkedHashMap(int maxSize) {
      super((int) Math.ceil(maxSize / 0.75), 0.75f, false);
      this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
      return super.size() > maxSize;
    }
  }
}
