package com.deepwise.cloud.client;


import com.deepwise.cloud.bean.NullTrace;
import com.deepwise.cloud.bean.TraceBean;
import com.deepwise.cloud.bean.TraceContextBean;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public class DefaultLogClient extends LogClient {

    /**
     * NullTrace对象缓存
     */
    private volatile static ThreadLocal<NullTrace> nullTraceThreadLocal = new ThreadLocal<NullTrace>();

    @Override
    public void startTrace(TraceBean trace) {

    }

    @Override
    public void finishTrace(boolean isSuccess) {

    }

    @Override
    public void logTrace(TraceBean trace) {

    }

    @Override
    public String getTraceId() {
        return "";
    }

    @Override
    public TraceBean getTrace() {
        return getNullTrace();
    }

    @Override
    public TraceBean getCurrentTrace() {
        return null;
    }

    @Override
    public void resetTraceContext(String contextString) {

    }

    @Override
    public void resetTraceContext(TraceContextBean traceContext) {

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
            nullTraceThreadLocal.set(nullTrace);
        }
        return nullTrace;
    }
}
