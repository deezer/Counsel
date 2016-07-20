package com.deezer.android.counsel.aspects;

import android.support.annotation.NonNull;

import com.deezer.android.counsel.annotations.RetryOnFailure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static com.deezer.android.counsel.aspects.TestingAspectUtils.createProceedingJoinPoint;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xavier Gouchet
 */
@Config(sdk = 18)
@RunWith(RobolectricTestRunner.class)
public class RetryAspectTest {

    private RetryAspect aspect;

    @Before
    public void setUp() {
        aspect = new RetryAspect();
    }

    @Test
    public void suceeding_joinpoint_should_not_retry() throws Throwable {
        // Given
        Object value = new Object();
        ProceedingJoinPoint pjp = createProceedingJoinPoint("foo", "com.sample.foo", Object.class);
        when(pjp.proceed()).thenReturn(value);
        RetryOnFailure retryOnFailure = createRetryOnFailure(3, 0);

        // When
        Object result = aspect.adviceAround(pjp, retryOnFailure);

        // Then
        verify(pjp).proceed();
        assertThat(result, is(value));
    }

    @Test
    public void temporary_failing_joinpoint_should_retry() throws Throwable {
        // Given
        Object value = new Object();
        ProceedingJoinPoint pjp = createProceedingJoinPoint("foo", "com.sample.foo", Object.class);
        when(pjp.proceed()).then(answerAfterFailing(value, new RuntimeException()));
        RetryOnFailure retryOnFailure = createRetryOnFailure(3, 0);

        // When
        Object result = aspect.adviceAround(pjp, retryOnFailure);

        // Then
        verify(pjp, times(2)).proceed();
        assertThat(result, is(value));
    }

    @Test
    public void failing_joinpoint_should_eventually_fail() throws Throwable {
        // Given
        Object value = new Object();
        ProceedingJoinPoint pjp = createProceedingJoinPoint("foo", "com.sample.foo", Object.class);
        when(pjp.proceed()).then(answerAfterFailing(value, new InterruptedException(), new IOException(), new RuntimeException()));
        RetryOnFailure retryOnFailure = createRetryOnFailure(3, 0);

        // When
        Exception thrown = null;
        try {
            aspect.adviceAround(pjp, retryOnFailure);
        } catch (Exception e) {
            thrown = e;
        }

        // Then
        verify(pjp, times(3)).proceed();
        assertThat(thrown, instanceOf(RuntimeException.class));
    }

    @Test
    public void temporary_failing_joinpoint_should_wait_before_retry() throws Throwable {
        // Given
        Object value = new Object();
        final long retryDelay = 250;
        ProceedingJoinPoint pjp = createProceedingJoinPoint("foo", "com.sample.foo", Object.class);
        when(pjp.proceed()).then(answerAfterFailing(value, new RuntimeException()));
        RetryOnFailure retryOnFailure = createRetryOnFailure(3, retryDelay);

        // When
        long start = System.currentTimeMillis();
        Object result = aspect.adviceAround(pjp, retryOnFailure);
        long end = System.currentTimeMillis();

        // Then
        verify(pjp, times(2)).proceed();
        assertThat(result, is(value));
        assertThat((end - start), new GreaterThan<>(retryDelay));
    }

    private RetryOnFailure createRetryOnFailure(int retryCount, long retryDelayMs) {
        RetryOnFailure retryOnFailure = mock(RetryOnFailure.class);
        when(retryOnFailure.retryCount()).thenReturn(retryCount);
        when(retryOnFailure.retryDelayMs()).thenReturn(retryDelayMs);
        return retryOnFailure;
    }

    @NonNull
    private AnswerAfterFailing<Object> answerAfterFailing(Object value, Exception... exceptions) {
        return new AnswerAfterFailing<>(value, exceptions);
    }

    private static class AnswerAfterFailing<T> implements Answer<T> {

        private final Exception[] exceptions;
        private final T result;
        private int calls = 0;

        private AnswerAfterFailing(T result, Exception... exceptions) {
            this.exceptions = exceptions;
            this.result = result;
        }

        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            if (calls >= exceptions.length) {
                return result;
            }
            throw exceptions[calls++];
        }
    }
}