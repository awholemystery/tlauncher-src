package org.tlauncher.tlauncher.entity.minecraft;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/JVMFile.class */
public class JVMFile {
    private DownloadJVMFile downloads;
    private String targetPath;
    private boolean executable;
    private String type;
    private String target;

    public void setDownloads(DownloadJVMFile downloads) {
        this.downloads = downloads;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String toString() {
        return "JVMFile(downloads=" + getDownloads() + ", targetPath=" + getTargetPath() + ", executable=" + isExecutable() + ", type=" + getType() + ", target=" + getTarget() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JVMFile) {
            JVMFile other = (JVMFile) o;
            if (other.canEqual(this)) {
                Object this$downloads = getDownloads();
                Object other$downloads = other.getDownloads();
                if (this$downloads == null) {
                    if (other$downloads != null) {
                        return false;
                    }
                } else if (!this$downloads.equals(other$downloads)) {
                    return false;
                }
                Object this$targetPath = getTargetPath();
                Object other$targetPath = other.getTargetPath();
                if (this$targetPath == null) {
                    if (other$targetPath != null) {
                        return false;
                    }
                } else if (!this$targetPath.equals(other$targetPath)) {
                    return false;
                }
                if (isExecutable() != other.isExecutable()) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }
                Object this$target = getTarget();
                Object other$target = other.getTarget();
                return this$target == null ? other$target == null : this$target.equals(other$target);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JVMFile;
    }

    public int hashCode() {
        Object $downloads = getDownloads();
        int result = (1 * 59) + ($downloads == null ? 43 : $downloads.hashCode());
        Object $targetPath = getTargetPath();
        int result2 = (((result * 59) + ($targetPath == null ? 43 : $targetPath.hashCode())) * 59) + (isExecutable() ? 79 : 97);
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $target = getTarget();
        return (result3 * 59) + ($target == null ? 43 : $target.hashCode());
    }

    public DownloadJVMFile getDownloads() {
        return this.downloads;
    }

    public String getTargetPath() {
        return this.targetPath;
    }

    public boolean isExecutable() {
        return this.executable;
    }

    public String getType() {
        return this.type;
    }

    public String getTarget() {
        return this.target;
    }
}
