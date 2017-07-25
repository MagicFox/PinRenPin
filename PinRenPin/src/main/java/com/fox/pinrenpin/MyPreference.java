package com.fox.pinrenpin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class MyPreference {

    private static MyPreference prefer = null;
    private String packName = "";
    private Context app;
    private SharedPreferences settings;

    private MyPreference() {
        app = MyApplication.getInstance();
        packName = app.getPackageName();
        settings = app.getSharedPreferences(packName, 0);
    }


    public static MyPreference getInstance() {
        if (prefer == null) {
            prefer = new MyPreference();
        }
        return prefer;
    }

    public void storeLabaYMD4PopupDialog(String ymdDate) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LABA_POPDIALOG_DYM, ymdDate);
        editor.commit();
    }
    public static final String LABA_POPDIALOG_DYM = "LABA_POPDIALOG_DYM";
    public static final String LABA_POPDIALOG_DYM_DEFAULT = "";
    public String getLabaYMD4PopupDialog() {
        return settings.getString(LABA_POPDIALOG_DYM, LABA_POPDIALOG_DYM_DEFAULT);
    }
    /**
     * 读取拉霸声音开关
     *
     * @return String
     */
    public boolean getLabaSound() {
        return settings.getBoolean("opensound", true);
    }

    /**
     * 存储拉霸声音开关
     *
     * @param opensound
     */
    public boolean storeLabaSound(boolean opensound) {
        boolean result = false;
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("opensound", opensound);
        editor.commit();
        result = true;
        return result;
    }
    /**
     * 读取laba history
     *
     * @return int
     */
    public int getLabaHistoryCount() {
        return settings.getInt("history_count", 0);
    }

    /**
     * 存储laba history
     *
     * @param history_count
     */
    public boolean storeLabaHistoryCount(int history_count) {
        boolean result = false;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("history_count", history_count);
        editor.commit();
        result = true;
        return result;
    }

}
