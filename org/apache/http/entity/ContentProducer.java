package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/http/entity/ContentProducer.class */
public interface ContentProducer {
    void writeTo(OutputStream outputStream) throws IOException;
}
