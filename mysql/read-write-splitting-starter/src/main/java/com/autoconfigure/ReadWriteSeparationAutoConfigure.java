package com.autoconfigure;

import com.readwrite.config.DynamicDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author wangyang
 * @ClassName ReadWriteSpearatonAutoConfigure
 * @Description TODO
 * @Date 2021/8/7 下午2:55
 * @Version 1.0
 */
@Configuration
@ComponentScan(value = "com.readwrite")
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = DynamicDataSourceProperties.class)
public class ReadWriteSeparationAutoConfigure {

}
