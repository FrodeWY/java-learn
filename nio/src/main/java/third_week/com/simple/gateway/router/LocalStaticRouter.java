package third_week.com.simple.gateway.router;

import io.netty.handler.codec.http.FullHttpRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个本地静态路由
 * 所有的可用服务都在static 代码块中配置好
 */
public class LocalStaticRouter extends AbstractRouter {
    private static final Map<String, List<String>> BACKEND_SERVER_CACHE = new ConcurrentHashMap<>();

    static {
        ArrayList<String> helloWorldServerUrl = new ArrayList<>();
        try {
            helloWorldServerUrl.add("http://127.0.0.1:8801");
            helloWorldServerUrl.add("http://127.0.0.1:8802");
            helloWorldServerUrl.add("http://127.0.0.1:8803");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BACKEND_SERVER_CACHE.put("helloWorld", helloWorldServerUrl);
    }

    @Override
    public List<String> route(FullHttpRequest request) {
        String uri = request.uri();
        String serverName = getServerName(uri);
        return BACKEND_SERVER_CACHE.get(serverName);
    }

}
