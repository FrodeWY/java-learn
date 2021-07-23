package com.spring.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public class MyAspectBeanPostProcessor implements BeanPostProcessor {

    private MetadataReaderFactory metadataReaderFactory=new CachingMetadataReaderFactory();
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return null;
    }
}
