package com.rpc.core.invoker;

import com.rpc.core.api.Directory;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @ClassName FailFastInvoker
 * @Description TODO
 * @Date 2021/8/18 下午12:26
 * @Version 1.0
 */
public class FailfastInvoker implements Invoker {

  public static final String NAME = "failfast";
  private final Directory directory;
  private final LoadBalancer loadBalancer;
  private final String group;
  private final String version;

  public FailfastInvoker(Directory dictionary, LoadBalancer loadBalancer, String group, String version) {
    this.directory = dictionary;
    this.loadBalancer = loadBalancer;
    this.group = group;
    this.version = version;
  }

  @Override
  public void destroy() {
    directory.destroy();
  }

  @Override
  public String getProviderUrl() {
    return null;
  }

  @Override
  public void refresh(Map<String, String> configure) {
  }

  @Override
  public RpcfxResponse invoke(RpcfxRequest request) {
    List<Invoker> invokerList = directory.getInvokers(request.getServiceClass(), group, version);
    if (invokerList == null || invokerList.size() == 0) {
      throw new RuntimeException("没有可用服务提供者");
    }
    Invoker invoker = loadBalancer.select(invokerList);
    RpcfxResponse response;
    try {
      response = invoker.invoke(request);
      response.setStatus(true);
    } catch (Exception e) {
      response = new RpcfxResponse(e);
      response.setStatus(false);
    }
    return response;
  }

  @Override
  public boolean isMatch(String group, String version) {
    return this.group.equals(group) && this.version.equals(version);
  }
}
