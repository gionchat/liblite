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

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private static final String ENCODING = "UTF-8";
    private static ThreadLocal<MessageDigest> DIGESTER_CONTEXT = new ThreadLocal<MessageDigest>() {
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

    };

    public static long halfDigest(String paramString)
            throws UnsupportedEncodingException {
        return halfValue(md5(paramString.getBytes(ENCODING)));
    }

    public static long halfDigest(byte[] paramArrayOfByte, int paramInt1,
                                  int paramInt2) {
        return halfValue(md5(paramArrayOfByte, paramInt1, paramInt2));
    }

    private static long halfValue(byte[] paramArrayOfByte) {
        long l = 0L;
        for (int i = 0; i < 8; i++)
            l |= (0xFF & paramArrayOfByte[i]) << 8 * (7 - i);
        return l;
    }

    public static byte[] md5(byte[] paramArrayOfByte) {
        return md5(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static byte[] md5(byte[] paramArrayOfByte, int paramInt1,
                             int paramInt2) {
        MessageDigest localMessageDigest = DIGESTER_CONTEXT.get();
        localMessageDigest.update(paramArrayOfByte, paramInt1, paramInt2);
        return localMessageDigest.digest();
    }

    public static String md5AndHex(String paramString) {
        try {
            String str = md5AndHex(paramString.getBytes(ENCODING));
            return str;
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            throw new RuntimeException(localUnsupportedEncodingException);
        }
    }

    public static String md5AndHex(byte[] paramArrayOfByte) {
        return md5AndHex(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static String md5AndHex(byte[] paramArrayOfByte, int paramInt1,
                                   int paramInt2) {
        return ByteUtils.byteToHex(md5(paramArrayOfByte, paramInt1, paramInt2));
    }

    public static String md5ToString(byte[] paramArrayOfByte) {
        return Base64.encodeToString(md5(paramArrayOfByte), Base64.NO_WRAP);
    }


    public static String paramsToMd5(String params) {


        return md5AndHex(params + "&" + "key=KZB!@#$%^&**$#").toUpperCase();

    }

}