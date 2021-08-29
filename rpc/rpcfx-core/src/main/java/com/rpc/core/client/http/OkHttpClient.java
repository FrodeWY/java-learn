package com.rpc.core.client.http;

import com.rpc.core.api.Client;
import com.rpc.core.api.Codec;
import com.rpc.core.common.RegistryConstants;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.connection.RealCall;

/**
 * @author wangyang
 * @ClassName OkHttpClient
 * @Description TODO
 * @Date 2021/8/18 下午3:08
 * @Version 1.0
 */
public class OkHttpClient implements Client {

  public static final String NAME = "okHttp";
  private static okhttp3.OkHttpClient CLIENT;
  private final String serverUrl;
  private final Codec codec;
  private final String scheme = "http://";

  static {
    CLIENT = new okhttp3.OkHttpClient().newBuilder()
        .callTimeout(2, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)
        .pingInterval(6, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(40, 60, TimeUnit.SECONDS))
        .build();
  }

  public OkHttpClient(String serverUrl, Codec codec) {
    this.serverUrl = serverUrl.substring(0, serverUrl.indexOf(RegistryConstants.QUESTION_MARK));
    this.codec = codec;
  }

  @Override
  public void destroy() {
    CLIENT = null;
  }

  @Override
  public RpcfxResponse send(RpcfxRequest request) {
    RpcfxResponse result;
    try {
      RequestBody requestBody = RequestBody.create(codec.encode(request), MediaType.parse("application/json;charset=utf-8"));
      Request getRequest = new Builder().url(scheme + serverUrl).post(requestBody).build();
      RealCall call = (RealCall) CLIENT.newCall(getRequest);
      Response execute = call.execute();
      ResponseBody body = execute.body();
      byte[] bytes = body == null ? null : body.bytes();
      result = codec.decode(bytes, RpcfxResponse.class);
      result.setResult(codec.decode((String) result.getResult(), request.getReturnType()));
    } catch (Exception e) {
      result = new RpcfxResponse(e);
    }
    return result;
  }
}
