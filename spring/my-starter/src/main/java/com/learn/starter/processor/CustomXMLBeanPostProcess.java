package com.learn.starter.processor;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author wangyang
 * @ClassName CustomXMLBeanPostProcess
 * @Description 使用自定义XML配置, 配置Bean
 * @Date 2021/7/25 上午10:35
 * @Version 1.0
 */
@Component
public class CustomXMLBeanPostProcess implements BeanDefinitionRegistryPostProcessor {

  public static final String BEAN = "bean";
  public static final String BEAN_NAME = "name";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_VALUE = "value";
  public static final String BEAN_CLASS = "class";
  public static final String PROPERTY = "property";

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    ClassPathResource resource = new ClassPathResource("CustomBeanXmlContext.xml");
    try (InputStream inputStream = resource.getInputStream()) {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(inputStream);
      Element root = document.getDocumentElement();
      NodeList childNodes = root.getElementsByTagName(BEAN);
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node item = childNodes.item(i);
        NamedNodeMap attributes = item.getAttributes();
        Node nameNode = attributes.getNamedItem(BEAN_NAME);
        Node classNode = attributes.getNamedItem(BEAN_CLASS);
        String name = nameNode.getNodeValue();
        String clazz = classNode.getNodeValue();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        NodeList propertyNodeList = ((DeferredElementImpl) item).getElementsByTagName(PROPERTY);
        for (int p = 0; p < propertyNodeList.getLength(); p++) {
          Node propertyNode = propertyNodeList.item(p);
          NamedNodeMap propertyNodeAttributes = propertyNode.getAttributes();
          Node propertyNameNode = propertyNodeAttributes.getNamedItem(PROPERTY_NAME);
          Node propertyValueNode = propertyNodeAttributes.getNamedItem(PROPERTY_VALUE);
          beanDefinitionBuilder.addPropertyValue(propertyNameNode.getNodeValue(), propertyValueNode.getNodeValue());
        }
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, name);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

  }
}
