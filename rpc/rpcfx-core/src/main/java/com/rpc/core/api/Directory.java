package com.rpc.core.api;

import java.util.List;

public interface Directory {

  List<Invoker> getInvokers(String serviceName);

}
