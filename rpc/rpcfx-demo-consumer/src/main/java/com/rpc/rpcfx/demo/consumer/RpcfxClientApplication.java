package com.rpc.rpcfx.demo.consumer;

import com.rpc.core.api.LoadBalancer;
import com.rpc.core.api.Router;
import com.rpc.core.client.Rpcfx;
import com.rpc.rpcfx.demo.api.User;
import com.rpc.rpcfx.demo.api.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById

		UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());

//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
//
//		//
//		UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

//		SpringApplication.run(RpcfxClientApplication.class, args);
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



