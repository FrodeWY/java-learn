package com.spring.myaspect;

import com.spring.aop.annotation.MyAspect;
import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName LogAspect
 * @Description 日志切面
 * @Date 2021/7/24 下午3:55
 * @Version 1.0
 */
@Component
public class LogAspect {

  @MyAspect(isAfter = true, isBefore = true, order = 1, classRegex = "com\\.spring\\.service\\..*")
  public void aroundPrint(MyJoinPoint myJoinPoint) {
    boolean processed = myJoinPoint.isProcessed();
    String suffix = processed ? "end" : "start";
    System.out
        .println("---------------" + myJoinPoint.uniqueKey() + "--------------" + suffix);
  }

  @MyAspect(isBefore = true, order = 2, classRegex = "com\\.spring\\.service\\..*")
  public void beforePrint(MyJoinPoint myJoinPoint) {
    String signature = getSignature(myJoinPoint);
    if (myJoinPoint.args() != null && myJoinPoint.args().length > 0) {
      String args = Arrays.toString(myJoinPoint.args());
      System.out.println(signature + "  start" + "  args:" + args);
    }
  }

  @MyAspect(isAfter = true, order = 3, classRegex = "com\\.spring\\.service\\.SendServiceImpl", methodRegex = "send")
  public void afterPrint(MyJoinPoint myJoinPoint) {
    String signature = getSignature(myJoinPoint);
    System.out
        .println(signature + "  result:" + myJoinPoint.returnValue());
  }

  private String getSignature(MyJoinPoint myJoinPoint) {
    Method method = myJoinPoint.invokeMethod();
    return method.getDeclaringClass().getName() + "#" + method.getName();
  }
}
