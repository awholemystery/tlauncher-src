package org.tlauncher.tlauncher.minecraft.auth;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.minecraft.auth.Account;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/AuthenticatorDatabase.class */
public class AuthenticatorDatabase {
    private final Map<String, Account> accounts;

    public AuthenticatorDatabase(Map<String, Account> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this.accounts = map;
    }

    public AuthenticatorDatabase() {
        this(new LinkedHashMap());
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(this.accounts.values());
    }

    public Account getByUUID(String uuid) {
        for (Account account : this.accounts.values()) {
            if (StringUtils.equals(account.getUUID(), uuid)) {
                return account;
            }
        }
        return null;
    }

    public Account getByUsername(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        for (Account acc : this.accounts.values()) {
            if (username.equals(acc.getUsername())) {
                return acc;
            }
        }
        return null;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/AuthenticatorDatabase$Serializer.class */
    public static class Serializer implements JsonDeserializer<AuthenticatorDatabase>, JsonSerializer<AuthenticatorDatabase> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public AuthenticatorDatabase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<String, Account> services = new LinkedHashMap<>();
            Map<String, Map<String, Object>> credentials = deserializeCredentials((JsonObject) json, context);
            for (Map.Entry<String, Map<String, Object>> en : credentials.entrySet()) {
                services.put(en.getKey(), new Account(en.getValue()));
            }
            return new AuthenticatorDatabase(services);
        }

        Map<String, Map<String, Object>> deserializeCredentials(JsonObject json, JsonDeserializationContext context) {
            Map<String, Map<String, Object>> result = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> authEntry : json.entrySet()) {
                Map<String, Object> credentials = new LinkedHashMap<>();
                for (Map.Entry<String, JsonElement> credentialsEntry : ((JsonObject) authEntry.getValue()).entrySet()) {
                    credentials.put(credentialsEntry.getKey(), deserializeCredential(credentialsEntry.getValue()));
                }
                result.put(authEntry.getKey(), credentials);
            }
            return result;
        }

        private Object deserializeCredential(JsonElement element) {
            if (element instanceof JsonObject) {
                Map<String, Object> result = new LinkedHashMap<>();
                for (Map.Entry<String, JsonElement> entry : ((JsonObject) element).entrySet()) {
                    result.put(entry.getKey(), deserializeCredential(entry.getValue()));
                }
                return result;
            } else if (element instanceof JsonArray) {
                List<Object> result2 = new ArrayList<>();
                Iterator<JsonElement> it = ((JsonArray) element).iterator();
                while (it.hasNext()) {
                    result2.add(deserializeCredential(it.next()));
                }
                return result2;
            } else {
                return element.getAsString();
            }
        }

        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(AuthenticatorDatabase src, Type typeOfSrc, JsonSerializationContext context) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Map.Entry<String, Account> en : src.accounts.entrySet()) {
                linkedHashMap.put(en.getKey(), en.getValue().createMap());
            }
            return context.serialize(linkedHashMap);
        }
    }

    public void cleanFreeAccount() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Account> account : this.accounts.entrySet()) {
            if (account.getValue().getType().equals(Account.AccountType.FREE)) {
                list.add(account.getKey());
            }
        }
        for (String string : list) {
            this.accounts.remove(string);
        }
    }

    public Account getByUsernameType(String username, String type) {
        if (username == null || type == null) {
            throw new NullPointerException();
        }
        for (Account acc : this.accounts.values()) {
            if (username.equals(acc.getUsername()) && type.equals(acc.getType().name())) {
                return acc;
            }
        }
        return null;
    }
}
