package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/client/utils/CloneUtils.class */
public class CloneUtils {
    public static <T> T cloneObject(T obj) throws CloneNotSupportedException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Cloneable) {
            Class<?> clazz = obj.getClass();
            try {
                Method m = clazz.getMethod("clone", null);
                try {
                    T result = (T) m.invoke(obj, null);
                    return result;
                } catch (IllegalAccessException ex) {
                    throw new IllegalAccessError(ex.getMessage());
                } catch (InvocationTargetException ex2) {
                    Throwable cause = ex2.getCause();
                    if (cause instanceof CloneNotSupportedException) {
                        throw ((CloneNotSupportedException) cause);
                    }
                    throw new Error("Unexpected exception", cause);
                }
            } catch (NoSuchMethodException ex3) {
                throw new NoSuchMethodError(ex3.getMessage());
            }
        }
        throw new CloneNotSupportedException();
    }

    public static Object clone(Object obj) throws CloneNotSupportedException {
        return cloneObject(obj);
    }

    private CloneUtils() {
    }
}
