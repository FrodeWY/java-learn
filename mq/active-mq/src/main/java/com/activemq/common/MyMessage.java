package com.activemq.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName MyMessage
 * @Description TODO
 * @Date 2021/9/10 下午11:02
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class MyMessage implements Serializable {

  private String name;
  private Integer age;
  private String content;

}
