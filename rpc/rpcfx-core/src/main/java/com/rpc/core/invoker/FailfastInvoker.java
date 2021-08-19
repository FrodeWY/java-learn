package com.rpc.core.invoker;

import com.rpc.core.api.Directory;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.LoadBalancer;
import com.rpc.core.api.RpcfxRequest;
import com.rpc.core.api.RpcfxResponse;

import java.util.List;

/**
 * @author wangyang
 * @ClassName FailFastInvoker
 * @Description TODO
 * @Date 2021/8/18 下午12:26
 * @Version 1.0
 */
public class FailfastInvoker implements Invoker {

    public static final String NAME = "failfast";
    private final Directory directory;
    private final LoadBalancer loadBalancer;

    public FailfastInvoker(Directory dictionary, LoadBalancer loadBalancer) {
        this.directory = dictionary;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public RpcfxResponse invoke(RpcfxRequest request) {
        List<Invoker> invokerList = directory.getInvokers(request.getServiceClass());
        Invoker invoker = loadBalancer.select(invokerList);
        RpcfxResponse response ;
        try {
            response = invoker.invoke(request);
            response.setStatus(true);
        } catch (Exception e) {
            response = new RpcfxResponse(e);
            response.setStatus(false);
        }
        return response;
    }
}
