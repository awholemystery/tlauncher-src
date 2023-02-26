package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/EofSensorWatcher.class */
public interface EofSensorWatcher {
    boolean eofDetected(InputStream inputStream) throws IOException;

    boolean streamClosed(InputStream inputStream) throws IOException;

    boolean streamAbort(InputStream inputStream) throws IOException;
}
