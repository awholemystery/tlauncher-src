package com.sothawo.mapjfx.cache;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/CachedDataInfo.class */
class CachedDataInfo implements Serializable {
    private String contentType;
    private String contentEncoding;
    private Map<String, List<String>> headerFields = Collections.emptyMap();

    public Map<String, List<String>> getHeaderFields() {
        return this.headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String toString() {
        return "CachedDataInfo{contentType='" + this.contentType + "', contentEncoding='" + this.contentEncoding + "', headerFields=" + this.headerFields + '}';
    }

    public void setFromHttpUrlConnection(HttpURLConnection httpUrlConnection) {
        this.contentType = httpUrlConnection.getContentType();
        this.contentEncoding = httpUrlConnection.getContentEncoding();
        this.headerFields = new HashMap();
        this.headerFields.putAll(httpUrlConnection.getHeaderFields());
    }
}
