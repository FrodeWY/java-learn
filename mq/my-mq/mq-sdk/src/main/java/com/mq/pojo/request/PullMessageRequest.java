package com.mq.pojo.request;

import com.mq.pojo.enums.ConsumeModelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName PullMessageRequest
 * @Description 拉取消息请求体
 * @Date 2021/9/17 下午10:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PullMessageRequest {

  /**
   * 消费的主题
   */
  private String topic;
  /**
   * 消息偏移量,只在Broadcasting 模式下生效
   */
  private Integer offset;
  /**
   * 获取消息个数
   */
  private Integer batchSize;
  /**
   * 消费组名
   */
  private String consumeGroupName;
  /**
   * 消费模式
   */
  private Integer consumeModel;


  public static PullMessageRequest Broadcasting(String topic, Integer offset, Integer batchSize, String consumeGroupName) {
    return new PullMessageRequest(topic, offset, batchSize, consumeGroupName, ConsumeModelEnum.BROADCASTING.getCode());
  }

  public static PullMessageRequest Clustering(String topic, Integer batchSize, String consumeGroupName) {
    return new PullMessageRequest(topic, null, batchSize, consumeGroupName, ConsumeModelEnum.CLUSTERING.getCode());
  }
}
