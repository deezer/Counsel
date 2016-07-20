package com.deezer.android.counsel.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.deezer.android.counsel.annotations.CachedResult;

import butterknife.BindView;
import butterknife.OnEditorAction;

import static butterknife.ButterKnife.bind;

public class CachedResultActivity extends AppCompatActivity {

    @BindView(R.id.cached_output)
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cached_result);

        bind(this);
    }

    @OnEditorAction(R.id.cached_input)
    boolean onEditorAction(TextView view, int actionId, KeyEvent key) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            processInput(view.getText().toString());
        }
        return true;
    }

    private void processInput(String value) {
        int input = -1;
        try {
            input = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return;
        }
        long start = System.nanoTime();
        long result = factorial(input);
        long end = System.nanoTime();
        float durationMs = (end - start) / 1000000.f;

        StringBuilder builder = new StringBuilder();
        builder.append(input)
                .append("! = ")
                .append(result)
                .append("(took ")
                .append(durationMs)
                .append("ms)");

        output.setText(builder.toString());
    }

    /**
     * Classical recursive computation of factorial.
     * <p/>
     * <ul>
     * <li><code>n</code>! = <code>n</code> × (<code>n</code> - 1) × (<code>n</code> - 2) × … × 2 × 1</li>
     * <li>1! = 0! = 1</li>
     * </ul>
     *
     * @param n a positive integer
     * @return the value of factorial n
     */
    @CachedResult
    private long factorial(long n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        } else if (n < 2) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }
}
