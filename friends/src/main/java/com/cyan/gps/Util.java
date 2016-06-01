package com.cyan.gps;

import android.content.Context;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-30
 * Time: 21:41
 * 类说明：
 */
public class Util
{
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }


}  
