package org.apache.http.impl.bootstrap;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/bootstrap/ThreadFactoryImpl.class */
class ThreadFactoryImpl implements ThreadFactory {
    private final String namePrefix;
    private final ThreadGroup group;
    private final AtomicLong count;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ThreadFactoryImpl(String namePrefix, ThreadGroup group) {
        this.namePrefix = namePrefix;
        this.group = group;
        this.count = new AtomicLong();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ThreadFactoryImpl(String namePrefix) {
        this(namePrefix, null);
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable target) {
        return new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
    }
}
