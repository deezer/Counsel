package com.deezer.android.counsel.checks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareWarning;
import org.aspectj.lang.annotation.DeclareError;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class ExceptionHandlingCheck {

    @DeclareError("handler(java.lang.Error+)")
    public static final String ERROR_HANDLING =
            "Error and subclasses represent unrecoverable errors. " +
                    "When thrown, they should not be caught by application code.";

    @DeclareWarning("handler(java.lang.Exception || java.lang.RuntimeException || java.lang.Throwable)")
    public static final String GENERING_EXCEPTION_HANDLING =
            "Catching a Throwable / Exception / RuntimeException is considered a code smell, " +
                    "you should catch the exact expected exception.";
}
