package com.rpc.autoconfigure.postprocessor;

import com.rpc.autoconfigure.annotation.RpcService;
import com.rpc.autoconfigure.config.RpcConfigProperties;
import com.rpc.core.api.Registry;
import com.rpc.core.common.RegistryConstants;
import com.rpc.core.registry.RegistryFactory;
import com.rpc.core.registry.ZookeeperRegistry;
import com.rpc.core.utils.ServicePathUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RpcServiceRegistryBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, EnvironmentAware {

  private RpcConfigProperties properties;
  private ListableBeanFactory beanFactory;
  private Environment environment;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

    Class<?> targetClass;
    if (AopUtils.isAopProxy(bean)) {
      targetClass = AopUtils.getTargetClass(bean);
    } else {
      targetClass = bean.getClass();
    }

    RpcService annotation = targetClass.getAnnotation(RpcService.class);
    if (annotation == null) {
      return bean;
    }
    if (properties == null) {
      properties = beanFactory.getBean(RpcConfigProperties.class);
      if (properties.getProvider() == null) {
        properties.setProvider(new RpcConfigProperties.Provider());
      }
    }
    Map<String, String> paramMap = new HashMap<>();
    getServiceConfig(annotation, paramMap);
    Registry registry = getRegistry(properties.getProvider());
    try {
      final Class<?>[] interfaces = targetClass.getInterfaces();
      for (Class<?> anInterface : interfaces) {
        registry.register(getRegisterPath(anInterface.getName(), paramMap));
      }

    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return bean;
  }

  private void getServiceConfig(RpcService annotation, Map<String, String> paramMap) {
    paramMap.put(RegistryConstants.GROUP, annotation.group());
    paramMap.put(RegistryConstants.VERSION, annotation.version());
  }

  @NotNull
  private String getRegisterPath(String serviceName, Map<String, String> paramMap) throws UnknownHostException {
    String hostAddress = InetAddress.getLocalHost().getHostAddress();
    String property = environment.getProperty("server.port");
    String parentPath = RegistryConstants.SPLITTER + serviceName + RegistryConstants.SPLITTER;
    String servicePath = hostAddress + ":" + property;
    String paramString = ServicePathUtil.mapToString(paramMap);
    String childPath = servicePath + "?" + ServicePathUtil.pathParamsEncode(paramString);
    return parentPath + childPath;
  }

  public Registry getRegistry(RpcConfigProperties.Provider property) {
    RpcConfigProperties.Registry propertyRegistry = property.getRegistry();
    if (propertyRegistry == null) {
      propertyRegistry = new RpcConfigProperties.Registry();
    }
    String registry = propertyRegistry.getRegistry();
    String registryType = getOrDefault(registry, ZookeeperRegistry.NAME);
    return RegistryFactory.getRegistry(registryType, propertyRegistry.getRegistryAddress());
  }

  private String getOrDefault(String type, String defaultValue) {
    if (StringUtils.isBlank(type)) {
      return defaultValue;
    }
    return type;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (ListableBeanFactory) beanFactory;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
