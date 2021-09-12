package com.activemq.producer;

import javax.jms.Destination;

public interface Producer {

  <T> void send(Destination destination, T message);

  void close();
}
