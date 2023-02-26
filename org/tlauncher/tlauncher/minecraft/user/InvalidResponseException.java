package org.tlauncher.tlauncher.minecraft.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/InvalidResponseException.class */
public class InvalidResponseException extends Exception {
    private final String response;

    public InvalidResponseException(String response) {
        super(response);
        this.response = response;
    }

    public InvalidResponseException(String response, String message) {
        super(message);
        this.response = response;
    }

    public InvalidResponseException(String response, Throwable cause) {
        super(response, cause);
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public JsonObject getResponseAsJson() {
        try {
            return (JsonObject) new JsonParser().parse(this.response);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
