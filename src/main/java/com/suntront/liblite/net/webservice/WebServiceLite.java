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
package com.suntront.liblite.net.webservice;

import android.content.Context;
import android.util.Log;

import com.suntront.liblite.gson.GsonLite;
import com.suntront.liblite.init.Liblite;
import com.suntront.liblite.net.response.Response;
import com.suntront.liblite.utils.NetworkUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 作者: Jeek.li
 * 时间: 2018/9/21
 * <p>
 * 描述:WebService 请求简化操作类
 */

public class WebServiceLite {

    private final String TAG = "WebServiceLite";

    /**
     * @param context     上下文
     * @param visitorInfo 信息验证部分，通用用户信息
     * @param loadingText 加载对话框文本  null不显示
     * @param jsonParams  请求信息，参数，真正请求时会转换为字符串
     * @param response    处理请求response回调接口
     * @return 访问返回的RESULT 对象
     */
    public <RESULT> void request(Context context, String url, String loadingText,
                                 Map<String, Object> visitorInfo, Map<String, Object> jsonParams,
                                 Response<RESULT> response) {
        if (NetworkUtils.isNetworkAvailable(context)) {

            Log.i(TAG, "jeek framework WebServiceLite: " + (response.getClazzName()));
            Log.w(TAG, "jeek framework WebServiceLite method  null***" + "  jsonParams: " + GsonLite.toJson(jsonParams));
            WebServiceAsyncTask<RESULT> mWebServiceAsyncTask = new WebServiceAsyncTask<RESULT>(context, loadingText, null, null, response);
            mWebServiceAsyncTask.execute(url, response.getClazzName(), GsonLite.toJson(visitorInfo),
                    GsonLite.toJson(jsonParams));
        } else {
            //Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            //context.sendBroadcast(new Intent("no_network_connect"));
        }

    }

    public <RESULT> void request(Context context, String loadingText,
                                 Map<String, Object> visitorInfo, Map<String, Object> jsonParams,
                                 Response<RESULT> response) {

        request(context, Liblite.getWebserviceURL(), loadingText, visitorInfo, jsonParams, response);

    }

    public <RESULT> void request(Context context, String url, String loadingText,
                                 Map<String, Object> visitorInfo, Map<String, Object> jsonParams, String method, Type type,
                                 Response<RESULT> response) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            WebServiceAsyncTask<RESULT> mWebServiceAsyncTask = new WebServiceAsyncTask<RESULT>(context, loadingText, method, type, response);
            Log.i(TAG, "jeek framework WebServiceLite: " + (response.getClazzName()));
            Log.w(TAG, "jeek framework WebServiceLite method: " + method + "  jsonParams: " + GsonLite.toJson(jsonParams));
            mWebServiceAsyncTask.execute(url, response.getClazzName(), GsonLite.toJson(visitorInfo),
                    GsonLite.toJson(jsonParams));
        } else {
            //Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            //context.sendBroadcast(new Intent("no_network_connect"));
        }

    }

}
