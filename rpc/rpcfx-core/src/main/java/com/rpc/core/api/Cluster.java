package com.rpc.core.api;


public interface Cluster {

  Invoker join(Directory directory, LoadBalancer loadBalancer);
}
