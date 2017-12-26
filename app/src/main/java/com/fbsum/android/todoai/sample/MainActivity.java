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
        result = Todoai.persist().isDone(tag);
        result = Todoai.persist().today().isDone(tag);
        result = Todoai.persist().newVersion(context).isDone(tag);
        result = Todoai.persist().afterInstall(context).isDone(tag);
        result = Todoai.persist().between(startTime, endTime).isDone(tag);
        // ...

        result = Todoai.persist().moreThan(5).check(tag);
        result = Todoai.persist().exactly(5).check(tag);
        result = Todoai.persist().lessThan(5).check(tag);
        result = Todoai.persist().today().moreThan(5).check(tag);
        result = Todoai.persist().newVersion(context).exactly(5).check(tag);
        result = Todoai.persist().between(startTime, endTime).lessThan(5).check(tag);
        // ...

        result = Todoai.session().isDone(tag);
        result = Todoai.session().today().isDone(tag);
        result = Todoai.session().between(startTime, endTime).isDone(tag);
        // ...

        result = Todoai.session().moreThan(5).check(tag);
        result = Todoai.session().exactly(5).check(tag);
        result = Todoai.session().lessThan(5).check(tag);
        result = Todoai.session().between(startTime, endTime).lessThan(5).check(tag);
        // ...

        Todoai.persist().makeDone(tag);
        Todoai.session().makeDone(tag);

        if (Todoai.persist().newVersion(this).check("SHOW_GUIDE")) {
            Log.e(TAG, "show guide ...");
            Todoai.persist().makeDone("SHOW_GUIDE");
        } else {
            Log.e(TAG, "skip show guide...");
        }
    }
}
