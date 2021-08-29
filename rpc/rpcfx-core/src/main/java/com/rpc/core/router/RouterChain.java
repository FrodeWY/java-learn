package com.rpc.core.router;

import com.rpc.core.api.Invoker;
import com.rpc.core.api.Router;
import java.util.List;

/**
 * @author wangyang
 * @ClassName RouterChain
 * @Description TODO
 * @Date 2021/8/18 下午2:02
 * @Version 1.0
 */
public class RouterChain implements Router {

  private final List<Router> routers;

  public RouterChain(List<Router> routers) {
    this.routers = routers;
  }

  @Override
  public List<Invoker> route(List<Invoker> invokers, String group, String version) {
    if (routers == null || routers.size() == 0 || invokers == null || invokers.size() == 0) {
      return invokers;
    }
    List<Invoker> invokerList = invokers;
    for (Router router : routers) {
      invokerList = router.route(invokerList, group, version);
    }
    return invokerList;
  }
}
