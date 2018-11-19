package com.deepwise.cloud.traceLog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/22
 * @Company: DeepWise
 */
@Configuration
public class DwLogConfig {

    private static String graylogGelfUrl;

    @Value("${graylog.gelf.http.url}")
    public void setGraylogGelfUrl(String url){
        graylogGelfUrl = url;
    }

    public static String getGraylogGelfUrl(){
        return graylogGelfUrl;
    }
}
