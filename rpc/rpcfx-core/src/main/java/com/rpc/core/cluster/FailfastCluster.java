package com.rpc.core.cluster;

import com.rpc.core.api.Cluster;
import com.rpc.core.api.Directory;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.invoker.FailfastInvoker;

/**
 * @author wangyang
 * @ClassName FailfastCluster
 * @Description TODO
 * @Date 2021/8/18 下午12:13
 * @Version 1.0
 */
public class FailfastCluster implements Cluster {

  public static String NAME = "failfast";

  @Override
  public Invoker join(Directory directory, LoadBalancer loadBalancer, String group, String version) {
    return new FailfastInvoker(directory, loadBalancer, group, version);
  }
}
