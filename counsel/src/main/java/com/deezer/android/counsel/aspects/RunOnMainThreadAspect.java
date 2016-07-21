package com.deezer.android.counsel.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class RunOnMainThreadAspect extends RunOnThreadAspect {

    @DeclareError("execution(@com.deezer.android.counsel.annotations.RunOnMainThread !void *(..))")
    public static final String NON_VOID_METHOD = "@RunOnMainThread annotation can only be used on void methods";

    @Pointcut("execution(@com.deezer.android.counsel.annotations.RunOnMainThread void *(..)) && if()")
    public static boolean executeAnnotatedMethod() {
        return !isOnMainThread();
    }

    @Around("executeAnnotatedMethod()")
    public void proceedOnMainThread(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        proceedInHandler(proceedingJoinPoint);
    }

    public RunOnMainThreadAspect() {
        super((String) null);
    }
}
