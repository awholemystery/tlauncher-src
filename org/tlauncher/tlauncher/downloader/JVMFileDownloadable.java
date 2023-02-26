package org.tlauncher.tlauncher.downloader;

import org.tlauncher.tlauncher.entity.minecraft.JVMFile;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/JVMFileDownloadable.class */
public class JVMFileDownloadable extends Downloadable {
    private JVMFile jvmFile;

    public void setJvmFile(JVMFile jvmFile) {
        this.jvmFile = jvmFile;
    }

    @Override // org.tlauncher.tlauncher.downloader.Downloadable
    public String toString() {
        return "JVMFileDownloadable(jvmFile=" + getJvmFile() + ")";
    }

    @Override // org.tlauncher.tlauncher.downloader.Downloadable
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JVMFileDownloadable) {
            JVMFileDownloadable other = (JVMFileDownloadable) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$jvmFile = getJvmFile();
                Object other$jvmFile = other.getJvmFile();
                return this$jvmFile == null ? other$jvmFile == null : this$jvmFile.equals(other$jvmFile);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JVMFileDownloadable;
    }

    @Override // org.tlauncher.tlauncher.downloader.Downloadable
    public int hashCode() {
        int result = super.hashCode();
        Object $jvmFile = getJvmFile();
        return (result * 59) + ($jvmFile == null ? 43 : $jvmFile.hashCode());
    }

    public JVMFile getJvmFile() {
        return this.jvmFile;
    }

    public JVMFileDownloadable(JVMFile file) {
        super(ClientInstanceRepo.EMPTY_REPO, file.getDownloads().getRaw());
        setJvmFile(file);
    }
}
