package com.rpc.autoconfigure;

import com.rpc.autoconfigure.config.RpcConfigProperties;
import com.rpc.core.api.*;
import com.rpc.core.client.OkHttpClient;
import com.rpc.core.cluster.ClusterFactory;
import com.rpc.core.cluster.FailfastCluster;
import com.rpc.core.codec.CodecFactory;
import com.rpc.core.codec.FastjsonCodec;
import com.rpc.core.filter.GenericInBoundFilter;
import com.rpc.core.loadbalance.LoadBalanceFactory;
import com.rpc.core.loadbalance.RandomLoadBalance;
import com.rpc.core.protocol.RegistryProtocol;
import com.rpc.core.registry.RegistryFactory;
import com.rpc.core.registry.ZookeeperRegistry;
import com.rpc.core.router.TestRouter;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(value = "com.rpc.autoconfigure")
@EnableConfigurationProperties(value = RpcConfigProperties.class)
public class AutoConfiguration {

  @Bean
  public Filter genericInBoundFilter() {
    return new GenericInBoundFilter();
  }

  @Bean
  public Router TestRouter() {
    return new TestRouter();
  }

  @Bean
  public RegistryProtocol registryProtocol(RpcConfigProperties properties, List<Router> routerList) {

    if (properties.getConsumer() == null) {
      return null;
    }
    return getRegistryProtocol(properties.getConsumer(), routerList);
  }

  private RegistryProtocol getRegistryProtocol(RpcConfigProperties.Consumer properties, List<Router> routerList) {
    String clientType = getOrDefault(properties.getClient(), OkHttpClient.NAME);
    String clusterType = getOrDefault(properties.getCluster(), FailfastCluster.NAME);
    String codecType = getOrDefault(properties.getCodec(), FastjsonCodec.NAME);
    RpcConfigProperties.Registry registryProperties = properties.getRegistry();
    String loadBalance = getOrDefault(properties.getLoadBalance(), RandomLoadBalance.NAME);
    Codec codec = CodecFactory.getCodec(codecType);
    if (registryProperties == null) {
      registryProperties = new RpcConfigProperties.Registry();
    }
    String registryType = getOrDefault(registryProperties.getRegistry(), ZookeeperRegistry.NAME);
    Registry registry = RegistryFactory.getRegistry(registryType, registryProperties.getRegistryAddress());
    Cluster cluster = ClusterFactory.getCluster(clusterType);
    LoadBalancer loadBalancer = LoadBalanceFactory.getLoadBalancer(loadBalance);
    return new RegistryProtocol(cluster, loadBalancer, clientType, registry, routerList, codec);
  }

  private String getOrDefault(String type, String defaultValue) {
    if (StringUtils.isBlank(type)) {
      return defaultValue;
    }
    return type;
  }
}
