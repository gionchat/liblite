/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Jeek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.suntront.liblite.payment.alipay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.suntront.liblite.gson.GsonLite;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, suntront
 * Author: Jeek
 * Date: 2019/12/17 11:18
 * Description:
 */
public class AliPay {

    public static final String TAG = "AliPay";
    private Activity activity;
    private AlipayCallBack alipayCallBack;

    public AliPay(Activity activity) {
        this.activity = activity;
    }

    public void pay(final String orderInfo, final AlipayCallBack alipayCallBack) {
        this.alipayCallBack = alipayCallBack;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                PayResult payResult = new PayResult(result);
                String resultInfo = payResult.getResult();
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    Log.d(TAG, "jeek AliPay 成功 resultInfo: " + resultInfo);
                    if (alipayCallBack != null) {
                        alipayCallBack.success(resultInfo);
                    }
                } else {
                    Log.d(TAG, "jeek AliPay 失败 : " + GsonLite.toJson(payResult));
                    if (alipayCallBack != null) {
                        alipayCallBack.fail();
                    }
                }

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public static interface AlipayCallBack {
        /**
         * 支付成功
         */
        void success(String resultInfo);

        /**
         * 支付失败
         */
        void fail();

    }
}
