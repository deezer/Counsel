package com.deezer.android.counsel.aspects;

import android.os.Handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.deezer.android.counsel.aspects.TestingAspectUtils.createProceedingJoinPoint;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Xavier Gouchet
 */
@Config(sdk = 18)
@RunWith(RobolectricTestRunner.class)
public class RunOnThreadAspectTest {

    ArgumentCaptor<Runnable> runnableCaptor;
    private RunOnThreadAspect aspect;
    @Mock
    private Handler mockHandler;

    @Before
    public void setUp() {
        initMocks(this);
        doReturn(true).when(mockHandler).post(Matchers.<Runnable>any());

        aspect = new RunOnThreadAspect(mockHandler);
        runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    }

    @Test
    public void methods_call_for_background_thread_should_be_dispatched_to_handler() throws Throwable {
        // Given
        ProceedingJoinPoint pjp = createProceedingJoinPoint("foo", "com.sample.Foo", Void.TYPE);
        doReturn(null).when(pjp).proceed();

        // When
        aspect.proceedInHandler(pjp);

        // Then
        verify(mockHandler).post(runnableCaptor.capture());
        verifyZeroInteractions(mockHandler, pjp);

        runnableCaptor.getValue().run();
        verify(pjp).proceed();
    }
}