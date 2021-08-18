package com.rpc.core.api;

/**
 * 泛型调用
 */
public interface GenericService {

  /**
   *
   * @param serviceName 服务名称
   * @param method 方法名称
   * @param parameterTypes 参数类型
   * @param args 参数
   * @return 调用结果
   */
  Object invoke(String serviceName,String method, String[] parameterTypes, Object[] args );
}
