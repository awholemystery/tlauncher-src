package org.tlauncher.tlauncher.minecraft.user.mcsauth;

import java.time.Instant;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/mcsauth/MinecraftServicesToken.class */
public class MinecraftServicesToken implements Validatable {
    private final Instant createdAt;
    private String accessToken;
    private int expiresIn;

    public MinecraftServicesToken(String accessToken, int expiresIn) {
        this.accessToken = accessToken;
        this.createdAt = Instant.now();
        this.expiresIn = expiresIn;
    }

    public MinecraftServicesToken(String accessToken, Instant expiresAt) {
        this.accessToken = accessToken;
        this.createdAt = expiresAt.minusSeconds(3600L);
        this.expiresIn = 3600;
    }

    public MinecraftServicesToken() {
        this.createdAt = Instant.now();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinecraftServicesToken that = (MinecraftServicesToken) o;
        if (this.expiresIn != that.expiresIn) {
            return false;
        }
        return this.accessToken.equals(that.accessToken);
    }

    public int hashCode() {
        int result = this.accessToken.hashCode();
        return (31 * result) + this.expiresIn;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public Instant calculateExpiryTime() {
        return this.createdAt.plusSeconds(this.expiresIn);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(calculateExpiryTime());
    }

    public String toString() {
        return "MinecraftServicesToken{accessToken='" + this.accessToken + "', expiresIn=" + this.expiresIn + '}';
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
    public void validate() {
        Validatable.notEmpty(this.accessToken, "accessToken");
        Validatable.notNegative(this.expiresIn, "expiresIn");
    }
}
