package com.rpc.core.protocol;

import com.rpc.core.api.Cluster;
import com.rpc.core.api.Codec;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.api.Protocol;
import com.rpc.core.api.Registry;
import com.rpc.core.api.Router;
import com.rpc.core.directory.RegistryDirectory;
import com.rpc.core.router.RouterChain;

import java.util.List;

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
    private final Codec codec;

    public RegistryProtocol(Cluster cluster, LoadBalancer loadBalancer, String clientType, Registry registry,
                            List<Router> routers, Codec codec) {
        this.cluster = cluster;
        this.loadBalancer = loadBalancer;
        this.clientType = clientType;
        this.registry = registry;
        this.routers = routers;
        this.codec = codec;
    }

    @Override
    public Invoker getInvoker(String serviceName) {
        RouterChain routerChain = new RouterChain(routers);
        RegistryDirectory registryDirectory = new RegistryDirectory(routerChain, registry, clientType, codec, serviceName);
        registryDirectory.subscribe(serviceName);
        return cluster.join(registryDirectory, loadBalancer);
    }
}
