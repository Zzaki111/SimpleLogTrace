package com.deepwise.cloud.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/11
 * @Company: DeepWise
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.deepwise.*" })
@EnableAutoConfiguration
public class DwLogMain {
    public static void main(String[] args) {
        SpringApplication.run(DwLogMain.class);
    }
}
