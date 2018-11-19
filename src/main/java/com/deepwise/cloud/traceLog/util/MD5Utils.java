package com.deepwise.cloud.traceLog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/7
 * @Company: DeepWise
 */
public class MD5Utils {

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String md5Hex(final String data) {
        return encodeHexString(md5(data));
    }

    public static byte[] md5(final String data) {
        return md5(data.getBytes());
    }

    public static byte[] md5(final byte[] data) {
        return getMd5Digest().digest(data);
    }

    public static MessageDigest getMd5Digest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
}
