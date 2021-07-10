# simple-gateway

## 基本实现

- 使用Netty作为gateway 网络通信框架

- 实现了基于**OkHttp,HttpClient,NettyClient** 3种方式的**Invoker**,通过Invoker调用后端服务
- 实现了简单的本地静态路由--**LocalStaticRouter**,通过配置本地静态路由,实现简单的服务注册,基于注册的服务进行路由
- 实现了简单的负载均衡器--**RandomLoadBalance**
- 实现了两个过滤器,对请求进行增强:**ElapsedTimeStatisticsFilter**(接口耗时统计过滤器),**HeaderAppendUniqueIdFilter**(添加唯一id过滤器)

## 使用方法

- 配置**LocalStaticRouter** ,将后端服务名和服务地址注册到**LocalStaticRouter**中,例如你有一个helloWorld服务,有三个实例

  ```java
  static {
          ArrayList<String> helloWorldServerUrl = new ArrayList<>();
          try {
              helloWorldServerUrl.add("http://localhost:8801");
              helloWorldServerUrl.add("http://localhost:8802");
              helloWorldServerUrl.add("http://localhost:8803");
          } catch (Exception e) {
              e.printStackTrace();
          }
          BACKEND_SERVER_CACHE.put("helloWorld", helloWorldServerUrl);
          
  
      }
  ```

  

- 当前网关端口8004,通过访问http://localhost:8004/helloWorld/请求helloWorld服务

