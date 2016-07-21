package com.deezer.android.counsel.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class PoolableAspect {

    @Pointcut("call(com.deezer.android.counsel.interfaces.Poolable+.new(..))")
    public static void callImplmentationConstructor() {
    }


    @Pointcut("execution(void com.deezer.android.counsel.interfaces.Poolable+.releaseInstance())")
    public static void executeImplementationReleaseInstance() {
    }

    @Around("callImplmentationConstructor()")
    public Object getInstanceFromPoolOrProceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String typeName = proceedingJoinPoint.getSignature().getDeclaringTypeName();

        List<Object> pool = getPool(typeName);

        if (pool.isEmpty()) {
            return proceedingJoinPoint.proceed();
        } else {
            return pool.remove(0);
        }
    }

    @After("executeImplementationReleaseInstance()")
    public void addInstanceToPool(JoinPoint joinPoint) throws Throwable {
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        List<Object> pool = getPool(typeName);
        pool.add(joinPoint.getTarget());
    }

    private Map<String, List<Object>> pools = new Hashtable<>();

    private List<Object> getPool(String typeName) {
        if (pools.containsKey(typeName)) {
            return pools.get(typeName);
        } else {
            List<Object> pool = new ArrayList<>();
            pools.put(typeName, pool);
            return pool;
        }
    }
}
