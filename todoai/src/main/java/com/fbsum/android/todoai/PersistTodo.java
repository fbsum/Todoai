package com.fbsum.android.todoai;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PersistTodo extends Todoai {

    private static final String SHARE_PREFERENCES_NAME = "persist_todo_preferences";

    public static void init(Application application) {
        Preferences.init(application, SHARE_PREFERENCES_NAME);
    }

    private static final String SEPARATOR = "#";
    private static final Map<String, List<Long>> persistMap = new HashMap<>();

    @NonNull
    @Override
    protected List<Long> getTimestamps(@NonNull String tag) {
        List<Long> timestamps = persistMap.get(tag);
        if (timestamps == null) {
            String string = Preferences.getString(tag, "");
            timestamps = stringToLongList(string);
        }
        return timestamps;
    }

    @NonNull
    private static List<Long> stringToLongList(String string) {
        ArrayList<Long> list = new ArrayList<>();
        if (!TextUtils.isEmpty(string)) {
            String[] timestampStrings = string.split(SEPARATOR);
            for (String timestampString : timestampStrings) {
                list.add(Long.valueOf(timestampString));
            }
        }
        return list;
    }

    @NonNull
    private String longListToString(@NonNull List<Long> list) {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty()) {
            return "";
        } else {
            for (Long l : list) {
                sb.append(l).append(SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    @Override
    public void makeDone(@NonNull String tag) {
        List<Long> timestamps = getTimestamps(tag);
        timestamps.add(System.currentTimeMillis());
        Preferences.putString(tag, longListToString(timestamps));
    }

    @Override
    public void reset(@NonNull String tag) {
        persistMap.remove(tag);
        Preferences.remove(tag);
    }

    @Override
    public void clear() {
        persistMap.clear();
        Preferences.clear();
    }

    /**
     * Inner Class: SharePreferences Utils
     */
    private static class Preferences {

        private static SharedPreferences sharedPreferences;

        static void init(Application application, String name) {
            sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        static void putString(String key, String value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

        static String getString(String key, String defaultValue) {
            return sharedPreferences.getString(key, defaultValue);
        }

        static void remove(String key) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }

        static void clear() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }
}