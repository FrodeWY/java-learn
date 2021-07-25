package com.spring.singleton;

import java.io.Serializable;

/**
 * @author wangyang
 * @ClassName EnumSingleton
 * @Description TODO
 * @Date 2021/7/25 上午11:10
 * @Version 1.0
 */
public enum EnumSingleton {
  INSTANCE;

  private void doSomething() {
    System.out.println("do something!!");
  }

  public static EnumSingleton getInstance() {
    return INSTANCE;
  }


}
