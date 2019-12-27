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

public class ByteUtils {
    public static float byteToFloat(byte[] paramArrayOfByte) {
        return Float.intBitsToFloat(byteToInt(paramArrayOfByte, 0));
    }

    public static String byteToHex(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = paramArrayOfByte.length;
        for (int j = 0; j < i; j++) {
            byte b = paramArrayOfByte[j];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b);
            localStringBuilder.append(String.format("%02x", arrayOfObject));
        }
        return localStringBuilder.toString();
    }

    public static int byteToInt(byte[] paramArrayOfByte, int paramInt) {
        return (0xFF & paramArrayOfByte[paramInt])
                + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 8)
                + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 16)
                + ((0xFF & paramArrayOfByte[(paramInt + 3)]) << 24);
    }

    public static long byteToLong(byte[] paramArrayOfByte) {
        return (paramArrayOfByte[7] << 56)
                + ((0xFF & paramArrayOfByte[6]) << 48)
                + ((0xFF & paramArrayOfByte[5]) << 40)
                + ((0xFF & paramArrayOfByte[4]) << 32)
                + ((0xFF & paramArrayOfByte[3]) << 24)
                + ((0xFF & paramArrayOfByte[2]) << 16)
                + ((0xFF & paramArrayOfByte[1]) << 8)
                + ((0xFF & paramArrayOfByte[0]) << 0);
    }

    public static byte[] floatToByte(float paramFloat) {
        return intToByte(Float.floatToIntBits(paramFloat));
    }

    public static byte[] intToByte(int paramInt) {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte) paramInt);
        arrayOfByte[1] = ((byte) (paramInt >>> 8));
        arrayOfByte[2] = ((byte) (paramInt >>> 16));
        arrayOfByte[3] = ((byte) (paramInt >>> 24));
        return arrayOfByte;
    }

    public static byte[] longToByte(long paramLong) {
        byte[] arrayOfByte = new byte[8];
        arrayOfByte[0] = ((byte) (int) paramLong);
        arrayOfByte[1] = ((byte) (int) (paramLong >>> 8));
        arrayOfByte[2] = ((byte) (int) (paramLong >>> 16));
        arrayOfByte[3] = ((byte) (int) (paramLong >>> 24));
        arrayOfByte[4] = ((byte) (int) (paramLong >>> 32));
        arrayOfByte[5] = ((byte) (int) (paramLong >>> 40));
        arrayOfByte[6] = ((byte) (int) (paramLong >>> 48));
        arrayOfByte[7] = ((byte) (int) (paramLong >>> 56));
        return arrayOfByte;
    }

    public static int readInt(byte[] paramArrayOfByte, int paramInt) {
        if (paramArrayOfByte.length < paramInt + 4)
            throw new ArrayIndexOutOfBoundsException("buffer overflow");
        return (0xFF & paramArrayOfByte[paramInt])
                + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 8)
                + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 16)
                + ((0xFF & paramArrayOfByte[(paramInt + 3)]) << 24);
    }

    public static long readLong(byte[] paramArrayOfByte, int paramInt) {
        if (paramArrayOfByte.length < paramInt + 8)
            throw new ArrayIndexOutOfBoundsException("buffer overflow");
        return (paramArrayOfByte[(paramInt + 7)] << 56)
                + ((0xFF & paramArrayOfByte[(paramInt + 6)]) << 48)
                + ((0xFF & paramArrayOfByte[(paramInt + 5)]) << 40)
                + ((0xFF & paramArrayOfByte[(paramInt + 4)]) << 32)
                + ((0xFF & paramArrayOfByte[(paramInt + 3)]) << 24)
                + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 16)
                + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 8)
                + (0xFF & paramArrayOfByte[paramInt]);
    }

    public static void writeInt(byte[] paramArrayOfByte, int paramInt1,
                                int paramInt2) {
        if (paramArrayOfByte.length < paramInt1 + 4)
            throw new ArrayIndexOutOfBoundsException("buffer overflow");
        paramArrayOfByte[paramInt1] = ((byte) (paramInt2 >>> 0));
        paramArrayOfByte[(paramInt1 + 1)] = ((byte) (paramInt2 >>> 8));
        paramArrayOfByte[(paramInt1 + 2)] = ((byte) (paramInt2 >>> 16));
        paramArrayOfByte[(paramInt1 + 3)] = ((byte) (paramInt2 >>> 24));
    }

    public static void writeLong(byte[] paramArrayOfByte, int paramInt,
                                 long paramLong) {
        if (paramArrayOfByte.length < paramInt + 8)
            throw new ArrayIndexOutOfBoundsException("buffer overflow");
        paramArrayOfByte[paramInt] = ((byte) (int) (paramLong >>> 0));
        paramArrayOfByte[(paramInt + 1)] = ((byte) (int) (paramLong >>> 8));
        paramArrayOfByte[(paramInt + 2)] = ((byte) (int) (paramLong >>> 16));
        paramArrayOfByte[(paramInt + 3)] = ((byte) (int) (paramLong >>> 24));
        paramArrayOfByte[(paramInt + 4)] = ((byte) (int) (paramLong >>> 32));
        paramArrayOfByte[(paramInt + 5)] = ((byte) (int) (paramLong >>> 40));
        paramArrayOfByte[(paramInt + 6)] = ((byte) (int) (paramLong >>> 48));
        paramArrayOfByte[(paramInt + 7)] = ((byte) (int) (paramLong >>> 56));
    }
}