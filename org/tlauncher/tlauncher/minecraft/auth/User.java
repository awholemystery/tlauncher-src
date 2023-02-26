package org.tlauncher.tlauncher.minecraft.auth;

import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/User.class */
public class User {
    private String id;
    private List<Map<String, String>> properties;

    public String getID() {
        return this.id;
    }

    public List<Map<String, String>> getProperties() {
        return this.properties;
    }
}
