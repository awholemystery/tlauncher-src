package org.apache.http.concurrent;

/* loaded from: TLauncher-2.876.jar:org/apache/http/concurrent/FutureCallback.class */
public interface FutureCallback<T> {
    void completed(T t);

    void failed(Exception exc);

    void cancelled();
}
