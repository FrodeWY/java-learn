package com.rpc.rpcfx.demo.consumer;

import com.rpc.autoconfigure.annotation.RpcReference;
import com.rpc.core.api.GenericService;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.api.Router;
import com.rpc.core.client.Rpcfx;
import com.rpc.core.protocol.RegistryProtocol;
import com.rpc.core.proxy.JdkProxy;
import com.rpc.rpcfx.demo.api.User;
import com.rpc.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
//@EnableAspectJAutoProxy
@RestController
public class RpcfxClientApplication {

  // 二方库
  // 三方库 lib
  // nexus, userserivce -> userdao -> user
  //

  @RpcReference
  private UserService userService;

  @Autowired
  private RegistryProtocol protocol;

  public static void main(String[] args) {

    // UserService service = new xxx();
    // service.findById

//		User user = userService.findById(1);
//		System.out.println("find user id=1 from server: " + user.getName());

//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
//
//		//
//		UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

    SpringApplication.run(RpcfxClientApplication.class, args);


  }

  @GetMapping(path = "/user/{id}")
  public User getUser(@PathVariable int id) {
    User user = userService.findById(1);
    System.out.println("find user id=" + id + " from server: " + user.getName());
    //泛型调用
    GenericService genericService = new JdkProxy().genericServiceProxy(protocol);
    Object genericResult = genericService
        .invoke("com.rpc.rpcfx.demo.api.UserService", "findById", new String[]{"java.lang.Integer"}, new Object[]{1});
    System.out.println("genericResult = " + genericResult);
    return user;
  }
//	private static class TagRouter implements Router {
//		@Override
//		public List<String> route(List<String> urls) {
//			return urls;
//		}
//	}
//
//	private static class RandomLoadBalancer implements LoadBalancer {
//		@Override
//		public String select(List<String> urls) {
//			return urls.get(0);
//		}
//	}

/*	@Slf4j
	private static class CuicuiFilter implements Filter {
		@Override
		public void filter(RpcfxRequest request) {
			log.info("filter {} -> {}", this.getClass().getName(), request.toString());
		}
	}*/
}



