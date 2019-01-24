package com.fbsum.android.todoai.sample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fbsum.android.todoai.Todoai;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Todoai.init(getApplication());

        test();
    }

    private void test() {
        Context context = this;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 864000000L;
        boolean result = false;
        String tag = "SHOW_TAG";

        result = Todoai.getInstance().isDone(tag);
        result = Todoai.getInstance().today().isDone(tag);
        result = Todoai.getInstance().newVersion(context).isDone(tag);
        result = Todoai.getInstance().afterInstall(context).isDone(tag);
        result = Todoai.getInstance().timestampBetween(startTime, endTime).isDone(tag);
        // ...
        result = Todoai.getInstance().countExactly(5).isDone(tag);
        result = Todoai.getInstance().countMoreThan(5).isDone(tag);
        result = Todoai.getInstance().countLessThan(5).isDone(tag);
        result = Todoai.getInstance().countBetween(1, 5).isDone(tag);
        result = Todoai.getInstance().today().countMoreThan(5).isDone(tag);
        result = Todoai.getInstance().newVersion(context).countExactly(5).isDone(tag);
        result = Todoai.getInstance().timestampBetween(startTime, endTime).countLessThan(5).isDone(tag);
        // ...

        result = Todoai.getInstance().autoDone().isDone(tag);
        // ...

        Todoai.getInstance().makeDone(tag);
    }
}
