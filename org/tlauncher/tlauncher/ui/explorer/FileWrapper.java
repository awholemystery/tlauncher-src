package org.tlauncher.tlauncher.ui.explorer;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.util.Objects;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/FileWrapper.class */
public class FileWrapper implements FileChooser {
    private CommonFilter filter;
    private FileDialog dialog;
    private File directory;
    private File selectedFile;
    private boolean multiSelectionMode = false;
    private String title;

    private FileDialog create(Component component) {
        if (component instanceof JFrame) {
            this.dialog = new FileDialog((JFrame) component);
        } else if (component instanceof JDialog) {
            this.dialog = new FileDialog((JDialog) component);
        } else {
            this.dialog = new FileDialog(new JFrame());
        }
        this.dialog.setAlwaysOnTop(true);
        this.dialog.setFilenameFilter(this.filter);
        if (Objects.nonNull(this.selectedFile)) {
            this.dialog.setFile(this.selectedFile.getAbsolutePath());
        }
        if (Objects.nonNull(this.directory)) {
            this.dialog.setDirectory(this.directory.getAbsolutePath());
        }
        this.dialog.setMultipleMode(this.multiSelectionMode);
        if (Objects.nonNull(this.title)) {
            this.dialog.setTitle(this.title);
        }
        return this.dialog;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showSaveDialog(Component component) {
        this.dialog = create(component);
        this.dialog.setMode(1);
        this.dialog.show();
        if (Objects.nonNull(this.dialog.getFile())) {
            return 0;
        }
        return 1;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public File getSelectedFile() {
        return new File(this.dialog.getDirectory(), this.dialog.getFile());
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setSelectedFile(File file) {
        this.selectedFile = file;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setCurrentDirectory(File file) {
        this.directory = file;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showDialog(Component parent) {
        this.dialog = create(parent);
        this.dialog.setMode(0);
        this.dialog.show();
        if (Objects.nonNull(this.dialog.getFile())) {
            return 0;
        }
        return 1;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public File[] getSelectedFiles() {
        return this.dialog.getFiles();
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setFileFilter(CommonFilter filter) {
        this.filter = filter;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setMultiSelectionEnabled(boolean b) {
        this.multiSelectionMode = b;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showDialog(Component component, String s) {
        setDialogTitle(s);
        return showDialog(component);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setDialogTitle(String s) {
        this.title = s;
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setFileSelectionMode(int mode) {
    }
}
