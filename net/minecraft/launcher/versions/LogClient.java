package net.minecraft.launcher.versions;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/LogClient.class */
public class LogClient {
    private String argument;
    private LogClientFile file;
    private String type;

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public void setFile(LogClientFile file) {
        this.file = file;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof LogClient) {
            LogClient other = (LogClient) o;
            if (other.canEqual(this)) {
                Object this$argument = getArgument();
                Object other$argument = other.getArgument();
                if (this$argument == null) {
                    if (other$argument != null) {
                        return false;
                    }
                } else if (!this$argument.equals(other$argument)) {
                    return false;
                }
                Object this$file = getFile();
                Object other$file = other.getFile();
                if (this$file == null) {
                    if (other$file != null) {
                        return false;
                    }
                } else if (!this$file.equals(other$file)) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                return this$type == null ? other$type == null : this$type.equals(other$type);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogClient;
    }

    public int hashCode() {
        Object $argument = getArgument();
        int result = (1 * 59) + ($argument == null ? 43 : $argument.hashCode());
        Object $file = getFile();
        int result2 = (result * 59) + ($file == null ? 43 : $file.hashCode());
        Object $type = getType();
        return (result2 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "LogClient(argument=" + getArgument() + ", file=" + getFile() + ", type=" + getType() + ")";
    }

    public String getArgument() {
        return this.argument;
    }

    public LogClientFile getFile() {
        return this.file;
    }

    public String getType() {
        return this.type;
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/LogClient$LogClientFile.class */
    public class LogClientFile {
        private String id;
        private String sha1;
        private long size;
        private String url;

        public LogClientFile() {
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof LogClientFile) {
                LogClientFile other = (LogClientFile) o;
                if (other.canEqual(this)) {
                    Object this$id = getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id != null) {
                            return false;
                        }
                    } else if (!this$id.equals(other$id)) {
                        return false;
                    }
                    Object this$sha1 = getSha1();
                    Object other$sha1 = other.getSha1();
                    if (this$sha1 == null) {
                        if (other$sha1 != null) {
                            return false;
                        }
                    } else if (!this$sha1.equals(other$sha1)) {
                        return false;
                    }
                    if (getSize() != other.getSize()) {
                        return false;
                    }
                    Object this$url = getUrl();
                    Object other$url = other.getUrl();
                    return this$url == null ? other$url == null : this$url.equals(other$url);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof LogClientFile;
        }

        public int hashCode() {
            Object $id = getId();
            int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
            Object $sha1 = getSha1();
            int result2 = (result * 59) + ($sha1 == null ? 43 : $sha1.hashCode());
            long $size = getSize();
            int result3 = (result2 * 59) + ((int) (($size >>> 32) ^ $size));
            Object $url = getUrl();
            return (result3 * 59) + ($url == null ? 43 : $url.hashCode());
        }

        public String toString() {
            return "LogClient.LogClientFile(id=" + getId() + ", sha1=" + getSha1() + ", size=" + getSize() + ", url=" + getUrl() + ")";
        }

        public String getId() {
            return this.id;
        }

        public String getSha1() {
            return this.sha1;
        }

        public long getSize() {
            return this.size;
        }

        public String getUrl() {
            return this.url;
        }
    }
}
