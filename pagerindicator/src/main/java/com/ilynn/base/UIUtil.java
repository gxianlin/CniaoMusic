package com.ilynn.base;

import android.content.Context;

/**
 * 描述：工具类
 * 作者：gong.xl
 * 创建日期：2017/7/5 下午2:00
 * 修改日期: 2017/7/5
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class UIUtil {
    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
