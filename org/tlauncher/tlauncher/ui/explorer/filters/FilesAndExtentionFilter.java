package org.tlauncher.tlauncher.ui.explorer.filters;

import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/filters/FilesAndExtentionFilter.class */
public class FilesAndExtentionFilter extends FileFilter {
    private FileNameExtensionFilter extensionFilter;
    private String s1;

    public FilesAndExtentionFilter(String s1, String... s2) {
        this.s1 = s1;
        this.extensionFilter = new FileNameExtensionFilter(s1, s2);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.filters.FileFilter
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (!super.accept(f)) {
            return false;
        }
        return this.extensionFilter.accept(f);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.filters.FileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        if (new File(dir, name).isDirectory()) {
            return true;
        }
        if (!super.accept(dir, name)) {
            return false;
        }
        return this.extensionFilter.accept(new File(dir, name));
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.filters.FileFilter
    public String getDescription() {
        return this.s1;
    }
}
