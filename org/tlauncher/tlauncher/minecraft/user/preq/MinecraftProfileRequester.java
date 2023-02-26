package org.tlauncher.tlauncher.minecraft.user.preq;

import java.io.IOException;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.GsonParser;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.Parser;
import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/preq/MinecraftProfileRequester.class */
public class MinecraftProfileRequester extends RequestAndParseStrategy<MinecraftServicesToken, MinecraftOAuthProfile> {
    private static final Logger LOGGER = LogManager.getLogger(MinecraftProfileRequester.class);

    public MinecraftProfileRequester() {
        this(new HttpClientRequester(token -> {
            return Request.Get("https://api.minecraftservices.com/minecraft/profile").addHeader("Authorization", "Bearer " + token.getAccessToken());
        }));
    }

    MinecraftProfileRequester(Requester<MinecraftServicesToken> requester) {
        this(requester, GsonParser.withDashlessUUIDAdapter(MinecraftOAuthProfile.class));
    }

    MinecraftProfileRequester(Requester<MinecraftServicesToken> requester, Parser<MinecraftOAuthProfile> parser) {
        super(LOGGER, requester, parser);
    }

    public MinecraftOAuthProfile requestProfile(MinecraftServicesToken token) throws MinecraftProfileRequestException, IOException {
        try {
            return requestAndParse(token);
        } catch (InvalidResponseException e) {
            throw new MinecraftProfileRequestException(e);
        }
    }
}
