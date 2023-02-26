package org.apache.http.impl.client;

import java.util.concurrent.atomic.AtomicLong;

/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/FutureRequestExecutionMetrics.class */
public final class FutureRequestExecutionMetrics {
    private final AtomicLong activeConnections = new AtomicLong();
    private final AtomicLong scheduledConnections = new AtomicLong();
    private final DurationCounter successfulConnections = new DurationCounter();
    private final DurationCounter failedConnections = new DurationCounter();
    private final DurationCounter requests = new DurationCounter();
    private final DurationCounter tasks = new DurationCounter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AtomicLong getActiveConnections() {
        return this.activeConnections;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AtomicLong getScheduledConnections() {
        return this.scheduledConnections;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DurationCounter getSuccessfulConnections() {
        return this.successfulConnections;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DurationCounter getFailedConnections() {
        return this.failedConnections;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DurationCounter getRequests() {
        return this.requests;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DurationCounter getTasks() {
        return this.tasks;
    }

    public long getActiveConnectionCount() {
        return this.activeConnections.get();
    }

    public long getScheduledConnectionCount() {
        return this.scheduledConnections.get();
    }

    public long getSuccessfulConnectionCount() {
        return this.successfulConnections.count();
    }

    public long getSuccessfulConnectionAverageDuration() {
        return this.successfulConnections.averageDuration();
    }

    public long getFailedConnectionCount() {
        return this.failedConnections.count();
    }

    public long getFailedConnectionAverageDuration() {
        return this.failedConnections.averageDuration();
    }

    public long getRequestCount() {
        return this.requests.count();
    }

    public long getRequestAverageDuration() {
        return this.requests.averageDuration();
    }

    public long getTaskCount() {
        return this.tasks.count();
    }

    public long getTaskAverageDuration() {
        return this.tasks.averageDuration();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[activeConnections=").append(this.activeConnections).append(", scheduledConnections=").append(this.scheduledConnections).append(", successfulConnections=").append(this.successfulConnections).append(", failedConnections=").append(this.failedConnections).append(", requests=").append(this.requests).append(", tasks=").append(this.tasks).append("]");
        return builder.toString();
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/FutureRequestExecutionMetrics$DurationCounter.class */
    static class DurationCounter {
        private final AtomicLong count = new AtomicLong(0);
        private final AtomicLong cumulativeDuration = new AtomicLong(0);

        DurationCounter() {
        }

        public void increment(long startTime) {
            this.count.incrementAndGet();
            this.cumulativeDuration.addAndGet(System.currentTimeMillis() - startTime);
        }

        public long count() {
            return this.count.get();
        }

        public long averageDuration() {
            long counter = this.count.get();
            if (counter > 0) {
                return this.cumulativeDuration.get() / counter;
            }
            return 0L;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[count=").append(count()).append(", averageDuration=").append(averageDuration()).append("]");
            return builder.toString();
        }
    }
}
