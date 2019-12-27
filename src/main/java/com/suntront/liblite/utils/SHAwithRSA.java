package com.suntront.liblite.utils;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: SHA1withRSA
 * Author: Jeek
 * Date: 2019/11/20 18:00
 * Description: ${DESCRIPTION}
 */

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: T
 * Author: Jeek
 * Date: 2019/11/21 9:03
 * Description: ${DESCRIPTION}
 */
public class SHAwithRSA {

    public static final String TAG = "SHA1withRSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private final static String DEFAULT_CHARSET = "UTF-8";

    /**
     * RSA私钥给入参签名
     *
     * @param param 签名的数据
     * @return 返回入参签名16进制字符串
     */
    public static String sign(String param, String privateKey) {
        try {
            byte[] privateKeyByte = Base64.decode(privateKey);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec encoderule = new PKCS8EncodedKeySpec(privateKeyByte);
            PrivateKey key = keyfactory.generatePrivate(encoderule);

            Signature sign = Signature.getInstance(SIGN_ALGORITHMS);
            sign.initSign(key);
            sign.update(param.getBytes(DEFAULT_CHARSET));

            byte[] signature = sign.sign();
            return bytesToHexStr(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA公钥验证签名
     *
     * @param param     入参
     * @param signature 使用私钥签名的入参字符串
     * @return 返回验证结果
     */

    public static boolean verifyRes(String param, String signature, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicKeyByte = new Base64().decode(publicKey);
            X509EncodedKeySpec encodeRule = new X509EncodedKeySpec(publicKeyByte);
            PublicKey key = keyFactory.generatePublic(encodeRule);

            //用获取到的公钥对   入参中未加签参数param 与  入参中的加签之后的参数signature 进行验签
            Signature sign = Signature.getInstance(SIGN_ALGORITHMS);
            sign.initVerify(key);
            sign.update(param.getBytes(DEFAULT_CHARSET));

            //将16进制码转成字符数组
            byte[] hexByte = hexStrToBytes(signature);
            //验证签名
            return sign.verify(hexByte);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * byte数组转换成十六进制字符串
     *
     * @param bytes byte数组
     * @return 返回十六进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            stringBuffer.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1).toUpperCase());
        }
        return stringBuffer.toString();
    }

    /**
     * 十六进制字符串转成byte数组
     *
     * @param hexStr 十六进制字符串
     * @return 返回byte数组
     */
    public static byte[] hexStrToBytes(String hexStr) {
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexStr.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
