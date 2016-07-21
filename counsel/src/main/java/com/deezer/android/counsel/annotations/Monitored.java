package com.deezer.android.counsel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will monitor the time spent to perform this method, and print the result in the
 * logcat
 *
 * @author Xavier Gouchet
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Monitored {
}
