package com.account.enums;

/**
 * @author wangyang
 * @ClassName FreezeStatusEnum
 * @Description 冻结记录状态
 * @Date 2021/8/22 上午1:26
 * @Version 1.0
 */
public enum FreezeStatusEnum {
  /**
   * 冻结
   */
  FREEZE(0),
  /**
   * 无效
   */
  INVALID(-1),
  /**
   * 解冻
   */
  UNFREEZE(1);


  private Integer value;

  public Integer getValue() {
    return value;
  }

  FreezeStatusEnum(Integer value) {
    this.value = value;
  }
}
