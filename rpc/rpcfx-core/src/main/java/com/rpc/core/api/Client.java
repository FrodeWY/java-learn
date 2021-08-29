package com.rpc.core.api;

import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;

public interface Client {

  /**
   * 发送请求
   */
  RpcfxResponse send(RpcfxRequest request);

  /**
   * 销毁客户端
   */
  void destroy();
}
