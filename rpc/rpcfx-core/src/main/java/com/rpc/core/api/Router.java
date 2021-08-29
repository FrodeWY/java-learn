package com.rpc.core.api;

import java.util.List;

public interface Router {

  List<Invoker> route(List<Invoker> invokers, String group, String version);
}
