package com.deezer.android.counsel.aspects;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import static com.deezer.android.counsel.aspects.LoggingUtils.getArguments;
import static com.deezer.android.counsel.aspects.LoggingUtils.getDeclaringTypeName;
import static com.deezer.android.counsel.aspects.LoggingUtils.getMethodName;
import static com.deezer.android.counsel.aspects.LoggingUtils.getTag;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class DeepTracingAspect {

    @Pointcut("execution(@com.deezer.android.counsel.annotations.DeepTrace *.new(..))")
    public static void executeAnnotatedConstructor() {
    }

    @Pointcut("execution(@com.deezer.android.counsel.annotations.DeepTrace * *(..))")
    public static void executeAnnotatedMethod() {
    }

    @Pointcut("cflow(executeAnnotatedConstructor()) || cflow(executeAnnotatedMethod())")
    public void withinFlow() {
    }

    @Pointcut("!within(com.deezer.android.counsel.aspects.*)")
    public void excludeAspect(){}

    @Before("call(* *(..)) && withinFlow() && excludeAspect()")
    public void logEnteringMethod(JoinPoint jp) {
        String tag = getTag(jp);
        String methodName = getMethodName(jp);
        String arguments = getArguments(jp);

        Log.v(tag, "  → " + methodName + arguments);
    }

    @Before("call(*.new(..)) && withinFlow() && excludeAspect()")
    public void logEnteringConstructor(JoinPoint jp) {
        String tag = getTag(jp);
        String type = getDeclaringTypeName(jp);
        String arguments = getArguments(jp);

        Log.v(tag, "  ✧ new " + type + arguments);
    }


    @AfterReturning(value = "call(* *(..)) && withinFlow() && excludeAspect()", returning = "result")
    public void logReturningMethod(JoinPoint jp, Object result) {
        String tag = getTag(jp);
        String methodName = getMethodName(jp);
        Class returnType = ((MethodSignature) jp.getSignature()).getReturnType();
        if ((returnType != Void.TYPE) && (returnType != Void.class)) {
            methodName += " = " + result;
        }
        Log.v(tag, "  ← " + methodName);
    }
}
