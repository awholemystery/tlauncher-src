package org.apache.http.entity;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.BasicHeader;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/entity/AbstractHttpEntity.class */
public abstract class AbstractHttpEntity implements HttpEntity {
    protected static final int OUTPUT_BUFFER_SIZE = 4096;
    protected Header contentType;
    protected Header contentEncoding;
    protected boolean chunked;

    @Override // org.apache.http.HttpEntity
    public Header getContentType() {
        return this.contentType;
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentEncoding() {
        return this.contentEncoding;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return this.chunked;
    }

    public void setContentType(Header contentType) {
        this.contentType = contentType;
    }

    public void setContentType(String ctString) {
        Header h = null;
        if (ctString != null) {
            h = new BasicHeader("Content-Type", ctString);
        }
        setContentType(h);
    }

    public void setContentEncoding(Header contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public void setContentEncoding(String ceString) {
        Header h = null;
        if (ceString != null) {
            h = new BasicHeader("Content-Encoding", ceString);
        }
        setContentEncoding(h);
    }

    public void setChunked(boolean b) {
        this.chunked = b;
    }

    @Override // org.apache.http.HttpEntity
    @Deprecated
    public void consumeContent() throws IOException {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (this.contentType != null) {
            sb.append("Content-Type: ");
            sb.append(this.contentType.getValue());
            sb.append(',');
        }
        if (this.contentEncoding != null) {
            sb.append("Content-Encoding: ");
            sb.append(this.contentEncoding.getValue());
            sb.append(',');
        }
        long len = getContentLength();
        if (len >= 0) {
            sb.append("Content-Length: ");
            sb.append(len);
            sb.append(',');
        }
        sb.append("Chunked: ");
        sb.append(this.chunked);
        sb.append(']');
        return sb.toString();
    }
}
