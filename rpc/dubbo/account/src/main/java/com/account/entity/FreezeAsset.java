package com.account.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 资产冻结表(FreezeAsset)实体类
 *
 * @author makejava
 * @since 2021-08-21 22:02:41
 */
@Data
public class FreezeAsset implements Serializable {

  private static final long serialVersionUID = 943984178741715395L;

  private Long id;
  /**
   * 账户id
   */
  private Long accountId;
  /**
   * 准备转入的账户id
   */
  private Long toAccountId;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 账户类型
   */
  private Integer accountType;
  /**
   * 锁定金额
   */
  private String amount;
  /**
   * 准备转入的金额
   */
  private String toAmount;

  /**
   * 交易流水号
   */
  private String transactionNo;
  /**
   * 创建时间
   */
  private Long createTime;
  /**
   * 修改时间
   */
  private Long modifyTime;
  /**
   * 冻结状态(0:冻结,1:解冻)
   */
  private Integer freezeStatus;
  /**
   * 版本
   */
  private Integer version;


}