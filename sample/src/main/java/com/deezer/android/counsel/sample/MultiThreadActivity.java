package com.deezer.android.counsel.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deezer.android.counsel.annotations.RunOnMainThread;
import com.deezer.android.counsel.annotations.RunOnWorkerThread;
import com.deezer.android.counsel.annotations.Trace;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
@Trace
public class MultiThreadActivity extends AppCompatActivity {

    @BindView(R.id.multi_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multi_thread);

        bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        longProcess();
    }

    @RunOnWorkerThread
    void longProcess() {
        toggleProgressVisibility(true);
        toast("Sleeping for 3 seconds");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignore) {
        }

        toast("Done");
        toggleProgressVisibility(false);
    }

    @RunOnMainThread
    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @RunOnMainThread
    private void toggleProgressVisibility(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
