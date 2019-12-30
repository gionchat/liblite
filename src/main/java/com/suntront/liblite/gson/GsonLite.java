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
package com.suntront.liblite.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.suntront.liblite.gson.exception.JsonException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonLite {
    private static final GsonBuilder DESERIALIZER_BUILDER = new GsonBuilder().disableHtmlEscaping();
    private static final GsonBuilder SERIALIZER_BUILDER   = new GsonBuilder().disableHtmlEscaping();
    private static Gson deserializer;
    private static Gson serializer;

    public static <T> String arrayToJson(T[] paramArrayOfT) {
        return getSerializer().toJson(paramArrayOfT);
    }

    public static Gson getDeserializer() {
        if (deserializer == null) {
            deserializer = DESERIALIZER_BUILDER.create();
        }
        return deserializer;
    }

    public static Gson getSerializer() {
        if (serializer == null) {
            serializer = SERIALIZER_BUILDER.create();
        }
        return serializer;
    }


    public static <T> T jsonToArray(String paramString, Class<T> paramClass) {
        try {
            return getDeserializer().fromJson(paramString, paramClass);
        } catch (Exception localException) {
            //FCLog.e(JsonMapper.class, localException);
            return null;
        }
    }

    public static <T> List<T> jsonToList(String paramString,
                                         TypeToken<List<T>> paramTypeToken) {
        try {
            return (List) getDeserializer().fromJson(paramString,
                    paramTypeToken.getType());
        } catch (Exception localException) {
            //FCLog.e(JsonMapper.class, localException);
            return null;
        }
    }

    public static <K, V> Map<K, V> jsonToMap(String paramString,
                                             TypeToken<Map<K, V>> paramTypeToken) {
        return (Map<K, V>) getDeserializer().fromJson(paramString,
                paramTypeToken.getType());
    }

    public static <T> String listToJson(List<T> paramList) {
        return getSerializer().toJson(paramList);
    }

    public static <K, V> String mapToJson(Map<K, V> paramMap) {
        return getSerializer().toJson(paramMap);
    }

    public static <T extends IJsonable> T parseJsonObject(
            JsonElement paramJsonElement, Class<T> paramClass)
            throws JsonException {
        try {
            IJsonable localIJsonable = getDeserializer().fromJson(
                    paramJsonElement, paramClass);
            return (T) localIJsonable;
        } catch (Throwable localThrowable) {
            throw new JsonException("json=" + paramJsonElement, localThrowable);
        }
    }

    public static <T extends IJsonable> T parseJsonObject(String paramString,
                                                          Class<T> paramClass) throws JsonException {
        try {
            IJsonable localIJsonable = getDeserializer().fromJson(
                    paramString, paramClass);
            return (T) localIJsonable;
        } catch (Throwable localThrowable) {
            //FCLog.e("JsonException", localThrowable);
            throw new JsonException("json=" + paramString, localThrowable);
        }
    }

    public static <T extends IJsonable> T parseJsonObject(
            JSONObject paramJSONObject, Class<T> paramClass)
            throws JsonException {
        return parseJsonObject(paramJSONObject.toString(), paramClass);
    }

    public static <T> T readValue(String paramString, Class<T> paramClass) {
        return getDeserializer().fromJson(paramString, paramClass);
    }

    public static <T> void registerDeserializer(Class<T> paramClass,
                                                JsonDeserializer<T> paramJsonDeserializer) {

        DESERIALIZER_BUILDER.registerTypeAdapter(paramClass,
                paramJsonDeserializer);
        deserializer = DESERIALIZER_BUILDER.create();

    }

    public static <T extends IJsonable> String toJson(T paramT) {
        return getSerializer().toJson(paramT);
    }

    public static <T extends IJsonable> JSONObject toJsonObject(T paramT)
            throws JsonException {
        try {
            JSONObject localJSONObject = new JSONObject(toJson(paramT));
            return localJSONObject;
        } catch (Throwable localThrowable) {
            throw new JsonException(localThrowable);
        }
    }

    /**
     * json转换为object
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Object toObject(String json, Class clazz) {
        return getSerializer().fromJson(json, clazz);
    }


    /**
     * json转换为object数组
     *
     * @param json
     * @param type
     * @return
     */
    public static Object toObject(String json, Type type) {
        if (json == null) {
            return null;
        }
        return getSerializer().fromJson(json, type);
    }

    /**
     * object转换为json
     *
     * @param paramObject
     * @return
     */
    public static String toJson(Object paramObject) {
        return getSerializer().toJson(paramObject);
    }
}