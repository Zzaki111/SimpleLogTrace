package com.deepwise.cloud.traceLog.client;


import com.deepwise.cloud.traceLog.bean.TraceBean;
import com.deepwise.cloud.traceLog.bean.TraceContextBean;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public abstract class LogClient {

    private volatile static LogClient logClient;

    public static LogClient getInstance(){
        if (logClient == null){
            synchronized (LogClient.class){
                if (logClient == null){
                    logClient = new DefaultLogClient();
                }
            }
        }
        return  logClient;
    }

    /**
     * 开始trace日志的记录，需要和{@link #finishTrace(boolean)}成对调用
     *
     * @param trace trace信息
     */
    public abstract void startTrace(TraceBean trace);

    /**
     * 完成一次日志记录
     *
     * @param isSuccess 调用是否成功
     */
    public abstract void finishTrace(boolean isSuccess)throws Exception;

    /**
     * 发送一个独立的trace对象日志
     *
     * @param trace trace对象
     */
    public abstract void logTrace(TraceBean trace) throws Exception;

    /**
     * 获取当前环境中的TraceId
     *
     * @return 全局唯一TraceId
     */
    public abstract String getTraceId();

    /**
     * 从Client中获取一个Trace日志。如果有可用日志，则返回当前日志，没有则返回一个新的日志Trace。
     * 当存在上下文信息是，返回的Trace会附带上下文内容
     *
     * @return 返回新trace对象节点
     */
    public abstract TraceBean getTrace();

    /**
     * 获取一个当前可用的Trace对象
     *
     * @return 当前Trace对象
     */
    public abstract TraceBean getCurrentTrace();

    /**
     * @param contextString trace上下文字符串
     */
    public abstract void resetTraceContext(String contextString);

    /**
     * @param traceContext trace上下文对象
     */
    public abstract void resetTraceContext(TraceContextBean traceContext);


}
