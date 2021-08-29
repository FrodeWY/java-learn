package com.rpc.core.router;

import com.rpc.core.api.Invoker;
import com.rpc.core.api.Router;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @ClassName GroupAndVersionRouter
 * @Description 选择符合group和version的 invoker列表
 * @Date 2021/8/27 下午9:51
 * @Version 1.0
 */
public class GroupAndVersionRouter implements Router {

  @Override
  public List<Invoker> route(List<Invoker> invokers, String group, String version) {
    List<Invoker> invokerList = new ArrayList<>(invokers);
    return invokerList.stream().filter(invoker -> invoker.isMatch(group, version)).collect(Collectors.toList());
  }
}
