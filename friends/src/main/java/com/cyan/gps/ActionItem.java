package com.cyan.gps;

import android.content.Context;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-30
 * Time: 21:42
 * 类说明：
 */
public class ActionItem
{
    public CharSequence mTitle;

    public ActionItem(CharSequence title) {
        this.mTitle = title;
    }

    public ActionItem(Context context, int titleId) {
        this.mTitle = context.getResources().getText(titleId);
    }

    public ActionItem(Context context, CharSequence title) {
        this.mTitle = title;
    }


}  
