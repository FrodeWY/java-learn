package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ReferenceAutowiredBeanDefinitionPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            final Class<?> aClass;
            try {
                aClass = Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                continue;
            }
            final Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                final RpcReference annotation = declaredFields[0].getAnnotation(RpcReference.class);
                if(annotation==null){
                    continue;
                }
                final Class<?> type = declaredField.getType();
                if(!type.isInterface()){
                    continue;
                }

            }


        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
