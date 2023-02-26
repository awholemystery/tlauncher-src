package org.tlauncher.tlauncher.minecraft.user.gos;

import java.io.IOException;
import java.util.List;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.user.GsonParser;
import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
import org.tlauncher.tlauncher.minecraft.user.Parser;
import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
import org.tlauncher.tlauncher.minecraft.user.Requester;
import org.tlauncher.tlauncher.minecraft.user.gos.MinecraftUserGameOwnershipResponse;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/gos/GameOwnershipValidator.class */
public class GameOwnershipValidator extends RequestAndParseStrategy<MinecraftServicesToken, MinecraftUserGameOwnershipResponse> {
    private static final Logger LOGGER = LogManager.getLogger(GameOwnershipValidator.class);

    public GameOwnershipValidator() {
        this(new HttpClientRequester(token -> {
            return Request.Get("https://api.minecraftservices.com/entitlements/mcstore").addHeader("Authorization", "Bearer " + token.getAccessToken());
        }));
    }

    GameOwnershipValidator(Requester<MinecraftServicesToken> requester) {
        this(requester, GsonParser.defaultParser(MinecraftUserGameOwnershipResponse.class));
    }

    GameOwnershipValidator(Requester<MinecraftServicesToken> requester, Parser<MinecraftUserGameOwnershipResponse> parser) {
        super(LOGGER, requester, parser);
    }

    public void checkGameOwnership(MinecraftServicesToken token) throws GameOwnershipValidationException, IOException {
        try {
            MinecraftUserGameOwnershipResponse response = requestAndParse(token);
            List<MinecraftUserGameOwnershipResponse.Item> items = response.getItems();
            if (items.isEmpty()) {
                throw new GameOwnershipValidationException("no ownership found");
            }
            if (items.stream().noneMatch(item -> {
                return "product_minecraft".equals(item.getName());
            })) {
                throw new GameOwnershipValidationException("no \"product_minecraft\"");
            }
            if (items.stream().noneMatch(item2 -> {
                return "game_minecraft".equals(item2.getName());
            })) {
                throw new GameOwnershipValidationException("no \"game_minecraft\"");
            }
        } catch (InvalidResponseException e) {
            throw new GameOwnershipValidationException(e);
        }
    }
}
