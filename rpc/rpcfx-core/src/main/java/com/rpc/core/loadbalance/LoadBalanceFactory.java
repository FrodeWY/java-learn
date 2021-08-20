package com.rpc.core.loadbalance;

import com.rpc.core.api.LoadBalancer;

/**
 * @author wangyang
 * @ClassName LoadBalanceFactory
 * @Description TODO
 * @Date 2021/8/18 下午9:01
 * @Version 1.0
 */
public class LoadBalanceFactory {

  public static LoadBalancer getLoadBalancer(String type) {
    LoadBalancer loadBalancer;
    if (RandomLoadBalance.NAME.equals(type)) {
      loadBalancer = new RandomLoadBalance();
    } else {
      throw new IllegalArgumentException("not found class type is :" + type + " loadBalancer");
    }
    return loadBalancer;
  }
}
