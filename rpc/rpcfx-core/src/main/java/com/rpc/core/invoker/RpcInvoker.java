package com.rpc.core.invoker;

import com.rpc.core.api.Client;
import com.rpc.core.api.Invoker;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;

/**
 * @author wangyang
 * @ClassName RpcInvoker
 * @Description TODO
 * @Date 2021/8/18 上午11:54
 * @Version 1.0
 */
public class RpcInvoker implements Invoker {

  private final Client client;
  private final String url;
  private final String serviceName;

  public RpcInvoker(Client client, String url, String serviceName) {
    this.client = client;
    this.url = url;
    this.serviceName = serviceName;
  }

  @Override
  public RpcfxResponse invoke(RpcfxRequest request) {
    return client.send(request);
  }
}
