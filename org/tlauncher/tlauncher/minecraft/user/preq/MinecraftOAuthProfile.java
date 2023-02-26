package org.tlauncher.tlauncher.minecraft.user.preq;

import ch.qos.logback.core.joran.action.Action;
import java.util.Objects;
import java.util.UUID;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/preq/MinecraftOAuthProfile.class */
public class MinecraftOAuthProfile implements Validatable {
    private UUID id;
    private String name;

    public MinecraftOAuthProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public MinecraftOAuthProfile() {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinecraftOAuthProfile that = (MinecraftOAuthProfile) o;
        if (!Objects.equals(this.id, that.id)) {
            return false;
        }
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        return (31 * result) + (this.name != null ? this.name.hashCode() : 0);
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "MinecraftOAuthProfile{id='" + this.id + "', name='" + this.name + "'}";
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
    public void validate() {
        Validatable.notNull(this.id, "id");
        Validatable.notEmpty(this.name, Action.NAME_ATTRIBUTE);
    }
}
