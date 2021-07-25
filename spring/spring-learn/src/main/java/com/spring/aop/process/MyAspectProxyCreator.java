package com.spring.aop.process;

import com.spring.aop.advice.AroundAdvice;
import com.spring.aop.annotation.MyAspect;
import com.spring.aop.advice.Advice;
import com.spring.aop.advice.AfterAdvice;
import com.spring.aop.advice.BeforeAdvice;
import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class MyAspectProxyCreator {

  private ListableBeanFactory beanFactory;
  private List<String> myAspectBeanNames;
  private volatile ConcurrentHashMap<String, List<Advice>> cache;
  private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

  public MyAspectProxyCreator(ListableBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public List<Advice> findCanToApplyAdvices(String beanName, Class<?> targetClass) {
    findCandidateAdvices();
    if (cache == null) {
      return null;
    }
    if (myAspectBeanNames == null || myAspectBeanNames.size() == 0) {
      return null;
    }
    List<Advice> advices = new ArrayList<>();
    for (String myAspectBeanName : myAspectBeanNames) {
      List<Advice> adviceList = cache.get(myAspectBeanName);
      for (Advice advice : adviceList) {
        if (advice.classMatch(targetClass)) {
          advices.add(advice);
        }
      }
    }
    return advices;
  }

  private void findCandidateAdvices() {
    try {
      if (myAspectBeanNames == null) {
        synchronized (this) {
          if (myAspectBeanNames == null) {
            myAspectBeanNames = new ArrayList<>();
            cache = new ConcurrentHashMap<>();
            String[] beanNames = BeanFactoryUtils
                .beanNamesForTypeIncludingAncestors(beanFactory, Object.class, true, false);
            for (String beanName : beanNames) {
              Class<?> beanType = beanFactory.getType(beanName, false);
              if (beanType == null) {
                continue;
              }
//              System.out.println(beanName);
              //是否是jdk动态代理
              if (Proxy.class.isAssignableFrom(beanType)) {
                Object bean = beanFactory.getBean(beanName);
                beanType = AopUtils.getTargetClass(bean);
              } else if (beanType.getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
                beanType = ClassUtils.getUserClass(beanType);
              }
              MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(beanType.getName());
              AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
              String annotationName = MyAspect.class.getName();
              boolean hasAnnotatedMethods = annotationMetadata.hasAnnotatedMethods(annotationName);
              if (!hasAnnotatedMethods) {
                continue;
              }
              Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(annotationName);
              Object bean = beanFactory.getBean(beanName);
              List<Advice> adviceList = new ArrayList<>();
              for (MethodMetadata annotatedMethod : annotatedMethods) {
                String methodName = annotatedMethod.getMethodName();
                Method method = beanType.getMethod(methodName, MyJoinPoint.class);
                Map<String, Object> annotationAttributes = annotatedMethod.getAnnotationAttributes(annotationName);
                if (annotationAttributes == null) {
                  continue;
                }
                boolean isBefore = (boolean) annotationAttributes.get("isBefore");
                boolean isAfter = (boolean) annotationAttributes.get("isAfter");
                int order = (int) annotationAttributes.get("order");
                String classRegex = (String) annotationAttributes.get("classRegex");
                Pattern classPattern = Pattern.compile(classRegex);
                String methodRegex = (String) annotationAttributes.get("methodRegex");
                Pattern methodPattern = Pattern.compile(methodRegex);
                if (isAfter && isBefore) {
                  AroundAdvice aroundAdvice = new AroundAdvice(method, bean, methodPattern, classPattern, order);
                  adviceList.add(aroundAdvice);
                } else if (isAfter) {
                  AfterAdvice afterAdvice = new AfterAdvice(method, bean, methodPattern, classPattern, order);
                  adviceList.add(afterAdvice);
                } else if (isBefore) {
                  BeforeAdvice beforeAdvice = new BeforeAdvice(method, bean, methodPattern, classPattern, order);
                  adviceList.add(beforeAdvice);
                }
              }
              cache.put(beanName, adviceList);
              myAspectBeanNames.add(beanName);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
