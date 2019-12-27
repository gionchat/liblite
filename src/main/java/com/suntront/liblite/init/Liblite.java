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
package com.suntront.liblite.init;

import android.app.Application;

import org.xutils.x;

/**
 * @author: Jeek
 * 时间:2018/11/9
 * 邮箱:464858540@qq.com
 * 描述:通过Liblite类 实例化liblite库中Application上下文
 */
public class Liblite {


    private static Liblite liblite;
    private static Application application;
    private static String httpURL = "";
    private static String webserviceURL = "";

    private Liblite() {


    }

    /**
     * 获取Liblite实例
     *
     * @return
     */
    public static Liblite getInstance() {

        if (liblite == null) {
            liblite = new Liblite();
        }

        return liblite;
    }


    /**
     * 获取Application实例
     *
     * @return
     */
    public static Application getApplication() {
        return application;
    }

    /**
     * 通过app中SuntrontApplication传参注入application
     *
     * @return
     */
    public static Liblite init(Application mApplication) {
        Liblite.application = mApplication;
        x.Ext.init(mApplication);
        return getInstance();
    }


    public static String getHttpURL() {
        return httpURL;
    }

    public static Liblite setHttpURL(String httpURL) {
        Liblite.httpURL = httpURL;
        return getInstance();
    }

    public static String getWebserviceURL() {
        return webserviceURL;
    }

    public static Liblite setWebserviceURL(String webserviceURL) {
        Liblite.webserviceURL = webserviceURL;
        return getInstance();
    }
}
