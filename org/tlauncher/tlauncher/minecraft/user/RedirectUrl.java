package org.tlauncher.tlauncher.minecraft.user;

import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/RedirectUrl.class */
public class RedirectUrl {
    private final URL url;

    public RedirectUrl(URL url) {
        this.url = url;
    }

    public RedirectUrl(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public static RedirectUrl of(String url) {
        try {
            return new RedirectUrl(url);
        } catch (MalformedURLException e) {
            throw new Error("invalid url: " + url, e);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedirectUrl that = (RedirectUrl) o;
        return this.url.equals(that.url);
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public URL getUrl() {
        return this.url;
    }

    public String toString() {
        return "RedirectUrl{uri=" + this.url + '}';
    }
}
