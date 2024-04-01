package com.sp.android_studio_project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class SharePreferenceUtil {


    private SharePreferenceUtil() {
    }

    private static final String KEY_CONFIG = "configs";

    private static SharedPreferences get(Context context) {
        return context.getApplicationContext().getSharedPreferences(KEY_CONFIG, Context.MODE_PRIVATE);
    }

    public static String getData(Context context, String key) {
        return getData(context, key, "");
    }

    public static String getData(Context context, String key, String defaultValue) {
        return get(context).getString(key, defaultValue);
    }

    public static void saveData(Context context, String key, String value) {
        get(context).edit()
                .putString(key, value)
                .apply();
    }

    public static void saveData(Context context, String key, boolean value) {
        get(context).edit()
                .putBoolean(key, value)
                .apply();
    }

    public static void saveObject(Context context, Object value) {
        if (value == null) return;
        String name = value.getClass().getSimpleName();
        saveObject(context, name, value);
    }

    public static void saveObject(Context context, String key, Object value) {
        String json = null;
        try {
            json = new Gson().toJson(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveData(context, key, json);
    }

    public static void cleanObject(Context context, Class clazz) {
        if (clazz == null || context == null) return;
        String name = clazz.getSimpleName();
        saveData(context, name, null);
    }

    public static <T> T getObject(Context context, Class<T> clazz) {
        if (clazz == null) return null;
        return getObject(context, clazz.getSimpleName(), clazz);
    }

    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        String json = getData(context, key);
        return new Gson().fromJson(json, clazz);
    }

    public static String getToken(Context context) {
        return getData(context, "token");
    }

    public static void saveToken(Context context, String token) {
        saveData(context, "token", token);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        get(context).edit()
                .putBoolean(key, value)
                .apply();

    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return get(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return get(context).getInt(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return get(context).getLong(key, defaultValue);
    }

    public static void setLong(Context context,String key,long value){
        get(context).edit().putLong(key,value);
    }

}
