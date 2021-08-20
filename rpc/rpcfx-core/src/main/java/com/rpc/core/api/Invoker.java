package com.rpc.core.api;

import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;

public interface Invoker {

  RpcfxResponse invoke(RpcfxRequest request);
}
