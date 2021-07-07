package third_week.com.simple.gateway.invoker;

import third_week.com.simple.gateway.result.Result;

import java.net.URL;

public interface Invoker {

  default Result get(String url) {
    throw new UnsupportedOperationException();
  }

  ;

  default Result post(String url) {
    throw new UnsupportedOperationException();
  }

  ;

  default Result delete(String url) {
    throw new UnsupportedOperationException();
  }

  ;

}
