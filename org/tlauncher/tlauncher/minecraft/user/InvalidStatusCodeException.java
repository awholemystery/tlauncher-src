package org.tlauncher.tlauncher.minecraft.user;

import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/InvalidStatusCodeException.class */
public class InvalidStatusCodeException extends InvalidResponseException {
    private final int statusCode;

    public InvalidStatusCodeException(int statusCode, String response) {
        super(response, format(statusCode, response));
        this.statusCode = statusCode;
    }

    private static String format(int statusCode, String response) {
        if (StringUtils.isEmpty(response)) {
            return String.valueOf(statusCode);
        }
        return statusCode + ": \"" + response + "\"";
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
