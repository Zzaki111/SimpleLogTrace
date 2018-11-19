package com.deepwise.cloud.traceLog.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/4
 * @Company: DeepWise
 */
public class TraceContextBean implements Serializable {

    private static final long serialVersionUID = 3682302749901273660L;

    @JSONField(name = "traceId")
    private String traceId = "";
    @JSONField(name = "id")
    private String id = "";
/*    @JSONField(name = "serviceName")
    private String serviceName = "";*/
    @JSONField(name = "className")
    private String className = "";
    @JSONField(name = "methodName")
    private String methodName = "";
    @JSONField(serialize = false)
    private boolean active = false;
    public static final String FIELD_SEPARATOR = "#";

    public TraceContextBean(){

    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.traceId);
        buf.append(FIELD_SEPARATOR).append(this.id);
        buf.append(FIELD_SEPARATOR).append(this.className);
        buf.append(FIELD_SEPARATOR).append(this.methodName);
        return buf.toString();
    }
}
