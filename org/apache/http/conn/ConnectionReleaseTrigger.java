package org.apache.http.conn;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/ConnectionReleaseTrigger.class */
public interface ConnectionReleaseTrigger {
    void releaseConnection() throws IOException;

    void abortConnection() throws IOException;
}
