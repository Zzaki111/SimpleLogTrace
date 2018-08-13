package com.deepwise.cloud;

import com.deepwise.cloud.bean.NullTrace;
import com.deepwise.cloud.bean.TraceBean;
import com.deepwise.cloud.bean.TraceContextBean;
import com.deepwise.cloud.util.UtilCompare;
import com.deepwise.cloud.util.UtilLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/8
 * @Company: DeepWise
 */
public class TraceManager  {

    private static Logger LOG = LoggerFactory.getLogger(TraceManager.class);

    /**
     * 调用链栈
     */
    private volatile static ThreadLocal<Stack<TraceBean>> traceStackThreadLocal = new ThreadLocal<>();

    /**
     * Trace上下文
     */
    private volatile static ThreadLocal<TraceContextBean> contextThreadLocal = new ThreadLocal<>();

    /**
     * NullTrace对象缓存
     */
    private volatile static ThreadLocal<NullTrace> nullTraceThreadLocal = new ThreadLocal<>();

    private void pushTrace(TraceBean trace){
        if (traceStackThreadLocal.get() == null){
            traceStackThreadLocal.set(new Stack<TraceBean>());
        }
        traceStackThreadLocal.get().push(trace);
    }

    private TraceBean popTrace(){
        if (traceStackThreadLocal.get() == null){
            traceStackThreadLocal.set(new Stack<TraceBean>());
            return null;
        }
        if (traceStackThreadLocal.get().isEmpty()){
            return null;
        }
        return traceStackThreadLocal.get().pop();
    }

    private TraceBean peekTrace(){
        if (traceStackThreadLocal.get() == null){
            traceStackThreadLocal.set(new Stack<TraceBean>());
            return null;
        }
        if (traceStackThreadLocal.get().isEmpty())
            return null;
        return traceStackThreadLocal.get().peek();
    }

    /**
     * 调用链日志开始
     *
     * @param trace trace对象
     */
    public void startTrace(TraceBean trace){
        pushTrace(trace);
    }

    /**
     * 调用链日志结束
     *
     * @param isSuccess 是否调用成功
     */
    public void finishTrace(boolean isSuccess){
        TraceBean trace = peekTrace();
        if(trace != null){
            flushTrace(isSuccess);
        }
    }

    /**
     * 根触发调用链日志发送请求
     *
     * @param isSuccess 是否调用成功
     */
    public void flushTrace(boolean isSuccess){

        TraceBean trace = popTrace();
        if (trace == null){
            return;
        }
        trace.setSuccess(isSuccess);
        trace.setElapsedTime((int)(System.currentTimeMillis() - trace.getStartTime()));
        sendTrace(trace);

    }

    /**
     * 发送一条trace类型的日志
     *
     * @param trace trace对象
     */
    public void logTrace(TraceBean trace) {
        trace.setSuccess(true);
        sendTrace(trace);
    }

    /**
     * <pre>
     * 1. 从调用栈中查找父Trace，如果存在，则构造并返回子Trace对象
     * 2. 如调用栈中未找到父Trace，新建一个Trace对象，并根据上下文信息设置新Trace对象为上游节点的子Trace对象
     * </pre>
     *
     * @return 根据上下文环境，获取Trace对象
     */
    public TraceBean getTrace(){
        TraceBean trace = peekTrace();
        if (trace != null){
            trace = trace.createSubTrace();
            return trace;
        }else {
            trace = new TraceBean();
            TraceContextBean traceContext = innerTraceContext();
            if (traceContext.isActive()){
                trace.convert2SubTrace(traceContext);
            }
            return trace;
        }
    }

    /**
     * 获取当前Trace的节点对象，如当前对象不存在，返回NullTrace
     *
     * @return 当前节点Trace
     */
    public TraceBean getCurrentTrace(){
        TraceBean trace = peekTrace();
        if (trace == null){
            trace = getNullTrace();
        }
        return trace;
    }

    /**
     * 获取当前环境中的traceId
     *
     * @return traceId
     */
    public String getTraceId() {
        TraceBean trace = getCurrentTrace();
        if (trace.isNull()) {
            return innerTraceContext().getTraceId();
        }
        return trace.getTraceId();
    }

    /**
     * 清除当前线程trace上下文信息，并且保存traceContext信息到线程上下文环境中
     *
     * @param contextString trace context string
     */
    public void resetTraceContext(String contextString) {
        clear();
        TraceContextBean traceContext = new TraceContextBean();
        if (UtilCompare.isNotEmpty(contextString)){
            String[] contextFields = contextString.split(TraceContextBean.FIELD_SEPARATOR);
            if (contextFields.length == 4){
                traceContext.setTraceId(contextFields[0]);
                traceContext.setId(contextFields[1]);
                traceContext.setClassName(contextFields[2]);
                traceContext.setMethodName(contextFields[3]);
                traceContext.setActive(true);
                getNullTrace().setTraceId(traceContext.getTraceId());
            }else{
                UtilLogger.warn(LOG,String.format("Reset traceContext failed, because contextString: %s" +
                        "the field size is not enough",contextString));
            }
        }else {
            UtilLogger.warn(LOG,String.format("Reset traceContext failed, because contextString: %s" +
                    "is empty",contextString));
        }
        contextThreadLocal.set(traceContext);
    }

    /**
     * 清除当前线程trace上下文信息，并且保存traceContext信息到线程上下文环境中
     *
     * @param traceContext trace context
     */
    public void resetTraceContext(TraceContextBean traceContext) {
        clear();
        if (traceContext == null){
            traceContext.setActive(true);
            contextThreadLocal.set(traceContext);
            getNullTrace().setTraceId(traceContext.getTraceId());
        }else {
            TraceContextBean tc = new TraceContextBean();
            contextThreadLocal.set(tc);

        }
    }

    /**
     * 返回当前Trace上下文
     *
     * @return trace context
     */
    public TraceContextBean getTraceContext() {
        TraceContextBean traceContext;

        TraceBean trace = peekTrace();
        if (trace != null) {
            traceContext = trace.getTraceContextBean();
            return traceContext;
        }
        traceContext = innerTraceContext();
        return traceContext;
    }

    /**
     * 返回当前Trace上下文环境信息
     *
     * @return trace context string
     */
    public String getTraceContextString() {
        return getTraceContext().toString();
    }

    private TraceContextBean innerTraceContext(){
        TraceContextBean traceContext = contextThreadLocal.get();
        if (traceContext == null){
            traceContext = new TraceContextBean();
            contextThreadLocal.set(traceContext);
        }
        return traceContext;
    }

    /**
     * 在全局线程变量中存放一个NullTrace对象，并且返回此对象
     *
     * @return NullTrace对象
     */
    private static NullTrace getNullTrace() {
        NullTrace nullTrace = nullTraceThreadLocal.get();
        if (nullTrace == null) {
            nullTrace = new NullTrace();
            if (contextThreadLocal.get() != null) {
                nullTrace.setTraceId(contextThreadLocal.get().getTraceId());
            }
            nullTraceThreadLocal.set(nullTrace);
        }
        return nullTrace;
    }

    /**
     * 清理所有线程上下文数据
     */
    public void clear() {
        if (contextThreadLocal.get() != null) {
            contextThreadLocal.remove();
        }
        Stack<TraceBean> stack = traceStackThreadLocal.get();
        if (stack != null) {
            stack.clear();
        }

    }
    /**
     * 发送trace信息
     */
    private void sendTrace(TraceBean trace){
        // TODO 确定链路日志存储方式，将链路日志存储
    }

}
