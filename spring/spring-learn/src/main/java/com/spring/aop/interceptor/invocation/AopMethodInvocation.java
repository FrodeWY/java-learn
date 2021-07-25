package com.spring.aop.interceptor.invocation;

import com.spring.aop.interceptor.MyMethodInterceptor;
import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author wangyang
 * @ClassName AopMethodInvocation
 * @Description TODO
 * @Date 2021/7/24 下午12:18
 * @Version 1.0
 */
public class AopMethodInvocation implements MethodInvocation {


  private final List<MyMethodInterceptor> myMethodInterceptorList;

  private final Object target;
  private final Object[] args;
  private final Method method;
  private int index;

  public AopMethodInvocation(List<MyMethodInterceptor> myMethodInterceptorList, Object target, Object[] args, Method method) {
    this.myMethodInterceptorList = myMethodInterceptorList;
    this.target = target;
    this.args = args;
    this.method = method;
  }

  @Override
  public Object process() throws InvocationTargetException, IllegalAccessException {
    if (myMethodInterceptorList == null || myMethodInterceptorList.size() == 0 || index == myMethodInterceptorList.size()) {
      return processTarget();
    }
    MyMethodInterceptor interceptor = myMethodInterceptorList.get(index++);
    if (interceptor.isMatch(this)) {
      return interceptor.invoke(this);
    } else {
      return process();
    }
  }

  @Override
  public void initJoinPoint(MyJoinPoint myJoinPoint) {
    THREAD_LOCAL.set(myJoinPoint);
  }

  private Object processTarget() throws InvocationTargetException, IllegalAccessException {
    ReflectionUtils.makeAccessible(method);
    Object result = method.invoke(target, args);
    if (method.getReturnType() != Void.TYPE) {
      currentJoinPoint().setReturnValue(result);
    }
    currentJoinPoint().end();
    return result;
  }

  @Override
  public MyJoinPoint currentJoinPoint() {
    return THREAD_LOCAL.get();
  }

}
