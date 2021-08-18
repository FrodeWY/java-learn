package com.rpc.rpcfx.demo.provider;

import com.rpc.rpcfx.demo.api.User;
import com.rpc.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
