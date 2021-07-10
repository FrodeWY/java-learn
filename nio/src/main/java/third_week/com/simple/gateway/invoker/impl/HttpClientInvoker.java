package third_week.com.simple.gateway.invoker.impl;

import io.netty.channel.ChannelHandlerContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.result.impl.SyncResult;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient实现http调用
 */
public class HttpClientInvoker implements Invoker {

  private static final CloseableHttpClient CLIENT;

  static {
    CLIENT = HttpClientBuilder.create().setMaxConnTotal(40).setMaxConnPerRoute(8)
        .setKeepAliveStrategy((response, context) -> 6000).build();
  }

  public Result doGet(String url) {
    SyncResult result;
    HttpGet request=null;
    try {
      final URI uri = new URI(url);
      request = new HttpGet(uri);
      HttpResponse response = CLIENT.execute(request);
      HttpEntity entity = response.getEntity();
      final Map<String, String> headerMap = getHeaderMap(response.getAllHeaders());
      result = new SyncResult(EntityUtils.toByteArray(entity), headerMap);
    } catch (Exception e) {
      result = new SyncResult(e);
    }finally {
      if(request!=null){
        request.releaseConnection();
      }
    }
    return result;
  }

  private Map<String, String> getHeaderMap(Header[] allHeaders) {
    Map<String, String> map = new HashMap<>();
    if (allHeaders == null) {
      return map;
    }
    Arrays.stream(allHeaders).forEach((header) -> map.put(header.getName(), header.getValue()));
    return map;
  }

  @Override
  public Result get(String url, ChannelHandlerContext ctx) {
    return doGet(url);
  }

}
