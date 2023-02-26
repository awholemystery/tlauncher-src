package org.tlauncher.tlauncher.ui.explorer.filters;

import ch.qos.logback.core.CoreConstants;
import java.io.File;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/filters/FileFilter.class */
public class FileFilter extends CommonFilter {
    @Override // java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return new File(dir, name).isFile();
    }

    public boolean accept(File f) {
        return f.isFile();
    }

    public String getDescription() {
        return CoreConstants.EMPTY_STRING;
    }
}
