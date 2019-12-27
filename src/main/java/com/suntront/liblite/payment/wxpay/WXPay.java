package com.suntront.liblite.payment.wxpay;

import android.content.Context;
import android.util.Log;

import com.suntront.liblite.gson.GsonLite;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: WXPay
 * Author: Jeek
 * Date: 2019/10/24
 * Description: 微信支付工具类 new WXPay.PayBuilder(result.getAppid(),result.getPartnerid(),result.getPrepayid(),result.get_package(),result.getNoncestr(),result.getTimestamp(),result.getSign()).build().toWeChatPay(PayChargeActivity.this);
 */

public class WXPay {


    public static final String TAG = "WXPay";
    private IWXAPI iwxapi; //微信支付api

    private PayBuilder builder;

    private WXPay(PayBuilder builder) {
        this.builder = builder;
    }

    /**
     * 调起微信支付的方法,不需要在客户端签名
     **/
    public void toWeChatPay(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, null); //初始化微信api
        iwxapi.registerApp(builder.getAppId()); //注册appid  appid可以在开发平台获取

        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = builder.getAppId();
                request.partnerId = builder.getPartnerId();
                request.prepayId = builder.getPrepayId();
                request.packageValue = builder.getPackageValue();
                request.nonceStr = builder.getNonceStr();
                request.timeStamp = builder.getTimeStamp();
                request.sign = builder.getSign();
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信支付的方法,需要在客户端签名
     **/
    public void toWeChatPaySign(Context context, String appid, final String key) {
        iwxapi = WXAPIFactory.createWXAPI(context, null); //初始化微信api
        iwxapi.registerApp(appid); //注册appid  appid可以在开发平台获取
        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = builder.getAppId();
                request.partnerId = builder.getPartnerId();
                request.prepayId = builder.getPrepayId();
                request.packageValue = "Sign=WXPay";
//                request.nonceStr = genNonceStr();
//                request.timeStamp = String.valueOf(genTimeStamp());
                request.nonceStr = builder.getNonceStr();
                request.timeStamp = builder.getTimeStamp();
                request.sign = builder.getSign();
                //签名
                LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
                signParams.put("appid", request.appId);
                signParams.put("noncestr", request.nonceStr);
                signParams.put("package", request.packageValue);
                signParams.put("partnerid", request.partnerId);
                signParams.put("prepayid", request.prepayId);
                signParams.put("timestamp", request.timeStamp);
                request.sign = genPackageSign(signParams, key);
                Log.i(TAG, "jeek wechatpay signParams: " + GsonLite.toJson(signParams));
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信APP支付，签名
     * 生成签名
     */
    private String genPackageSign(LinkedHashMap<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(key);

        String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    /**
     * md5加密
     *
     * @param buffer
     * @return
     */
    private String getMessageDigest(byte[] buffer) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    /**
     * 获取时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static class PayBuilder {
        public String appId;
        public String partnerId;

        public String prepayId;
        public String packageValue;
        public String nonceStr;
        public String timeStamp;
        public String sign;

        public PayBuilder(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign) {
            this.appId = appId;
            this.partnerId = partnerId;
            this.prepayId = prepayId;
            this.packageValue = packageValue;
            this.nonceStr = nonceStr;
            this.timeStamp = timeStamp;
            this.sign = sign;
        }

        public WXPay build() {
            return new WXPay(this);
        }

        public String getAppId() {
            return appId;
        }

        public PayBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public PayBuilder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public PayBuilder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public PayBuilder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public PayBuilder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public PayBuilder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public String getSign() {
            return sign;
        }

        public PayBuilder setSign(String sign) {
            this.sign = sign;
            return this;
        }
    }


}
