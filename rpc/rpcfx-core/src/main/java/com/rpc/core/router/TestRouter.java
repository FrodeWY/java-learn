package com.rpc.core.router;

import com.rpc.core.api.Invoker;
import com.rpc.core.api.Router;
import java.util.List;

/**
 * @author wangyang
 * @ClassName TestRouter
 * @Description TODO
 * @Date 2021/8/18 下午11:01
 * @Version 1.0
 */
public class TestRouter implements Router {

  @Override
  public List<Invoker> route(List<Invoker> invokers) {
    return invokers;
  }
}
