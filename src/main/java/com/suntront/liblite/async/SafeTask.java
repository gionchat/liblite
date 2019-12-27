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

/**
 * 作者: Jeek.li
 * 时间: 2018/9/10
 * <p>
 * 描述:安全异步任务，可以捕获任意异常，并反馈给给开发者
 */
public abstract class SafeTask<Params, Progress, Result> extends AsyncTaskLite<Params, Progress, Result> {
    private Exception cause;
    private boolean printStackTrace = true;

    @Override
    protected final void onPreExecute() {
        try {
            onPreExecuteSafely();
        } catch (Exception e) {
            if (printStackTrace) e.printStackTrace();
        }
    }

    @Override
    protected final Result doInBackground(Params... params) {
        try {
            return doInBackgroundSafely(params);
        } catch (Exception e) {
            if (printStackTrace) e.printStackTrace();
            cause = e;
        }
        return null;
    }

    @Override
    protected final void onProgressUpdate(Progress... values) {
        try {
            onProgressUpdateSafely(values);
        } catch (Exception e) {
            if (printStackTrace) e.printStackTrace();
        }
    }

    @Override
    protected final void onPostExecute(Result result) {
        try {
            onPostExecuteSafely(result, cause);
        } catch (Exception e) {
            if (printStackTrace) e.printStackTrace();
        }
    }

    @Override
    protected final void onCancelled(Result result) {
        onCancelled();
    }

    /**
     * <p> 取代了{@link AsyncTaskLite#onPreExecute()}, 这个方法的任意异常都能被捕获：它是安全的。
     * <p> 注意：本方法将在开发者启动任务的线程执行。
     */
    protected void onPreExecuteSafely() throws Exception {
    }

    /**
     * <p> Child Thread
     * <p> 取代了{@link AsyncTaskLite#doInBackground(Object...)}, 这个方法的任意异常都能被捕获：它是安全的。
     * <p> 如果它发生了异常，Exception将会通过{@link #onPostExecuteSafely(Object, Exception)}
     * 传递。
     *
     * @param params 入参
     * @return
     */
    protected abstract Result doInBackgroundSafely(Params... params) throws Exception;

    /**
     * <p> Main UI Thread
     * <p> 用于取代{@link AsyncTaskLite#onPostExecute(Object)}。
     * <p> 注意：本方法一定执行在主线程, 这个方法的任意异常都能被捕获：它是安全的。
     *
     * @param result
     */
    protected void onPostExecuteSafely(Result result, Exception e) throws Exception {
    }

    /**
     * <p> Main UI Thread
     * <p> 用于取代{@link AsyncTaskLite#onProgressUpdate(Object...)},
     * <p> 这个方法的任意异常都能被捕获：它是安全的。
     *
     * @param values 更新传递的值
     */
    protected void onProgressUpdateSafely(Progress... values) throws Exception {
    }

}
