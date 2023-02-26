package org.tlauncher.tlauncher.minecraft.user.xb.xsts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.Locale;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.InvalidStatusCodeException;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthStrategy;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/xsts/XSTSAuthenticator.class */
public class XSTSAuthenticator extends XboxServiceAuthStrategy {
    private static final Logger LOGGER = LogManager.getLogger(XSTSAuthenticator.class);

    public XSTSAuthenticator() {
        super(LOGGER, new HttpClientRequester(xboxLiveToken -> {
            return Request.Post("https://xsts.auth.xboxlive.com/xsts/authorize").bodyString(String.format(Locale.ROOT, "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}", xboxLiveToken), ContentType.APPLICATION_JSON);
        }));
    }

    XSTSAuthenticator(Requester<String> requester) {
        super(LOGGER, requester);
    }

    private static XSTSAuthenticationException parseXErr(JsonObject response) {
        if (response.has("XErr")) {
            JsonElement xErr = response.get("XErr");
            if (xErr instanceof JsonPrimitive) {
                String code = xErr.getAsString();
                boolean z = true;
                switch (code.hashCode()) {
                    case 664499191:
                        if (code.equals("2148916233")) {
                            z = false;
                            break;
                        }
                        break;
                    case 664499196:
                        if (code.equals("2148916238")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        return new NoXboxAccountException();
                    case true:
                        return new ChildAccountException();
                    default:
                        return null;
                }
            }
            return null;
        }
        return null;
    }

    public XboxServiceAuthenticationResponse xstsAuthenticate(String xboxLiveToken) throws XSTSAuthenticationException, IOException {
        JsonObject response;
        XSTSAuthenticationException e1;
        try {
            return requestAndParse(xboxLiveToken);
        } catch (InvalidResponseException e) {
            if ((e instanceof InvalidStatusCodeException) && ((InvalidStatusCodeException) e).getStatusCode() == 401 && (response = e.getResponseAsJson()) != null && (e1 = parseXErr(response)) != null) {
                throw e1;
            }
            throw new XSTSAuthenticationException(e);
        }
    }
}
