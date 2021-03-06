package com.rpc.core.common;

import lombok.Data;

@Data
public class RpcfxRequest {
    private String serviceClass;
    private String method;
    private Boolean generic;
    private Object[] params;
    private String[] parameterTypes;
    private Class<?> returnType;
}
