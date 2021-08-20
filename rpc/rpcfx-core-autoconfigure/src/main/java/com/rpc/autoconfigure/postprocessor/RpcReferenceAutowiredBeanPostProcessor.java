package com.rpc.autoconfigure.postprocessor;

import com.rpc.autoconfigure.config.RpcConfigProperties;
import com.rpc.autoconfigure.annotation.RpcReference;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.Router;
import com.rpc.core.protocol.RegistryProtocol;
import com.rpc.core.proxy.JdkProxy;
import com.rpc.core.proxy.ProxyFactories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

@Component
public class RpcReferenceAutowiredBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private RpcConfigProperties properties;

    private List<Router> routerList;

    private ListableBeanFactory beanFactory;

    private RegistryProtocol protocol;

    public RpcReferenceAutowiredBeanPostProcessor() {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass;
        if (AopUtils.isAopProxy(bean)) {
            targetClass = AopUtils.getTargetClass(bean);
        } else if (bean.getClass().getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
            targetClass = bean.getClass().getSuperclass();
        } else {
            targetClass = bean.getClass();
        }
        final Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            final RpcReference annotation = declaredFields[0].getAnnotation(RpcReference.class);
            if (annotation == null) {
                continue;
            }
            final Class<?> type = declaredField.getType();
            if (!type.isInterface()) {
                continue;
            }
            if (routerList == null) {
                Map<String, Router> beansOfType = beanFactory.getBeansOfType(Router.class);
                routerList = new ArrayList<>(beansOfType.values());
            }
            if (properties == null) {
                properties = beanFactory.getBean(RpcConfigProperties.class);
                if (properties.getConsumer() == null) {
                    properties.setConsumer(new RpcConfigProperties.Consumer());
                }
            }
            if (protocol == null) {
                protocol = beanFactory.getBean(RegistryProtocol.class);
            }
            Invoker invoker = protocol.getInvoker(type.getName());
            Object proxy = ProxyFactories.proxy(getOrDefault(properties.getConsumer().getProxy(), JdkProxy.NAME), type, invoker);
            declaredField.setAccessible(true);
            try {
                declaredField.set(bean, proxy);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }

    private String getOrDefault(String type, String defaultValue) {
        if (StringUtils.isBlank(type)) {
            return defaultValue;
        }
        return type;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

}
