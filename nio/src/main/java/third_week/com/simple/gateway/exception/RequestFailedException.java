package third_week.com.simple.gateway.exception;

public class RequestFailedException extends RuntimeException {

    public RequestFailedException(String message) {
        super(message);
    }
}
