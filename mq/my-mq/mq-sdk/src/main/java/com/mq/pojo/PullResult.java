package com.mq.pojo;

import com.mq.pojo.message.Message;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName PullResult
 * @Description TODO
 * @Date 2021/9/20 下午2:21
 * @Version 1.0
 */
@Data
@Builder
public class PullResult {

  /**
   * topic
   */
  private String topic;
  /**
   * 当前topic 队列的最大offset
   */
  private Integer maxOffset;
  /**
   * 下次消费起始 offset
   */
  private Integer nextBeginOffset;
  /**
   * 存储消息结果编码
   *
   * @see com.mq.pojo.enums.ResponseCodeEnum
   */
  private Integer pullResultCode;
  /**
   * 错误信息
   */
  private String errorMessage;

  /**
   * 消息列表
   */
  private List<Message> messageList;
}
