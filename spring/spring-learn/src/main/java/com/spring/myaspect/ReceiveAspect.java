package com.spring.myaspect;

import com.spring.aop.annotation.MyAspect;
import com.spring.aop.joinpoint.MyJoinPoint;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName ReceiveAspect
 * @Description TODO
 * @Date 2021/7/24 下午9:07
 * @Version 1.0
 */

@Component
public class ReceiveAspect {

  @MyAspect(isBefore = true, order = 1, classRegex = "com\\.spring\\.service\\.ReceiveService")
  public void before(MyJoinPoint myJoinPoint) {
    System.out.println("---------ReceiveAspect before-------");
  }


}
