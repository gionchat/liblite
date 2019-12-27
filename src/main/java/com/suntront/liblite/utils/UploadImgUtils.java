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
package com.suntront.liblite.utils;


import android.app.Activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UploadImgUtils {


    public static final String KEY = "key";
    public static final String T = "t";
    public static final String V = "v";
    public static final String DEVICEID = "deviceid";
    public static final String APP = "app";
    private static UploadImgUtils fileUpload;
    private static UploadCallback uploadCallback;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    private String upLoadServerUri;
    private int serverResponseCode;
    private String localFilePath;
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            uploadFile(localFilePath);
        }
    };

    private UploadImgUtils(Activity activity) {
        uploadCallback = (UploadCallback) activity;
    }

    public static UploadImgUtils getInstance(Activity activity) {

        if (fileUpload == null) {

            fileUpload = new UploadImgUtils(activity);
        }

        return fileUpload;

    }

    public void startUpload(final String fileUploadUrl, final String localFilePath) {
        this.upLoadServerUri = fileUploadUrl;
        this.localFilePath = localFilePath;
        new Thread(networkTask).start();

    }

    public int uploadFile(final String localFilePath) {
        sendStartMsg();
        String fileName = localFilePath.substring(localFilePath.lastIndexOf("/") + 1);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 2 * 1024 * 1024;
        File sourceFile = new File(localFilePath);

        if (!sourceFile.isFile()) {
            LogUtils.d("Source File not exist :" + localFilePath);
            return 0;
        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                LogUtils.d("fileInputStream");
                URL url = new URL(upLoadServerUri);
                LogUtils.d("upLoadServerUri: " + upLoadServerUri + " sourceFileUri: " + localFilePath);
                LogUtils.d("url");
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                String time = String.valueOf(System.currentTimeMillis() / 1000);
                conn.setRequestProperty(KEY, SecUtil.getKey(time));
                conn.setRequestProperty(V, String.valueOf(VersionUtils.getVersionCode()));
                conn.setRequestProperty(DEVICEID, "ffffffff-9eaf-9658-ffff-ffff99d603a9");
                conn.setRequestProperty(APP, "android");
                conn.setRequestProperty(T, time);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"upload\";" +
                        "filename= \"" + fileName + "\""
                        + lineEnd);
                dos.writeBytes(lineEnd);
                LogUtils.d("writeBytes");
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                LogUtils.i("lijun bytesRead: " + bytesRead);
                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    LogUtils.d("bytesRead");
                }
                LogUtils.d("while");
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                //dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                StringBuilder sb = new StringBuilder();
                sb.append(twoHyphens + boundary + lineEnd);
                sb.append("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                sb.append(lineEnd);
//                sb.append(SPreference.readLoginResult().getUserId() + lineEnd);
                dos.writeBytes(sb.toString());
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                LogUtils.d("serverResponseMessage: " + serverResponseCode);
                LogUtils.i("HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseMessage);
                if (serverResponseCode != 200) {
                    LogUtils.d("upload error");
                    sendErrorMsg();
                }

                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                final String result = br.readLine();
                LogUtils.i("result:" + result);

//                final AvatarData avatarData = GsonHelper.parseJsonObject(result, AvatarData.class);
//
//                if (serverResponseCode == 200) {
//                    LogUtils.i("upload success " + avatarData.getData());
//                    sendSuccessMsg(avatarData.getData());
//                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                LogUtils.d("MalformedURLException");
                sendErrorMsg();
                ex.printStackTrace();
                LogUtils.i("error: " + ex.getMessage());
            } catch (Exception e) {
                LogUtils.d("Exception: " + e.toString());
                sendErrorMsg();
                e.printStackTrace();
            }

            sendFinishMsg();
            return serverResponseCode;
        } // End else block
    }

    private void sendStartMsg() {

        uploadCallback.onUploadStart();

    }


    private void sendFinishMsg() {

        uploadCallback.onUploadFinish();

    }

    private void sendSuccessMsg(String backUri) {

        uploadCallback.onUploadSuccess(backUri);

    }


    private void sendErrorMsg() {

        uploadCallback.onUploadError();

    }

    public interface UploadCallback {

        void onUploadStart();

        void onUploadFinish();

        void onUploadSuccess(String avatarUrl);

        void onUploadError();

    }

}
