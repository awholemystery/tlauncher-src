package org.tlauncher.tlauncher.minecraft.user;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/CodeRequestErrorException.class */
public class CodeRequestErrorException extends MicrosoftOAuthCodeRequestException {
    public CodeRequestErrorException(String error, String errorDescription) {
        super(format(error, errorDescription));
    }

    private static String format(String error, String errorDescription) {
        if (errorDescription != null) {
            return error + ": " + errorDescription;
        }
        return error;
    }
}
