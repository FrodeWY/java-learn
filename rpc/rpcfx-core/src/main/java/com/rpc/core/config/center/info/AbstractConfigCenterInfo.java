package com.rpc.core.config.center.info;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author wangyang
 * @ClassName AbstractConfigCenterInfo
 * @Description TODO
 * @Date 2021/8/28 上午8:57
 * @Version 1.0
 */
@Data
public class AbstractConfigCenterInfo implements ConfigCenterInfo {

  /**
   * 配置中心地址
   */
  private String url;
  /**
   * 用户名
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  /**
   * 命名空间
   */
  private String namespace;
  /**
   * 配置信息id
   */
  private String dataId;


  @Override
  public boolean checkValid() {
    return !(StringUtils.isBlank(url) || StringUtils.isBlank(namespace) || StringUtils.isBlank(dataId));
  }

  @Override
  public String centerUniqueKey() {
    return url + username + password;
  }
}
