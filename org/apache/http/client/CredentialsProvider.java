package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/CredentialsProvider.class */
public interface CredentialsProvider {
    void setCredentials(AuthScope authScope, Credentials credentials);

    Credentials getCredentials(AuthScope authScope);

    void clear();
}
