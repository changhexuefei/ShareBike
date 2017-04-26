package com.dcch.sharebike.utils;

/**
 * Created by Administrator on 2017/4/26 0026.
 * 防止用户连续点击相同的按钮，多次弹出相同的页面
 */

public class ClickUtils {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
