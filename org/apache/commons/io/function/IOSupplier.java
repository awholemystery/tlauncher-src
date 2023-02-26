package org.apache.commons.io.function;

import java.io.IOException;

@FunctionalInterface
/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/function/IOSupplier.class */
public interface IOSupplier<T> {
    T get() throws IOException;
}
