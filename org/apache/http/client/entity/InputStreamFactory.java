package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/entity/InputStreamFactory.class */
public interface InputStreamFactory {
    InputStream create(InputStream inputStream) throws IOException;
}
