package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/io/SocketOutputBuffer.class */
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        int n = buffersize;
        n = n < 0 ? socket.getSendBufferSize() : n;
        init(socket.getOutputStream(), n < 1024 ? 1024 : n, params);
    }
}
