package com.rpc.core.api;

import java.io.IOException;

public interface Codec {

  byte[] encode(Object object) throws IOException;

  Object decode(byte[] bytes) throws IOException;
}
