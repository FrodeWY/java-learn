package com.spring.learn.starter.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.*;


/**
 * CustomComponent 注册器
 */
public class CustomComponentRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScanSet = getPackagesToScan(importingClassMetadata);
        registerCustomComponentAnnotationBeanPostProcessor(packagesToScanSet, registry);
    }


    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableCustomComponent.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        Class<?>[] basePackageClasses = (Class<?>[]) annotationAttributes.get("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }

    private void registerCustomComponentAnnotationBeanPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        //编程式装配Bean ,生成CustomComponentAnnotationBeanPostProcessor的beanDefinition,手动装配到ioc容器中
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CustomComponentAnnotationBeanPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);

    }
}
