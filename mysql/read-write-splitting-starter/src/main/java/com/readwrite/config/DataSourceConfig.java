package com.readwrite.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

/**
 * @author wangyang
 * @ClassName DataSourceConfig
 * @Description TODO
 * @Date 2021/8/6 下午6:09
 * @Version 1.0
 */
@Configuration
public class DataSourceConfig {

  private final DynamicDataSourceProperties properties;
  public static final String MASTER = "master";
  public static final String SLAVE = "slave";

  public DataSourceConfig(DynamicDataSourceProperties properties) {
    this.properties = properties;
  }

  @Bean
  @Primary
  public DataSource dataSource() {
    DynamicDataSourceProperties.DataSourceProperties master = properties.getMaster();
    List<DynamicDataSourceProperties.DataSourceProperties> slave = properties.getSlave();
    if (master == null) {
      throw new IllegalArgumentException("DataSource Missing required parameters");
    }
    HikariDataSource masterDataSource = new HikariDataSource(DynamicDataSourceProperties.getHikariConfig(master));
    Map<Object, Object> dataSourceMap = new HashMap<>();
    if (!CollectionUtils.isEmpty(slave)) {
      int i = 0;
      for (DynamicDataSourceProperties.DataSourceProperties dataSourceProperties : slave) {
        HikariDataSource hikariDataSource = new HikariDataSource(DynamicDataSourceProperties.getHikariConfig(dataSourceProperties));
        dataSourceMap.put(SLAVE + i, hikariDataSource);
        i++;
      }
    }
    dataSourceMap.put(MASTER, masterDataSource);
    return new DynamicDataSource(masterDataSource, dataSourceMap);
  }


}
