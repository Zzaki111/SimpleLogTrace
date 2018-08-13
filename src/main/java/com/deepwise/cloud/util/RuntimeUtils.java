package com.deepwise.cloud.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/4
 * @Company: DeepWise
 */
public class RuntimeUtils {
    private static String HOST_IP;
    private static String VM_IP;
    private static String CONTAINER_IP;
    private static volatile boolean isInit = false;


    /**
     * Get current process id, if any exception thrown, use the random integer instead
     *
     * @return
     */
    public static int getPid() {
        String processId;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int index = name.indexOf('@');
        if (index > 0) {
            processId = name.substring(0, index);
        } else {
            processId = name;
        }
        try {
            return Integer.valueOf(processId);
        } catch (Exception e) {
            // 随机数设置为负值
            return -new Random().nextInt(65535);
        }
    }

    public static String getHostIp() {
        if (UtilCompare.isEmpty(HOST_IP)) {
            if (getVMIp().equals(getContainerIp())) {
                HOST_IP = getVMIp();
            } else {
                HOST_IP = getVMIp() + "," + getContainerIp();
            }
        }
        return HOST_IP;
    }

    public static String getVMIp() {
        if (UtilCompare.isEmpty(VM_IP)) {
            try {
                String vmIp = System.getProperty("server.ip");
                if (UtilCompare.isNotEmpty(vmIp)) {
                    VM_IP = vmIp;
                    return VM_IP;
                }

                if (UtilCompare.isEmpty(VM_IP)) {
                    VM_IP = InetAddress.getLocalHost().getHostAddress();
                }
            } catch (Exception e) {
                VM_IP = "127.0.0.1";
            }
        }
        return VM_IP;
    }

    public static String getContainerIp() {
        if (UtilCompare.isEmpty(CONTAINER_IP)) {
            try {
                CONTAINER_IP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return "127.0.0.1";
            }
        }
        return CONTAINER_IP;
    }
}
