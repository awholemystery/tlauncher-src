package org.tlauncher.tlauncher.minecraft.auth;

import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/GameProfile.class */
public class GameProfile {
    public static final GameProfile DEFAULT_PROFILE = new GameProfile("0", "(Default)");
    private final String id;
    private final String name;

    private GameProfile(String id, String name) {
        if (StringUtils.isBlank(id) && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isComplete() {
        return StringUtils.isNotBlank(getId()) && StringUtils.isNotBlank(getName());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameProfile that = (GameProfile) o;
        if (!this.id.equals(that.id)) {
            return false;
        }
        return this.name.equals(that.name);
    }

    public int hashCode() {
        int result = this.id.hashCode();
        return (31 * result) + this.name.hashCode();
    }

    public String toString() {
        return "GameProfile{id='" + this.id + "', name='" + this.name + "'}";
    }
}
