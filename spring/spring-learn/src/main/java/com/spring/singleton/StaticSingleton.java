package com.spring.singleton;

import java.io.Serializable;

/**
 * @author wangyang
 * @ClassName StaticSingleton
 * @Description TODO
 * @Date 2021/7/25 上午11:17
 * @Version 1.0
 */
public class StaticSingleton implements Serializable {

  private static final StaticSingleton INSTANCE;

  static {
    INSTANCE = new StaticSingleton();
  }

  private StaticSingleton() {
    if (INSTANCE != null) {
      throw new RuntimeException("创建了两个实例");
    }
  }

  public static StaticSingleton getInstance() {
    return INSTANCE;
  }

  /**
   * 防止反序列化破坏单例
   */
  private Object readResolve() {
    return INSTANCE;
  }
}
