package com.deezer.android.counsel.aspects;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * TODO allow nonvoid returning methods using Future?
 * @author Xavier Gouchet
 */
class RunOnThreadAspect {

    @NonNull
    private final Handler handler;

    public RunOnThreadAspect(@Nullable String threadName) {
        Looper actualLooper;
        if (threadName == null) {
            actualLooper = Looper.getMainLooper();
        } else {
            HandlerThread handlerThread = new HandlerThread(threadName);
            handlerThread.start();
            actualLooper = handlerThread.getLooper();
        }
        handler = new Handler(actualLooper);
    }

    @VisibleForTesting
    RunOnThreadAspect(@NonNull Handler handler) {
        this.handler = handler;
    }

    protected void proceedInHandler(final ProceedingJoinPoint proceedingJoinPoint) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    proceedingJoinPoint.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        };

        handler.post(runnable);
    }

    protected static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
