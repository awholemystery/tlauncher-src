package org.tlauncher.tlauncher.ui.explorer.filters;

import java.io.File;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/filters/ImageFileFilter.class */
public class ImageFileFilter extends javax.swing.filechooser.FileFilter {
    public static final Pattern extensionPattern = Pattern.compile("^(?:jp(?:e|)g|png)$", 2);

    public boolean accept(File f) {
        String extension = FileUtil.getExtension(f);
        if (extension == null) {
            return true;
        }
        return extensionPattern.matcher(extension).matches();
    }

    public String getDescription() {
        return Localizable.get("explorer.type.image");
    }
}
