package seventh.com.pojo;

import lombok.Data;

/**
 * @author wangyang
 * @ClassName Order
 * @Description TODO
 * @Date 2021/8/5 下午2:47
 * @Version 1.0
 */
@Data
public class Order {

  private Long id;
  private String orderId;
  private Long buyerMemberId;
  private String parentOrderId;
  private String orderAmount;
  private String payAmount;
  private String discountAmount;
  private String taxAmount;
  private String scoreDeduction;
  private Integer score;
  private String receiveAddress;
  private Integer growthValue;
  private Integer status;
  private String receiverName;
  private Long receiverPhone;
  private Long createTime;
  private Long updateTime;
  private Integer enabled;
  private String currency;

}
