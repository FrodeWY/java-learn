package third_week.com.simple.gateway.invoker.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import kotlin.Pair;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.connection.RealCall;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.result.impl.SyncResult;

/**
 * @author wangyang
 * @ClassName OkHttpInvoker
 * @Description OkHttp 实现http调用
 * @Date 2021/7/7 下午9:01
 * @Version 1.0
 */
public class OkHttpInvoker implements Invoker {

  private static final OkHttpClient CLIENT;

  static {
    CLIENT = new OkHttpClient().newBuilder()
        .callTimeout(2, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)
        .pingInterval(6, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(40, 60, TimeUnit.SECONDS))
        .build();
  }

  @Override
  public Result get(String url, ChannelHandlerContext ctx) {
    SyncResult result;
    Request getRequest = new Builder().url(url).get().build();
    RealCall call = (RealCall)CLIENT.newCall(getRequest);
    try {
      Response execute = call.execute();
      Headers headers = execute.headers();
      Map<String, String> headerMap = getHeaderMap(headers);
      ResponseBody body = execute.body();
      byte[] bytes = body == null ? null : body.bytes();
      result = new SyncResult(bytes, headerMap);
    } catch (IOException e) {
      result = new SyncResult(e);
    }
    return result;
  }

  private Map<String, String> getHeaderMap(Headers headers) {
    if (headers == null) {
      return new HashMap<>();
    }
    Map<String, String> headerMap = new HashMap<>();
    Iterator<Pair<String, String>> iterator = headers.iterator();
    while (iterator.hasNext()) {
      Pair<String, String> next = iterator.next();
      headerMap.put(next.getFirst(), next.getSecond());
    }
    return headerMap;
  }


}
