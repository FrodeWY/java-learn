package com.spring.aop.process;

import com.spring.aop.advice.Advice;
import com.spring.aop.proxy.AopProxyFactory;
import com.spring.aop.proxy.ByteBuddyAopProxy;
import com.spring.aop.proxy.CGLibDynamicAopProxy;
import com.spring.aop.proxy.JdkDynamicAopProxy;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

@Component
public class MyAspectBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, PriorityOrdered {

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
    private BeanFactory beanFactory;
    private MyAspectProxyCreator myAspectProxyCreator;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> targetClass = bean.getClass();
        try {
            List<Advice> canToApplyAdvices = myAspectProxyCreator.findCanToApplyAdvices(beanName, targetClass);
            if (canToApplyAdvices == null || canToApplyAdvices.size() == 0) {
                return bean;
            }
            AopProxyFactory aopProxyFactory;
            if (targetClass.getInterfaces().length == 0) {
//        aopProxyFactory = new CGLibDynamicAopProxy(bean, canToApplyAdvices);
                aopProxyFactory = new ByteBuddyAopProxy(bean, canToApplyAdvices);
            } else {
                aopProxyFactory = new JdkDynamicAopProxy(bean, canToApplyAdvices);
            }
            return aopProxyFactory.getProxy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ListableBeanFactory) {
            myAspectProxyCreator = new MyAspectProxyCreator((ListableBeanFactory) beanFactory);
        } else {
            throw new IllegalArgumentException("MyAspectBeanPostProcessor need ListableBeanFactory,but current beanFactory is:" + beanFactory.getClass());
        }
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}
