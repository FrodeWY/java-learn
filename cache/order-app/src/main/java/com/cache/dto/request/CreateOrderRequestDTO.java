package com.cache.dto.request;

import java.util.List;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName CreateOrderRequestDTO
 * @Description TODO
 * @Date 2021/9/4 上午12:19
 * @Version 1.0
 */
@Data
public class CreateOrderRequestDTO {

  /**
   * 订单主数据
   */
  private Order order;
  /**
   * 订单明细
   */
  private List<OrderDetail> orderDetailList;

  @Data
  public static class Order {

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
     * 币种
     */
    private String currency;

  }

  @Data
  public static class OrderDetail {

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
     * 赠送积分(单个)
     */
    private Integer unitExchangeScore;
    /**
     * 币种
     */
    private String currency;

  }
}
