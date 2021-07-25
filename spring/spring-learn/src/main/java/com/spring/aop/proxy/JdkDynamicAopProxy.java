package com.spring.aop.proxy;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.MyMethodAfterAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodAroundAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodBeforeAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodInterceptor;
import com.spring.aop.interceptor.invocation.AopMethodInvocation;
import com.spring.aop.joinpoint.MyJoinPoint;
import com.spring.aop.joinpoint.MyJoinPointImpl;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.OrderComparator;

/**
 * @author wangyang
 * @ClassName JdkDynamicProxy
 * @Description TODO
 * @Date 2021/7/24 下午12:56
 * @Version 1.0
 */
public class JdkDynamicAopProxy implements AopProxyFactory, InvocationHandler {


  private final Object target;

  private List<MyMethodInterceptor> myMethodInterceptorList;

  public JdkDynamicAopProxy(Object target, List<Advice> adviceList) {
    this.target = target;
    myMethodInterceptorList = convertMethodInterceptorList(adviceList);
  }


  @Override
  public Object getProxy() {
    Class<?> targetClass = target.getClass();
    return Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), this);
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MyJoinPoint myJoinPoint = new MyJoinPointImpl(method, args, method.getParameterTypes());
    AopMethodInvocation aopMethodInvocation = new AopMethodInvocation(myMethodInterceptorList, target, args, method);
    aopMethodInvocation.initJoinPoint(myJoinPoint);
    return aopMethodInvocation.process();
  }
}
