package com.rpc.rpcfx.demo.provider;

import com.rpc.rpcfx.demo.api.OrderService;
import com.rpc.rpcfx.demo.api.Order;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
