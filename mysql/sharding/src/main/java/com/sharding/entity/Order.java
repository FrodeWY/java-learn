package com.sharding.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 订单表(正向)(Order)实体类
 *
 * @author makejava
 * @since 2021-08-06 12:49:41
 */
@Data
public class Order implements Serializable {

  private static final long serialVersionUID = -33911695877874088L;

  private Long id;
  /**
   * 订单id
   */
  private Integer orderId;
  /**
   * 买家会员id
   */
  private Long buyerMemberId;
  /**
   * 父订单id
   */
  private String parentOrderId;
  /**
   * 订单金额
   */
  private String orderAmount;
  /**
   * 支付金额
   */
  private String payAmount;
  /**
   * 优惠金额
   */
  private String discountAmount;
  /**
   * 税费
   */
  private String taxAmount;
  /**
   * 积分抵扣
   */
  private String scoreDeduction;
  /**
   * 赠送积分
   */
  private Integer score;
  /**
   * 成长值
   */
  private Integer growthValue;
  /**
   * -2:超时取消,-1:用户取消,0:待支付,1:待发货,2:待收货,3:交易完成,4:待退款,5:仅退款,6:退货退款
   */
  private Integer status;
  /**
   * 收货地址
   */
  private String receiveAddress;
  /**
   * 收货人姓名
   */
  private String receiverName;
  /**
   * 收货人电话
   */
  private Long receiverPhone;
  /**
   * 订单创建时间
   */
  private Long createTime;
  /**
   * 订单修改时间
   */
  private Long updateTime;
  /**
   * 数据是否可用(1:可用,0:不可用(逻辑删除))
   */
  private Integer enabled;
  /**
   * 币种
   */
  private String currency;


}