package com.deezer.android.counsel.checks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.Pointcut;

/**
 * TODO make sure that all fields are also immutable (known mutable classes : Date, Arrays, ...)
 * @author Xavier Gouchet
 */
@Aspect
public class ImmutabilityCheck {

    @Pointcut("within(@com.deezer.android.counsel.annotations.Immutable *)")
    public static void withinAnnotatedType() {
    }

    @DeclareError("set(!static !final * *) && withinAnnotatedType()")
    public static final String NON_FINAL_FIELD =
            "Non static fields of @Immutable types must be declared final.";
}
