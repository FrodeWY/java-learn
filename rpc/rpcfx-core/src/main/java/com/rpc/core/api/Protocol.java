package com.rpc.core.api;

/**
 * @author wangyang
 * @ClassName Protocol
 * @Description TODO
 * @Date 2021/8/18 下午5:50
 * @Version 1.0
 */
public interface Protocol {

  Invoker getInvoker(String serviceName);

}
