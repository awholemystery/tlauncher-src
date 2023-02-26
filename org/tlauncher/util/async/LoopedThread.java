package org.tlauncher.util.async;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/LoopedThread.class */
public abstract class LoopedThread extends ExtendedThread {
    protected static final String LOOPED_BLOCK = "iteration";

    protected abstract void iterateOnce();

    public LoopedThread(String name) {
        super(name);
    }

    public LoopedThread() {
        this("LoopedThread");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.util.async.ExtendedThread
    public final void lockThread(String reason) {
        if (reason == null) {
            throw new NullPointerException();
        }
        if (!reason.equals(LOOPED_BLOCK)) {
            throw new IllegalArgumentException("Illegal block reason. Expected: iteration, got: " + reason);
        }
        super.lockThread(reason);
    }

    public final boolean isIterating() {
        return !isThreadLocked();
    }

    public final void iterate() {
        if (!isIterating()) {
            unlockThread(LOOPED_BLOCK);
        }
    }

    @Override // org.tlauncher.util.async.ExtendedThread, java.lang.Thread, java.lang.Runnable
    public final void run() {
        while (true) {
            lockThread(LOOPED_BLOCK);
            iterateOnce();
        }
    }
}
