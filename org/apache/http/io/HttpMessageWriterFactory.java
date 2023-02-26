package org.apache.http.io;

import org.apache.http.HttpMessage;

/* loaded from: TLauncher-2.876.jar:org/apache/http/io/HttpMessageWriterFactory.class */
public interface HttpMessageWriterFactory<T extends HttpMessage> {
    HttpMessageWriter<T> create(SessionOutputBuffer sessionOutputBuffer);
}
