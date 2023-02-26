package org.tlauncher.tlauncher.minecraft.user;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
import org.tlauncher.util.StringUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/LoginCredentials.class */
public class LoginCredentials {
    private final String username;
    private final String session;
    private final String accessToken;
    private final String playerName;
    private final String userType;
    private final String profileName;
    private final String properties;
    private final UUID uuid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoginCredentials(String username, String accessToken, String properties, String playerName, UUID uuid, String userType, String profileName) {
        this.username = StringUtil.requireNotBlank(username, "username");
        this.accessToken = StringUtil.requireNotBlank(accessToken, "accessToken");
        this.properties = StringUtils.isBlank(properties) ? "{}" : properties;
        this.playerName = StringUtil.requireNotBlank(playerName, "playerName");
        this.uuid = uuid;
        this.userType = StringUtil.requireNotBlank(userType, "userType");
        this.profileName = StringUtil.requireNotBlank(profileName, "profileName");
        this.session = String.format(Locale.ROOT, "token:%s:%s", accessToken, UUIDTypeAdapter.fromUUID(uuid));
    }

    public String getUsername() {
        return this.username;
    }

    public String getSession() {
        return this.session;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getUserType() {
        return this.userType;
    }

    public String getProfileName() {
        return this.profileName;
    }

    public String getProperties() {
        return this.properties;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public LinkedHashMap<String, String> map() {
        return new LinkedHashMap<String, String>() { // from class: org.tlauncher.tlauncher.minecraft.user.LoginCredentials.1
        };
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("session", this.session).append("accessToken", this.accessToken).append("playerName", this.playerName).append("userType", this.userType).append("profileName", this.profileName).append("properties", this.properties).append("uuid", this.uuid).build();
    }
}
