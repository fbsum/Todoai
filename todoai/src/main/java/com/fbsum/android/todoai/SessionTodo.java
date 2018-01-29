package com.fbsum.android.todoai;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SessionTodo extends Todoai {

    private static final Map<String, List<Long>> sessionMap = new HashMap<>();

    @NonNull
    @Override
    protected List<Long> getTimestamps(@NonNull String tag) {
        List<Long> timestamps = sessionMap.get(tag);
        if (timestamps == null) {
            timestamps = new ArrayList<>(1);
        }
        return timestamps;
    }

    @Override
    public void makeDone(@NonNull String tag) {
        List<Long> timestamps = getTimestamps(tag);
        timestamps.add(System.currentTimeMillis());
    }

    @Override
    public void reset(@NonNull String tag) {
        sessionMap.remove(tag);
    }

    @Override
    public void clear() {
        sessionMap.clear();
    }
}