package org.tlauncher.tlauncher.ui.explorer;

import java.awt.Component;
import java.io.File;
import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/FileChooser.class */
public interface FileChooser {
    void setFileFilter(CommonFilter commonFilter);

    int showSaveDialog(Component component);

    File getSelectedFile();

    void setSelectedFile(File file);

    void setCurrentDirectory(File file);

    int showDialog(Component component);

    File[] getSelectedFiles();

    void setMultiSelectionEnabled(boolean z);

    int showDialog(Component component, String str);

    void setDialogTitle(String str);

    void setFileSelectionMode(int i);
}
