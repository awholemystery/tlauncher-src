package org.tlauncher.tlauncher.ui.explorer.filters;

import java.io.File;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/filters/ExtensionFileFilter.class */
public class ExtensionFileFilter extends javax.swing.filechooser.FileFilter {
    private final String extension;
    private final boolean acceptNull;

    public ExtensionFileFilter(String extension, boolean acceptNullExtension) {
        if (extension == null) {
            throw new NullPointerException("Extension is NULL!");
        }
        if (extension.isEmpty()) {
            throw new IllegalArgumentException("Extension is empty!");
        }
        this.extension = extension;
        this.acceptNull = acceptNullExtension;
    }

    public ExtensionFileFilter(String extension) {
        this(extension, true);
    }

    public String getExtension() {
        return this.extension;
    }

    public boolean acceptsNull() {
        return this.acceptNull;
    }

    public boolean accept(File f) {
        String currentExtension = FileUtil.getExtension(f);
        if (this.acceptNull && currentExtension == null) {
            return true;
        }
        return this.extension.equals(currentExtension);
    }

    public String getDescription() {
        return Localizable.get("explorer.extension.format", this.extension.toUpperCase());
    }
}
