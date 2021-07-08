package third_week.com.simple.gateway.router;

import io.netty.handler.codec.http.FullHttpRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
            helloWorldServerUrl.add("http://localhost:8801");
            helloWorldServerUrl.add("http://localhost:8802");
            helloWorldServerUrl.add("http://localhost:8803");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BACKEND_SERVER_CACHE.put("helloWorld", helloWorldServerUrl);
        BACKEND_SERVER_CACHE.put("nettyServer", Collections.singletonList("http://localhost:8808"));

    }

    @Override
    public List<String> route(FullHttpRequest request) {
        String uri = request.uri();
        String serverName = getServerName(uri);
        return BACKEND_SERVER_CACHE.get(serverName);
    }

}
