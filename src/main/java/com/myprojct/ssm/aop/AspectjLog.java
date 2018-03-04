package com.myprojct.ssm.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
* 切面类
* Created by sam on 16/6/1.
*/
@Order(1)
@Aspect
@Component
public class AspectjLog {
  private Logger logger = LoggerFactory.getLogger(AspectjLog.class);

  //此处定义一个通用的切点,以便下方4个通知使用
  @Pointcut("execution(* com.myprojct.ssm.aop.AopTest.aopTest(..))")
  public void logAop() {
  }

  // 如果没有定义公用的切点或者需要单独指定其他切点,可以使用表达式指定
  // @Before("execution(* com.sam.aop.service.AspectService.*(..)) && args(email)")
  @Before("logAop() && args(email)")
  public void logBefore(JoinPoint joinPoint,String email) {
      logger.info("前置通知Before>>{}"+email);
      Object[] aa = joinPoint.getArgs();
      System.out.println("aa.toString()—"+joinPoint.getArgs()[0].toString());
  }

  @AfterReturning("logAop() && args(email)")
  public void logAfterReturning(String email) {
      logger.info("返回通知AfterReturning>>{}"+email);
  }

  @After("logAop() && args(email)")
  public void logAfter(String email) {
      logger.info("后置通知After>>{}"+email);
  }

  @AfterThrowing("logAop() && args(email)")
  public void logAfterThrow(String email) {
      logger.info("异常通知AfterThrowing>>{}"+ email);
  }

  //环绕通知功能很强,可以替代上面的所有通知
  @Around("logAop() && args(email)")
  public void logAround(ProceedingJoinPoint jp, String email) {
      try {
          logger.info("自定义前置通知Before>>>{}"+ email);
          jp.proceed();//将控制权交给被通知的方法，也就是执行sayHello方法
          logger.info("自定义返回通知AfterReturning>>>{}"+ email);
      } catch (Throwable throwable) {
          logger.info("异常处理>>>>{}"+ email);
          throwable.printStackTrace();
      }
       logger.info("自定义后置通知After>>>{}"+ email);
  }
}
