package com.fbsum.android.todoai;

import android.os.Looper;
import android.support.annotation.CheckResult;

import java.util.Calendar;

/**
 * Calendar Holder
 */
class CalendarHolder {

    private static Calendar calendar = Calendar.getInstance();

    @CheckResult
    static Calendar getCalendar() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return calendar;
        } else {
            return (Calendar) calendar.clone();
        }
    }
}