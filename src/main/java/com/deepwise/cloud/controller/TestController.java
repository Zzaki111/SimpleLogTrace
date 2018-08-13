package com.deepwise.cloud.controller;

import com.deepwise.cloud.annotation.DwTrace;
import com.deepwise.cloud.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/11
 * @Company: DeepWise
 */
@RestController
public class TestController {

    @Autowired
    TestService testService;


    @DwTrace(bizCode = "DW_LOG")
    @RequestMapping("/test")
    public String test(int flag) throws Exception {
        return testService.test(flag);
    }

    @DwTrace(bizCode = "DW_LOG")
    @RequestMapping("/test2")
    public String test2() throws Exception {
        return testService.test2();
    }
}
