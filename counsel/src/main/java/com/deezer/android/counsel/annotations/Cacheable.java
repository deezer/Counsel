package com.deezer.android.counsel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will ensure that results from this method will be cached. If several calls with
 * the same arguments are made, only one actual call will be made, and subsequent ones will return
 * the cached value.
 *
 * @author Xavier Gouchet
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Cacheable {
}
