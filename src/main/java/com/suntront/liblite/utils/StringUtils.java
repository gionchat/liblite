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

public class StringUtils {
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    public static int charLength(String paramString) {
        if (isEmpty(paramString)) {
            return 0;
        }
        char[] arrayOfChar = paramString.toCharArray();
        int k = 0;
        for (int i = 0; i < arrayOfChar.length; i++) {
            int m = arrayOfChar[i];
            if (m < 0 || m > 127) {
                k += 2;
                continue;
            }
            k++;
        }
        return k;
    }

    public static String forceEllipsizeEnd(String paramString, int length) {
        if (paramString.length() > length) {
            paramString = paramString.substring(0, length) + "...";
        }
        return paramString;
    }

    public static boolean isBlank(String paramString) {
        if (paramString != null) {
            if (paramString.length() != 0) {
                for (int i = 0; i < paramString.length(); i++) {
                    if (!Character.isWhitespace(paramString.charAt(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(String paramString) {
        return (paramString == null) || (paramString.length() == 0);
    }

    public static boolean isNotBlank(String paramString) {
        return !isBlank(paramString);
    }

    public static boolean isNotEmpty(String paramString) {
        return !isEmpty(paramString);
    }
}
