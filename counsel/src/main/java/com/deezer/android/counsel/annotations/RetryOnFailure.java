package com.deezer.android.counsel.annotations;

import android.support.annotation.IntRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will ensure that calls to this method will be retried on failures
 *
 * @author Xavier Gouchet
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOnFailure {
    /**
     * @return how many retries to use before failing
     */
    @IntRange(from = 0) int retryCount() default 3;

    /**
     * @return the duration (in millis) to wait before trying again
     */
    @IntRange(from = 0) long retryDelayMs() default 500;
}
