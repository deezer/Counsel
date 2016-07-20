package com.deezer.android.counsel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will ensure that this method will always be run on a worker thread
 *
 * @author Xavier Gouchet
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface RunOnWorkerThread {
}
