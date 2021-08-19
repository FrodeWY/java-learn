package com.rpc.autoconfigure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
@Getter
@Setter
public class RpcConfigProperties {

    private Consumer consumer;

    private Provider provider;
    @Getter
    @Setter
    public static class Consumer {
        private String cluster;
        private String client;
        private String codec;
        private String loadBalance;
        private String proxy;
        private Registry registry;
    }
    @Getter
    @Setter
    public static class Provider {
        private String codec;
        private String client;
        private Registry registry;
    }
    @Getter
    @Setter
    public static class Registry {
        private String registryAddress;
        private String registry;
    }
}
