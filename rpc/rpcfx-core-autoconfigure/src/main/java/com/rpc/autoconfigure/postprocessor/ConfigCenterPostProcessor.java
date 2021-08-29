package com.rpc.autoconfigure.postprocessor;

import com.rpc.core.config.center.ConfigCenter;
import com.rpc.core.config.center.ConfigCenterFactory;
import com.rpc.core.config.center.ZookeeperConfigCenter;
import com.rpc.core.config.center.info.ConfigCenterInfo;
import com.rpc.core.config.center.info.ZookeeperConfigCenterInfo;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName ZookeeperConfigPostPocessor
 * @Description TODO
 * @Date 2021/8/28 上午8:52
 * @Version 1.0
 */
@Component
public class ConfigCenterPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

  private Environment environment;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    String configCenterType = environment.getProperty("config.center.type", ZookeeperConfigCenter.NAME);
    ConfigCenter configCenter = ConfigCenterFactory.getConfigCenter(configCenterType);
    ConfigCenterInfo configCenterInfo = getConfigCenterInfo(configCenterType);
    Properties properties = configCenter.getProperties(configCenterInfo);
    String active = properties.getProperty("spring.profiles.active");
    if (StringUtils.isBlank(active)) {
      active = "";
    } else {
      active = "-" + active;
    }

    PropertySource<?> propertySource = ((StandardEnvironment) environment).getPropertySources()
        .get("applicationConfig: [classpath:/application" + active + ".yml]");
    Map<String, Object> source = ((OriginTrackedMapPropertySource) propertySource).getSource();
    for (Object propertyName : properties.keySet()) {
      source.put(String.valueOf(propertyName), properties.get(propertyName));
    }
    MutablePropertySources propertySources = ((StandardEnvironment) environment).getPropertySources();
    propertySources.addLast(new PropertySource<Properties>("configCenter", properties) {
      @Override
      public Object getProperty(String name) {
        return properties.getProperty(name);
      }
    });
  }

  private ConfigCenterInfo getConfigCenterInfo(String configCenterType) {
    if (ZookeeperConfigCenter.NAME.equals(configCenterType)) {
      ZookeeperConfigCenterInfo zookeeperConfigCenterInfo = new ZookeeperConfigCenterInfo();
      zookeeperConfigCenterInfo.setUrl(environment.getProperty("config.center.info.url"));
      zookeeperConfigCenterInfo.setUsername(environment.getProperty("config.center.info.username"));
      zookeeperConfigCenterInfo.setPassword(environment.getProperty("config.center.info.password"));
      zookeeperConfigCenterInfo.setNamespace(environment.getProperty("config.center.info.namespace"));
      zookeeperConfigCenterInfo.setDataId(environment.getProperty("config.center.info.dataId"));
      return zookeeperConfigCenterInfo;
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
