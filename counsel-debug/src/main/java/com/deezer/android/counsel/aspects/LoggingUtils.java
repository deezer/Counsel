package com.deezer.android.counsel.aspects;

import org.aspectj.lang.JoinPoint;

/**
 * @author Xavier Gouchet
 */
public class LoggingUtils {


    public static String getTag(JoinPoint jp) {
        return jp.getSignature().getDeclaringType().getSimpleName();
    }

    public static String getMethodName(JoinPoint jp) {
        return jp.getSignature().getName();
    }

    public static String getDeclaringTypeName(JoinPoint jp) {
        return jp.getSignature().getDeclaringType().getSimpleName();
    }

    public static String getArguments(JoinPoint jp) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');

        Object[] args = jp.getArgs();
        for (int i = 0; i < args.length; ++i) {
            if (i > 0) builder.append(", ");
            appendArgument(args[i], builder);
        }

        builder.append(')');

        return builder.toString();
    }

    private static void appendArgument(Object arg, StringBuilder builder) {
        if (arg == null) {
            builder.append('ø');
        } else if (arg instanceof String) {
            builder.append('"').append(arg).append('"');
        } else {
            builder.append(arg);
        }
    }
}
