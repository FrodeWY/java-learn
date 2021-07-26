package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName Person
 * @Description TODO
 * @Date 2021/7/22 下午9:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

  private Long id;

  private String name;

  private String address;

  private Integer age;


  public Person(String name, String address, Integer age) {
    this(null, name, address, age);
  }


}
