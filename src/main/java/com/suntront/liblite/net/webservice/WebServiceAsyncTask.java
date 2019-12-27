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

import com.suntront.liblite.async.AsyncTaskLite;
import com.suntront.liblite.dialog.DialogLite;
import com.suntront.liblite.gson.GsonLite;
import com.suntront.liblite.init.Liblite;
import com.suntront.liblite.net.response.Response;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.lang.reflect.Type;


/**
 * 作者: Jeek.li
 * 时间: 2018/9/25
 * <p>
 * 描述:异步任务处理WebService请求
 */
public class WebServiceAsyncTask<RESULT> extends AsyncTaskLite<String, Integer, String> {

    private final static String TAG = "WebServiceAsyncTask";
    private Response response;
    private Context context;
    private String loadingText = null;
    private Type type;
    private String method = "";
    private DialogLite dialogLite;

    public WebServiceAsyncTask() {

    }

    public WebServiceAsyncTask(Context context) {
        this.context = context;
    }

    public WebServiceAsyncTask(Context context, String loadingText, String method, Type type, Response response) {
        this.context = context;
        this.loadingText = loadingText;
        this.response = response;
        this.type = type;
        this.method = method;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (loadingText != null) {
            dialogLite = new DialogLite();
            dialogLite.showLoadingDialog(context, loadingText);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String NAME_SPACE = "http://www.suntront.com/";
        String url = params[0];
        SoapObject request = null;
        if (params[1] == null || params[1].equals("")) {
            request = new SoapObject(NAME_SPACE, method);
        } else {
            request = new SoapObject(NAME_SPACE, params[1]);
        }
        request.addProperty("visitorInfo", params[2]);
        request.addProperty("jsonParams", params[3]);

        //Log.i(TAG, "jeek framework url:" + params[0]);
        //Log.i(TAG, "jeek framework class method:" + params[1]);
        //Log.i(TAG, "jeek framework list  method:" + method);
        Log.i(TAG, "jeek framework webService请求接口:" + params[0] + "?op=" + (method == null ? params[1] : method));
        Log.i(TAG, "jeek framework visitorInfo:" + params[2]);
        Log.i(TAG, "jeek framework jsonParams:" + params[3]);
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        AndroidHttpTransport androidHttpTransport;
        if (url.equals("")) {
            androidHttpTransport = new AndroidHttpTransport(Liblite.getWebserviceURL(), 1000 * 10);
        } else {
            androidHttpTransport = new AndroidHttpTransport(url, 1000 * 10);
        }
        try {
            String SOAP_ACTION = "";
            if (params[1] == null || params[1].equals("")) {
                SOAP_ACTION = "http://www.suntront.com/" + method;
            } else {
                SOAP_ACTION = "http://www.suntront.com/" + params[1];
            }
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d(TAG, "june framework envelope.bodyIn:" + envelope.bodyIn + "");
            SoapObject result = (SoapObject) envelope.bodyIn;
            Object property = result.getProperty(0);
            String string = property.toString();
            Log.i(TAG, "jeek request methons: " + method + params[1] + "  Params: " + request.toString());
            Log.i(TAG, "jeek framework class method " + params[1] + " list method: " + method + " webService返回信息: " + string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "jeek framework WebServiceAsyncTask doInBackground Exception: " + e.toString());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (result.equals("") && response != null) {
                //Toast.makeText(context, "接口请求异常", Toast.LENGTH_SHORT).show();
                response.onFailed(new Exception("WebServiceAsyncTask doInBackground return result null Exception........"));
                return;
            }
            JSONObject mJSONObject = new JSONObject(result.replace("\\", "").replace(":\"{", ":{").replace("}\"}", "}}").replace("�����ɹ�", ""));
            if (mJSONObject.getInt("Result") == 1) {
                if (response != null) {
                    Log.w(TAG, "jeek framework WebServiceAsyncTask Data: " + mJSONObject.getString("Data"));
                    response.onData(mJSONObject.getString("Data"));
                    Log.i(TAG, "jeek framework response.getClazz(): " + response.getClazz());
                    if (type != null) {
                        response.onSuccess(GsonLite.toObject(mJSONObject.getString("Data"), type));
                    } else {
                        response.onSuccess(GsonLite.toObject(mJSONObject.getString("Data"), response.getClazz()));
                    }
                    if (loadingText != null) {
                        dialogLite.dismissLoadingDialog();
                    }
                }
            } else {
                if (loadingText != null) {
                    dialogLite.dismissLoadingDialog();
                }
                response.onFailed(new Exception(mJSONObject.getString("Msg")));
            }
            if (loadingText != null) {
                dialogLite.dismissLoadingDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (response != null) {
                response.onFailed(e);
            }
            Log.e(TAG, "jeek framework WebServiceAsyncTask onPostExecute Exception: " + e.toString());
            return;
        } finally {
            if (loadingText != null) {
                dialogLite.dismissLoadingDialog();
            }
        }

    }

}
