package com.deepwise.cloud.traceLog.util;

import com.alibaba.fastjson.JSON;
import com.deepwise.cloud.util.UtilLogger;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/17
 * @Company: DeepWise
 */
public class HttpUtils {

    private static PoolingHttpClientConnectionManager connectionManager;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 5000;
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());
        connectionManager.setValidateAfterInactivity(1000);
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(MAX_TIMEOUT)
                .setSocketTimeout(MAX_TIMEOUT).setConnectionRequestTimeout(MAX_TIMEOUT).build();
    }

    public static void doPost(String url, Object json) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(json), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            UtilLogger.error(LOG, e, e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    UtilLogger.error(LOG, e, e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void doPost(String url, Object[] jsons) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        httpPost.setConfig(requestConfig);
        for (Object json : jsons) {
            try {
                StringEntity stringEntity = new StringEntity(JSON.toJSONString(json), "UTF-8");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                UtilLogger.error(LOG, e, e.getMessage());
                e.printStackTrace();
            } finally {
                if (response != null) {
                    try {
                        EntityUtils.consume(response.getEntity());
                    } catch (IOException e) {
                        UtilLogger.error(LOG, e, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
