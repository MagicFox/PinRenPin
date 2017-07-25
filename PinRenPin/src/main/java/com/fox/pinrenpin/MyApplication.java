package com.fox.pinrenpin;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by MagicFox on 2016/8/5.
 */
public class MyApplication extends Application {


    protected static Application instance = null;

    private static int screenWidth;
    private static int screenHeight;
    private static float density;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenHeight = mDisplayMetrics.heightPixels;
        screenWidth = mDisplayMetrics.widthPixels;
        density = mDisplayMetrics.density;

    }

    public static Application getInstance() {
        return instance;
    }

    public static int dip2Px(float dip) {
        return (int) (dip * density + 0.5f);
    }
    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
