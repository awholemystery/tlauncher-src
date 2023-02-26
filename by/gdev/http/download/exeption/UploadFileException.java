package by.gdev.http.download.exeption;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/exeption/UploadFileException.class */
public class UploadFileException extends IOException {
    private static final long serialVersionUID = -2684927056566219164L;
    private String uri;
    private String localPath;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "UploadFileException(uri=" + getUri() + ", localPath=" + getLocalPath() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UploadFileException) {
            UploadFileException other = (UploadFileException) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$uri = getUri();
                Object other$uri = other.getUri();
                if (this$uri == null) {
                    if (other$uri != null) {
                        return false;
                    }
                } else if (!this$uri.equals(other$uri)) {
                    return false;
                }
                Object this$localPath = getLocalPath();
                Object other$localPath = other.getLocalPath();
                return this$localPath == null ? other$localPath == null : this$localPath.equals(other$localPath);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadFileException;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $uri = getUri();
        int result2 = (result * 59) + ($uri == null ? 43 : $uri.hashCode());
        Object $localPath = getLocalPath();
        return (result2 * 59) + ($localPath == null ? 43 : $localPath.hashCode());
    }

    public String getUri() {
        return this.uri;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public UploadFileException(String uri, String localPath, String message) {
        super(message);
        this.uri = uri;
        this.localPath = localPath;
    }
}
