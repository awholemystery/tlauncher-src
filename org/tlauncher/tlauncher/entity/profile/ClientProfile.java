package org.tlauncher.tlauncher.entity.profile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.auth.AuthenticatorDatabase;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/profile/ClientProfile.class */
public class ClientProfile {
    private UUID clientToken = UUID.randomUUID();
    private Map<String, Account> accounts = Collections.synchronizedMap(new HashMap());
    private AuthenticatorDatabase authenticationDatabase;
    private volatile String selectedAccountUUID;
    private volatile String freeAccountUUID;

    public void setClientToken(UUID clientToken) {
        this.clientToken = clientToken;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public void setAuthenticationDatabase(AuthenticatorDatabase authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public void setSelectedAccountUUID(String selectedAccountUUID) {
        this.selectedAccountUUID = selectedAccountUUID;
    }

    public void setFreeAccountUUID(String freeAccountUUID) {
        this.freeAccountUUID = freeAccountUUID;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ClientProfile) {
            ClientProfile other = (ClientProfile) o;
            if (other.canEqual(this)) {
                Object this$clientToken = getClientToken();
                Object other$clientToken = other.getClientToken();
                if (this$clientToken == null) {
                    if (other$clientToken != null) {
                        return false;
                    }
                } else if (!this$clientToken.equals(other$clientToken)) {
                    return false;
                }
                Object this$accounts = getAccounts();
                Object other$accounts = other.getAccounts();
                if (this$accounts == null) {
                    if (other$accounts != null) {
                        return false;
                    }
                } else if (!this$accounts.equals(other$accounts)) {
                    return false;
                }
                Object this$authenticationDatabase = getAuthenticationDatabase();
                Object other$authenticationDatabase = other.getAuthenticationDatabase();
                if (this$authenticationDatabase == null) {
                    if (other$authenticationDatabase != null) {
                        return false;
                    }
                } else if (!this$authenticationDatabase.equals(other$authenticationDatabase)) {
                    return false;
                }
                Object this$selectedAccountUUID = getSelectedAccountUUID();
                Object other$selectedAccountUUID = other.getSelectedAccountUUID();
                if (this$selectedAccountUUID == null) {
                    if (other$selectedAccountUUID != null) {
                        return false;
                    }
                } else if (!this$selectedAccountUUID.equals(other$selectedAccountUUID)) {
                    return false;
                }
                Object this$freeAccountUUID = getFreeAccountUUID();
                Object other$freeAccountUUID = other.getFreeAccountUUID();
                return this$freeAccountUUID == null ? other$freeAccountUUID == null : this$freeAccountUUID.equals(other$freeAccountUUID);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientProfile;
    }

    public int hashCode() {
        Object $clientToken = getClientToken();
        int result = (1 * 59) + ($clientToken == null ? 43 : $clientToken.hashCode());
        Object $accounts = getAccounts();
        int result2 = (result * 59) + ($accounts == null ? 43 : $accounts.hashCode());
        Object $authenticationDatabase = getAuthenticationDatabase();
        int result3 = (result2 * 59) + ($authenticationDatabase == null ? 43 : $authenticationDatabase.hashCode());
        Object $selectedAccountUUID = getSelectedAccountUUID();
        int result4 = (result3 * 59) + ($selectedAccountUUID == null ? 43 : $selectedAccountUUID.hashCode());
        Object $freeAccountUUID = getFreeAccountUUID();
        return (result4 * 59) + ($freeAccountUUID == null ? 43 : $freeAccountUUID.hashCode());
    }

    public String toString() {
        return "ClientProfile(clientToken=" + getClientToken() + ", accounts=" + getAccounts() + ", authenticationDatabase=" + getAuthenticationDatabase() + ", selectedAccountUUID=" + getSelectedAccountUUID() + ", freeAccountUUID=" + getFreeAccountUUID() + ")";
    }

    public UUID getClientToken() {
        return this.clientToken;
    }

    public Map<String, Account> getAccounts() {
        return this.accounts;
    }

    public AuthenticatorDatabase getAuthenticationDatabase() {
        return this.authenticationDatabase;
    }

    public String getSelectedAccountUUID() {
        return this.selectedAccountUUID;
    }

    public String getFreeAccountUUID() {
        return this.freeAccountUUID;
    }

    public boolean isSelected() {
        return Objects.nonNull(this.selectedAccountUUID);
    }
}
