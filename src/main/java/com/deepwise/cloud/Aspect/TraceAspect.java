package com.deepwise.cloud.Aspect;

import com.deepwise.cloud.LogManager;
import com.deepwise.cloud.annotation.DwTrace;
import com.deepwise.cloud.bean.TraceBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/9
 * @Company: DeepWise
 */
@Aspect
@Component
public class TraceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(TraceAspect.class);

    @Pointcut("@annotation(traceAnnotation)")
    public void serviceStatistics(DwTrace traceAnnotation){
    }


    @Around("serviceStatistics(traceAnnotation)")
    public Object process(ProceedingJoinPoint point,DwTrace traceAnnotation) throws Throwable {
        TraceBean trace = LogManager.client().getTrace();
        trace.setSysCode(traceAnnotation.bizCode());
        Object result = null;
        try {
            // 调用被拦截方法前执行
            before(point,trace);
        }catch (Throwable t){
            LOG.warn("",t);
        }
        try {
            result = point.proceed();
        }catch (Throwable t){
            //被拦截方法抛出异常之后执行
            afterThrowing(point,t,trace);
            throw t;
        }
        try {
            //被拦截方法正常返回后执行
            afterReturning(point,result,trace);
        }catch (Throwable t){
            LOG.warn("",t);
        }
        return  result;
    }


    public void before(JoinPoint joinPoint,TraceBean trace){
        LOG.info("++++++++Aspect before method+++++++++");
        trace.setClassName(joinPoint.getTarget().getClass().getName());
        trace.setMethodName(joinPoint.getSignature().getName());
        trace.setParamsIn(Arrays.toString(joinPoint.getArgs()));
        trace.setStartTime(System.currentTimeMillis());
        //trace.setSysCode();
        LogManager.client().startTrace(trace);
        LOG.info("className: "+trace.getClassName()+" method: "+trace.getMethodName()+" traceId: "+trace.getTraceId()+" id: "+trace.getId()
                +" parentId "+trace.getParentId());
    }


    public void afterReturning(JoinPoint joinPoint, Object returnValue, TraceBean trace){
        LOG.info("++++++++Aspect after method return+++++++++");
        if (returnValue != null){
            trace.setParamsOut(returnValue.toString());
        }
        trace.setMsgTime(System.currentTimeMillis());
        trace.setElapsedTime((int)(trace.getMsgTime()-trace.getStartTime()));
        trace.setSuccess(true);
        LogManager.client().finishTrace(true);
        LogManager.client().logTrace(trace);
        System.out.println(trace.toString());
    }

    public void afterThrowing(JoinPoint joinPoint,Throwable e,TraceBean trace){
        LOG.info("++++++++Aspect after method throw exception+++++++++");
        trace.setMsgTime(System.currentTimeMillis());
        trace.setElapsedTime((int)(trace.getMsgTime()-trace.getStartTime()));
        trace.setThrowable(e);
        trace.setSuccess(false);
        LogManager.client().finishTrace(false);
        LogManager.client().logTrace(trace);
        System.out.println(trace.toString());
    }

}
