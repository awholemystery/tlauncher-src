package org.tlauncher.tlauncher.minecraft.user;

import java.io.IOException;
import org.tlauncher.tlauncher.minecraft.user.gos.GameOwnershipValidator;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesAuthenticator;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchanger;
import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftOAuthProfile;
import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftProfileRequester;
import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;
import org.tlauncher.tlauncher.minecraft.user.xb.auth.XboxLiveAuthenticator;
import org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticator;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MinecraftOAuthAuthenticate.class */
public class MinecraftOAuthAuthenticate {
    private final MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger;
    private final XboxLiveAuthenticator xboxLiveAuthenticator;
    private final XSTSAuthenticator xstsAuthenticator;
    private final MinecraftServicesAuthenticator minecraftServicesAuthenticator;
    private final GameOwnershipValidator gameOwnershipValidator;
    private final MinecraftProfileRequester minecraftProfileRequester;
    private final MinecraftProfileConverter minecraftProfileConverter;

    public MinecraftOAuthAuthenticate(MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger, XboxLiveAuthenticator xboxLiveAuthenticator, XSTSAuthenticator xstsAuthenticator, MinecraftServicesAuthenticator minecraftServicesAuthenticator, GameOwnershipValidator gameOwnershipValidator, MinecraftProfileRequester minecraftProfileRequester, MinecraftProfileConverter minecraftProfileConverter) {
        this.microsoftOAuthCodeExchanger = microsoftOAuthCodeExchanger;
        this.xboxLiveAuthenticator = xboxLiveAuthenticator;
        this.xstsAuthenticator = xstsAuthenticator;
        this.minecraftServicesAuthenticator = minecraftServicesAuthenticator;
        this.gameOwnershipValidator = gameOwnershipValidator;
        this.minecraftProfileRequester = minecraftProfileRequester;
        this.minecraftProfileConverter = minecraftProfileConverter;
    }

    public MinecraftUser authenticate(MicrosoftOAuthExchangeCode code) throws MinecraftAuthenticationException, IOException {
        MicrosoftOAuthToken oaex = this.microsoftOAuthCodeExchanger.exchangeMicrosoftOAuthCode(code);
        XboxServiceAuthenticationResponse xbAuth = this.xboxLiveAuthenticator.xboxLiveAuthenticate(oaex.getAccessToken());
        XboxServiceAuthenticationResponse xbXsts = this.xstsAuthenticator.xstsAuthenticate(xbAuth.getToken());
        MinecraftServicesToken mcsToken = this.minecraftServicesAuthenticator.minecraftServicesAuthenticate(xbXsts);
        this.gameOwnershipValidator.checkGameOwnership(mcsToken);
        MinecraftOAuthProfile preq = this.minecraftProfileRequester.requestProfile(mcsToken);
        return this.minecraftProfileConverter.convertToMinecraftUser(oaex, mcsToken, preq);
    }
}
