package com.spring.singleton;

/**
 * @author wangyang
 * @ClassName StaticInnerClassSingleton
 * @Description 静态内部类
 * @Date 2021/7/25 上午11:17
 * @Version 1.0
 */
public class StaticInnerClassSingleton {

  private static class StaticInnerClassSingletonHolder {

    private static StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
  }

  private StaticInnerClassSingleton() {
    if (StaticInnerClassSingletonHolder.INSTANCE != null) {
      throw new RuntimeException("创建了两个实例");
    }
  }

  public static StaticInnerClassSingleton getInstance() {
    return StaticInnerClassSingletonHolder.INSTANCE;
  }

  /**
   * 防止反序列化破坏单例
   */
  private Object readResolve() {
    return StaticInnerClassSingletonHolder.INSTANCE;
  }
}
