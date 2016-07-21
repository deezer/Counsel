package com.deezer.android.counsel.aspects;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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
public class TracingAspect {

    @Pointcut("execution(@com.deezer.android.counsel.annotations.Trace *.new(..))")
    public static void executeAnnotatedConstructor() {
    }

    @Pointcut("execution(@com.deezer.android.counsel.annotations.Trace * *(..))")
    public static void executeAnnotatedMethod() {
    }


    @Pointcut("within(@com.deezer.android.counsel.annotations.Trace *)")
    public static void withinAnnotatedType() {
    }

    @Pointcut("execution(* *(..)) && withinAnnotatedType()")
    public static void executeMethodWithinAnotatedType() {
    }

    @Pointcut("execution(*.new(..)) && withinAnnotatedType()")
    public static void executeConstructorWithinAnnotatedType() {
    }


    @Before("executeAnnotatedMethod() || executeMethodWithinAnotatedType()")
    public void logEnteringMethod(JoinPoint jp) {
        String tag = getTag(jp);
        String methodName = getMethodName(jp);
        String arguments = getArguments(jp);

        Log.d(tag, "→ " + methodName + arguments);
    }

    @Before("executeAnnotatedConstructor() || executeConstructorWithinAnnotatedType()")
    public void logEnteringConstructor(JoinPoint jp) {
        String tag = getTag(jp);
        String type = getDeclaringTypeName(jp);
        String arguments = getArguments(jp);

        Log.d(tag, "✧ new " + type + arguments);
    }


    @AfterReturning(value = "executeAnnotatedMethod() || executeMethodWithinAnotatedType()", returning = "result")
    public void logReturningMethod(JoinPoint jp, Object result) {
        String tag = getTag(jp);
        String methodName = getMethodName(jp);
        Class returnType = ((MethodSignature) jp.getSignature()).getReturnType();
        if ((returnType != Void.TYPE) && (returnType != Void.class)) {
            methodName += " = " + result;
        }
        Log.d(tag, "← " + methodName);
    }

    @AfterThrowing(value = "executeAnnotatedMethod() || executeMethodWithinAnotatedType()", throwing = "thrown")
    public void logThrowingMethod(JoinPoint jp, Throwable thrown) {
        String tag = getTag(jp);
        String methodName = getMethodName(jp);
        String throwableName = thrown.getClass().getSimpleName();

        Log.e(tag, "✝ " + methodName + " ! " + throwableName + " : \"" + thrown.getMessage() + "\"");
    }
}
