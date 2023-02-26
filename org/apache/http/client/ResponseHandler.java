package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/ResponseHandler.class */
public interface ResponseHandler<T> {
    T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException;
}
