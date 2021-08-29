package com.rpc.core.directory;

import com.rpc.core.api.Directory;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.Registry;
import com.rpc.core.api.RegistryCenterListener;
import com.rpc.core.common.RegistryConstants;
import com.rpc.core.invoker.RpcInvoker;
import com.rpc.core.router.RouterChain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @ClassName RegistryDirectory
 * @Description TODO
 * @Date 2021/8/18 下午1:42
 * @Version 1.0
 */
public class RegistryDirectory implements Directory, RegistryCenterListener {

  private List<Invoker> invokers = new CopyOnWriteArrayList<>();

  private final RouterChain routerChain;

  private final Registry registry;

  private final String clientType;

  private final String codecType;

  private final String serviceName;

  public RegistryDirectory(RouterChain routerChain, Registry registry, String clientType, String codecType, String serviceName) {
    this.routerChain = routerChain;
    this.registry = registry;
    this.clientType = clientType;
    this.codecType = codecType;
    this.serviceName = serviceName;
  }

  @Override
  public List<Invoker> getInvokers(String serviceName, String group, String version) {
    if (invokers == null || invokers.size() == 0) {
      throw new RuntimeException("not exist available " + serviceName + " service provider");
    }
    synchronized (this) {
      List<Invoker> unmodifiableList = Collections.unmodifiableList(invokers);
      return routerChain.route(unmodifiableList, group, version);
    }
  }

  private String getSubscribePath(String serviceName) {
    return RegistryConstants.SPLITTER + serviceName;
  }

  public void subscribe() {
    registry.subscribe(getSubscribePath(serviceName), this);
  }

  @Override
  public void destroy() {
    if (invokers != null) {
      invokers.forEach(Invoker::destroy);
    }
  }

  @Override
  public void notify(List<String> updateChildren) {
    updateInvokers(updateChildren);
  }

  private void updateInvokers(List<String> updateChildren) {
    if (updateChildren == null || updateChildren.size() == 0) {
      invokers = new ArrayList<>();
      return;
    }
    List<Invoker> retainProviderInvokerList = invokers.stream().filter(invoker -> {
      String providerUrl = invoker.getProviderUrl();
      if (!updateChildren.contains(providerUrl)) {
        invoker.destroy();
        return false;
      }
      updateChildren.remove(providerUrl);
      return true;
    }).collect(Collectors.toList());
    for (String newProviderUrl : updateChildren) {
      RpcInvoker rpcInvoker = new RpcInvoker(clientType, newProviderUrl, serviceName, codecType);
      retainProviderInvokerList.add(rpcInvoker);
    }
    synchronized (this) {
      invokers = retainProviderInvokerList;
    }
  }
}
