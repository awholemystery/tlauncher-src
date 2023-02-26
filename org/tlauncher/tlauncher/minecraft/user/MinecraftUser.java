package org.tlauncher.tlauncher.minecraft.user;

import java.util.Objects;
import java.util.UUID;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftOAuthProfile;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MinecraftUser.class */
public class MinecraftUser extends User {
    public static final String TYPE = "minecraft";
    private MinecraftOAuthProfile profile;
    private MicrosoftOAuthToken microsoftToken;
    private MinecraftServicesToken minecraftToken;

    public MinecraftUser(MinecraftOAuthProfile profile, MicrosoftOAuthToken microsoftToken, MinecraftServicesToken minecraftToken) {
        setPayload(profile, microsoftToken, minecraftToken);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public String getUsername() {
        return this.profile.getName();
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public String getDisplayName() {
        return this.profile.getName();
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public UUID getUUID() {
        return this.profile.getId();
    }

    public MicrosoftOAuthToken getMicrosoftToken() {
        return this.microsoftToken;
    }

    void setMicrosoftToken(MicrosoftOAuthToken microsoftToken) {
        this.microsoftToken = (MicrosoftOAuthToken) Objects.requireNonNull(microsoftToken, "microsoftToken");
    }

    public MinecraftServicesToken getMinecraftToken() {
        return this.minecraftToken;
    }

    void setMinecraftToken(MinecraftServicesToken minecraftToken) {
        this.minecraftToken = (MinecraftServicesToken) Objects.requireNonNull(minecraftToken, "minecraftToken");
    }

    void setPayload(MinecraftOAuthProfile profile, MicrosoftOAuthToken microsoftToken, MinecraftServicesToken minecraftToken) {
        setProfile(profile);
        setMicrosoftToken(microsoftToken);
        setMinecraftToken(minecraftToken);
    }

    void setProfile(MinecraftOAuthProfile profile) {
        this.profile = (MinecraftOAuthProfile) Objects.requireNonNull(profile, "profile");
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public String getType() {
        return TYPE;
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    protected boolean equals(User user) {
        return user != null && this.profile.getId().equals(((MinecraftUser) user).profile.getId());
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public int hashCode() {
        int result = this.profile.hashCode();
        return (31 * ((31 * result) + this.microsoftToken.hashCode())) + this.minecraftToken.hashCode();
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.User
    public LoginCredentials getLoginCredentials() {
        return new LoginCredentials(this.profile.getName(), this.minecraftToken.getAccessToken(), "{}", this.profile.getName(), this.profile.getId(), "mojang", this.profile.getName());
    }
}
