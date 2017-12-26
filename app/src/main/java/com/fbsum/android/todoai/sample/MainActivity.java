package com.fbsum.android.todoai.sample;

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
        if (Todoai.persist().newVersion(this).check("SHOW_GUIDE")) {
            Log.e(TAG, "show guide ...");
            Todoai.persist().makeDone("SHOW_GUIDE");
        } else {
            Log.e(TAG, "skip show guide...");
        }
    }
}
