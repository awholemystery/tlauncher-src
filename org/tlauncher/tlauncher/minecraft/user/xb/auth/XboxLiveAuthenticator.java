package org.tlauncher.tlauncher.minecraft.user.xb.auth;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.util.Locale;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthStrategy;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/auth/XboxLiveAuthenticator.class */
public class XboxLiveAuthenticator extends XboxServiceAuthStrategy {
    private static final Logger LOGGER = LogManager.getLogger(XboxLiveAuthenticator.class);

    public XboxLiveAuthenticator(OAuthApplication application) {
        super(LOGGER, new HttpClientRequester(accessToken -> {
            Request Post = Request.Post("https://user.auth.xboxlive.com/user/authenticate");
            Locale locale = Locale.ROOT;
            Object[] objArr = new Object[1];
            objArr[0] = (application.isUseWeirdXboxTokenPrefix() ? "d=" : CoreConstants.EMPTY_STRING) + accessToken;
            return Post.bodyString(String.format(locale, "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", objArr), ContentType.APPLICATION_JSON);
        }));
    }

    XboxLiveAuthenticator(Requester<String> requester) {
        super(LOGGER, requester);
    }

    public XboxServiceAuthenticationResponse xboxLiveAuthenticate(String accessToken) throws XboxLiveAuthenticationException, IOException {
        try {
            return requestAndParse(accessToken);
        } catch (InvalidResponseException e) {
            throw new XboxLiveAuthenticationException(e);
        }
    }

    public XboxServiceAuthenticationResponse xboxLiveAuthenticate(MicrosoftOAuthToken token) throws XboxLiveAuthenticationException, IOException {
        return xboxLiveAuthenticate(token.getAccessToken());
    }
}
