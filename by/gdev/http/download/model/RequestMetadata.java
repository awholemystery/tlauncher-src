package by.gdev.http.download.model;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/model/RequestMetadata.class */
public class RequestMetadata {
    private String contentLength;
    private String lastModified;
    private String eTag;
    private String sha1;

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RequestMetadata) {
            RequestMetadata other = (RequestMetadata) o;
            if (other.canEqual(this)) {
                Object this$contentLength = getContentLength();
                Object other$contentLength = other.getContentLength();
                if (this$contentLength == null) {
                    if (other$contentLength != null) {
                        return false;
                    }
                } else if (!this$contentLength.equals(other$contentLength)) {
                    return false;
                }
                Object this$lastModified = getLastModified();
                Object other$lastModified = other.getLastModified();
                if (this$lastModified == null) {
                    if (other$lastModified != null) {
                        return false;
                    }
                } else if (!this$lastModified.equals(other$lastModified)) {
                    return false;
                }
                Object this$eTag = getETag();
                Object other$eTag = other.getETag();
                if (this$eTag == null) {
                    if (other$eTag != null) {
                        return false;
                    }
                } else if (!this$eTag.equals(other$eTag)) {
                    return false;
                }
                Object this$sha1 = getSha1();
                Object other$sha1 = other.getSha1();
                return this$sha1 == null ? other$sha1 == null : this$sha1.equals(other$sha1);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestMetadata;
    }

    public int hashCode() {
        Object $contentLength = getContentLength();
        int result = (1 * 59) + ($contentLength == null ? 43 : $contentLength.hashCode());
        Object $lastModified = getLastModified();
        int result2 = (result * 59) + ($lastModified == null ? 43 : $lastModified.hashCode());
        Object $eTag = getETag();
        int result3 = (result2 * 59) + ($eTag == null ? 43 : $eTag.hashCode());
        Object $sha1 = getSha1();
        return (result3 * 59) + ($sha1 == null ? 43 : $sha1.hashCode());
    }

    public String toString() {
        return "RequestMetadata(contentLength=" + getContentLength() + ", lastModified=" + getLastModified() + ", eTag=" + getETag() + ", sha1=" + getSha1() + ")";
    }

    public String getContentLength() {
        return this.contentLength;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public String getETag() {
        return this.eTag;
    }

    public String getSha1() {
        return this.sha1;
    }
}
