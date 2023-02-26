package org.tlauncher.tlauncher.minecraft.launcher;

import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftException.class */
public class MinecraftException extends Exception {
    private static final long serialVersionUID = -2415374288600214879L;
    private final String langPath;
    private final String[] langVars;

    MinecraftException(String message, String langPath, Throwable cause, Object... langVars) {
        super(message, cause);
        if (langPath == null) {
            throw new NullPointerException("Lang path required!");
        }
        langVars = langVars == null ? new Object[0] : langVars;
        this.langPath = langPath;
        this.langVars = Localizable.checkVariables(langVars);
    }

    public MinecraftException(String message, String langPath, Throwable cause) {
        this(message, langPath, cause, new Object[0]);
    }

    public MinecraftException(String message, String langPath, Object... vars) {
        this(message, langPath, null, vars);
    }

    public String getLangPath() {
        return this.langPath;
    }

    public String[] getLangVars() {
        return this.langVars;
    }
}
