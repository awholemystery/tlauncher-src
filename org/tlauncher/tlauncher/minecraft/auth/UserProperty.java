package org.tlauncher.tlauncher.minecraft.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/UserProperty.class */
public class UserProperty {
    private String name;
    private String value;

    public UserProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getKey() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "Property{" + this.name + " = " + this.value + "}";
    }
}
