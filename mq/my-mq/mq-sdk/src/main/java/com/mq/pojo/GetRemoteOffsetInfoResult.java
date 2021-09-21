package com.mq.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName GetOffsetResult
 * @Description TODO
 * @Date 2021/9/20 下午10:31
 * @Version 1.0
 */
@Data
@Builder
public class GetRemoteOffsetInfoResult {

  /**
   * 当前消费offset
   */
  private Integer offset;
  /**
   * 当前topic 最大offset
   */
  private Integer maxOffset;

  /**
   * 响应编码
   *
   * @see com.mq.pojo.enums.ResponseCodeEnum
   */
  private Integer code;

  /**
   * 错误信息
   */
  private String errorMessage;
}
