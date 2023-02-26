package org.apache.http.impl.client;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/TargetAuthenticationStrategy.class */
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {
    public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl, org.apache.http.client.AuthenticationStrategy
    public /* bridge */ /* synthetic */ void authFailed(HttpHost x0, AuthScheme x1, HttpContext x2) {
        super.authFailed(x0, x1, x2);
    }

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl, org.apache.http.client.AuthenticationStrategy
    public /* bridge */ /* synthetic */ void authSucceeded(HttpHost x0, AuthScheme x1, HttpContext x2) {
        super.authSucceeded(x0, x1, x2);
    }

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl, org.apache.http.client.AuthenticationStrategy
    public /* bridge */ /* synthetic */ Queue select(Map x0, HttpHost x1, HttpResponse x2, HttpContext x3) throws MalformedChallengeException {
        return super.select(x0, x1, x2, x3);
    }

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl, org.apache.http.client.AuthenticationStrategy
    public /* bridge */ /* synthetic */ Map getChallenges(HttpHost x0, HttpResponse x1, HttpContext x2) throws MalformedChallengeException {
        return super.getChallenges(x0, x1, x2);
    }

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl, org.apache.http.client.AuthenticationStrategy
    public /* bridge */ /* synthetic */ boolean isAuthenticationRequested(HttpHost x0, HttpResponse x1, HttpContext x2) {
        return super.isAuthenticationRequested(x0, x1, x2);
    }

    public TargetAuthenticationStrategy() {
        super(HttpStatus.SC_UNAUTHORIZED, "WWW-Authenticate");
    }

    @Override // org.apache.http.impl.client.AuthenticationStrategyImpl
    Collection<String> getPreferredAuthSchemes(RequestConfig config) {
        return config.getTargetPreferredAuthSchemes();
    }
}
