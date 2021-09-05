package com.cache.entity;

import java.io.Serializable;

/**
 * 订单详情表(OrderDetail)实体类
 *
 * @author makejava
 * @since 2021-09-03 23:36:07
 */
public class OrderDetail implements Serializable {

  private static final long serialVersionUID = 237867734102675931L;

  private Long id;
  /**
   * 订单id
   */
  private Long orderId;
  /**
   * sku 编码
   */
  private String skuCode;
  /**
   * 数量
   */
  private Integer quantity;
  /**
   * 单价
   */
  private String unitPrice;
  /**
   * 税费
   */
  private String taxAmount;
  /**
   * 实际金额
   */
  private String actualAmount;
  /**
   * 数据是否可用(1:可用,0:不可用(逻辑删除))
   */
  private Integer enabled;
  /**
   * 创建时间
   */
  private Long createTime;
  /**
   * 修改时间
   */
  private Long updateTime;
  /**
   * 赠送积分(单个)
   */
  private Integer unitExchangeScore;
  /**
   * 币种
   */
  private String currency;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getSkuCode() {
    return skuCode;
  }

  public void setSkuCode(String skuCode) {
    this.skuCode = skuCode;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(String taxAmount) {
    this.taxAmount = taxAmount;
  }

  public String getActualAmount() {
    return actualAmount;
  }

  public void setActualAmount(String actualAmount) {
    this.actualAmount = actualAmount;
  }

  public Integer getEnabled() {
    return enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getUnitExchangeScore() {
    return unitExchangeScore;
  }

  public void setUnitExchangeScore(Integer unitExchangeScore) {
    this.unitExchangeScore = unitExchangeScore;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

}