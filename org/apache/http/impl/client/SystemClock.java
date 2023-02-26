package org.apache.http.impl.client;

/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/SystemClock.class */
class SystemClock implements Clock {
    @Override // org.apache.http.impl.client.Clock
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
