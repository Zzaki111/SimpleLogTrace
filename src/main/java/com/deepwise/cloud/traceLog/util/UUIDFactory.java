package com.deepwise.cloud.traceLog.util;

import com.deepwise.cloud.util.UtilCompare;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public class UUIDFactory {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static int JVM = (int) (System.currentTimeMillis() >>> 8);
    /**
     * virtual machine or the container ip, when app running directly on machine , the container ip is same as virtual machine
     */
    private static String ip;

    /**
     * the app progress id
     */
    private static String pid = formatPID(RuntimeUtils.getPid());


    /**
     * JVM Serialize number
     */
    private static String formatJvm = format(getJVM());


    static {
        ip = RuntimeUtils.getHostIp();
    }

    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - -128 + bytes[i];
        }
        return result;
    }

    private static long getTime() {
        return System.currentTimeMillis();
    }

    private static String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    private static String formatPID(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("0000");
        if (formatted.length() > 4) {
            formatted = formatted.substring(0, 4);
        }
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    private static String format(long longval) {
        String formatted = Long.toHexString(longval);
        StringBuffer buf = new StringBuffer("00000000000");
        buf.replace(11 - formatted.length(), 11, formatted);
        return buf.toString();
    }

    public static int getJVM() {
        return JVM;
    }

    private static String getCount() {
        return String.valueOf((counter.getAndIncrement() & 0x7fffffff) % 9000 + 1000);
    }


    /**
     * 返回根据主机IP、进程号、JVM编号、时间、计数器等组合计算的全局唯一ID
     *
     * @return 全局唯一ID，长度为20位
     */
    public static String createUUID() {
        return createUUID("");
    }

    /**
     * 返回根据主机IP、进程号、JVM编号、时间、计数器、后缀等组合计算的全局唯一ID
     *
     * @param suffix 后缀标识，长度最长为4位
     * @return 全局唯一ID，长度为20~24位
     */
    public static String createUUID(String suffix) {
        String count = getCount();
        String timeStr = format(getTime());
        String prefix = MD5Utils.md5Hex(timeStr + ip + pid + formatJvm + count);
        prefix = prefix.substring(0,5);
        StringBuilder buf = new StringBuilder(24);
        if (UtilCompare.isNotEmpty(suffix)) {
            buf.append(prefix).append(timeStr).append(count);
            buf.append(suffix.length() > 4 ? suffix.substring(0, 4) : suffix);
            return buf.toString();
        }

        return buf.append(prefix).append(timeStr).append(count).toString();
    }

    /**
     * 返回根据时间和计数器生成15位全局唯一ID
     *
     * @return 全局唯一ID，长度为15位
     */
    public static String createShortUUID() {
        return format(getTime()) + getCount();
    }
}
