package com.deezer.android.counsel.aspects;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.text.NumberFormat;

import static com.deezer.android.counsel.aspects.LoggingUtils.getMethodName;
import static com.deezer.android.counsel.aspects.LoggingUtils.getTag;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class PerformanceAspect {


    @Pointcut("execution(@com.deezer.android.counsel.annotations.Monitored * *(..))")
    public void annotatedMethod() {
    }

    @Around("annotatedMethod() ")
    public Object adviceAroundMethod(ProceedingJoinPoint pjp) throws Throwable {

        long startNano = System.nanoTime();
        Object result = pjp.proceed();
        long endNano = System.nanoTime();

        final String methodName = getMethodName(pjp);
        final String duration = NumberFormat.getIntegerInstance().format(endNano - startNano);

        Log.i(getTag(pjp), "⌚ " + methodName + " took " + duration + " nanos");

        return result;
    }
}
