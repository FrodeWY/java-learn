package com.rpc.core.invoker;

import com.rpc.core.api.Client;
import com.rpc.core.api.Invoker;
import com.rpc.core.client.ClientFactory;
import com.rpc.core.codec.CodecFactory;
import com.rpc.core.common.RegistryConstants;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import com.rpc.core.utils.ServicePathUtil;
import java.util.Map;

/**
 * @author wangyang
 * @ClassName RpcInvoker
 * @Description TODO
 * @Date 2021/8/18 上午11:54
 * @Version 1.0
 */
public class RpcInvoker implements Invoker {

  private final Client client;
  private final String url;
  private final String serviceName;
  private final String version;
  private final String group;
  private final Map<String, String> paramMap;

  public RpcInvoker(String clientType, String url, String serviceName, String codecType) {
    this.url = url.substring(0, url.indexOf(RegistryConstants.QUESTION_MARK));
    this.serviceName = serviceName;
    this.paramMap = ServicePathUtil.getParamMap(url);
    this.version = paramMap.get(RegistryConstants.VERSION);
    this.group = paramMap.get(RegistryConstants.GROUP);
    this.client = ClientFactory.getClient(clientType, url, CodecFactory.getCodec(codecType));
  }

  @Override
  public RpcfxResponse invoke(RpcfxRequest request) {
    return client.send(request);
  }

  @Override
  public void destroy() {
    client.destroy();
  }

  @Override
  public String getProviderUrl() {
    return url;
  }

  @Override
  public void refresh(Map<String, String> configure) {

  }

  @Override
  public boolean isMatch(String group, String version) {
    return this.group.equals(group) && this.version.equals(version);
  }
}
