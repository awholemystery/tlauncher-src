package org.apache.http.client.methods;

import org.apache.http.concurrent.Cancellable;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/methods/HttpExecutionAware.class */
public interface HttpExecutionAware {
    boolean isAborted();

    void setCancellable(Cancellable cancellable);
}
