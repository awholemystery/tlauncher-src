package org.tlauncher.tlauncher.minecraft.auth;

import java.io.IOException;
import java.util.Objects;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthExchangeCode;
import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
import org.tlauncher.tlauncher.minecraft.user.MinecraftOAuthAuthenticate;
import org.tlauncher.tlauncher.minecraft.user.MinecraftProfileConverter;
import org.tlauncher.tlauncher.minecraft.user.MinecraftUser;
import org.tlauncher.tlauncher.minecraft.user.RedirectUrl;
import org.tlauncher.tlauncher.minecraft.user.gos.GameOwnershipValidator;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesAuthenticator;
import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchanger;
import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchangerImpl;
import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthRefreshCodeExchangerIMpl;
import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftProfileRequester;
import org.tlauncher.tlauncher.minecraft.user.xb.auth.XboxLiveAuthenticator;
import org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticator;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/MicrosoftAuthenticator.class */
public class MicrosoftAuthenticator extends Authenticator {
    public static final OAuthApplication microsoftAuth = OAuthApplication.TLAUNCHER_PARAMETERS;

    public MicrosoftAuthenticator(Account account) {
        super(account);
    }

    @Override // org.tlauncher.tlauncher.minecraft.auth.Authenticator
    protected void pass() throws AuthenticatorException {
        try {
            if (Objects.isNull(this.account.getAccessToken())) {
                MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger = new MicrosoftOAuthCodeExchangerImpl(microsoftAuth.getClientId(), microsoftAuth.getTokenURL());
                MicrosoftOAuthExchangeCode o = new MicrosoftOAuthExchangeCode(this.account.getPassword(), new RedirectUrl(microsoftAuth.getRedirectURL()));
                doRequest(microsoftOAuthCodeExchanger, o);
            } else if (this.account.getMicrosoftOAuthToken().isExpired()) {
                MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger2 = new MicrosoftOAuthRefreshCodeExchangerIMpl(microsoftAuth.getClientId(), microsoftAuth.getTokenURL());
                MicrosoftOAuthExchangeCode o2 = new MicrosoftOAuthExchangeCode(this.account.getMicrosoftOAuthToken().getRefreshToken(), new RedirectUrl(microsoftAuth.getRedirectURL()));
                doRequest(microsoftOAuthCodeExchanger2, o2);
            }
        } catch (IOException | MinecraftAuthenticationException e) {
            U.log(e);
            throw new AuthenticatorException(e);
        }
    }

    private void doRequest(MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger, MicrosoftOAuthExchangeCode payload) throws IOException, MinecraftAuthenticationException {
        XboxLiveAuthenticator xboxLiveAuthenticator = new XboxLiveAuthenticator(microsoftAuth);
        MinecraftOAuthAuthenticate minecraftOAuthAuthenticate = new MinecraftOAuthAuthenticate(microsoftOAuthCodeExchanger, xboxLiveAuthenticator, new XSTSAuthenticator(), new MinecraftServicesAuthenticator(), new GameOwnershipValidator(), new MinecraftProfileRequester(), new MinecraftProfileConverter());
        MinecraftUser user = minecraftOAuthAuthenticate.authenticate(payload);
        this.account.setUUID(UUIDTypeAdapter.toUUID(user.getUUID().toString()));
        this.account.setDisplayName(user.getDisplayName());
        this.account.setType(Account.AccountType.MICROSOFT);
        this.account.setMicrosoftOAuthToken(user.getMicrosoftToken());
        this.account.setMinecraftServicesToken(user.getMinecraftToken());
        this.account.setPassword(null);
    }
}
