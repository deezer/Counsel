package com.deezer.android.counsel.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class RunOnWorkerThreadAspect extends RunOnThreadAspect {

    @DeclareError("execution(@com.deezer.android.counsel.annotations.RunOnWorkerThread !void *(..))")
    public static final String NON_VOID_METHOD = "@RunOnWorkerThread annotation can only be used on void methods";

    public RunOnWorkerThreadAspect() {
        super("WorkerAspect");
    }

    @Pointcut("execution(@com.deezer.android.counsel.annotations.RunOnWorkerThread void *(..)) && if()")
    public static boolean backgroundThreadMethod(JoinPoint jp) {
        return isOnMainThread();
    }

    @Around("backgroundThreadMethod(jp)")
    public void adviceAroundMainThread(final ProceedingJoinPoint proceedingJoinPoint,
                                       final JoinPoint jp) throws Throwable {
        proceedInHandler(proceedingJoinPoint);
    }
}
