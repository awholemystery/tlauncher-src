package org.apache.commons.compress.archivers.examples;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/CloseableConsumer.class */
public interface CloseableConsumer {
    public static final CloseableConsumer CLOSING_CONSUMER = (v0) -> {
        v0.close();
    };
    public static final CloseableConsumer NULL_CONSUMER = c -> {
    };

    void accept(Closeable closeable) throws IOException;
}
