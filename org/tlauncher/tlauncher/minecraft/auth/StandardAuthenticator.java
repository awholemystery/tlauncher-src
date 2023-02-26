package org.tlauncher.tlauncher.minecraft.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.launcher.Http;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.entity.auth.AuthenticationRequest;
import org.tlauncher.tlauncher.entity.auth.AuthenticationResponse;
import org.tlauncher.tlauncher.entity.auth.RefreshRequest;
import org.tlauncher.tlauncher.entity.auth.RefreshResponse;
import org.tlauncher.tlauncher.entity.auth.Request;
import org.tlauncher.tlauncher.entity.auth.Response;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
import org.tlauncher.tlauncher.exceptions.auth.BlockedUserException;
import org.tlauncher.tlauncher.exceptions.auth.InvalidCredentialsException;
import org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException;
import org.tlauncher.tlauncher.exceptions.auth.UserMigratedException;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/StandardAuthenticator.class */
public class StandardAuthenticator extends Authenticator {
    protected final Gson gson;
    private final URL AUTHENTICATE_URL;
    private final URL REFRESH_URL;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StandardAuthenticator(Account account, String authUrl, String refreshUrl) {
        super(account);
        this.gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
        this.AUTHENTICATE_URL = Http.constantURL(authUrl);
        this.REFRESH_URL = Http.constantURL(refreshUrl);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.minecraft.auth.Authenticator
    public void pass() throws AuthenticatorException {
        if (this.account.isFree()) {
            throw new IllegalArgumentException("invalid account type");
        }
        if (this.account.getPassword() == null && this.account.getAccessToken() == null) {
            throw new AuthenticatorException(new NullPointerException("password/accessToken"));
        }
        log("Staring to authenticate:", this.account);
        log("hasUsername:", this.account.getUsername());
        Object[] objArr = new Object[2];
        objArr[0] = "hasPassword:";
        objArr[1] = Boolean.valueOf(this.account.getPassword() != null);
        log(objArr);
        Object[] objArr2 = new Object[2];
        objArr2[0] = "hasAccessToken:";
        objArr2[1] = Boolean.valueOf(this.account.getAccessToken() != null);
        log(objArr2);
        if (this.account.getPassword() == null) {
            validateToken();
        } else {
            passwordLogin();
        }
        log("Log in successful!");
        Object[] objArr3 = new Object[2];
        objArr3[0] = "hasUUID:";
        objArr3[1] = Boolean.valueOf(this.account.getUUID() != null);
        log(objArr3);
        Object[] objArr4 = new Object[2];
        objArr4[0] = "hasAccessToken:";
        objArr4[1] = Boolean.valueOf(this.account.getAccessToken() != null);
        log(objArr4);
        Object[] objArr5 = new Object[2];
        objArr5[0] = "hasProfiles:";
        objArr5[1] = Boolean.valueOf(this.account.getProfiles() != null);
        log(objArr5);
        Object[] objArr6 = new Object[2];
        objArr6[0] = "hasProfile:";
        objArr6[1] = Boolean.valueOf(this.account.getProfiles() != null);
        log(objArr6);
        Object[] objArr7 = new Object[2];
        objArr7[0] = "hasProperties:";
        objArr7[1] = Boolean.valueOf(this.account.getProperties() != null);
        log(objArr7);
    }

    private void passwordLogin() throws AuthenticatorException {
        log("Loggining in with password");
        AuthenticationRequest request = new AuthenticationRequest(this);
        try {
            AuthenticationResponse response = (AuthenticationResponse) makeRequest(this.AUTHENTICATE_URL, request, AuthenticationResponse.class);
            this.account.setPassword(null);
            this.account.setUserID(response.getUserId() != null ? response.getUserId() : this.account.getUsername());
            this.account.setAccessToken(response.getAccessToken());
            this.account.setProfiles(response.getAvailableProfiles());
            this.account.setProfile(response.getSelectedProfile());
            this.account.setUser(response.getUser());
            this.account.setUUID(request.getClientToken());
            if (response.getSelectedProfile() != null) {
                this.account.setUUID(response.getSelectedProfile().getId());
                this.account.setDisplayName(response.getSelectedProfile().getName());
            }
            if (Account.AccountType.MOJANG.equals(this.account.getType())) {
                Alert.showLocWarning(null, Localizable.get("auth.warn.default.auth"), null);
            }
        } catch (InvalidCredentialsException e) {
            throw new AuthenticatorException("Invalid user or password", "restore.on.site." + this.account.getType().toString().toLowerCase(Locale.ROOT));
        }
    }

    private void validateToken() throws AuthenticatorException {
        log("Loggining in with token");
        RefreshRequest request = new RefreshRequest(this);
        try {
            RefreshResponse response = (RefreshResponse) makeRequest(this.REFRESH_URL, request, RefreshResponse.class);
            this.account.setAccessToken(response.getAccessToken());
            if (StringUtils.isNotBlank(response.getAccessToken())) {
                if (this instanceof TlauncherAuthenticator) {
                    this.account.setType(Account.AccountType.TLAUNCHER);
                } else {
                    this.account.setType(Account.AccountType.MOJANG);
                }
            } else {
                this.account.setType(Account.AccountType.FREE);
            }
            this.account.setUser(response.getUser());
            this.account.setProfile(response.getSelectedProfile());
            this.account.setUser(response.getUser());
        } catch (InvalidCredentialsException e) {
            throw new NotCorrectTokenOrIdException();
        }
    }

    private <T extends Response> T makeRequest(URL url, Request input, Class<T> classOfT) throws AuthenticatorException {
        String jsonResult;
        if (url == null) {
            throw new NullPointerException("url");
        }
        try {
            if (input == null) {
                jsonResult = Http.performGet(url);
            } else {
                jsonResult = Http.performPost(url, this.gson.toJson(input), Http.JSON_CONTENT_TYPE);
            }
            try {
                T result = (T) this.gson.fromJson(jsonResult, (Class<Object>) classOfT);
                if (result == null) {
                    return null;
                }
                if (StringUtils.isBlank(result.getError())) {
                    return result;
                }
                throw getException(result);
            } catch (RuntimeException rE) {
                throw new AuthenticatorException("Error parsing response: \"" + jsonResult + "\"", "unparseable", rE);
            }
        } catch (IOException e) {
            if (e.getMessage().contains("Server returned HTTP response code: 403")) {
                throw new InvalidCredentialsException();
            }
            throw new AuthenticatorException("Error making request, uncaught IOException", "unreachable", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AuthenticatorException getException(Response result) {
        if ("UserMigratedException".equals(result.getCause())) {
            return new UserMigratedException();
        }
        if ("ForbiddenOperationException".equals(result.getError())) {
            return new InvalidCredentialsException();
        }
        if (Objects.nonNull(result.getErrorMessage()) && result.getErrorMessage().contains("User is blocked")) {
            return new BlockedUserException(result.getCause(), result.getErrorMessage());
        }
        return new AuthenticatorException(result, "internal");
    }
}
