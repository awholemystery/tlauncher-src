package org.tlauncher.tlauncher.updater.bootstrapper.model;

import java.io.File;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/model/JavaDownloadedElement.class */
public class JavaDownloadedElement extends DownloadedElement {
    private String javaFolder;
    private boolean originalJVM;

    public void setJavaFolder(String javaFolder) {
        this.javaFolder = javaFolder;
    }

    public void setOriginalJVM(boolean originalJVM) {
        this.originalJVM = originalJVM;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement
    public String toString() {
        return "JavaDownloadedElement(javaFolder=" + getJavaFolder() + ", originalJVM=" + isOriginalJVM() + ")";
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JavaDownloadedElement) {
            JavaDownloadedElement other = (JavaDownloadedElement) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$javaFolder = getJavaFolder();
                Object other$javaFolder = other.getJavaFolder();
                if (this$javaFolder == null) {
                    if (other$javaFolder != null) {
                        return false;
                    }
                } else if (!this$javaFolder.equals(other$javaFolder)) {
                    return false;
                }
                return isOriginalJVM() == other.isOriginalJVM();
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement
    protected boolean canEqual(Object other) {
        return other instanceof JavaDownloadedElement;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement
    public int hashCode() {
        int result = super.hashCode();
        Object $javaFolder = getJavaFolder();
        return (((result * 59) + ($javaFolder == null ? 43 : $javaFolder.hashCode())) * 59) + (isOriginalJVM() ? 79 : 97);
    }

    public String getJavaFolder() {
        return this.javaFolder;
    }

    public boolean isOriginalJVM() {
        return this.originalJVM;
    }

    public boolean existFolder(File folder) {
        return new File(folder, this.javaFolder).exists();
    }
}
