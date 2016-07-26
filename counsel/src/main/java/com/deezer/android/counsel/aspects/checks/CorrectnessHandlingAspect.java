package com.deezer.android.counsel.aspects.checks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareWarning;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class CorrectnessHandlingAspect {

    @DeclareWarning("get(public !final * *) || set(public !final * *)")
    private static final String ONLY_PRIVATE_FIELDS =
            "Don't use non final public, protected or default fields.";
}
