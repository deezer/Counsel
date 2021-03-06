package com.deezer.android.counsel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will generate logs when this method is called
 * Note : this will only work on Debug builds, and only if you've added the <code>counsel-debug</code> dependency
 *
 * @author Xavier Gouchet
 */
@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Trace {
}
