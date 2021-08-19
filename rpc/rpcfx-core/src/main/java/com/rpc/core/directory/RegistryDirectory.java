package com.rpc.core.directory;

import com.rpc.core.api.Client;
import com.rpc.core.api.Codec;
import com.rpc.core.api.Directory;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.Listener;
import com.rpc.core.api.Registry;
import com.rpc.core.client.ClientFactory;
import com.rpc.core.common.RegistryConstants;
import com.rpc.core.invoker.RpcInvoker;
import com.rpc.core.router.RouterChain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangyang
 * @ClassName RegistryDirectory
 * @Description TODO
 * @Date 2021/8/18 下午1:42
 * @Version 1.0
 */
public class RegistryDirectory implements Directory, Listener {

    private List<Invoker> invokers = new CopyOnWriteArrayList<>();

    private final RouterChain routerChain;

    private final Registry registry;

    private final String clientType;

    private final Codec codec;

    private final String serviceName;

    public RegistryDirectory(RouterChain routerChain, Registry registry, String clientType, Codec codec, String serviceName) {
        this.routerChain = routerChain;
        this.registry = registry;
        this.clientType = clientType;
        this.codec = codec;
        this.serviceName = serviceName;
    }

    @Override
    public List<Invoker> getInvokers(String serviceName) {
        if (invokers == null || invokers.size() == 0) {
            throw new RuntimeException("not exist available " + serviceName + " service provider");
        }
        synchronized (this) {
            List<Invoker> unmodifiableList = Collections.unmodifiableList(invokers);
            return routerChain.route(unmodifiableList);
        }
    }

    //  public void register(String port) {
//    try {
//      String registerPath = getRegisterPath(port);
//      registry.register(registerPath);
//    } catch (UnknownHostException e) {
//      e.printStackTrace();
//    }
//  }
//
//  private String getRegisterPath(String port) throws UnknownHostException {
//    String root = "/myRpc/";
//    return root + serviceName + "/" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
//  }
    private String getSubscribePath(String serviceName) {
        return RegistryConstants.ROOT + RegistryConstants.SPLITTER + serviceName;
    }

    public void subscribe(String serviceName) {
        registry.subscribe(getSubscribePath(serviceName), this);
    }

    @Override
    public void notify(List<String> updateChildren) {
        if (updateChildren == null || updateChildren.size() == 0) {
            invokers = new ArrayList<>();
        }

        List<Invoker> newInvokers = new ArrayList<>();
        for (String updateChild : updateChildren) {
            Client client = ClientFactory.getClient(clientType, updateChild, codec);
            RpcInvoker rpcInvoker = new RpcInvoker(client, updateChild, serviceName);
            newInvokers.add(rpcInvoker);
        }
        synchronized (this) {
            invokers = newInvokers;
        }
    }
}
