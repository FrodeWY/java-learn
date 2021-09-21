package com.mq.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName GetConsumeGroupOffsetResponse
 * @Description 获取消费组offset 信息响应
 * @Date 2021/9/20 下午10:24
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRemoteOffsetInfoResponse {

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
