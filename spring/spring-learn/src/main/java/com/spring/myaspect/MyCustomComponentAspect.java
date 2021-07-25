package com.spring.myaspect;

import com.spring.aop.annotation.MyAspect;
import com.spring.aop.joinpoint.MyJoinPoint;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName MyCustomComponentAspect
 * @Description TODO
 * @Date 2021/7/24 下午10:33
 * @Version 1.0
 */
@Component
public class MyCustomComponentAspect {

  @MyAspect(isAfter = true, classRegex = "com\\.learn\\.starter\\.pojo\\.MyCustomComponent")
  public void print(MyJoinPoint myJoinPoint) {
    System.out.println("-------MyCustomComponentAspect------");
  }
}
