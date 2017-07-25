package com.fox.pinrenpin.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.fox.pinrenpin.MyApplication;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class CommonHelper {
    public static int dipToPixel(final float dip) {
        DisplayMetrics metrics;
        metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }
}
