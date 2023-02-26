package org.tlauncher.tlauncher.downloader.modern;

import java.nio.file.Path;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/modern/DownloadableElement.class */
public class DownloadableElement {
    private List<String> inList;
    private Path out;

    public DownloadableElement(List<String> outList, Path in) {
        this.inList = outList;
        this.out = in;
    }

    public List<String> getInList() {
        return this.inList;
    }

    public void setInList(List<String> inList) {
        this.inList = inList;
    }

    public Path getOut() {
        return this.out;
    }

    public void setOut(Path out) {
        this.out = out;
    }
}
