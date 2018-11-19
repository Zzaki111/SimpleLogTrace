package com.deepwise.cloud.traceLog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.deepwise.cloud.traceLog.util.ExceptionUtils;

import java.io.Serializable;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/4
 * @Company: DeepWise
 */
public class Thrown implements Serializable {

    private static final long serialVersionUID = 5486531197458937471L;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "stack")
    private String stack;

    public Thrown(Throwable t){
        this.name = t.getClass().getName();
        this.message = t.getLocalizedMessage();
        this.stack = ExceptionUtils.getStackTrace(t);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
