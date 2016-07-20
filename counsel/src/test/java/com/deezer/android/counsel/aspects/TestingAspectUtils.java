package com.deezer.android.counsel.aspects;

import android.support.annotation.NonNull;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Xavier Gouchet
 */
public class TestingAspectUtils {

    @NonNull
    public static <R> ProceedingJoinPoint createProceedingJoinPoint(String methodName,
                                                                    String declaringTypeName,
                                                                    Class<R> returnType,
                                                                    R returnValue,
                                                                    Object... arguments) throws Throwable {
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getReturnType()).thenReturn(returnType);
        when(methodSignature.getName()).thenReturn(methodName);
        when(methodSignature.getDeclaringTypeName()).thenReturn(declaringTypeName);

        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getArgs()).thenReturn(arguments == null ? new Object[]{} : arguments);
        when(pjp.getSignature()).thenReturn(methodSignature);

        doReturn(returnValue).when(pjp).proceed();
        return pjp;
    }

    @NonNull
    public static <R> ProceedingJoinPoint createProceedingJoinPoint(String methodName,
                                                                    String declaringTypeName,
                                                                    Class<R> returnType) throws Throwable {
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getReturnType()).thenReturn(returnType);
        when(methodSignature.getName()).thenReturn(methodName);
        when(methodSignature.getDeclaringTypeName()).thenReturn(declaringTypeName);

        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        return pjp;
    }
}
