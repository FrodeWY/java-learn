package two_week.homework;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


/**
 * @author wangyang
 * @ClassName Http
 * @Description TODO
 * @Date 2021/7/3 上午10:11
 * @Version 1.0
 */
public class HttpClientTest {

  public static void main(String[] args) {
    doGet("http://localhost:8801");
  }

  public static void doGet(String url) {
    try {

      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (HttpStatus.SC_OK == statusCode) {
        System.out
            .println("Get request success, response:" + EntityUtils.toString(response.getEntity()));
      } else {
        System.out.println("Get request failed");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
