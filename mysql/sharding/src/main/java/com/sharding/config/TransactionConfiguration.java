package com.sharding.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wangyang
 * @ClassName TransactionConfiguration
 * @Description TODO
 * @Date 2021/8/12 下午10:14
 * @Version 1.0
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {

  @Bean
  public PlatformTransactionManager txManager(final DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

}
