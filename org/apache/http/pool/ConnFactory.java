package org.apache.http.pool;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/pool/ConnFactory.class */
public interface ConnFactory<T, C> {
    C create(T t) throws IOException;
}
