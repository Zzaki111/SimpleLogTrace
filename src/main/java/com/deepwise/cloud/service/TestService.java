package com.deepwise.cloud.service;

import com.deepwise.cloud.annotation.DwTrace;
import org.springframework.stereotype.Component;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/13
 * @Company: DeepWise
 */
@Component
public class TestService {

    @DwTrace(bizCode = "DW_LOG")
    public String test(int flag) throws Exception {
        if (flag == 0 )
            throw new Exception("wrong flag");
        return "test";
    }

    @DwTrace(bizCode = "DW_LOG")
    public String test2() throws Exception{
        return "test";
    }
}
