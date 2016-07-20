package com.deezer.android.counsel.aspects;

import com.deezer.android.counsel.annotations.RetryOnFailure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class RetryAspect {

    @Pointcut("execution(@com.deezer.android.counsel.annotations.RetryOnFailure * *(..))")
    public void annotatedMethod() {
    }

    @Around("annotatedMethod() && @annotation(retry)")
    public Object adviceAround(ProceedingJoinPoint pjp, RetryOnFailure retry) throws Throwable {

        int retryCount = retry.retryCount();
        long retryDelayMs = retry.retryDelayMs();

        Exception lastThrowable = new IllegalStateException();

        while (retryCount-- > 0) {
            try {
                return pjp.proceed();
            } catch (Exception e) {
                lastThrowable = e;
                if ((retryCount > 0) && (retryDelayMs > 0)) {
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        throw lastThrowable;
    }
}
