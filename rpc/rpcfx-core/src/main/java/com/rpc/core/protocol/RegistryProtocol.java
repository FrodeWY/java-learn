package com.rpc.core.protocol;

import com.rpc.core.api.Cluster;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.api.Protocol;
import com.rpc.core.api.Registry;
import com.rpc.core.api.Router;
import com.rpc.core.directory.RegistryDirectory;
import com.rpc.core.router.RouterChain;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyang
 * @ClassName RegistryProtocol
 * @Description TODO
 * @Date 2021/8/18 下午6:06
 * @Version 1.0
 */
public class RegistryProtocol implements Protocol {

  private final Cluster cluster;
  private final LoadBalancer loadBalancer;
  private final String clientType;
  private final Registry registry;
  private final List<Router> routers;
  private final String codecType;
  private final RouterChain routerChain;
  private static Map<String, RegistryDirectory> CACHE = new ConcurrentHashMap<>();

  public RegistryProtocol(Cluster cluster, LoadBalancer loadBalancer, String clientType, Registry registry,
      List<Router> routers, String codecType) {
    this.cluster = cluster;
    this.loadBalancer = loadBalancer;
    this.clientType = clientType;
    this.registry = registry;
    this.routers = routers;
    this.codecType = codecType;
    this.routerChain = new RouterChain(routers);
  }

  @Override
  public Invoker getInvoker(String serviceName, String group, String version) {
    RegistryDirectory registryDirectory = CACHE
        .computeIfAbsent(serviceName, k -> new RegistryDirectory(routerChain, registry, clientType, codecType, serviceName));
    registryDirectory.subscribe();
    return cluster.join(registryDirectory, loadBalancer, group, version);
  }
}
