package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.GsonParser;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/oauth/exchange/MicrosoftOAuthCodeExchangerImpl.class */
public class MicrosoftOAuthCodeExchangerImpl extends MicrosoftOAuthCodeExchanger {
    private static final Logger LOGGER = LogManager.getLogger(MicrosoftOAuthCodeExchangerImpl.class);

    public MicrosoftOAuthCodeExchangerImpl(String clientId, String url) {
        super(new HttpClientRequester(code -> {
            return Request.Post(url).bodyForm(Form.form().add("client_id", clientId).add("code", code.getCode()).add("grant_type", "authorization_code").add("redirect_uri", code.getRedirectUrl().getUrl().toString()).build());
        }), GsonParser.lowerCaseWithUnderscores(MicrosoftOAuthToken.class));
    }
}
