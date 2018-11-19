package com.deepwise.cloud.traceLog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.deepwise.cloud.traceLog.util.RuntimeUtils;
import com.deepwise.cloud.traceLog.util.UUIDFactory;

import java.io.Serializable;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/4
 * @Company: DeepWise
 */
public class TraceBean implements Serializable {
    private static final long serialVersionUID = -6692084347220065679L;

    @JSONField(serialize = false)
    private TraceContextBean traceContextBean;
    //调用的机构码
    @JSONField(name = "message")
    private String message = "";
    @JSONField(name = "sysCode")
    private String sysCode = "";
    @JSONField(name = "hostName")
    private String hostName = RuntimeUtils.getHostIp();
    @JSONField(name = "procId")
    private int procId = RuntimeUtils.getPid();
    @JSONField(name = "msgTime")
    private long msgTime;
    @JSONField(name = "parentId")
    private String parentId;
    @JSONField(name = "startTime")
    private long startTime;
    @JSONField(name = "elapsedTime")
    private int elapsedTime;
    @JSONField(name = "success")
    private boolean success;
    @JSONField(name = "ps")
    private boolean ps;
    @JSONField(serialize = false)
    private Throwable throwable;
    @JSONField(name = "thrown")
    private Thrown thrown;
    @JSONField(name = "paramsIn")
    private String paramsIn;
    @JSONField(name = "paramsOut")
    private String paramsOut;
    private boolean newFlag = true;


    public TraceBean(){
        traceContextBean = new TraceContextBean();
        this.setTraceId(UUIDFactory.createUUID());
        this.setId(UUIDFactory.createUUID());
    }


    /* -------------------- TraceContextBean getter/setter ---------------------- */
    @JSONField(name = "traceId")
    public String getTraceId(){
        return traceContextBean.getTraceId();
    }
    @JSONField(name = "traceId")
    public void setTraceId(String traceId){
        traceContextBean.setTraceId(traceId);
    }
    @JSONField(name = "id")
    public String getId(){
        return traceContextBean.getId();
    }
    @JSONField(name = "id")
    public void setId(String id){
        traceContextBean.setId(id);
    }
    @JSONField(name = "className")
    public String getClassName(){
        return traceContextBean.getClassName();
    }
    @JSONField(name = "className")
    public void setClassName(String className){
        traceContextBean.setClassName(className);
    }
    @JSONField(name = "methodName")
    public String getMethodName(){
        return traceContextBean.getMethodName();
    }
    @JSONField(name = "methodName")
    public void setMethodName(String className){
        traceContextBean.setMethodName(className);
    }

    /* -------------------- TraceBean getter/setter ---------------------- */

    public TraceContextBean getTraceContextBean(){
        return traceContextBean;
    }
    public String getTraceContextBeanString() {
        return traceContextBean.toString();
    }

    public void setTraceContextBean(TraceContextBean context) {
        if (context != null){
            traceContextBean.setActive(context.isActive());
            setTraceId(context.getTraceId());
            setId(context.getId());
            setClassName(context.getClassName());
            setMethodName(context.getMethodName());
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getProcId() {
        return procId;
    }

    public void setProcId(int procId) {
        this.procId = procId;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPs() {
        return ps;
    }

    public void setPs(boolean ps) {
        this.ps = ps;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        if (throwable !=null){
            this.thrown = new Thrown(throwable);
        }
    }

    public Thrown getThrown() {
        return thrown;
    }

    public String getParamsIn() {
        return paramsIn;
    }

    public void setParamsIn(String paramsIn) {
        this.paramsIn = paramsIn;
    }

    public String getParamsOut() {
        return paramsOut;
    }

    public void setParamsOut(String paramsOut) {
        this.paramsOut = paramsOut;
    }

    public boolean isNewFlag() {
        return newFlag;
    }

    public void setNewFlag(boolean newFlag) {
        this.newFlag = newFlag;
    }

    /* ---------------------- Other operations ----------------------- */

    public void growOld(){
        this.newFlag = false;
    }

    public boolean isNew(){
        return this.newFlag;
    }

    public boolean isOld(){
        return !this.newFlag;
    }

    public boolean isNull() {
        return false;
    }

    /**
     * 根据传递的上下文信息，转换当前的Trace为其子Trace对象
     *
     * @param context
     */
    public void convert2SubTrace(TraceContextBean context){
        if(context != null){
            traceContextBean.setActive(context.isActive());
            setTraceId(context.getTraceId());
            setId(context.getId());
            setClassName(context.getClassName());
            setMethodName(context.getMethodName());
            growOld();
        }
    }

    /**
     * 复制Trace对象，不包括开始时间，耗时及返回状态三个属性以及throwable
     *
     * @return 新的Trace对象
     */
    public TraceBean copy(){
        TraceBean cloneTrace = new TraceBean();
        cloneTrace.setTraceContextBean(this.getTraceContextBean());
        cloneTrace.setParentId(this.getParentId());
        cloneTrace.setSysCode(this.getSysCode());
        cloneTrace.growOld();
        return cloneTrace;

    }
    public TraceBean createSubTrace(){
        TraceBean subTrace = this.copy();
        subTrace.setParentId(this.getId());
        subTrace.setId(UUIDFactory.createUUID());
        return subTrace;
    }
}
