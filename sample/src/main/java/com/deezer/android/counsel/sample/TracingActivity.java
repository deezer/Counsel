package com.deezer.android.counsel.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.deezer.android.counsel.annotations.DeepTrace;
import com.deezer.android.counsel.annotations.Trace;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TracingActivity extends AppCompatActivity {

    @Override
    @Trace
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foo();
        long value = bar();
    }

    @Override
    @Trace
    protected void onResume() {
        super.onResume();
        updateText();
    }

    @Trace
    private static void foo() {
        // Nothing to see here
    }

    private long bar() {
        return System.currentTimeMillis();
    }

    @DeepTrace
    private void updateText() {
        long value = bar();
        Date date = new Date(value);

        Toast.makeText(this, SimpleDateFormat.getDateInstance().format(date), Toast.LENGTH_LONG)
                .show();
    }
}
