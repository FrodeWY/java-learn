package com.mq.pojo.enums;

/**
 * @author wangyang
 * @ClassName ConsumeMessageStatus
 * @Description TODO
 * @Date 2021/9/20 下午8:47
 * @Version 1.0
 */
public enum ConsumeMessageStatus {
  CONSUME_SUCCESS(1),
  CONSUME_FAIL(-1);


  private Integer code;

  public Integer getCode() {
    return code;
  }

  ConsumeMessageStatus(Integer code) {
    this.code = code;
  }
}
