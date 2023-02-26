package org.tlauncher.util.async;

import java.util.concurrent.atomic.AtomicInteger;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/ExtendedThread.class */
public abstract class ExtendedThread extends Thread {
    private static AtomicInteger threadNum = new AtomicInteger();
    private final ExtendedThreadCaller caller;
    private final Object monitor;
    private volatile String blockReason;

    @Override // java.lang.Thread, java.lang.Runnable
    public abstract void run();

    public ExtendedThread(String name) {
        super(name + "#" + threadNum.incrementAndGet());
        this.monitor = new Object();
        this.caller = new ExtendedThreadCaller();
    }

    public ExtendedThread() {
        this("ExtendedThread");
    }

    public ExtendedThreadCaller getCaller() {
        return this.caller;
    }

    public void startAndWait() {
        super.start();
        while (!isThreadLocked()) {
            U.sleepFor(100L);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void lockThread(String reason) {
        if (reason == null) {
            throw new NullPointerException();
        }
        checkCurrent();
        this.blockReason = reason;
        synchronized (this.monitor) {
            while (this.blockReason != null) {
                try {
                    this.monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unlockThread(String reason) {
        if (reason == null) {
            throw new NullPointerException();
        }
        if (!reason.equals(this.blockReason)) {
            throw new IllegalStateException("Unlocking denied! Locked with: " + this.blockReason + ", tried to unlock with: " + reason);
        }
        this.blockReason = null;
        synchronized (this.monitor) {
            this.monitor.notifyAll();
        }
    }

    public void tryUnlock(String reason) {
        if (reason == null) {
            throw new NullPointerException();
        }
        if (reason.equals(this.blockReason)) {
            unlockThread(reason);
        }
    }

    public boolean isThreadLocked() {
        return this.blockReason != null;
    }

    public boolean isCurrent() {
        return Thread.currentThread().equals(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkCurrent() {
        if (!isCurrent()) {
            throw new IllegalStateException("Illegal thread!");
        }
    }

    protected void threadLog(Object... o) {
        U.log("[" + getName() + "]", o);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/ExtendedThread$ExtendedThreadCaller.class */
    public class ExtendedThreadCaller extends RuntimeException {
        private ExtendedThreadCaller() {
        }
    }
}
