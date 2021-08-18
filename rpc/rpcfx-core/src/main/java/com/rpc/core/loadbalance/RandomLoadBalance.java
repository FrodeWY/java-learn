package com.rpc.core.loadbalance;

import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import java.util.List;
import java.util.Random;

/**
 * @author wangyang
 * @ClassName RandomLoadBalance
 * @Description TODO
 * @Date 2021/8/18 下午8:45
 * @Version 1.0
 */
public class RandomLoadBalance implements LoadBalancer {

  public static final String NAME = "random";

  @Override
  public Invoker select(List<Invoker> invokerList) {
    Random random = new Random(invokerList.size());
    return invokerList.get(random.nextInt());
  }
}
