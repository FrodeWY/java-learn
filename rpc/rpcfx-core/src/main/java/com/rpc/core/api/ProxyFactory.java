package com.rpc.core.api;

/**
 * @author wangyang
 * @ClassName ProxyFactory
 * @Description TODO
 * @Date 2021/8/18 下午9:55
 * @Version 1.0
 */
public interface ProxyFactory {

  public <T> T proxy();
}
