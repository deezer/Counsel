package com.deezer.android.counsel.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.deezer.android.counsel.annotations.Cacheable;
import com.deezer.android.counsel.annotations.Monitored;
import com.deezer.android.counsel.annotations.Trace;

import butterknife.BindView;
import butterknife.OnEditorAction;

import static butterknife.ButterKnife.bind;

public class TracingActivity extends AppCompatActivity {

    @BindView(R.id.cached_output)
    TextView output;

    @Override
    @Trace
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foo();
    }

    @Trace
    private static void foo(){
        // Nothing to see here
    }

    @Trace
    private long bar(){
        return System.currentTimeMillis();
    }
}
