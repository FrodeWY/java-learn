package com.readwrite.aspect;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author wangyang
 * @ClassName ReadWriteContext
 * @Description TODO
 * @Date 2021/8/6 下午3:02
 * @Version 1.0
 */
public class ReadWriteContext {

  private static final TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<String>();

  public static void set(String dataSourceName) {
    threadLocal.set(dataSourceName);
  }

  public static String get() {
    return threadLocal.get();
  }

  public static void remove() {
    threadLocal.remove();
  }
}
