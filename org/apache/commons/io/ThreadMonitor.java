package org.apache.commons.io;

import java.time.Duration;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/ThreadMonitor.class */
class ThreadMonitor implements Runnable {
    private final Thread thread;
    private final Duration timeout;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Thread start(Duration timeout) {
        return start(Thread.currentThread(), timeout);
    }

    static Thread start(Thread thread, Duration timeout) {
        if (timeout.isZero() || timeout.isNegative()) {
            return null;
        }
        ThreadMonitor timout = new ThreadMonitor(thread, timeout);
        Thread monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
        monitor.setDaemon(true);
        monitor.start();
        return monitor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void stop(Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private ThreadMonitor(Thread thread, Duration timeout) {
        this.thread = thread;
        this.timeout = timeout;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            sleep(this.timeout);
            this.thread.interrupt();
        } catch (InterruptedException e) {
        }
    }

    private static void sleep(Duration duration) throws InterruptedException {
        long millis = duration.toMillis();
        long finishAtMillis = System.currentTimeMillis() + millis;
        long remainingMillis = millis;
        do {
            Thread.sleep(remainingMillis);
            remainingMillis = finishAtMillis - System.currentTimeMillis();
        } while (remainingMillis > 0);
    }
}
