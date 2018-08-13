package com.deepwise.cloud.client;

import com.deepwise.cloud.LogManager;
import com.deepwise.cloud.TraceManager;
import com.deepwise.cloud.bean.TraceBean;
import com.deepwise.cloud.bean.TraceContextBean;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/8
 * @Company: DeepWise
 */
public class BasicLogClient extends LogClient {

    private TraceManager traceManager;
    private LogManager logManager;

    public BasicLogClient(){
        traceManager = new TraceManager();
        logManager = new LogManager();
    }

    @Override
    public void startTrace(TraceBean trace) {
        traceManager.startTrace(trace);
    }

    public void flushTrace(boolean isSuccess) {
        traceManager.flushTrace(isSuccess);
    }

    @Override
    public void finishTrace(boolean isSuccess) {
        traceManager.finishTrace(isSuccess);
    }

    @Override
    public void logTrace(TraceBean trace) {
        traceManager.logTrace(trace);
    }

    @Override
    public String getTraceId() {
        return traceManager.getTraceId();
    }

    @Override
    public TraceBean getTrace() {
        return traceManager.getTrace();
    }

    @Override
    public TraceBean getCurrentTrace() {
        return traceManager.getCurrentTrace();
    }

    @Override
    public void resetTraceContext(String contextString) {
        traceManager.resetTraceContext(contextString);
    }

    @Override
    public void resetTraceContext(TraceContextBean traceContext) {
        traceManager.resetTraceContext(traceContext);
    }

    public String getTraceContextString() {
        return traceManager.getTraceContextString();
    }

    public String getTraceContext() {
        return traceManager.getTraceContextString();
    }

    public void clearAllThreadLocal() {
        traceManager.clear();
    }
}
