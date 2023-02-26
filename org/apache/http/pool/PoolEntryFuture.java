package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;

/* JADX INFO: Access modifiers changed from: package-private */
@ThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/pool/PoolEntryFuture.class */
public abstract class PoolEntryFuture<T> implements Future<T> {
    private final Lock lock;
    private final FutureCallback<T> callback;
    private final Condition condition;
    private volatile boolean cancelled;
    private volatile boolean completed;
    private T result;

    protected abstract T getPoolEntry(long j, TimeUnit timeUnit) throws IOException, InterruptedException, TimeoutException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolEntryFuture(Lock lock, FutureCallback<T> callback) {
        this.lock = lock;
        this.condition = lock.newCondition();
        this.callback = callback;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.lock.lock();
        try {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;
            if (this.callback != null) {
                this.callback.cancelled();
            }
            this.condition.signalAll();
            this.lock.unlock();
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.completed;
    }

    @Override // java.util.concurrent.Future
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(0L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            throw new ExecutionException(ex);
        }
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Args.notNull(unit, "Time unit");
        this.lock.lock();
        try {
            try {
                if (this.completed) {
                    T t = this.result;
                    this.lock.unlock();
                    return t;
                }
                this.result = getPoolEntry(timeout, unit);
                this.completed = true;
                if (this.callback != null) {
                    this.callback.completed(this.result);
                }
                T t2 = this.result;
                this.lock.unlock();
                return t2;
            } catch (IOException ex) {
                this.completed = true;
                this.result = null;
                if (this.callback != null) {
                    this.callback.failed(ex);
                }
                throw new ExecutionException(ex);
            }
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    public boolean await(Date deadline) throws InterruptedException {
        boolean success;
        this.lock.lock();
        try {
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            if (deadline != null) {
                success = this.condition.awaitUntil(deadline);
            } else {
                this.condition.await();
                success = true;
            }
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            return success;
        } finally {
            this.lock.unlock();
        }
    }

    public void wakeup() {
        this.lock.lock();
        try {
            this.condition.signalAll();
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }
}
