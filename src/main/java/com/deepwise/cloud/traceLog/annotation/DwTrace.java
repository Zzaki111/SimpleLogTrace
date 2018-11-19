package com.deepwise.cloud.traceLog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/9
 * @Company: DeepWise
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DwTrace {

    String bizCode() default "";
}
