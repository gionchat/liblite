package com.suntront.liblite.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Copyright (C), 2015-2019, suntront
 * FileName: AES
 * Author: Jeek
 * Date: 2019/12/19 20:28
 * Description: AES加密和解密
 */
public class AES {

    private static final String AES_KEY = "AES";

    public static String encode(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不满足条件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_KEY);
        Cipher cipher = Cipher.getInstance(AES_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    public static String decode(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不满足条件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_KEY);
        Cipher cipher = Cipher.getInstance(AES_KEY);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        String content = "jeek";
        System.out.println("原内容 = " + content);
        String encrypt = AES.encode(content, "suntront_3002599");
        System.out.println("加密后 = " + encrypt);
        String decrypt = AES.decode(encrypt, "suntront_3002599");
        System.out.println("解密后 = " + decrypt);
    }

}