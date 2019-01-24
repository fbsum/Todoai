package com.fbsum.android.todoai;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 12/25/17.
 */

public class Todoai {

    private static final int EXACTLY = 0;
    private static final int MORE_THAN = 1;
    private static final int LESS_THAN = 2;
    private static final int BETWEEN = 3;

    private long startTimestamp = -1L;
    private long endTimestamp = -1L;
    private int startCount = -1;
    private int endCount = -1;
    private int compare = EXACTLY;
    private boolean autoDone;

    private static Todoai INSTANCE = new Todoai();

    private static final String SHARE_PREFERENCES_NAME = "persist_todo_preferences";
    private static final String SEPARATOR = "#";
    private static final Map<String, List<Long>> map = new HashMap<>();

    public static void init(Application application) {
        Preferences.init(application, SHARE_PREFERENCES_NAME);
    }

    public static Todoai getInstance() {
        return INSTANCE;
    }

    private Todoai() {
    }

    public Todoai today() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startTimestamp = calendar.getTimeInMillis();
        endTimestamp = startTimestamp + 86400000L;
        return this;
    }

    public Todoai newVersion(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            long lastAppUpdatedTime = packageInfo.lastUpdateTime;
            return timestampBetween(lastAppUpdatedTime, Long.MAX_VALUE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Todoai afterInstall(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            long firstInstallTime = packageInfo.firstInstallTime;
            return timestampBetween(firstInstallTime, Long.MAX_VALUE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Todoai timestampBetween(long start, long end) {
        if (start > end) {
            throw new IllegalArgumentException("The endTimestamp must be more than startTimestamp.");
        }
        startTimestamp = start;
        endTimestamp = end;
        return this;
    }

    public Todoai countExactly(int count) {
        compare = EXACTLY;
        startCount = count;
        return this;
    }


    public Todoai countMoreThan(int count) {
        compare = MORE_THAN;
        startCount = count;
        return this;
    }

    public Todoai countLessThan(int count) {
        compare = LESS_THAN;
        startCount = count;
        return this;
    }

    public Todoai countBetween(int min, int max) {
        compare = BETWEEN;
        startCount = min;
        endCount = max;
        return this;
    }

    public Todoai autoDone() {
        autoDone = true;
        return this;
    }

    @CheckResult
    public boolean isDone(@NonNull String tag) {
        List<Long> timestamps = getTimestamps(tag);
        boolean result = false;

        if (startCount == -1) {
            startCount = 0;
            compare = MORE_THAN;
        }

        int savedCount = 0;

        if (startTimestamp == -1 && endTimestamp == -1) {
            savedCount = timestamps.size();
        } else {
            for (Long timestamp : timestamps) {
                if (startTimestamp <= timestamp && timestamp <= endTimestamp) {
                    savedCount++;
                }
            }
        }

        switch (compare) {
            case EXACTLY:
                result = (savedCount == startCount);
                break;
            case MORE_THAN:
                result = (savedCount > startCount);
                break;
            case LESS_THAN:
                result = (savedCount < startCount);
                break;
            case BETWEEN:
                result = (startCount <= savedCount) && (savedCount <= endCount);
                break;
        }

        if (!result) {
            if (autoDone) {
                makeDone(tag, timestamps);
            }
        }

        return result;
    }

    public void makeDone(@NonNull String tag) {
        makeDone(tag, null);
    }

    public void reset(@NonNull String tag) {
        map.remove(tag);
        Preferences.remove(tag);
    }

    public void clear() {
        map.clear();
        Preferences.clear();
    }

    private static void makeDone(@NonNull String tag, List<Long> timestamps) {
        if (timestamps == null) {
            timestamps = getTimestamps(tag);
        }
        timestamps.add(System.currentTimeMillis());
        Preferences.putString(tag, longListToString(timestamps));
    }

    @NonNull
    private static List<Long> getTimestamps(@NonNull String tag) {
        List<Long> timestamps = map.get(tag);
        if (timestamps == null) {
            String string = Preferences.getString(tag, "");
            timestamps = stringToLongList(string);
            map.put(tag, timestamps);
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
    private static String longListToString(@NonNull List<Long> list) {
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
