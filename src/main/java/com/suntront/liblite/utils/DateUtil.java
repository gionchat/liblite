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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 作者: Jeek.li
 * 时间: 2018/9/21
 * <p>
 * 描述:DateUtil 根据时间格式，获取时间字符串
 */

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    /**
     * TAG:用于打印日志的
     */
    public static final String TAG = "DateUtil";

    /**
     * 日期formatter
     */
    public static final SimpleDateFormat FRIEND_MANAGER_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    /**
     * 日期formatter by yyyy/MM/dd HH:mm:ss
     */
    public static final SimpleDateFormat FRIEND_MANAGER_FORMATTER_ONE = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");
    /**
     * 日期formatter
     */
    public static final SimpleDateFormat NOSS_FRIEND_MANAGER_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    /**
     * 通话时间
     */
    public static final SimpleDateFormat VOIP_TALKING_TIME = new SimpleDateFormat(
            "mm:ss");

    /**
     * 时间戳格式
     */
    public static final SimpleDateFormat TIMESTAMP_DF = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    /**
     * 一秒
     */
    private static final long SECOND = 1000;

    /**
     * 一分钟
     */
    private static final long ONE_MINUTE = 60 * SECOND;

    /**
     * 一小时
     */
    private static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * [一句话功能简述]<BR>
     * 根据默认的格式获得当前时间字符串yyyyMMddHHmmss
     *
     * @return 当前时间
     */
    public static String getCurrentDateString() {
        return TIMESTAMP_DF.format(new Date());
    }

    /**
     * 转化格式化的日期字符串<BR>
     *
     * @param date 日期
     * @return 格式化的日期字符串
     */
    public static String getDateString(Date date) {
        if (null == date) {
            return getCurrentDateString();
        }
        return TIMESTAMP_DF.format(date);
    }

    /**
     *
     *
     * @param diffTime
     *            通话时长
     * @return 通话时长
     */
    /**
     * 设置通话时长格式
     *
     * @param diffTime long
     * @param hh       时
     * @param mm       分
     * @param ss       秒
     * @return 通话时长格式
     */
    public static String getDiffTime(long diffTime, String hh, String mm,
                                     String ss) {
        //小时常数 
        long hourMarker = 60 * 60;

        // 分钟常数
        long minuteMarker = 60;

        //秒常数 
        long secondMarker = 1;

        DecimalFormat decfmt = new DecimalFormat();
        //小时
        long hour = diffTime / hourMarker;
        //分钟
        long minute = (diffTime - hour * hourMarker) / minuteMarker;
        //秒
        long second = (diffTime - hour * hourMarker - minute * minuteMarker)
                / secondMarker;

        if (hour == 0 && minute == 0) {
            return decfmt.format(second) + ss;
        }
        if (hour == 0 && minute != 0) {
            return decfmt.format(minute) + mm + decfmt.format(second) + ss;
        } else {
            return decfmt.format(hour) + hh + decfmt.format(minute) + mm
                    + decfmt.format(second) + ss;
        }
    }

    /**
     * 通话详情通话时长 设置通话时长格式
     *
     * @param diffTime long
     * @return 通话时长格式
     */
    public static String getDiffTime2(long diffTime) {
        //        //小时常数 
        //        long hourMarker = 60 * 60;

        // 分钟常数
        long minuteMarker = 60;

        //秒常数 
        long secondMarker = 1;

        DecimalFormat decfmt = new DecimalFormat();
        //小时
        // long hour = diffTime / hourMarker;
        //分钟
        long minute = diffTime / minuteMarker;
        //秒
        long second = (diffTime - minute * minuteMarker) / secondMarker;

        if (minute == 0) {
            return decfmt.format(second);
        } else {
            return decfmt.format(minute) + ":" + decfmt.format(second);
        }
    }

    /**
     * [一句话功能简述]<BR>
     * 根据默认的格式获得生日时间字符串
     *
     * @param date Date
     * @return 生日格式时间
     */
    public static String getBirthdayDateString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 找朋友小助手时间转换<BR>
     *
     * @param date date
     * @return 日期的时间串
     */
    public static String getFormatTimeStringForFriendManager(Date date) {
        return null == date ? FRIEND_MANAGER_FORMATTER.format(new Date())
                : FRIEND_MANAGER_FORMATTER.format(date);
    }

    /**
     * 时间转换<BR>
     *
     * @param date date
     * @return 日期的时间串
     */
    public static String getFormatTimeStringForFriendManagerOne(Date date) {
        return null == date ? FRIEND_MANAGER_FORMATTER_ONE.format(new Date())
                : FRIEND_MANAGER_FORMATTER_ONE.format(date);
    }

    /**
     * 找朋友小助手时间转换<BR>
     *
     * @param date date
     * @return 日期的时间串
     */
    public static String getFormatTimeStringNOSS(Date date) {
        return null == date ? NOSS_FRIEND_MANAGER_FORMATTER.format(new Date())
                : NOSS_FRIEND_MANAGER_FORMATTER.format(date);
    }

    /**
     * 找朋友小助手时间串转Date<BR>
     *
     * @param friendManagerTimeString 找朋友小助手时间串
     * @return 日期对象
     * @throws ParseException 解析发生异常
     */
    public static Date getDateFromFriendManageTimeString(String friendManagerTimeString) {
        try {
            return FRIEND_MANAGER_FORMATTER.parse(friendManagerTimeString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通话记录详情年-月-日
     *
     * @param context  Context
     * @param lastDate Date
     * @return 通话记录详情年-月-日
     */
    public static String getCommunicationLogDetailTimeByDate(Context context,
                                                             Date lastDate) {
        //取出具体时间
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String time = df.format(lastDate);
        if (time.charAt(0) == '0') {
            time = time.substring(1);
        }
        // 再把时间的年月日取出来
        df = new SimpleDateFormat("yyyy-MM-dd");
        String lastTimeDate = df.format(lastDate);
        return lastTimeDate;
    }


    /**
     * 服务器返回的离线消息时间为UTC时间，需要转换为本地时间
     *
     * @param dateStr String 解析前的字符串时间对象
     * @return Date 经过解析后的时间对象
     * @throws ParseException 解析错误
     * @author 李颖00124251
     * @version [RCS Client V100R001C03, 2012-3-16]
     */
    public static Date getDelayTime(String dateStr) throws ParseException {

        SimpleDateFormat imDelayFormatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0");
        imDelayFormatter.setTimeZone(timeZone);

        return imDelayFormatter.parse(dateStr);
    }

    /**
     * 计算date2比date1延迟多少秒
     *
     * @param dateString1
     * @param dateString2
     * @return
     */
    public static long get2TimeDelay(String dateString1, String dateString2) {
        long delay = -1;
        try {
            Date date1 = TIMESTAMP_DF.parse(dateString1);
            Date date2 = TIMESTAMP_DF.parse(dateString2);
            delay = date2.getTime() - date1.getTime();
        } catch (Exception e) {

        }
        return delay;
    }


    public static String getNormalDateString(String dateStr) throws ParseException {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateStr);
        return date.toString();
    }

    //============================================
    // 从Lib里弄过来的东西


    public static String dateToString(Date d) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    public static String dateToYyyyMmDdString(Date d) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    public static String getdateToString(Date d) {
        String format = "yyyyMMddHHmmss";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }


    public static String dateToString(Date d, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    public static Date addDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        return calendar.getTime();
    }

    public static Date addDay(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, n);
        return calendar.getTime();
    }

    public static Date addMonth(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    public static Date getMonthAndLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getMonthAndFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, n);
        return calendar.getTime();
    }

    public static Date addMilliSecond(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, n);
        return calendar.getTime();
    }

    public static Date stringToDate(String d, String partern)
            throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(partern);
        return format.parse(d);
    }

    public static Date stringToDate(String d) throws ParseException {
        String partern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(partern);
        return format.parse(d);
    }

    public static String StringToLastDate(Date d) {
        return dateToString(d, "yyyy-MM-dd") + " 23:59:59";
    }

    public static Date millisToDate(long mill) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mill);
        return calendar.getTime();
    }

    public static long DateTomillis(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.getTimeInMillis();
    }

    // 格式：ddHHmmss
    public static String time2ddHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /***
     * 今天 昨天 前天 日期转化  * @author wanghq<br/>
     *  * @version 创建时间：2012-9-6 下午4:15:15<br/>
     *  * @param time  * @return<br/>
     *  
     */
    public static String calcTime(String time) {

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(now);
        // 昨天
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        String yesterdayStr = df.format(yesterday);
        // 前天
        cal.add(Calendar.DATE, -1);
        Date qianTian = cal.getTime();
        String qianTianStr = df.format(qianTian);

        if (time == null) {
            time = "";
        } else if (time.contains(todayStr)) {
            time = time.replace(todayStr, "今天 ");
        } else if (time.contains(yesterdayStr)) {
            time = time.replace(yesterdayStr, "昨天 ");
        } else if (time.contains(qianTianStr)) {
            time = time.replace(qianTianStr, "前天 ");
        }
        return time;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = format.format(date);
        return curTime;
    }

    /**
     * 获得中文的星期几
     *
     * @param date
     * @return
     */
    public static String getDayOfWeek_CN(Date date) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayofweek) {
            case Calendar.SUNDAY:
                result = "周日";
                break;
            case Calendar.MONDAY:
                result = "星期一";
                break;
            case Calendar.TUESDAY:
                result = "星期二";
                break;
            case Calendar.WEDNESDAY:
                result = "星期三";
                break;
            case Calendar.THURSDAY:
                result = "星期四";
                break;
            case Calendar.FRIDAY:
                result = "星期五";
                break;
            case Calendar.SATURDAY:
                result = "星期六";
                break;
            default:
                break;
        }
        return result;
    }

    //============================================

    /**
     * 获取yyyyMMdd格式日期
     *
     * @param time
     * @return
     */
    public static Date getDate(String time) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Context context, long date) {
        @SuppressWarnings("deprecation")
        int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | android.text.format.DateUtils.FORMAT_ABBREV_ALL
                | android.text.format.DateUtils.FORMAT_CAP_AMPM
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_TIME;
        return android.text.format.DateUtils.formatDateTime(context, date,
                format_flags);
    }

    /**
     * 获取当前月日年汉字版
     *
     * @return
     */
    public static String getCurrentTimeChinese() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String curTime = format.format(date);
        return curTime;
    }

    /**
     * 获取当前月日年标准版
     *
     * @return
     */
    public static String getStandardTime(long time) {
        String stTime = null;
        if (time != 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            stTime = format.format(new Date(time));
        }
        return stTime;
    }

    /**
     * 获取当前月日年标准版
     *
     * @return
     */
    public static long getStandardTimeMills(String time) {
        long mills = 0;
        if (time != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                mills = format.parse(time).getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mills;
    }

    /**
     * 获取今天00：00：00时到明天00：00：00时的毫秒数
     *
     * @return
     */
    public static Map<String, Long> getNowTimePlusOneDay() {
        Map<String, Long> map = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String datestr = format.format(date);

        long date1 = 0, date2 = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date temp = sdf.parse(datestr);
            date1 = temp.getTime();
            date2 = date1 + 86400000;
            map = new HashMap<String, Long>();
            map.put("before", date1);
            map.put("after", date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }


    /*
     * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm
     */
    public static String getStrTime_ymd_hm(String cc_time) {
        String re_StrTime = "";
        if (TextUtils.isEmpty(cc_time) || "null".equals(cc_time)) {
            return re_StrTime;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;

    }

    /*
     * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getStrTime_ymd_hms(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;

    }

    /*
     * 将时间戳转为字符串 ，格式：yyyy.MM.dd
     */
    public static String getStrTime_ymd(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将时间戳转为字符串 ，格式：yyyy
     */
    public static String getStrTime_y(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将时间戳转为字符串 ，格式：MM-dd
     */
    public static String getStrTime_md(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将时间戳转为字符串 ，格式：HH:mm
     */
    public static String getStrTime_hm(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将时间戳转为字符串 ，格式：HH:mm:ss
     */
    public static String getStrTime_hms(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将时间戳转为字符串 ，格式：MM-dd HH:mm:ss
     */
    public static String getNewsDetailsDate(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
     * 将字符串转为时间戳
     */
    public static String getTime() {
        String re_time = null;
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date d;
        d = new Date(currentTime);
        long l = d.getTime();
        String str = String.valueOf(l);
        re_time = str.substring(0, 10);
        return re_time;
    }

    /*
     * 将时间戳转为字符串 ，格式：yyyy.MM.dd  星期几
     */
    public static String getSection(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
//		对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
//		yyyy代表年份，如“2010”；dd代表天，如“25”
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    public static String getChineseMMDDHHMMTime(String date) {
        Date date2 = null;
        try {
            date2 = DateUtil.getDateFromFriendManageTimeString(date);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            return format.format(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChineseYYMMDDTime(String date) {
        Date date2 = null;
        try {
            date2 = DateUtil.getDateFromFriendManageTimeString(date);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            return format.format(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChineseYYMMDDTimeByDate(Date date) {
        Date date2 = null;
        try {
            date2 = date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            return format.format(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChineseMMDDTime(String date) {
        Date date2 = null;
        try {
            date2 = DateUtil.getDateFromFriendManageTimeString(date);
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
            return format.format(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
//	public static String getTodayDate(){
//		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//		String nowTime=format.format(new Date());
//		return 
//	}

    /**
     * @return 获取时间 字符串
     */
    public static String getDateString(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);

        //时间为1970时表示订单未完成
        if (year == 1970) {
            return "未完成";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static long getDateString(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
