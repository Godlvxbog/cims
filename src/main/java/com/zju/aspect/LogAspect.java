package com.zju.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Created by Administrator on 2016/10/2.
 */
@Aspect
@Component
public class LogAspect {
    //日志
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.zju.controller.test.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        //打印出joinPoint
        StringBuffer sb=new StringBuffer();
        for (Object arg :joinPoint.getArgs()){
            sb.append("arg:"+arg+"\n");
        }

        logger.info("Before Method" +new Date()+sb.toString());
    }

    @After("execution(* com.zju.controller.test.*Controller.*(..))")
    public void aferMethod(){
        logger.info("After Method" +new Date());
    }


}
