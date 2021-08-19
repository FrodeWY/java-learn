package com.rpc.core.api;

import java.io.IOException;

public interface Codec {

    byte[] encode(Object object) throws IOException;

    <T> T decode(byte[] bytes, Class<T> type) throws IOException;

    <T> T decode(String str, Class<T> type) throws IOException;
}
