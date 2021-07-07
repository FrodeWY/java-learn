package third_week.com.simple.gateway.invoker;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import third_week.com.simple.gateway.exception.RequestFailedException;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.result.SyncResult;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpClientInvoker implements Invoker {
    private final HttpClient CLIENT = HttpClientBuilder.create().setMaxConnTotal(40).setMaxConnPerRoute(8).setKeepAliveStrategy((response, context) -> 6000).build();

    public Result doGet(String url) {
        SyncResult result;
        try {
            final URI uri = new URI(url);
            HttpGet request = new HttpGet(uri);
            HttpResponse response = CLIENT.execute(request);
            HttpEntity entity = response.getEntity();
//            final Map<String, String> headerMap = getHeaderMap(response.getAllHeaders());
            result = new SyncResult(EntityUtils.toByteArray(entity));
        } catch (Exception e) {
            result = new SyncResult(e);
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
    public Result get(String url) {
        return doGet(url);
    }

    @Override
    public Result post(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result delete(String url) {
        throw new UnsupportedOperationException();
    }
}
