package third_week.com.simple.gateway.router;

import java.net.URL;

public abstract class AbstractRouter implements Router {

    protected String getServerName(String uri) {
        try {
            final String[] split = uri.split("/");
            if (split.length > 0) {
                return split[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
