package com.deezer.android.counsel.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.FieldSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.deezer.android.counsel.aspects.TestingAspectUtils.createProceedingJoinPoint;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xavier Gouchet
 */
@Config(sdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CachedResultAspectTest {

    private CachedResultAspect aspect;


    private Result result1;
    private Result result2;

    @Before
    public void setUp() {
        aspect = new CachedResultAspect();
        result1 = new Result("eggs");
        result2 = new Result("bacon");
    }

    @Test
    public void second_call_with_same_arguments_should_use_cached_value() throws Throwable {
        // Given
        ProceedingJoinPoint firstPJP = createProceedingJoinPoint("bar", "com.sample.Foo", Result.class, result1, 42);
        ProceedingJoinPoint secondPJP = createProceedingJoinPoint("bar", "com.sample.Foo", Result.class, result2, 42);

        // When
        Result firstResult = (Result) aspect.adviceAround(firstPJP);
        Result secondResult = (Result) aspect.adviceAround(secondPJP);

        // Then
        assertThat(firstResult, is(secondResult));
        assertThat(firstResult, equalTo(result1));
        verify(firstPJP).proceed();
        verify(secondPJP, never()).proceed();
    }


    @Test
    public void second_call_with_different_arguments_should_not_use_cached_value() throws Throwable {
        // Given
        ProceedingJoinPoint firstPJP = createProceedingJoinPoint("bar", "com.sample.Foo", Result.class, result1, 42);
        ProceedingJoinPoint secondPJP = createProceedingJoinPoint("bar", "com.sample.Foo", Result.class, result2, 666);

        // When
        Result firstResult = (Result) aspect.adviceAround(firstPJP);
        Result secondResult = (Result) aspect.adviceAround(secondPJP);

        // Then
        assertThat(firstResult, not(is(secondResult)));
        assertThat(firstResult, equalTo(result1));
        assertThat(secondResult, equalTo(result2));
        verify(firstPJP).proceed();
        verify(secondPJP).proceed();
    }

    @Test
    public void non_method_joinpoints_cant_be_cached() throws Throwable {
        // Given
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(mock(FieldSignature.class));
        when(pjp.proceed()).thenReturn(new Result("foo"), new Result("foo"));

        // When
        Result firstResult = (Result) aspect.adviceAround(pjp);
        Result secondResult = (Result) aspect.adviceAround(pjp);

        // Then
        assertThat(firstResult != secondResult, is(true));
        assertThat(firstResult, equalTo(secondResult));
        verify(pjp, times(2)).proceed();
    }

    // because Strings are internalized, we use objects to test == vs equals
    private class Result {
        private final String value;

        public Result(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Result result = (Result) o;

            return value != null ? value.equals(result.value) : result.value == null;

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}