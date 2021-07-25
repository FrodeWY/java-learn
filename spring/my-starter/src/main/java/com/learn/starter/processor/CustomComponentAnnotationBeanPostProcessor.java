package com.learn.starter.processor;

import com.learn.starter.annotation.CustomComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 扫描@CustomComponent 声明的类,并装配到容器中
 */
public class CustomComponentAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ResourceLoaderAware {
    private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
    protected final Set<String> packagesToScan;
    private Environment environment;
    private ResourceLoader resourceLoader;


    public CustomComponentAnnotationBeanPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        List<BeanDefinition> beanDefinitions = scanBeanDefinitions(beanDefinitionRegistry);
        if (beanDefinitions != null && beanDefinitions.size() > 0) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
                beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
            }
        }

    }

    public List<BeanDefinition> scanBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry, false, environment, resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(CustomComponent.class));
        return packagesToScan.stream().map(scanner::findCandidateComponents).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
