package org.tlauncher.util.async;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/RunnableThread.class */
public class RunnableThread extends ExtendedThread {
    private final Runnable r;

    public RunnableThread(Runnable r) {
        if (r == null) {
            throw new NullPointerException();
        }
        this.r = r;
    }

    @Override // org.tlauncher.util.async.ExtendedThread, java.lang.Thread, java.lang.Runnable
    public void run() {
        this.r.run();
    }
}
