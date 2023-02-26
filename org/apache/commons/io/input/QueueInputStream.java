package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.output.QueueOutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/QueueInputStream.class */
public class QueueInputStream extends InputStream {
    private final BlockingQueue<Integer> blockingQueue;

    public QueueInputStream() {
        this(new LinkedBlockingQueue());
    }

    public QueueInputStream(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = (BlockingQueue) Objects.requireNonNull(blockingQueue, "blockingQueue");
    }

    public QueueOutputStream newQueueOutputStream() {
        return new QueueOutputStream(this.blockingQueue);
    }

    @Override // java.io.InputStream
    public int read() {
        Integer value = this.blockingQueue.poll();
        if (value == null) {
            return -1;
        }
        return 255 & value.intValue();
    }
}
