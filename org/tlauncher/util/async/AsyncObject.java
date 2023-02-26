package org.tlauncher.util.async;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/AsyncObject.class */
public abstract class AsyncObject<E> extends ExtendedThread {
    private boolean gotValue;
    private E value;
    private AsyncObjectGotErrorException error;

    protected abstract E execute() throws AsyncObjectGotErrorException;

    /* JADX INFO: Access modifiers changed from: protected */
    public AsyncObject() {
        super("AsyncObject");
    }

    @Override // org.tlauncher.util.async.ExtendedThread, java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.value = execute();
            this.gotValue = true;
        } catch (Throwable e) {
            this.error = new AsyncObjectGotErrorException(this, e);
        }
    }

    public E getValue() throws AsyncObjectNotReadyException, AsyncObjectGotErrorException {
        if (this.error != null) {
            throw this.error;
        }
        if (!this.gotValue) {
            throw new AsyncObjectNotReadyException();
        }
        return this.value;
    }

    public AsyncObjectGotErrorException getError() {
        return this.error;
    }
}
