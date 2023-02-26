package org.apache.http.util;

import java.util.Collection;

/* loaded from: TLauncher-2.876.jar:org/apache/http/util/Args.class */
public class Args {
    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void check(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void check(boolean expression, String message, Object arg) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    public static <T> T notNull(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        return argument;
    }

    public static <T extends CharSequence> T notEmpty(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (TextUtils.isEmpty(argument)) {
            throw new IllegalArgumentException(name + " may not be empty");
        }
        return argument;
    }

    public static <T extends CharSequence> T notBlank(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (TextUtils.isBlank(argument)) {
            throw new IllegalArgumentException(name + " may not be blank");
        }
        return argument;
    }

    public static <T extends CharSequence> T containsNoBlanks(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (TextUtils.containsBlanks(argument)) {
            throw new IllegalArgumentException(name + " may not contain blanks");
        }
        return argument;
    }

    public static <E, T extends Collection<E>> T notEmpty(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (argument.isEmpty()) {
            throw new IllegalArgumentException(name + " may not be empty");
        }
        return argument;
    }

    public static int positive(int n, String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " may not be negative or zero");
        }
        return n;
    }

    public static long positive(long n, String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " may not be negative or zero");
        }
        return n;
    }

    public static int notNegative(int n, String name) {
        if (n < 0) {
            throw new IllegalArgumentException(name + " may not be negative");
        }
        return n;
    }

    public static long notNegative(long n, String name) {
        if (n < 0) {
            throw new IllegalArgumentException(name + " may not be negative");
        }
        return n;
    }
}