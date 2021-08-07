package com.readwrite.aspect;


import com.readwrite.config.DataSourceConfig;
import com.readwrite.annotation.ReadOnly;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName ReadWriteAspect
 * @Description TODO
 * @Date 2021/8/6 下午2:53
 * @Version 1.0
 */
@Component
@Aspect
public class ReadWriteAspect {

  @Pointcut(value = "within(com.*.service..*)")
  public void point() {
  }

  @Around("point()")
  public Object choiceDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    ReadOnly annotation = method.getAnnotation(ReadOnly.class);
    if (annotation == null) {
      ReadWriteContext.set(DataSourceConfig.MASTER);
    } else {
      ReadWriteContext.set(DataSourceConfig.SLAVE);
    }
    Object result = joinPoint.proceed();
    ReadWriteContext.remove();
    return result;
  }
}
