package com.spring.aop.proxy;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.MyMethodAfterAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodAroundAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodBeforeAdviceInterceptor;
import com.spring.aop.interceptor.MyMethodInterceptor;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.OrderComparator;

public interface AopProxyFactory {

  /**
   * 获取Aop代理对象
   */
  Object getProxy() throws IllegalAccessException, InstantiationException;

  /**
   * 将Advice转换成MyMethodInterceptor
   */
  default List<MyMethodInterceptor> convertMethodInterceptorList(List<Advice> adviceList) {
    if (adviceList == null || adviceList.size() == 0) {
      return null;
    }
    List<MyMethodInterceptor> myMethodInterceptorList = new ArrayList<>();
    OrderComparator.sort(adviceList);
    for (Advice advice : adviceList) {
      MyMethodInterceptor interceptor;
      if (advice.isBefore()) {
        interceptor = new MyMethodBeforeAdviceInterceptor(advice);
      } else if (advice.isAfter()) {
        interceptor = new MyMethodAfterAdviceInterceptor(advice);
      } else if (advice.isAround()) {
        interceptor = new MyMethodAroundAdviceInterceptor(advice);
      } else {
        continue;
      }
      myMethodInterceptorList.add(interceptor);
    }
    return myMethodInterceptorList;
  }
}
