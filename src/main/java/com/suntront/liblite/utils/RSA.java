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
public class RSA {

    public static final String TAG = "RSA";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuiwg24865vMhFrjs1gBzwFnD/lC95EjZ2vCjh/oaXunwDl1Eys6z73hf944G7e/tFykuulQLiYtFjR6ID/0l5iKLx9hJ4kjDSvTGx0h/q78ncWwk1Ch8/18dPTO8b6tjaTPYNXJUjwmK+FV1fdqeHZ6BnvLaBKNzaYN5WLmH1Oq8WbANYMkjR9o6WuE9wa4cRsEyKlYHkkcFQDi+61jS53POoZDqM3vcKkxgqEr6YBcwhaiCaAlndKctUJGaqXqX/OP/Dv9txW+gns0CDbdbwhrmpHqrfrYg8KXAIgyv+qB1vxq53nridNVnZw4svTod+QFoscNzFExdSh4d58PjowIDAQAB";
    public static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6LCDbjzrm8yEWuOzWAHPAWcP+UL3kSNna8KOH+hpe6fAOXUTKzrPveF/3jgbt7+0XKS66VAuJi0WNHogP/SXmIovH2EniSMNK9MbHSH+rvydxbCTUKHz/Xx09M7xvq2NpM9g1clSPCYr4VXV92p4dnoGe8toEo3Npg3lYuYfU6rxZsA1gySNH2jpa4T3BrhxGwTIqVgeSRwVAOL7rWNLnc86hkOoze9wqTGCoSvpgFzCFqIJoCWd0py1QkZqpepf84/8O/23Fb6CezQINt1vCGuakeqt+tiDwpcAiDK/6oHW/GrneeuJ01WdnDiy9Oh35AWixw3MUTF1KHh3nw+OjAgMBAAECggEAfyXaPvmAWfDcvV8I6VMfutPTxqGwPvP+Tcghd6G5Vu9gsv7CF9y6CSfmKvVGCETfYP0QW9NAmeyTUMD0Xi8fLiCT5KX0N+ZH8q5gAASkpVCFCLK6KGeKjwKPvpfR71ZXK6dr13zBAXub/OSovI6NhF8X2c20VoynKv3O52i2uxN0QD00kBwWcEFgwR8PjzHK+epjW4ugzhqj6wa4DQgKJEVwceZDPJZIWL4iHcG2JP7AIh83HrAnEWyirs/bCw5Cf0/nFuXXwkQ2HH4PJMwPPPu2YQ/PDi7KCtOfeiLQiuAO/Dizhzh4Z1cBCtVc0UIUUGguKDNiHDdgKkT9rvnNoQKBgQD/J+whsXqKop/PtXbiVGennyZ2wgUZpJrFmOvwnu/pchJSur33IF6LANIUD3MdyWVIJXmrDGvjkBXzq1LIxpBSb+DZuuSwWip6EZjWCYLKVt8k84am70tasoBluODqmP5wsA8qoU2H+vlLQvpiii+FO/ZGQyfurxiN7Uzx/4kEMQKBgQC6ycmc2dEFZPhuMHyU05V/m/gFhvG52ozJEpa/U4HZGsk2/yUk2j4LZSn1Oe3DJOlSyzjHx0igosb/3rwkH0WAJlbB1q/sk2pk2aw8+cKlfQnf5bV/P+wgMiMHgIWpLKqsl+XdFQPDYnEW/RLWMI3ScJ3O8Sn0TkUh23O7YyDUEwKBgExZC8UBMCOviZZDPDF5BBMaAg5i+9eyc0VQ/rhfNnZp1aW/jvwVSXTaaAHspYtyKcYuwHs4GkhbYtYom3rMP38LTwKXc7p5IGCqR0s0SVh2KgsKe24UCD45T0Ygzdwkr23UoJUm8uYPaOzmO7mkk5eQW56mlZpL9lLxgKagycThAoGANU5HAkNLGn4BQ/kEZLipTnylYHZtX7vWN7/dU4ayZNH3IIHaCNVbpYbeXYSFrQ5Io/8pGiJuQKGTpwvap3hhLjxCwBjTpfFACL3yIIiqmu2/Nq1oiYMlkN6eDpMIEgqUPqMuz1Qi+cyLN6NLj+LVZnwLHTbH4VfA9POa6JxWjw8CgYAfwpfFmjKIr+NkVQAbXOWZ+JYGKx5UGx4oARkCGzI42I5baoRMcZE2ewc/uKcz3ThqFPKIR9v4tNFPeDWLS5mOHhmqlbBQ9mwsy7zUpBqiRT0dgvOtwiY4w0rS5ehFww1CnzG7id4GPR9icb7wkkdHf8YI05jMKunMlyXgIhhqbw==";
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
        //初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(RSA_KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.encode(publicKey.getEncoded());
        String privateKeyString = Base64.encode((privateKey.getEncoded()));
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
