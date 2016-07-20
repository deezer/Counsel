package com.deezer.android.counsel.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.android.counsel.annotations.RetryOnFailure;
import com.deezer.android.counsel.annotations.RunOnMainThread;
import com.deezer.android.counsel.annotations.RunOnWorkerThread;

import butterknife.OnClick;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class RetryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retry);

        bind(this);
    }

    @OnClick(R.id.retry_success_button)
    void callWithImmediateSuccess() {
        PredictableFailure failure = new PredictableFailure(0, "Foo");
        callPredictableFailure(failure);
    }

    @OnClick(R.id.retry_1failure_button)
    void callWith1Failure() {
        PredictableFailure failure = new PredictableFailure(1, "Bar");
        callPredictableFailure(failure);
    }

    @OnClick(R.id.retry_3failure_button)
    void callWith3Failure() {
        PredictableFailure failure = new PredictableFailure(3, "Bar");
        callPredictableFailure(failure);
    }

    @RunOnWorkerThread
    private void callPredictableFailure(PredictableFailure predictableFailure) {
        try {
            String result = predictableFailure.computeResult();
            toast("Got a result : " + result);
        } catch (Exception e) {
            toastError(e.getMessage());
        }
    }

    @RunOnMainThread
    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @RunOnMainThread
    private void toastError(String message) {
        final Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        TextView messageView = (TextView) toast.getView().findViewById(android.R.id.message);
        messageView.setTextColor(Color.RED);
        toast.show();
    }


    private static class PredictableFailure {
        private final String result;
        private int failureCount;


        private PredictableFailure(int failureCount, String result) {
            this.failureCount = failureCount;
            this.result = result;
        }

        @RetryOnFailure(retryCount = 3, retryDelayMs = 1000)
        private String computeResult() {
            if (failureCount-- > 0) {
                throw new RuntimeException("An error occured.\n" +
                        "Still expecting " + failureCount + " failures");
            }
            return result;
        }
    }
}
