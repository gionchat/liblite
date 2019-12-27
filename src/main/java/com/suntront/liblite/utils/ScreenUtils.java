package com.suntront.liblite.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: ScreenUtils
 * Author: Jeek
 * Date: 2019/9/19 9:35
 * Description: ${DESCRIPTION}
 */
public class ScreenUtils {

    public static final String TAG = "ScreenUtils";

    /**
     * @ 获取当前手机屏幕的尺寸(单位:像素)
     */
    public static float getScreenSize(Context mContext) {
        int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        float density = mContext.getResources().getDisplayMetrics().density;
        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;

        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / xdpi) * (width / xdpi);
        float height2 = (height / ydpi) * (width / xdpi);

        return (float) Math.sqrt(width2 + height2);
    }


    public static void printScreenSize(Activity activity) {
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(outSize);
        Log.w(TAG, "jeek printScreenSize screenWidth: " + outSize.x + " screenHeight: " + outSize.y);
    }

}
