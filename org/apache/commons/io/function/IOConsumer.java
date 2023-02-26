package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/function/IOConsumer.class */
public interface IOConsumer<T> {
    public static final IOConsumer<?> NOOP_IO_CONSUMER = t -> {
    };

    void accept(T t) throws IOException;

    static <T> IOConsumer<T> noop() {
        return (IOConsumer<T>) NOOP_IO_CONSUMER;
    }

    default IOConsumer<T> andThen(IOConsumer<? super T> after) {
        Objects.requireNonNull(after, "after");
        return t -> {
            accept(after);
            after.accept(after);
        };
    }
}
