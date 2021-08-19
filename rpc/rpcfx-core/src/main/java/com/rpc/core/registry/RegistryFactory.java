package com.rpc.core.registry;

import com.rpc.core.api.Registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyang
 * @ClassName RegistryFactory
 * @Description TODO
 * @Date 2021/8/18 下午8:02
 * @Version 1.0
 */
public class RegistryFactory {

    private static final Map<String, Map<String, Registry>> CACHE = new ConcurrentHashMap<>();

    public static Registry getRegistry(String type, String address) {
        Registry registry;
        if (ZookeeperRegistry.NAME.equals(type)) {

            Map<String, Registry> map = CACHE.computeIfAbsent(address, k -> new ConcurrentHashMap<>());
            registry = map.putIfAbsent(ZookeeperRegistry.NAME, new ZookeeperRegistry(address));
            if(registry==null){
                registry=map.get(ZookeeperRegistry.NAME);
            }
        } else {
            throw new IllegalArgumentException("not found class type is :" + type + " registry");
        }
        return registry;
    }
}
