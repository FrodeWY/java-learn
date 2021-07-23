package com.spring.process;

import com.spring.aop.advice.Advice;
import org.springframework.beans.factory.BeanFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyAspectProxyCreator {

    private BeanFactory beanFactory;
    private List<String> myAspectBeanNames;
    private ConcurrentHashMap<String ,List<Advice>> cache;
}
