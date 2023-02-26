package org.tlauncher.tlauncher.minecraft.user.xb;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Objects;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/XboxServiceAuthenticationResponse.class */
public class XboxServiceAuthenticationResponse implements Validatable {
    private final String token;
    private final String uhs;

    public XboxServiceAuthenticationResponse(String token, String uhs) {
        this.token = token;
        this.uhs = uhs;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XboxServiceAuthenticationResponse that = (XboxServiceAuthenticationResponse) o;
        if (!Objects.equals(this.token, that.token)) {
            return false;
        }
        return Objects.equals(this.uhs, that.uhs);
    }

    public int hashCode() {
        int result = this.token != null ? this.token.hashCode() : 0;
        return (31 * result) + (this.uhs != null ? this.uhs.hashCode() : 0);
    }

    public String getToken() {
        return this.token;
    }

    public String getUHS() {
        return this.uhs;
    }

    public String toString() {
        return "XboxServiceAuthenticationResponse{token='" + this.token + "', uhs='" + this.uhs + "'}";
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
    public void validate() {
        Validatable.notEmpty(this.token, "token");
        Validatable.notEmpty(this.uhs, "uhs");
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/XboxServiceAuthenticationResponse$Deserializer.class */
    public static class Deserializer implements JsonDeserializer<XboxServiceAuthenticationResponse> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public XboxServiceAuthenticationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject o = json.getAsJsonObject();
            return new XboxServiceAuthenticationResponse(o.get("Token").getAsString(), o.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString());
        }
    }
}
