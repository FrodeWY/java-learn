package com.mq.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName PutResult
 * @Description TODO
 * @Date 2021/9/17 下午9:56
 * @Version 1.0
 */
@Data
@Builder
public class PutResult {

  /**
   * topic
   */
  private String topic;
  /**
   * 当前topic 队列的最大offset
   */
  private Integer maxOffset;
  /**
   * 存储消息结果编码
   *
   * @see com.mq.pojo.enums.ResponseCodeEnum
   */
  private Integer putResultCode;
  /**
   * 错误信息
   */
  private String errorMessage;


}
