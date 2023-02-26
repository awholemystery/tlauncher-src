package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthExchangeCode;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
import org.tlauncher.tlauncher.minecraft.user.Parser;
import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
import org.tlauncher.tlauncher.minecraft.user.Requester;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/oauth/exchange/MicrosoftOAuthCodeExchanger.class */
public class MicrosoftOAuthCodeExchanger extends RequestAndParseStrategy<MicrosoftOAuthExchangeCode, MicrosoftOAuthToken> {
    private static final Logger LOGGER = LogManager.getLogger(MicrosoftOAuthCodeExchanger.class);

    public MicrosoftOAuthCodeExchanger(Requester<MicrosoftOAuthExchangeCode> requester, Parser<MicrosoftOAuthToken> parser) {
        super(LOGGER, requester, parser);
    }

    public MicrosoftOAuthToken exchangeMicrosoftOAuthCode(MicrosoftOAuthExchangeCode payload) throws MicrosoftOAuthCodeExchangeException, IOException {
        try {
            return requestAndParse(payload);
        } catch (InvalidResponseException e) {
            throw new MicrosoftOAuthCodeExchangeException(e);
        }
    }
}
