package com.activemq.consumer;

import java.util.concurrent.CompletableFuture;
import javax.jms.Destination;
import org.springframework.lang.Nullable;

public interface Consumer {

  <T> void start(Destination destination, Class<T> clazz, @Nullable CompletableFuture<T> future);

  void close();
}
