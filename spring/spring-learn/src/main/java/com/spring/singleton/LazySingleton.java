package com.spring.singleton;

import java.io.Serializable;

/**
 * @author wangyang
 * @ClassName LazySingleton
 * @Description 懒汉模式
 * @Date 2021/7/25 上午10:51
 * @Version 1.0
 */
public class LazySingleton implements Serializable {

  private volatile static LazySingleton instance;

  private LazySingleton() {
    //防止反射破坏单例
    synchronized (LazySingleton.class) {
      if (instance != null) {
        throw new RuntimeException("创建了两个实例");
      }
    }
  }

  public void print() {
    System.out.println(this.getClass() + "   LazySingleton,print");
  }

  public static LazySingleton getInstance() {
    if (instance == null) {
      synchronized (LazySingleton.class) {
        if (instance == null) {
          instance = new LazySingleton();
        }
      }
    }
    return instance;
  }

  /**
   * 防止反序列化破坏单例
   */
  private Object readResolve() {
    return instance;
  }
}
