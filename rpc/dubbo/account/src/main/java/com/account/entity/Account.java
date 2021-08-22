package com.account.entity;

import java.io.Serializable;

/**
 * 账户表(Account)实体类
 *
 * @author makejava
 * @since 2021-08-21 22:02:41
 */
public class Account implements Serializable {

  private static final long serialVersionUID = -76237247630701605L;
  /**
   * 账户id
   */
  private Long accountId;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 用户名
   */
  private String accountName;
  /**
   * 账户类型(人民币账户:1,美元账户:2....)
   */
  private Integer accountType;
  /**
   * 账户金额
   */
  private String accountBalance;
  /**
   * 账户状态(-1:注销,0:冻结,1:可用)
   */
  private Integer accountStatus;
  /**
   * 货币
   */
  private String currency;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 修改时间
   */
  private Long modifyTime;
  /**
   * 开户时间
   */
  private Long createTime;


  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public Integer getAccountType() {
    return accountType;
  }

  public void setAccountType(Integer accountType) {
    this.accountType = accountType;
  }

  public String getAccountBalance() {
    return accountBalance;
  }

  public void setAccountBalance(String accountBalance) {
    this.accountBalance = accountBalance;
  }

  public Integer getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(Integer accountStatus) {
    this.accountStatus = accountStatus;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Long getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Long modifyTime) {
    this.modifyTime = modifyTime;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

}