package com.ilynn.base;

/**
 * 描述：实现颜色渐变
 * 作者：gong.xl
 * 创建日期：2017/7/5 下午2:20
 * 修改日期: 2017/7/5
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class ArgbEvaluatorHolder {

    public static int eval(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (startValue >> 24) & 0xff;
        int endR = (startValue >> 16) & 0xff;
        int endG = (startValue >> 8) & 0xff;
        int endB = startValue & 0xff;

        int currentA = (startA + (int) (fraction * (endA - startA))) << 24;
        int currentR = (startR + (int) (fraction * (endR - startR))) << 16;
        int currentG = (startG + (int) (fraction * (endG - startG))) << 8;
        int currentB = startB + (int) (fraction * (endB - startB));

        return currentA | currentR | currentG | currentB;
    }
}

