package by.gdev.http.download.model;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.http.HttpHeaders;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/model/Headers.class */
public enum Headers {
    SHA1(MessageDigestAlgorithms.SHA_1),
    ETAG(HttpHeaders.ETAG),
    CONTENTLENGTH("Content-Length"),
    LASTMODIFIED(HttpHeaders.LAST_MODIFIED);
    
    private final String value;

    Headers(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
