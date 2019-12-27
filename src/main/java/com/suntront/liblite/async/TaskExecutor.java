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

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 作者: Jeek.li
 * 时间: 2018/9/10
 * <p>
 * 描述:
 * 1. OrderedTask, 有序的执行一些列任务。
 * 2. CyclicBarrierTask, 并发的执行一系列任务，且会在所有任务执行完成时集中到一个关卡点（执行特定的函数）。
 * 3. Delayed Task, 延时任务。
 * 4. Timer Runnable, 定时任务。
 */

public class TaskExecutor {

    /**
     * 开子线程
     *
     * @param run
     */
    public static void start(Runnable run) {
        AsyncTaskLite.execute(run);
    }

    /**
     * 开子线程，并发超出数量限制时允许丢失任务。
     *
     * @param run
     */
    public static void startAllowingLoss(Runnable run) {
        AsyncTaskLite.executeAllowingLoss(run);
    }

    /**
     * 有序异步任务执行器
     *
     * @return
     */
    public static OrderedTaskExecutor newOrderedExecutor() {
        return new OrderedTaskExecutor();
    }

    /**
     * 关卡异步任务执行器
     *
     * @return
     */
    public static CyclicBarrierExecutor newCyclicBarrierExecutor() {
        return new CyclicBarrierExecutor();
    }

    /**
     * 延时异步任务
     *
     * @param task
     * @param time
     * @param unit
     */
    public static void startDelayedTask(final AsyncTaskLite<?, ?, ?> task, long time, TimeUnit unit) {
        long delay = time;
        if (unit != null) delay = unit.toMillis(time);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                task.execute();
            }
        }, delay);
    }

    /**
     * 启动定时任务
     *
     * @param run
     * @param delay  >0 延迟时间
     * @param period >0 心跳间隔时间
     * @return
     */
    public static Timer startTimerTask(final Runnable run, long delay, long period) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                run.run();
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, period);
        return timer;
    }

    public static class OrderedTaskExecutor {
        LinkedList<AsyncTaskLite<?, ?, ?>> taskList = new LinkedList<AsyncTaskLite<?, ?, ?>>();
        private transient boolean isRunning = false;

        public OrderedTaskExecutor put(AsyncTaskLite<?, ?, ?> task) {
            synchronized (taskList) {
                if (task != null) taskList.add(task);
            }
            return this;
        }

        public void start() {
            if (isRunning) return;
            isRunning = true;
            for (AsyncTaskLite<?, ?, ?> each : taskList) {
                final AsyncTaskLite<?, ?, ?> task = each;
                task.setFinishedListener(new AsyncTaskLite.FinishedListener() {

                    @Override
                    public void onPostExecute() {
                        synchronized (taskList) {
                            executeNext();
                        }
                    }

                    @Override
                    public void onCancelled() {
                        synchronized (taskList) {
                            taskList.remove(task);
                            if (task.getStatus() == AsyncTaskLite.Status.RUNNING) {
                                executeNext();
                            }
                        }
                    }
                });
            }
            executeNext();
        }

        @SuppressWarnings("unchecked")
        private void executeNext() {
            AsyncTaskLite<?, ?, ?> next = null;
            if (taskList.size() > 0) {
                next = taskList.removeFirst();
            }
            if (next != null) {
                next.execute();
            } else {
                isRunning = false;
            }
        }
    }

    public static class CyclicBarrierExecutor {
        ArrayList<AsyncTaskLite<?, ?, ?>> taskList = new ArrayList<AsyncTaskLite<?, ?, ?>>();
        private transient boolean isRunning = false;

        public CyclicBarrierExecutor put(AsyncTaskLite<?, ?, ?> task) {
            if (task != null) taskList.add(task);
            return this;
        }

        public void start(final AsyncTaskLite<?, ?, ?> finishTask) {
            start(finishTask, 0, null);
        }

        @SuppressWarnings("unchecked")
        public void start(final AsyncTaskLite<?, ?, ?> endOnUiTask, final long time, final TimeUnit unit) {
            if (isRunning) throw new RuntimeException("CyclicBarrierExecutor only can start once.");
            isRunning = true;
            final CountDownLatch latch = new CountDownLatch(taskList.size());
            new SimpleTask<Boolean>() {

                @Override
                protected Boolean doInBackground() {
                    try {
                        if (unit == null) latch.await();
                        else latch.await(time, unit);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    endOnUiTask.execute();
                }
            }.execute();
            startInternal(latch);
        }

        public void start(Runnable endOnUiThread) {
            start(endOnUiThread, 0, null);
        }

        public void start(final Runnable endOnUiThread, final long time, final TimeUnit unit) {
            if (isRunning) throw new RuntimeException("CyclicBarrierExecutor only can start once.");
            isRunning = true;
            final CountDownLatch latch = new CountDownLatch(taskList.size());
            new SimpleTask<Boolean>() {

                @Override
                protected Boolean doInBackground() {
                    try {
                        if (unit == null) latch.await();
                        else latch.await(time, unit);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    endOnUiThread.run();
                }
            }.execute();
            startInternal(latch);
        }

        private void startInternal(final CountDownLatch latch) {
            for (AsyncTaskLite<?, ?, ?> each : taskList) {
                each.setFinishedListener(new AsyncTaskLite.FinishedListener() {

                    @Override
                    public void onPostExecute() {
                        latch.countDown();
                    }

                    @Override
                    public void onCancelled() {
                        latch.countDown();
                    }
                });
                each.execute();
            }
        }

    }
}
