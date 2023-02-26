package org.tlauncher.tlauncher.minecraft.user;

import java.time.Instant;
import java.util.Objects;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MicrosoftOAuthToken.class */
public class MicrosoftOAuthToken implements Validatable {
    private final Instant createdAt;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;

    public MicrosoftOAuthToken(String accessToken, String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = Instant.now();
        this.expiresIn = expiresIn;
    }

    public MicrosoftOAuthToken(String accessToken, String refreshToken, Instant expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = expiresAt.minusSeconds(3600L);
        this.expiresIn = 3600;
    }

    public MicrosoftOAuthToken() {
        this.createdAt = Instant.now();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MicrosoftOAuthToken that = (MicrosoftOAuthToken) o;
        if (this.expiresIn != that.expiresIn || !Objects.equals(this.accessToken, that.accessToken)) {
            return false;
        }
        return Objects.equals(this.refreshToken, that.refreshToken);
    }

    public int hashCode() {
        int result = this.accessToken != null ? this.accessToken.hashCode() : 0;
        return (31 * ((31 * result) + (this.refreshToken != null ? this.refreshToken.hashCode() : 0))) + this.expiresIn;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Instant calculateExpiryTime() {
        return this.createdAt.plusSeconds(this.expiresIn);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(calculateExpiryTime());
    }

    public String toString() {
        return "MicrosoftOAuthToken{accessToken='" + this.accessToken + "', refreshToken='" + this.refreshToken + "', expiresIn=" + this.expiresIn + ", createdAt=" + this.createdAt + '}';
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
    public void validate() {
        Validatable.notNull(this.accessToken, "accessToken");
        Validatable.notNull(this.createdAt, "createdAt");
    }
}
