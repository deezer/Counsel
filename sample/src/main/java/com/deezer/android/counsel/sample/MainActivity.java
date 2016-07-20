package com.deezer.android.counsel.sample;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends LauncherActivity {

    @Override
    protected Intent getTargetIntent() {
        return new Intent("com.deezer.android.counsel.SAMPLE_ACTION");
    }
}
