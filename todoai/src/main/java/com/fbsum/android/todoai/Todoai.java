package com.fbsum.android.todoai;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.CheckResult;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

/**
 * Created by xin on 12/25/17.
 */

public abstract class Todoai {

    public static void init(Application application) {
        PersistTodo.init(application);
    }

    private static final int EXACTLY = 0;
    private static final int MORE_THAN = 1;
    private static final int LESS_THAN = 2;

    private long startTimestamp = 0L;
    private long endTimestamp = 0L;
    private int count = 0;
    private int compare = EXACTLY;

    public static Todoai session() {
        return new SessionTodo();
    }

    public static Todoai persist() {
        return new PersistTodo();
    }

    @CheckResult
    public Todoai today() {
        Calendar calendar = CalendarHolder.getCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.startTimestamp = calendar.getTimeInMillis();
        this.endTimestamp = this.startTimestamp + 86400000L;
        return this;
    }

    public Todoai newVersion(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            long lastAppUpdatedTime = packageInfo.lastUpdateTime;
            return between(lastAppUpdatedTime, Long.MAX_VALUE);
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
            return between(firstInstallTime, Long.MAX_VALUE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @CheckResult
    public Todoai between(@IntRange(from = 1) long startTimestamp, @IntRange(from = 1) long endTimestamp) {
        if (startTimestamp > endTimestamp) {
            throw new IllegalArgumentException("The endTimestamp must be more than startTimestamp.");
        }
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        return this;
    }

    public Todoai exactly(int count) {
        this.count = count;
        this.compare = EXACTLY;
        return this;
    }

    public Todoai moreThan(int count) {
        this.count = count;
        this.compare = MORE_THAN;
        return this;
    }

    public Todoai lessThan(int count) {
        this.count = count;
        this.compare = LESS_THAN;
        return this;
    }

    @NonNull
    protected abstract List<Long> getTimestamps(@NonNull String tag);

    @CheckResult
    public boolean isDone(@NonNull String tag) {
        if (count != 0) {
            throw new IllegalStateException("please invoke check() method when count is not zero.");
        }
        List<Long> timestamps = getTimestamps(tag);
        if (startTimestamp == 0 && endTimestamp == 0) {
            return timestamps.size() > 0;
        } else {
            for (Long timestamp : timestamps) {
                if (startTimestamp <= timestamp && timestamp <= endTimestamp) {
                    return true;
                }
            }
            return false;
        }
    }

    @CheckResult
    public boolean check(@NonNull String tag) {
        List<Long> timestamps = getTimestamps(tag);
        int savedCount = 0;
        if (startTimestamp == 0 && endTimestamp == 0) {
            savedCount = timestamps.size();
        } else {
            for (Long timestamp : timestamps) {
                if (startTimestamp <= timestamp && timestamp <= endTimestamp) {
                    savedCount++;
                } else if (timestamp > endTimestamp) {
                    break;
                }
            }
        }
        boolean result = false;
        switch (compare) {
            case EXACTLY:
                result = (savedCount == count);
                break;
            case MORE_THAN:
                result = (savedCount > count);
                break;
            case LESS_THAN:
                result = (savedCount < count);
                break;
        }
        return result;
    }

    public abstract void makeDone(@NonNull String tag);

    public abstract void reset(@NonNull String tag);

    public abstract void clear();
}
