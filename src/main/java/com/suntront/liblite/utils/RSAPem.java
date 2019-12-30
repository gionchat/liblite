package com.suntront.liblite.utils;

import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: RSA
 * Author: Jeek
 * Date: 2019/12/20 8:41
 * Description: RSA公钥加密-私钥解密/私钥加密-公钥解密
 */
public class RSAPem {

    public static final String TAG = "RSA";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoM1J1XRDOUkbFv1f7gbViuYDZ+UQF9IlWHRtObiQtiyFHdptaE9CMSkLnWi46fs7pcExlDmdNVWJKLemua8jCp8o/ExQGG7IhBYPINqygRIuaWs18uuYKz8zFLyxv9xK7XyaIDvESjWXdwIesDvw/lckeiiUxBe1XdYhmOUTmIn4jnTimsgO+OR515D4Aj2kJl0G6OqP3/4RvTQ/KrSdg0i/tDGpmNQ3yE73uhYfd92v+ggd2BOHPpoGLJNxkthzL9KjrW0ARBLDgEvfS00ejqIvei2K/J5HjB+8Esp28WDDlIjrKT+dIF1uz2R/2AjC080z1ayPaDkcueqDjASZcwIDAQAB";
    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSYv8QMSyPj/XcSN1YjR6X+CeBrp2P0+W1TvGoR8tpnT3407jxEtbQqOfNGNDC3Htxep3wUwk4ed/DNVqpcmS09T/LpGz4xVG5qnxmwBXG2YctJhMVuWg8sOUltQvXCvjcXeWq80Mu5zhQfhqrfZROCnYJWbtkpu1iF3ae2ezYdLRO/K7p9Bbn1U+S0eplCwBD9XmTenOg509RSFbTp7gxPN0xHtgyCJPTUu2W68f4Hrl3Iew0SI3GlGXDy0Cr1v8Hy7RbtT641ZL77XyYutgQVTbxvev0uPflPLkLq453w16Q7W8+a1Mmc18kPHS2ETfgOtYbz3BY0I+nSR0yG/QBAgMBAAECggEAKK3h+3nuzCZRSFnnSMhOjCSltd/0YRedafj6Jq1s1Xig1Bjh9/woPevyHoia2suyciKE6Vfp5AvaJsEY5KWpXrAYflq+/ZK310SNNXCDnLKuYdmGzC6tzM/LneLl22htJ6/gNtVLAim2+IdSmf1XzSgTDR4meR36MZwblqEwtCWupUtDctNksch/inoX5aOzGgYPCt4G+jrwIpSEg186dhET03+b0HqgcTZJCKw5tH6xdU1iHcZKL+Z/S5DOLdRYGfZmKGy67tox/8fz1FoiWx29ASe+WMNj8EROJ3c4SCjzQkwaYTEJvIJ6emkskyEEbD1ESuReHOecj9Lmup4VkQKBgQDGjFhxrZ0DeuBv3Fqcsg77f2We/gbNivKh835Vot9hZu2ZARZ+oG6YoUuyO3hrWJVzG/wEWNLgo3cYY6EdOc31BRM3LpC8JhiGyqp5v76QnxfEpDeqoO267ey8c7p0DA/XoQhPRCBdkyYw+KbYfxaieVcYHiHbtlsusXT/UTpcZwKBgQC8vrwuazF3jHuhbZLD1ctfRL4OovzCegMmg3OnOr0C/oz6BD1wAICcLLRg4APxgFMoRlxI+PkUHRatxn7nW7vs2lCUlUo/Rpd560nR7RZGIa2PjmZE6zAZUrvLn5G/D6QSq8HcfM2WwWeCN3QAPNw6PabXWM/rxJ69r5vpD2zrVwKBgQCHe10etQlfRom/yu1J33a5xsThKRYbS6UlPbIHXYxwOnwG2+ctoOvW2rl8dXhGR7wzP/VIQhQjlWe/DLzz5G3VtBDYpr28pYI2+DQ7v8uZg/YECMqzc/zEgXX5Yj7o0RI+YXP6ItKzB6YX5JR1RsN7OrFjk6wm8NhnlE/xqMvaIQKBgCRHcTpm5kI5YYYc3qn/zTCJsRJ1J6pl83cSq0dIlg4eSZDBAeImZpIeUamRRZDE/rbuF0z7djz0fmh2j+v+iPe02upEa9oPwovvuue7750ZWhgO4f97SWqEqY4xJL2Zzwq7su6jmozQrgmsiQKGsDoNoaUg8A48Y9rIB2rbyS7PAoGACuD4Mg8CQkuwnfkzx0yOZjZN3iRLXcu5rkYbKGYms6kpm7XVOMg9DTFFnU8Qp0c+KfeJIUpITaLWZTB68O4rGLzswDgkfvSrGRi9LNL3pTSIE9Duh0GpxuXHHzdFniGViQX0llWwtPeIi6i152kRnId/DLZqi2QBb9KuI9oKIhs=";
    public final static String UTF8 = "utf-8";
    private static final String KEY_ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 2048;
    //private static Map<Integer, String> keyMap = new HashMap<Integer, String>();

    public static String formatString(String source) {
        if (source == null) {
            return null;
        }
        return source.replaceAll("\\r", "").replaceAll("\\n", "");
    }

    //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //genKeyPair();
        //加密字符串
        String message = "suntront";
        System.out.println("PUBLIC_KEY:" + PUBLIC_KEY);
        System.out.println("PRIVATE_KEY:" + PRIVATE_KEY);
        System.out.println("===============================================公钥加密、私钥解密==============================================");
        //公钥加密、私钥解密
        String messageEn = encodeWithPublicKey(message, PUBLIC_KEY);
        System.out.println(message + "公钥加密后的字符串为:" + messageEn);
        String messageDe = decodeWithPrivateKey(messageEn, PRIVATE_KEY);
        System.out.println("私钥解密字符串为:" + messageDe);
        System.out.println("===============================================私钥加密、公钥解密==============================================");
        //私钥加密、公钥解密
        String enc = encodeWithPrivateKey(message, PRIVATE_KEY);
        System.out.println(message + "私钥加密后的字符串为:" + enc);
        String de = decodeWithPublicKey(enc, PUBLIC_KEY);
        System.out.println("公钥解密后的字符串:" + de);
        System.out.println("===============================================签名、验签====================================================");
        System.out.println(message + "签名后的字符串为:" + SHAwithRSA.sign(message, PRIVATE_KEY));
        System.out.println(message + "验证签名结果:" + SHAwithRSA.verifyRes(message, SHAwithRSA.sign(message, PRIVATE_KEY), PUBLIC_KEY));


    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(RSA_KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.encode(publicKey.getEncoded());
        String privateKeyString = Base64.encode((privateKey.getEncoded()));
        System.out.println("===============================================公钥==============================================");
        System.out.println("公钥为:" + publicKeyString);
        System.out.println("私钥为:" + privateKeyString);
        System.out.println("===============================================私钥==============================================");
        // 将公钥和私钥保存到Map
        //keyMap.put(0, publicKeyString);// 0表示公钥
        //keyMap.put(1, privateKeyString);// 1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */

    public static String encodeWithPublicKey(String str, String publicKey) throws Exception {
        publicKey = formatString(publicKey);
        //base64编码的公钥
        byte[] decoded = Base64.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encode(cipher.doFinal(str.getBytes(UTF8)));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decodeWithPrivateKey(String str, String privateKey) throws Exception {
        privateKey = formatString(privateKey);
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str);
        // base64编码的私钥
        byte[] decoded = Base64.decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 私钥加密
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     */
    public static String encodeWithPrivateKey(String data, String privateKey) {
        try {
            privateKey = formatString(privateKey);
            byte[] kb = Base64.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(kb);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = data.getBytes(UTF8);
            byte[] encrypt = cipher.doFinal(b);
            return Base64.encode(encrypt);
        } catch (Exception e) {
            Log.d(TAG, "jeek Failed to encryptByPrivateKey data： " + e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * 公钥解密
     *
     * @param data      解密数据
     * @param publicKey 公钥
     * @return
     */
    public static String decodeWithPublicKey(String data, String publicKey) {
        try {
            publicKey = formatString(publicKey);
            byte[] kb = Base64.decode(publicKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(kb);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //Cipher cipher = Cipher.getInstance(RSA_PADDING_KEY);
            cipher.init(Cipher.DECRYPT_MODE, key);
            //byte[] b = data.getBytes(UTF8);
            byte[] decrypt = cipher.doFinal(Base64.decode(data));
            return new String(decrypt, UTF8);
        } catch (Exception e) {
            Log.d(TAG, "jeek Failed to encryptByPrivateKey data： " + e.getMessage());
            throw new RuntimeException();
        }
    }
}