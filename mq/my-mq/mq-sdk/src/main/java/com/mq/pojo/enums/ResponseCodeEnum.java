package com.mq.pojo.enums;

/**
 * @author wangyang
 * @ClassName ResponseCodeEnum
 * @Description 响应编码
 * @Date 2021/9/20 上午10:38
 * @Version 1.0
 */
public enum ResponseCodeEnum {
  SUCCESS(1),
  FAIL(-1);
  private final Integer code;

  public Integer getCode() {
    return code;
  }

  ResponseCodeEnum(Integer code) {
    this.code = code;
  }
}
