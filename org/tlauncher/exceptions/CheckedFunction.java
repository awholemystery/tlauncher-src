package org.tlauncher.exceptions;

@FunctionalInterface
/* loaded from: TLauncher-2.876.jar:org/tlauncher/exceptions/CheckedFunction.class */
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
