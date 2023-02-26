package org.tlauncher.tlauncher.minecraft.user.mcsauth;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.GsonParser;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.Parser;
import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/mcsauth/MinecraftServicesAuthenticator.class */
public class MinecraftServicesAuthenticator extends RequestAndParseStrategy<XboxServiceAuthenticationResponse, MinecraftServicesToken> {
    private static final Logger LOGGER = LogManager.getLogger(MinecraftServicesAuthenticator.class);

    public MinecraftServicesAuthenticator() {
        this(new HttpClientRequester(r -> {
            return Request.Post("https://api.minecraftservices.com/authentication/login_with_xbox").bodyString(String.format(Locale.ROOT, "{\"identityToken\":\"XBL3.0 x=%s;%s\"}", r.getUHS(), r.getToken()), ContentType.APPLICATION_JSON);
        }));
    }

    MinecraftServicesAuthenticator(Requester<XboxServiceAuthenticationResponse> requester) {
        this(requester, GsonParser.lowerCaseWithUnderscores(MinecraftServicesToken.class));
    }

    MinecraftServicesAuthenticator(Requester<XboxServiceAuthenticationResponse> requester, Parser<MinecraftServicesToken> parser) {
        super(LOGGER, requester, parser);
    }

    public MinecraftServicesToken minecraftServicesAuthenticate(XboxServiceAuthenticationResponse xstsResponse) throws MinecraftServicesAuthenticationException, IOException {
        try {
            return requestAndParse(xstsResponse);
        } catch (InvalidResponseException e) {
            throw new MinecraftServicesAuthenticationException(e);
        }
    }
}
