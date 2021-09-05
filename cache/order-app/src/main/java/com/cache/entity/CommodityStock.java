package com.cache.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品库存表(CommodityStock)实体类
 *
 * @author makejava
 * @since 2021-09-03 23:36:07
 */
@Data
public class CommodityStock implements Serializable {

  private static final long serialVersionUID = -91231253162913856L;
  /**
   * 库存主键
   */
  private Long id;

  private String skuCode;
  /**
   * 记录是否可用 0 不可用, 1 可用
   */
  private Integer enabled;
  /**
   * 库存数量
   */
  private Long stockAmount;
  /**
   * 创建时间
   */
  private Object createTime;
  /**
   * 修改时间
   */
  private Object updateTime;

  private Integer version;


}