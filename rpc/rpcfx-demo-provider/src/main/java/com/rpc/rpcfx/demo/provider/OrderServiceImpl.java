package com.rpc.rpcfx.demo.provider;

import com.rpc.autoconfigure.annotation.RpcService;
import com.rpc.rpcfx.demo.api.Order;
import com.rpc.rpcfx.demo.api.OrderService;

@RpcService(group = "group1", version = "1.0.1")
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
