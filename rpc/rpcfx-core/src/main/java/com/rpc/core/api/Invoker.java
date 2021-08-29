package com.rpc.core.api;

import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import java.util.Map;

public interface Invoker {

  RpcfxResponse invoke(RpcfxRequest request);

  /**
   * 和指定的group,version是否匹配
   *
   * @param group 分组
   * @param version 版本号
   */
  boolean isMatch(String group, String version);

  /**
   * 销毁
   */
  void destroy();

  /**
   * 获取对应的提供者url
   */
  String getProviderUrl();

  /**
   * 使用配置刷新invoker
   *
   * @param configure 配置
   */
  void refresh(Map<String, String> configure);
}
