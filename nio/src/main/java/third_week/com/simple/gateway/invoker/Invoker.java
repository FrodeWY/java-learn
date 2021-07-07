package third_week.com.simple.gateway.invoker;

import third_week.com.simple.gateway.result.Result;

import java.net.URL;

public interface Invoker {

    Result get(String url);

    Result post(String url);

    Result delete(String url);

}
