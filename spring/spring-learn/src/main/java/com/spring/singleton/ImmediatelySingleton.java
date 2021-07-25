package com.spring.singleton;

import java.io.Serializable;

/**
 * @author wangyang
 * @ClassName LazySingleton
 * @Description 饿汉模式
 * @Date 2021/7/25 上午10:51
 * @Version 1.0
 */
public class ImmediatelySingleton implements Serializable {

  private static final ImmediatelySingleton INSTANCE = new ImmediatelySingleton();

  private ImmediatelySingleton() {
    //防止反射破坏单例
    synchronized (ImmediatelySingleton.class) {
      if (INSTANCE != null) {
        throw new RuntimeException("创建了两个实例");
      }
    }
  }

  public void print() {
    System.out.println(this + "   LazySingleton,print");
  }

  public static ImmediatelySingleton getInstance() {
    return INSTANCE;
  }

  /**
   * 防止反序列化破坏单例
   */
  private Object readResolve() {
    return INSTANCE;
  }


}
