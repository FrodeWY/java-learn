package com.rpc.core.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RpcfxResponse {

  private Object result;
  private boolean status;
  private Exception exception;

  public RpcfxResponse(Exception exception) {
    this.exception = exception;
  }
}
