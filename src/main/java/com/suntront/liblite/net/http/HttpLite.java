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
package com.suntront.liblite.net.http;
/**
 * 作者: Jeek.li
 * 时间: 2018/7/31
 * <p>
 * 描述:Http 请求简化操作类
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.suntront.liblite.dialog.DialogLite;
import com.suntront.liblite.gson.GsonLite;
import com.suntront.liblite.net.response.Response;
import com.suntront.liblite.utils.NetworkUtils;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;


public class HttpLite {

    public final static int GET = 0;
    public final static int POST = 1;
    private final static String TAG = "HttpLite";
    private Response response;
    private Context context;
    private DialogLite dialogLite;
    private Type type;


    /**
     * @param context     上下文
     * @param loadingText 加载对话框文本  null不显示
     * @param requestType 请求方法 get post
     * @param params      请求参数
     * @param mResponse   请求回调
     * @return 访问返回的RESULT 对象
     */
    /**
     * @param context     上下文
     * @param loadingText 加载对话框文本  null不显示
     * @param params      请求参数
     * @param mResponse   请求回调
     * @return 访问返回的RESULT 对象
     */
    public <RESULT> void request(Context context, HttpMethod method, RequestParams params, String loadingText, final Response<RESULT> mResponse) {
        Log.d(TAG, "jeek request request: " + params.getUri());
        Log.d(TAG, "jeek request params: " + params.getBodyContent());
        this.type = type;
        if (!NetworkUtils.isNetworkAvailable(context)) {
            context.sendBroadcast(new Intent("no_network_connect"));
            return;
        }
        this.response = mResponse;
        this.context = context;
        if (loadingText != null) {
            dialogLite = new DialogLite();
            dialogLite.showLoadingDialog(context, loadingText);

        }

        x.http().request(method, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "jeek request post result: " + result);
                onHandleSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "jeek request post onError: " + GsonLite.toJson(ex.getMessage()));
                onHandleError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                onHandleCancelled(cex);
            }

            @Override
            public void onFinished() {
                onHandleFinished();
                dialogLite.dismissLoadingDialog();
            }

        });


    }

    /**
     * @param context     上下文
     * @param loadingText 加载对话框文本  null不显示
     * @param params      请求参数
     * @param mResponse   请求回调
     * @return 访问返回的RESULT 对象
     */
    public <RESULT> void post(Context context, RequestParams params, String loadingText, final Response<RESULT> mResponse) {
        post(context, params, loadingText, false, null, mResponse);

    }

    /**
     * @param context     上下文
     * @param loadingText 加载对话框文本  null不显示
     * @param params      请求参数
     * @param mResponse   请求回调
     * @return 访问返回的RESULT 对象
     */
    public <RESULT> void post(Context context, RequestParams params, String loadingText, boolean isOtherContentType, Type type, final Response<RESULT> mResponse) {
        Log.d(TAG, "jeek request post: " + params.getUri());
        Log.d(TAG, "jeek request params: " + params.getBodyContent());
        if (!NetworkUtils.isNetworkAvailable(context)) {
            context.sendBroadcast(new Intent("no_network_connect"));
            return;
        }
        this.response = mResponse;
        this.context = context;
        if (loadingText != null) {
            dialogLite = new DialogLite();
            dialogLite.showLoadingDialog(context, loadingText);
        }
        if (!isOtherContentType) {
            params.setAsJsonContent(true);
            params.addHeader("Content-Type", "application/json");
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "jeek request post result: " + result);
                onHandleSuccess(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "jeek request post onError: " + GsonLite.toJson(ex.getMessage()));
                onHandleError(ex, isOnCallback);

            }

            @Override
            public void onCancelled(CancelledException cex) {
                onHandleCancelled(cex);

            }

            @Override
            public void onFinished() {
                onHandleFinished();
                if (dialogLite != null) {
                    dialogLite.dismissLoadingDialog();
                }
            }

        });


    }

    /**
     * @param context     上下文
     * @param loadingText 加载对话框文本  null不显示
     * @param params      请求参数
     * @param mResponse   请求回调
     * @return 访问返回的RESULT 对象
     */
    public <RESULT> void get(Context context, RequestParams params, String loadingText, final Response<RESULT> mResponse) {
        Log.d(TAG, "jeek request get: " + params.getUri());
        Log.d(TAG, "jeek request params: " + GsonLite.toJson(params.getQueryStringParams()));
        if (!NetworkUtils.isNetworkAvailable(context)) {
            context.sendBroadcast(new Intent("no_network_connect"));
            return;
        }
        this.response = mResponse;
        this.context = context;
        if (loadingText != null) {
            dialogLite = new DialogLite();
            dialogLite.showLoadingDialog(context, loadingText);
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "jeek request get result: " + result);
                onHandleSuccess(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "jeek request get onError: " + GsonLite.toJson(ex.getMessage()));
                onHandleError(ex, isOnCallback);

            }

            @Override
            public void onCancelled(CancelledException cex) {
                onHandleCancelled(cex);

            }

            @Override
            public void onFinished() {
                onHandleFinished();
                Log.d(TAG, "jeek onFinished.");
                if (dialogLite != null) {
                    dialogLite.dismissLoadingDialog();
                }
            }

        });


    }


    private void onHandleSuccess(Object result) {
        if (response != null) {
            try {
                JSONObject jo;
                Log.i(TAG, "jeek framework onHandleSuccess:" + result);
//                if (result.toString().contains("发送成功")) {
//
//                    Toast toast = Toast.makeText(context, "验证码发送成功", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    return;
//                }
                jo = new JSONObject(result.toString());

                if (jo.getString("result").equals("1")) {
                    Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "jeek onHandleSuccess jo.getString(\"data\"): " + jo.getString("data"));

                if (jo.getString("data") == null) {
                    response.onSuccess(null);
                } else {
                    if (type != null) {
                        response.onSuccess(GsonLite.toObject(jo.getString("data"), type));
                    } else {
                        response.onSuccess(GsonLite.toObject(jo.getString("data"), response.getClazz()));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "jeek framework onHandleSuccess Exception: " + e.getMessage());
            }
        }
        Log.i(TAG, "jeek  framework onHandleSuccess result: " + result);

    }


    private void onHandleError(Throwable ex, boolean isOnCallback) {
        if (response != null) {
            response.onFailed(new Exception(ex.getMessage()));
        }
        Log.e(TAG, "jeek  framework onHandleError: " + ex.getMessage());
    }

    private void onHandleCancelled(Callback.CancelledException cex) {

        Log.i(TAG, "jeek  framework onHandleCancelled.");
    }

    private void onHandleFinished() {

        Log.i(TAG, "jeek  framework onHandleFinished.");
        if (response != null) {
            response.onFinish();
        }
    }

}
