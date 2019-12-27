package com.suntront.liblite.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: OrderNumber
 * Author: Jeek
 * Date: 2019/11/25 15:46
 * Description: 生成订单号
 */
public class OrderNumber {
    private static SimpleDateFormat nextSecond = new SimpleDateFormat("SSS");
    /**
     * 随机号时间格式
     */
    private String timeFormatter = "yyyyMMddHHmmss";
    private SimpleDateFormat sdf = null;
    /**
     * 秒（一秒有多少毫秒）
     */
    private long secondUnit = 1000;

    /**
     * 随机数字位数
     */
    private int randomNumLength = 3;

    /**
     * 编号前缀
     */
    private String prefix = "";

    /**
     * 是否颠倒字符串
     */
    private boolean reverse = false;

    /**
     * 上一次生成时间
     */
    private String preGenTime = "";

    /**
     * 当前时间最大请求数量
     */
    private double maxRequest;

    /**
     * 数字格式
     */
    private String numFormat;

    /**
     * 当前秒内生成订单号的随机数字
     */
    private Set<Long> currentSecondRandomNos = Collections.synchronizedSet(new HashSet<Long>());

    public OrderNumber() {
        sdf = new SimpleDateFormat(timeFormatter);
    }

    /**
     * @param randomNumLength 随机数字位数
     */
    public OrderNumber(int randomNumLength) {
        this.randomNumLength = randomNumLength;
        this.maxRequest = Math.pow(10, this.randomNumLength);
        this.numFormat = MessageFormat.format("%0{0}d", randomNumLength);
    }

    /**
     * @param timeFormatter   时间格式
     * @param randomNumLength 随机数字位数
     * @param reverse         是否颠倒
     */
    public OrderNumber(String timeFormatter, int randomNumLength, boolean reverse) {
        if (StringUtils.isNotBlank(timeFormatter)) {
            this.timeFormatter = timeFormatter.trim();
            sdf = new SimpleDateFormat(this.timeFormatter);
        }
        this.randomNumLength = randomNumLength;
        this.reverse = reverse;
        this.maxRequest = Math.pow(10, this.randomNumLength);
        this.numFormat = MessageFormat.format("%0{0}d", randomNumLength);
    }

    /**
     * @param prefix          前缀
     * @param timeFormatter   时间格式
     * @param randomNumLength 随机数字位数
     * @param reverse         是否颠倒
     */
    public OrderNumber(String prefix, String timeFormatter, int randomNumLength, boolean reverse) {
        if (StringUtils.isNotBlank(prefix))
            this.prefix = prefix.trim();
        if (StringUtils.isNotBlank(timeFormatter)) {
            this.timeFormatter = timeFormatter.trim();
            sdf = new SimpleDateFormat(this.timeFormatter);
        }
        this.randomNumLength = randomNumLength;
        this.reverse = reverse;
        this.maxRequest = Math.pow(10, this.randomNumLength);
        this.numFormat = MessageFormat.format("%0{0}d", randomNumLength);
    }

    public static void main(String[] args) {
        OrderNumber random = new OrderNumber("", "yyyyMMddHHmmss", 10, false);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            System.out.println(random.genNewId());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 生成随机号
     *
     * @return
     * @author Neo
     * @date 2018年1月18日11:49:27
     */
    public synchronized String genNewId() {
        if (currentSecondRandomNos.size() >= maxRequest) {
            try {
                Thread.currentThread();
                //计算当前秒距离下一秒还有多少毫秒
                long sleepTime = secondUnit - Long.valueOf(nextSecond.format(new Date()));
                Thread.sleep(sleepTime);// 休息到下一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String currentTime = sdf.format(new Date());
        if (!preGenTime.equals(currentTime)) {
            preGenTime = currentTime;
            currentSecondRandomNos.clear();
        }
        long random = getRandom();// 随机数字
        currentSecondRandomNos.add(random);

        String formated = String.format(numFormat, random);
        String id = currentTime + formated;
        return prefix + id;
    }

    /**
     * 获得随机数字
     *
     * @return
     * @author Neo
     * @date 2018年1月18日11:49:44
     */
    private Long getRandom() {
        Long random = Math.round(Math.random() * maxRequest);
        if (currentSecondRandomNos.add(random)) {
            return random;
        }
        return getRandom();
    }
}
