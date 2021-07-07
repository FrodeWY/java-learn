package third_week.com.simple.gateway.result;

import org.apache.http.Header;

import java.util.Map;

/**
 * 同步调用结果
 */
public class SyncResult implements Result {
    private byte[] body;
    private Throwable throwable;
    private Map<String, String> header;

    public SyncResult(Throwable throwable) {
        this.throwable = throwable;
    }

    public SyncResult(byte[] body) {
        this(body, null);
    }

    public SyncResult(byte[] body, Map<String, String> header) {
        this.body = body;
        this.header = header;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public String getHeader(String headerName) {
        if (header == null) {
            return null;
        }
        return header.get(headerName);
    }

    @Override
    public boolean hasException() {
        return throwable != null;
    }

    @Override
    public Throwable getException() {
        return throwable;
    }
}
