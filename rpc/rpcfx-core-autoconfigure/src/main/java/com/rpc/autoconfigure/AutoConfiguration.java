package com.rpc.autoconfigure;

import com.rpc.autoconfigure.config.RpcConfigProperties;
import com.rpc.core.api.Filter;
import com.rpc.core.api.Router;
import com.rpc.core.filter.GenericInBoundFilter;
import com.rpc.core.router.TestRouter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

}
