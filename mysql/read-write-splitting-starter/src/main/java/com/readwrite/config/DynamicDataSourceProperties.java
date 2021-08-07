package com.readwrite.config;

import com.zaxxer.hikari.HikariConfig;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangyang
 * @ClassName DynamicDataSouceProperties
 * @Description TODO
 * @Date 2021/8/6 下午6:06
 * @Version 1.0
 */
@Data
@ConfigurationProperties(value = "readwrite.datasource")
public class DynamicDataSourceProperties {

  /**
   * 从库url
   */
  private List<DataSourceProperties> slave;
  /**
   * 主库url
   */
  private DataSourceProperties master;
  private String name;

  public static HikariConfig getHikariConfig(DataSourceProperties properties) {
    HikariConfig hikariConfig = new HikariConfig();
    if (properties.getUrl() == null || properties.getUsername() == null) {
      throw new IllegalArgumentException("Missing required parameters");
    }
    if (properties.getConnectionTimeout() != null) {
      hikariConfig.setConnectionTimeout(properties.getConnectionTimeout());
    }
    if (properties.getIdleTimeout() != null) {
      hikariConfig.setIdleTimeout(properties.getIdleTimeout());
    }
    if (properties.getMaxLifetime() != null) {
      hikariConfig.setMaxLifetime(properties.getMaxLifetime());
    }
    if (properties.getMaxPoolSize() != null) {
      hikariConfig.setMaximumPoolSize(properties.getMaxPoolSize());
    }
    if (properties.getMinIdle() != null) {
      hikariConfig.setMinimumIdle(properties.getMinIdle());
    }
    if (properties.getPassword() != null) {
      hikariConfig.setPassword(properties.getPassword());
    }
    hikariConfig.setUsername(properties.getUsername());
    hikariConfig.setJdbcUrl(properties.getUrl());
    if (properties.getIsAutoCommit()) {
      hikariConfig.setAutoCommit(properties.getIsAutoCommit());
    }
    if (properties.getPoolName() != null) {
      hikariConfig.setPoolName(properties.getPoolName());
    }
    return hikariConfig;
  }

  @Data
  public static class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private Long connectionTimeout;
    private Long idleTimeout;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer minIdle;
    private Boolean isAutoCommit;
    private String poolName;
  }
}
