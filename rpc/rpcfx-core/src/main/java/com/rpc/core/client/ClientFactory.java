package com.rpc.core.client;

import com.rpc.core.api.Client;
import com.rpc.core.api.Codec;
import com.rpc.core.client.http.OkHttpClient;
import com.rpc.core.client.netty.NettyClient;

/**
 * @author wangyang
 * @ClassName ClientFactory
 * @Description TODO
 * @Date 2021/8/18 下午4:01
 * @Version 1.0
 */
public class ClientFactory {

  public static Client getClient(String type, String url, Codec codec) {
    Client client;
    if (OkHttpClient.NAME.equals(type)) {
      client = new OkHttpClient(url, codec);
    } else if (NettyClient.NAME.equals(type)) {
      client = new NettyClient(url, codec);
    }else {
      throw new IllegalArgumentException("not found class type is :" + type + " client");
    }
    return client;
  }
}
