package org.apache.commons.compress.archivers.examples;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/CloseableConsumerAdapter.class */
final class CloseableConsumerAdapter implements Closeable {
    private final CloseableConsumer consumer;
    private Closeable closeable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CloseableConsumerAdapter(CloseableConsumer consumer) {
        this.consumer = (CloseableConsumer) Objects.requireNonNull(consumer, "consumer");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <C extends Closeable> C track(C closeable) {
        this.closeable = closeable;
        return closeable;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeable != null) {
            this.consumer.accept(this.closeable);
        }
    }
}
