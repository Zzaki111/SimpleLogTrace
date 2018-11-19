package com.deepwise.cloud.traceLog.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.deepwise.cloud.traceLog.LogManager;
import com.deepwise.cloud.traceLog.bean.TraceBean;
import com.deepwise.cloud.util.UtilCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/22
 * @Company: DeepWise
 */
@Activate(value = "traceFilter")
public class TraceFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(TraceFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException{
        RpcContext context = RpcContext.getContext();
        Result result = null;
        String traceId = context.getAttachment("traceId");
        String id = context.getAttachment("id");
        TraceBean trace = LogManager.client().getTrace();
        if (UtilCompare.isNotEmpty(traceId)&&UtilCompare.isNotEmpty(id)){
           try {
               // 调用方法之前
               before(invocation,trace,traceId,id);
           }catch (Exception e){
               LOG.warn("",e.getMessage());
           }

           try {
                result = invoker.invoke(invocation);
            }catch (Throwable t){
               // 执行被拦截方法返回异常执行
               try {
                   afterThrowing(t,trace);
               } catch (Exception e) {
                   LOG.warn("",e.getMessage());
               }finally {
                   throw t;
               }
            }
            //正常返回执行
            try {
               afterReturing(result,trace);
            }catch (Exception e){
                LOG.warn("",e.getMessage());
            }
        }else {
            trace = LogManager.client().getCurrentTrace();
            context.setAttachment("traceId",trace.getTraceId());
            context.setAttachment("id",trace.getId());
            result = invoker.invoke(invocation);
        }
        return result;
    }

    private void before(Invocation invocation,TraceBean trace,String traceId,String id){
        LOG.info("++++++++Dubbo Filter before method+++++++++");
        trace.setClassName(invocation.getInvoker().getClass().getName());
        trace.setMethodName(invocation.getMethodName());
        trace.setParamsIn(Arrays.toString(invocation.getArguments()));
        trace.setStartTime(System.currentTimeMillis());
        trace.setTraceId(traceId);
        trace.setParentId(id);
        LogManager.client().startTrace(trace);
    }

    private void afterReturing(Result result,TraceBean trace) throws Exception {
        LOG.info("++++++++Dubbo Filter after method return+++++++++");
        if (result != null){
            trace.setParamsOut(result.toString());
        }
        trace.setMsgTime(System.currentTimeMillis());
        trace.setElapsedTime((int)(trace.getMsgTime()-trace.getStartTime()));
        trace.setSuccess(true);
        LogManager.client().finishTrace(true);
    }

    private void afterThrowing(Throwable t,TraceBean trace) throws Exception {
        LOG.info("++++++++Dubbo Filter after method throw exception+++++++++");
        trace.setMsgTime(System.currentTimeMillis());
        trace.setElapsedTime((int)(trace.getMsgTime()-trace.getStartTime()));
        trace.setThrowable(t);
        trace.setSuccess(false);
        LogManager.client().finishTrace(false);
    }
}
