package com.account.dao.bo;

import lombok.Data;

/**
 * @author wangyang
 * @ClassName UpdateAccountAmountBO
 * @Description TODO
 * @Date 2021/8/22 上午12:42
 * @Version 1.0
 */
@Data
public class UpdateAccountAmountBO {

  /**
   * 账户id
   */
  private Long accountId;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 修改时间
   */
  private Long modifyTime;


  /**
   * 账户金额
   */
  private String accountBalance;
}
