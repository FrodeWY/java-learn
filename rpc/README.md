# 项目结构

- rpcfx-core

  > rpcfx框架核心模块,rpc框架实现的核心包

  1. 负责远程代理类的创建
  2. 实现基于Zookeeper的服务注册与发现
  3. 支持http server和基于tcp的Netty server 两种启动方式
  4. 实现了Zookeeper作为配置中心
  5. 实现了FailfastCluster集群容错策略
  6. 提供RandomLoadBalance负载均衡器
  7. 实现fastjson和msgpack两种序列化方式
  8. 提供泛化调用能力
  9. 支持服务的分组和版本管理

  

- rpcfx-demo-api 

  >  公共接口包

- rpcfx-core-autoconfigure

  >  Spring 自动装配包

  1. 提供@RpcService,@RpcReference注解,分别提供声明式服务注册和发现
  2. 
  3. 提供加载配置中心配置的能力

# 核心功能

- 服务注册

  通过**RpcServiceRegistryBeanPostProcessor** 实现声明式服务注册,对使用了@RpcService的服务进行注册,注册中心默认实现ZookeeperRegistry

- 服务发现

  通过**RpcReferenceAutowiredBeanPostProcessor** 实现声明式服务发现,对使用了@RpcReference的字段,进行自动注入RPC代理,实现远程调用透明化

- 注册中心

  实现了配置中心**ZookeeperConfigCenter**,提供远程配置的读取能力,**ConfigCenterPostProcessor**实现了订阅配置的自动装配

- 分组和版本管理

  服务的注册与发现都支持配置group和version,通过**GroupAndVersionRouter**实现分组和版本管理







