package org.tlauncher.tlauncher.minecraft.user.xb;

import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.GsonParser;
import org.tlauncher.tlauncher.minecraft.user.Parser;
import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/XboxServiceAuthStrategy.class */
public abstract class XboxServiceAuthStrategy extends RequestAndParseStrategy<String, XboxServiceAuthenticationResponse> {
    /* JADX INFO: Access modifiers changed from: protected */
    public XboxServiceAuthStrategy(Logger logger, Requester<String> requester) {
        this(logger, requester, createGsonParser());
    }

    protected XboxServiceAuthStrategy(Logger logger, Requester<String> requester, Parser<XboxServiceAuthenticationResponse> parser) {
        super(logger, requester, parser);
    }

    protected static GsonParser<XboxServiceAuthenticationResponse> createGsonParser() {
        return GsonParser.withDeserializer(XboxServiceAuthenticationResponse.class, new XboxServiceAuthenticationResponse.Deserializer());
    }
}
