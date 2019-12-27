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
package com.suntront.liblite.async;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 作者: Jeek.li
 * 时间: 2018/9/10
 * <p>
 * 描述:Cached AsyncTaskLite 缓存异步任务
 */
public abstract class CachedTask<Params, Progress, Result extends Serializable>
        extends SafeTask<Params, Progress, Result> {
    private static final String TAG = CachedTask.class.getSimpleName();
    private static final String DEFAULT_PATH = "/cachedtask";
    private static String cachePath;
    private static ConcurrentHashMap<String, Long> cachedTimeMap = new ConcurrentHashMap<String, Long>();
    private long expiredTime = 0;
    private String key;

    /**
     * @param context   app context
     * @param key       identify label, each single cachedtask should not be the same.
     * @param cacheTime expired time
     * @param unit      if timeunit is null, see cacheTime as millisecond.
     */
    public CachedTask(Context context, String key, long cacheTime, TimeUnit unit) {
        if (context == null) throw new RuntimeException("CachedTask Initialized Must has Context");
        cachePath = context.getFilesDir().getAbsolutePath() + DEFAULT_PATH;
        if (key == null) throw new RuntimeException("CachedTask Must Has Key for Search ");
        this.key = key;
        if (unit != null) expiredTime = unit.toMillis(cacheTime);
        else expiredTime = cacheTime;
    }

    public static void cleanCacheFiles(Context context) {
        cachedTimeMap.clear();
        cachePath = context.getFilesDir().getAbsolutePath() + DEFAULT_PATH;
        File file = new File(cachePath);
        final File[] fileList = file.listFiles();
        if (fileList != null) {
            TaskExecutor.start(new Runnable() {
                @Override
                public void run() {
                    for (File f : fileList) {
                        if (f.isFile()) f.delete();
                    }
                }
            });

        }
    }

    public static void removeKeyValue(String key) {
        cachedTimeMap.remove(key);
    }

    protected abstract Result doConnectNetwork(Params... params) throws Exception;

    @Override
    protected final Result doInBackgroundSafely(Params... params) throws Exception {
        Result res = null;
        try {
            Long time = cachedTimeMap.get(key);
            long lastTime = time == null ? 0 : time;
            if (System.currentTimeMillis() - lastTime >= expiredTime) {
                res = doConnectNetwork(params);
                if (res != null) {
                    if (Log.isPrint) Log.d(TAG, "doConnectNetwork: sucess");
                    cachedTimeMap.put(key, System.currentTimeMillis());
                    saveResultToCache(res);
                } else {
                    if (Log.isPrint) Log.d(TAG, "doConnectNetwork: false");
                    res = getResultFromCache();
                }
            } else {
                res = getResultFromCache();
                if (res == null) {
                    res = doConnectNetwork(params);
                    if (res != null) {
                        if (Log.isPrint) Log.d(TAG, "doConnectNetwork: sucess");
                        cachedTimeMap.put(key, System.currentTimeMillis());
                        saveResultToCache(res);
                    } else {
                        if (Log.isPrint) Log.d(TAG, "doConnectNetwork: false");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private Result getResultFromCache() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(cachePath, key)));
            Object obj = ois.readObject();

            if (obj != null) {
                if (Log.isPrint) Log.i(TAG, key + " read from cache: " + obj);
                return (Result) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Log.isPrint) Log.e(TAG, "read ResultFromCache: fail ");
        return null;
    }

    private boolean saveResultToCache(Result res) {
        ObjectOutputStream oos = null;
        try {
            File dir = new File(cachePath);
            if (!dir.exists()) dir.mkdirs();
            oos = new ObjectOutputStream(new FileOutputStream(new File(dir, key)));
            oos.writeObject(res);
            if (Log.isPrint) Log.i(TAG, key + "  saveto cache: " + res);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Log.isPrint) Log.e(TAG, "save Result To Cache: fail");
        return false;
    }
}
