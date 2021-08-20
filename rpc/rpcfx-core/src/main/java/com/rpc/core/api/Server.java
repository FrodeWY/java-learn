package com.rpc.core.api;

public interface Server {
    void init() throws InterruptedException;

    void close();
}
