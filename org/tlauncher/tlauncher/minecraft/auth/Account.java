package org.tlauncher.tlauncher.minecraft.auth;

import ch.qos.logback.core.joran.action.Action;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.apache.log4j.spi.Configurator;
import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
import org.tlauncher.util.Reflect;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/Account.class */
public class Account {
    private String username;
    private String userID;
    private String displayName;
    private String password;
    private String accessToken;
    private MicrosoftOAuthToken microsoftOAuthToken;
    private MinecraftServicesToken minecraftServicesToken;
    private String uuid;
    private List<Map<String, String>> userProperties;
    private AccountType type;
    @Expose
    private boolean premiumAccount;
    private GameProfile[] profiles;
    private GameProfile selectedProfile;
    private User user;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/Account$AccountType.class */
    public enum AccountType {
        TLAUNCHER,
        MOJANG,
        FREE,
        SPECIAL,
        MICROSOFT;
        
        public static Set<AccountType> OFFICIAL_ACCOUNTS = Sets.newHashSet(new AccountType[]{MOJANG, MICROSOFT});
        public static Set<AccountType> NONE_OFFICIAL_ACCOUNTS = Sets.newHashSet(new AccountType[]{TLAUNCHER, FREE});
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Account) {
            Account other = (Account) o;
            if (other.canEqual(this)) {
                Object this$username = getUsername();
                Object other$username = other.getUsername();
                if (this$username == null) {
                    if (other$username != null) {
                        return false;
                    }
                } else if (!this$username.equals(other$username)) {
                    return false;
                }
                Object this$userID = getUserID();
                Object other$userID = other.getUserID();
                if (this$userID == null) {
                    if (other$userID != null) {
                        return false;
                    }
                } else if (!this$userID.equals(other$userID)) {
                    return false;
                }
                Object this$displayName = getDisplayName();
                Object other$displayName = other.getDisplayName();
                if (this$displayName == null) {
                    if (other$displayName != null) {
                        return false;
                    }
                } else if (!this$displayName.equals(other$displayName)) {
                    return false;
                }
                Object this$password = getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }
                Object this$accessToken = getAccessToken();
                Object other$accessToken = other.getAccessToken();
                if (this$accessToken == null) {
                    if (other$accessToken != null) {
                        return false;
                    }
                } else if (!this$accessToken.equals(other$accessToken)) {
                    return false;
                }
                Object this$microsoftOAuthToken = getMicrosoftOAuthToken();
                Object other$microsoftOAuthToken = other.getMicrosoftOAuthToken();
                if (this$microsoftOAuthToken == null) {
                    if (other$microsoftOAuthToken != null) {
                        return false;
                    }
                } else if (!this$microsoftOAuthToken.equals(other$microsoftOAuthToken)) {
                    return false;
                }
                Object this$minecraftServicesToken = getMinecraftServicesToken();
                Object other$minecraftServicesToken = other.getMinecraftServicesToken();
                if (this$minecraftServicesToken == null) {
                    if (other$minecraftServicesToken != null) {
                        return false;
                    }
                } else if (!this$minecraftServicesToken.equals(other$minecraftServicesToken)) {
                    return false;
                }
                Object this$uuid = getUUID();
                Object other$uuid = other.getUUID();
                if (this$uuid == null) {
                    if (other$uuid != null) {
                        return false;
                    }
                } else if (!this$uuid.equals(other$uuid)) {
                    return false;
                }
                Object this$userProperties = this.userProperties;
                Object other$userProperties = other.userProperties;
                if (this$userProperties == null) {
                    if (other$userProperties != null) {
                        return false;
                    }
                } else if (!this$userProperties.equals(other$userProperties)) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }
                if (isPremiumAccount() == other.isPremiumAccount() && Arrays.deepEquals(getProfiles(), other.getProfiles())) {
                    Object this$selectedProfile = this.selectedProfile;
                    Object other$selectedProfile = other.selectedProfile;
                    if (this$selectedProfile == null) {
                        if (other$selectedProfile != null) {
                            return false;
                        }
                    } else if (!this$selectedProfile.equals(other$selectedProfile)) {
                        return false;
                    }
                    Object this$user = getUser();
                    Object other$user = other.getUser();
                    return this$user == null ? other$user == null : this$user.equals(other$user);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Account;
    }

    public int hashCode() {
        Object $username = getUsername();
        int result = (1 * 59) + ($username == null ? 43 : $username.hashCode());
        Object $userID = getUserID();
        int result2 = (result * 59) + ($userID == null ? 43 : $userID.hashCode());
        Object $displayName = getDisplayName();
        int result3 = (result2 * 59) + ($displayName == null ? 43 : $displayName.hashCode());
        Object $password = getPassword();
        int result4 = (result3 * 59) + ($password == null ? 43 : $password.hashCode());
        Object $accessToken = getAccessToken();
        int result5 = (result4 * 59) + ($accessToken == null ? 43 : $accessToken.hashCode());
        Object $microsoftOAuthToken = getMicrosoftOAuthToken();
        int result6 = (result5 * 59) + ($microsoftOAuthToken == null ? 43 : $microsoftOAuthToken.hashCode());
        Object $minecraftServicesToken = getMinecraftServicesToken();
        int result7 = (result6 * 59) + ($minecraftServicesToken == null ? 43 : $minecraftServicesToken.hashCode());
        Object $uuid = getUUID();
        int result8 = (result7 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $userProperties = this.userProperties;
        int result9 = (result8 * 59) + ($userProperties == null ? 43 : $userProperties.hashCode());
        Object $type = getType();
        int result10 = (((((result9 * 59) + ($type == null ? 43 : $type.hashCode())) * 59) + (isPremiumAccount() ? 79 : 97)) * 59) + Arrays.deepHashCode(getProfiles());
        Object $selectedProfile = this.selectedProfile;
        int result11 = (result10 * 59) + ($selectedProfile == null ? 43 : $selectedProfile.hashCode());
        Object $user = getUser();
        return (result11 * 59) + ($user == null ? 43 : $user.hashCode());
    }

    public Account() {
        this.type = AccountType.TLAUNCHER;
    }

    public Account(String username) {
        this();
        setUsername(username);
    }

    public Account(Map<String, Object> map) {
        this();
        fillFromMap(map);
    }

    public String getUsername() {
        return this.username;
    }

    public boolean hasUsername() {
        return this.username != null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return this.displayName == null ? this.username : this.displayName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        if (this.type.equals(AccountType.MICROSOFT) && Objects.nonNull(this.minecraftServicesToken)) {
            return this.minecraftServicesToken.getAccessToken();
        }
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        if (Configurator.NULL.equals(accessToken)) {
            accessToken = null;
        }
        this.accessToken = accessToken;
    }

    public String getUUID() {
        return this.uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public GameProfile[] getProfiles() {
        return this.profiles;
    }

    public void setProfiles(GameProfile[] p) {
        this.profiles = p;
    }

    public GameProfile getProfile() {
        return this.selectedProfile != null ? this.selectedProfile : GameProfile.DEFAULT_PROFILE;
    }

    public void setProfile(GameProfile p) {
        this.selectedProfile = p;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, List<String>> getProperties() {
        Map<String, List<String>> map = new HashMap<>();
        List<UserProperty> list = new ArrayList<>();
        if (this.userProperties != null) {
            for (Map<String, String> properties : this.userProperties) {
                list.add(new UserProperty(properties.get(Action.NAME_ATTRIBUTE), properties.get("value")));
            }
        }
        if (this.user != null && this.user.getProperties() != null) {
            for (Map<String, String> properties2 : this.user.getProperties()) {
                list.add(new UserProperty(properties2.get(Action.NAME_ATTRIBUTE), properties2.get("value")));
            }
        }
        for (UserProperty property : list) {
            List<String> values = new ArrayList<>();
            values.add(property.getValue());
            map.put(property.getKey(), values);
        }
        return map;
    }

    void setProperties(List<Map<String, String>> properties) {
        this.userProperties = properties;
    }

    public AccountType getType() {
        return this.type;
    }

    public void setType(AccountType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = type;
    }

    public boolean isFree() {
        return this.type.equals(AccountType.FREE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, Object> createMap() {
        Map<String, Object> r = new HashMap<>();
        r.put("username", this.username);
        r.put("userid", this.userID);
        r.put("uuid", UUIDTypeAdapter.toUUID(this.uuid));
        r.put("displayName", this.displayName);
        if (!isFree()) {
            r.put("type", this.type);
            r.put("accessToken", this.accessToken);
        }
        if (this.userProperties != null) {
            r.put("userProperties", this.userProperties);
        }
        return r;
    }

    void fillFromMap(Map<String, Object> map) {
        if (map.containsKey("username")) {
            setUsername(map.get("username").toString());
        }
        setUserID(map.containsKey("userid") ? map.get("userid").toString() : getUsername());
        setDisplayName(map.containsKey("displayName") ? map.get("displayName").toString() : getUsername());
        setProperties(map.containsKey("userProperties") ? (List) map.get("userProperties") : null);
        setUUID(map.containsKey("uuid") ? UUIDTypeAdapter.toUUID(map.get("uuid").toString()) : null);
        boolean hasAccessToken = map.containsKey("accessToken");
        if (hasAccessToken) {
            setAccessToken(map.get("accessToken").toString());
        }
        setType(map.containsKey("type") ? (AccountType) Reflect.parseEnum(AccountType.class, map.get("type").toString()) : hasAccessToken ? AccountType.MOJANG : AccountType.FREE);
    }

    public static Account createFreeAccountByUsername(String username) {
        Account account = new Account();
        account.setUUID(UUIDTypeAdapter.fromUUID(UUID.randomUUID()));
        account.setUsername(username);
        account.setUserID(account.getUsername());
        account.setDisplayName(account.getUsername());
        account.setType(AccountType.FREE);
        return account;
    }

    public String toString() {
        return toDebugString();
    }

    public String toDebugString() {
        Map<String, Object> map = createMap();
        if (map.containsKey("accessToken")) {
            map.remove("accessToken");
            map.put("accessToken", "(not null)");
        }
        return "Account" + map;
    }

    public static Account randomAccount() {
        return new Account("empty_manage " + new Random().nextLong());
    }

    public boolean isPremiumAccount() {
        return this.premiumAccount;
    }

    public void setPremiumAccount(boolean premiumAccount) {
        this.premiumAccount = premiumAccount;
    }

    public MicrosoftOAuthToken getMicrosoftOAuthToken() {
        return this.microsoftOAuthToken;
    }

    public void setMicrosoftOAuthToken(MicrosoftOAuthToken microsoftOAuthToken) {
        this.microsoftOAuthToken = microsoftOAuthToken;
    }

    public MinecraftServicesToken getMinecraftServicesToken() {
        return this.minecraftServicesToken;
    }

    public void setMinecraftServicesToken(MinecraftServicesToken minecraftServicesToken) {
        this.minecraftServicesToken = minecraftServicesToken;
    }

    public String getShortUUID() {
        return UUIDTypeAdapter.toUUID(this.uuid);
    }
}
