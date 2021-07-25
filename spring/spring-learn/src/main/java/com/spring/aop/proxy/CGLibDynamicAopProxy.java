package com.spring.aop.proxy;


import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.MyMethodInterceptor;
import com.spring.aop.interceptor.invocation.AopMethodInvocation;
import com.spring.aop.joinpoint.MyJoinPoint;
import com.spring.aop.joinpoint.MyJoinPointImpl;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author wangyang
 * @ClassName CGLibDynamicProxy
 * @Description TODO
 * @Date 2021/7/24 下午8:15
 * @Version 1.0
 */
public class CGLibDynamicAopProxy implements MethodInterceptor, AopProxyFactory {

  private Object target;
  private List<MyMethodInterceptor> myMethodInterceptorList;

  public CGLibDynamicAopProxy(Object target, List<Advice> adviceList) {
    this.target = target;
    myMethodInterceptorList = convertMethodInterceptorList(adviceList);
  }

  @Override
  public Object getProxy() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(target.getClass());
    enhancer.setCallback(this);
    return enhancer.create();
  }

  @Override
  public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    MyJoinPoint myJoinPoint = new MyJoinPointImpl(method, args, method.getParameterTypes());
    AopMethodInvocation aopMethodInvocation = new AopMethodInvocation(myMethodInterceptorList, target, args, method);
    aopMethodInvocation.initJoinPoint(myJoinPoint);
    return aopMethodInvocation.process();
  }
}
