package com.deezer.android.counsel.aspects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.deezer.android.counsel.annotations.CachedResult;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * This Aspect tracks calls to methods annotated with the {@link CachedResult} annotation
 * <p/>
 * Possible ameliorations : have a per target cache, with configurable size
 *
 * @author Xavier Gouchet
 */
@Aspect
public class CachedResultAspect {


    @Pointcut("execution(@com.deezer.android.counsel.annotations.CachedResult * *(..))")
    public static void annotatedMethod() {
    }

    @Around("annotatedMethod()")
    public Object adviceAroundAnnotatedMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        JoinPointDescription key = generateCacheKey(proceedingJoinPoint);

        Object result = null;

        if (key != null) {
            result = CACHE.get(key);
        }

        if (result == null) {
            result = proceedingJoinPoint.proceed();
            if (key != null) {
                CACHE.put(key, result);
            }
        }

        return result;
    }

    @Nullable
    private static JoinPointDescription generateCacheKey(ProceedingJoinPoint proceedingJoinPoint) {
        String className;
        String returnType;
        String methodName;

        Signature signature = proceedingJoinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            returnType = methodSignature.getReturnType().getCanonicalName();
            methodName = methodSignature.getName();
            className = methodSignature.getDeclaringTypeName();
        } else {
            return null;
        }

        return new JoinPointDescription(className, methodName, returnType, proceedingJoinPoint.getArgs());
    }

    private static final int MAX_ENTRY_COUNT = 1024;
    private final LruCache<JoinPointDescription, Object> CACHE = new LruCache<>(MAX_ENTRY_COUNT);

    /**
     * Describes a join point uniquely to be used as key in a LRU map
     */
    private static final class JoinPointDescription {

        @NonNull
        private final String className;
        @NonNull
        private final String methodName;
        @NonNull
        private final String resultType;
        @Nullable
        private final Object[] arguments;

        private JoinPointDescription(@NonNull String className,
                                     @NonNull String methodName,
                                     @NonNull String resultType,
                                     @Nullable Object[] arguments) {
            this.className = className;
            this.methodName = methodName;
            this.resultType = resultType;
            this.arguments = arguments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JoinPointDescription that = (JoinPointDescription) o;

            if (!className.equals(that.className)) return false;
            if (!methodName.equals(that.methodName)) return false;
            if (!resultType.equals(that.resultType)) return false;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(arguments, that.arguments);
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + methodName.hashCode();
            result = 31 * result + resultType.hashCode();
            result = 31 * result + Arrays.hashCode(arguments);
            return result;
        }
    }
}
