package com.rpc.rpcfx.demo.provider;

import com.rpc.autoconfigure.annotation.RpcService;
import com.rpc.rpcfx.demo.api.User;
import com.rpc.rpcfx.demo.api.UserService;

@RpcService(group = "group1", version = "1.0.1")
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
