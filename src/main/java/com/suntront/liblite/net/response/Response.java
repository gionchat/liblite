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
package com.suntront.liblite.net.response;

import android.util.Log;

import java.lang.reflect.ParameterizedType;

/**
 * 作者: Jeek.li
 * 时间: 2018/10/12
 * <p>
 * 描述:HttpLite、WebServiceLite返回结果回调
 */
public abstract class Response<RESULT> {

    private final static String TAG = "Response";
    private Class<RESULT> clazz;

    public Response() {

        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.clazz = (Class<RESULT>) type.getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public abstract void onSuccess(RESULT result);

    public void onData(String data) {
        Log.i(TAG, "jeek  framework onData: " + data);
    }

    public void onFinish() {
        Log.i(TAG, "jeek  framework onFinish.");
    }

    public void onFailed(Exception e) {

        Log.e(TAG, "jeek  framework onFailed Exception: " + e.getMessage());
    }

    public Class<RESULT> getClazz() {
        return clazz;
    }

    public String getClazzName() {
        String className = "";
        try {
            className = clazz.getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return className;
    }

}
