package com.rpc.core.api;

import java.util.List;

public interface LoadBalancer {

  Invoker select(List<Invoker> invokerList);

}
