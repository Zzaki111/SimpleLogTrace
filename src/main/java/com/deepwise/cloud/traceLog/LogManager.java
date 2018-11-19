package com.deepwise.cloud.traceLog;


import com.deepwise.cloud.traceLog.client.BasicLogClient;
import com.deepwise.cloud.traceLog.client.LogClient;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public class LogManager {

    private static LogClient client;

    public static LogClient client(){
        if(client == null){
            synchronized (LogClient.class){
                if(client == null){
                    client = new BasicLogClient();
                }
            }
        }
        return client;
    }

}
