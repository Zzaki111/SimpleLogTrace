package com.deepwise.cloud.traceLog.bean;

import java.io.Serializable;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public class NullTrace extends TraceBean implements Serializable {

    public NullTrace(){
        this.setId("");
        this.growOld();
    }

    public boolean isNull(){
        return true;
    }

    @Override
    public void convert2SubTrace(TraceContextBean context) {
    }

    @Override
    public TraceBean copy() {
        return this;
    }

    @Override
    public TraceBean createSubTrace() {
        return this.copy();
    }
}
