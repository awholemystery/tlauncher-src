package org.tlauncher.tlauncher.ui.explorer.filters;

import ch.qos.logback.core.CoreConstants;
import java.io.File;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/filters/FolderFilter.class */
public class FolderFilter extends CommonFilter {
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return new File(dir, name).isDirectory();
    }

    public String getDescription() {
        return CoreConstants.EMPTY_STRING;
    }
}
