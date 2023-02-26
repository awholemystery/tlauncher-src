package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/BasicResponseHandler.class */
public class BasicResponseHandler extends AbstractResponseHandler<String> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.http.impl.client.AbstractResponseHandler
    public String handleEntity(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }

    @Override // org.apache.http.impl.client.AbstractResponseHandler, org.apache.http.client.ResponseHandler
    public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        return (String) super.handleResponse(response);
    }
}
