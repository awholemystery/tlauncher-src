package by.gdev.util;

@FunctionalInterface
/* loaded from: TLauncher-2.876.jar:by/gdev/util/CheckedFunction.class */
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
