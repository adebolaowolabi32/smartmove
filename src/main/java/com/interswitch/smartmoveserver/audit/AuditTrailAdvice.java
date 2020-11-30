//package com.interswitch.smartmoveserver.audit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class AuditTrailAdvice {
//
//
//    @Around(value="")
//    public Object onAudit(ProceedingJoinPoint pjp) throws Throwable {
//        ObjectMapper mapper = new ObjectMapper();
//        String methodName = pjp.getSignature().getName();
//        String className = pjp.getTarget().getClass().toString();
//        //gets the method parameters
//        Object[] arr = pjp.getArgs();
//        log.info("Method Invoked ans class ===>"+methodName+"=>class=>"+className);
//        //gets the response of the method being called
//        Object auditObj = pjp.proceed();
//        log.info("auditObj===>"+mapper.writeValueAsString(auditObj));
//        return auditObj;
//    }
//}
