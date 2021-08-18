package com.rpc.autoconfigure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName RpcConfigProperties
 * @Description TODO
 * @Date 2021/8/18 下午6:31
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "rpc.props")
@Component
@Getter
@Setter
public class RpcConfigProperties {

  private String registry;
  private String cluster;
  private String client;
  private String codec;
  private String loadBalance;
  private String registryAddress;
  private String proxy;
}
