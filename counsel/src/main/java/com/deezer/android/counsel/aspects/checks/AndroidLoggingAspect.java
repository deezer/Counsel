package com.deezer.android.counsel.aspects.checks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class AndroidLoggingAspect {

    @DeclareError("get(* java.lang.System.out)")
    public static final String OUT_STREAM = "Should not log directly to System.out " +
            "Use the Android Log.?() methods instead.";

    @DeclareError("get(* java.lang.System.err)")
    public static final String ERR_STREAM = "You should not log directly to System.err. " +
            "Use the Android Log.e() method instead.";

    @DeclareError("call(* java.lang.Throwable+.printStackTrace(..))")
    public static final String PRINT_STACK_TRACE =
            "You should not directly print throwable stacktraces. " +
                    "Use the Android Log.e() method instead.";
}
