package org.tlauncher.tlauncher.updater.bootstrapper.model;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/model/DownloadedBootInfo.class */
public class DownloadedBootInfo {
    List<DownloadedElement> libraries;
    JavaDownloadedElement element;

    public void setLibraries(List<DownloadedElement> libraries) {
        this.libraries = libraries;
    }

    public void setElement(JavaDownloadedElement element) {
        this.element = element;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloadedBootInfo) {
            DownloadedBootInfo other = (DownloadedBootInfo) o;
            if (other.canEqual(this)) {
                Object this$libraries = getLibraries();
                Object other$libraries = other.getLibraries();
                if (this$libraries == null) {
                    if (other$libraries != null) {
                        return false;
                    }
                } else if (!this$libraries.equals(other$libraries)) {
                    return false;
                }
                Object this$element = getElement();
                Object other$element = other.getElement();
                return this$element == null ? other$element == null : this$element.equals(other$element);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadedBootInfo;
    }

    public int hashCode() {
        Object $libraries = getLibraries();
        int result = (1 * 59) + ($libraries == null ? 43 : $libraries.hashCode());
        Object $element = getElement();
        return (result * 59) + ($element == null ? 43 : $element.hashCode());
    }

    public String toString() {
        return "DownloadedBootInfo(libraries=" + getLibraries() + ", element=" + getElement() + ")";
    }

    public List<DownloadedElement> getLibraries() {
        return this.libraries;
    }

    public JavaDownloadedElement getElement() {
        return this.element;
    }
}
