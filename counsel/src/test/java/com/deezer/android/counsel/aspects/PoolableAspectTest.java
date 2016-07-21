package com.deezer.android.counsel.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;

import static com.deezer.android.counsel.aspects.TestingAspectUtils.createProceedingJoinPoint;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xavier Gouchet
 */
public class PoolableAspectTest {

    private PoolableAspect aspect;

    @Before
    public void setUp() {
        aspect = new PoolableAspect();
    }

    @Test
    public void first_call_should_proceed() throws Throwable {
        // Given
        ProceedingJoinPoint pjp = createProceedingJoinPoint("new", "com.sample.Foo", String.class);
        final String value = "foo";
        when(pjp.proceed()).thenReturn(value);

        // When
        String result = (String) aspect.getInstanceFromPoolOrProceed(pjp);

        // Then
        verify(pjp).proceed();
        assertThat(result, is(value));
    }

    @Test
    public void second_call_should_reuse_released_instance() throws Throwable {
        // Given
        final String value = "foo";
        ProceedingJoinPoint pjp1 = createProceedingJoinPoint("new", "com.sample.Foo", String.class);
        when(pjp1.proceed()).thenReturn(value);
        JoinPoint jp2 = createProceedingJoinPoint("releaseInstance", "com.sample.Foo", String.class);
        when(jp2.getTarget()).thenReturn(value);
        ProceedingJoinPoint pjp3 = createProceedingJoinPoint("new", "com.sample.Foo", String.class);
        when(pjp3.proceed()).thenReturn("spam");

        // When
        String result1 = (String) aspect.getInstanceFromPoolOrProceed(pjp1);
        aspect.addInstanceToPool(jp2);
        String result2 = (String) aspect.getInstanceFromPoolOrProceed(pjp3);

        // Then
        verify(pjp1).proceed();
        verify(pjp3, never()).proceed();
        assertThat(result1, is(value));
        assertThat(result2, is(value));
    }

    @Test
    public void second_call_should_proceed_when_no_released_instance() throws Throwable {
        // Given
        final String value1 = "foo";
        final String value2 = "bar";
        ProceedingJoinPoint pjp1 = createProceedingJoinPoint("new", "com.sample.Foo", String.class);
        when(pjp1.proceed()).thenReturn(value1);
        ProceedingJoinPoint pjp2 = createProceedingJoinPoint("new", "com.sample.Foo", String.class);
        when(pjp2.proceed()).thenReturn(value2);

        // When
        String result1 = (String) aspect.getInstanceFromPoolOrProceed(pjp1);
        String result2 = (String) aspect.getInstanceFromPoolOrProceed(pjp2);

        // Then
        verify(pjp1).proceed();
        verify(pjp2).proceed();
        assertThat(result1, is(value1));
        assertThat(result2, is(value2));
    }
}