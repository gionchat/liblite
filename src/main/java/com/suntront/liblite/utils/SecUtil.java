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


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecUtil {
    public static final String key = "kaoqingbao!@#$%^&**$#";
    public static final String encryptKey = "kaozhengbaoniu";
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String sha1(String s) {
        try {
            // Create SHA1 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA1");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String toHexString(byte[] b) {
        // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString().toLowerCase();
    }

    public static String sec(String time, String data) {
        String s = "eb253e476ab0b7e8472c0780f90a0f4e";
        String sec = md5(sha1("android365_bangzuoye" + time + data));
        return sec;
    }

    public static String getKey(String time) {
        String str = SecUtil.md5(SecUtil.sha1(key + time));
        StringBuffer sb = new StringBuffer(str);
        str = sb.reverse().toString();
        String key = "";
        for (int i = 0; i < str.length(); i++) {
            if (i != 0 && (i < encryptKey.length() - 2)) {
                key += String.valueOf(str.charAt(++i)) + encryptKey.charAt(i);
            } else {
                key += String.valueOf(str.charAt(i));
            }
        }
        return key;
    }

//	public static String getUrlKey(String time) {
//		String sid = DataSource.getInstance().getPrefStore().getSid();
//		String phone = DataSource.getInstance().getPrefStore().getUserName();
//		return SecUtil.md5(getKey(time)+phone+sid);
//	}

}
