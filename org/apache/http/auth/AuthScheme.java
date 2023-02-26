package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/AuthScheme.class */
public interface AuthScheme {
    void processChallenge(Header header) throws MalformedChallengeException;

    String getSchemeName();

    String getParameter(String str);

    String getRealm();

    boolean isConnectionBased();

    boolean isComplete();

    @Deprecated
    Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException;
}
