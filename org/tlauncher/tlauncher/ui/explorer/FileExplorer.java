package org.tlauncher.tlauncher.ui.explorer;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/explorer/FileExplorer.class */
public class FileExplorer implements FileChooser {
    private JFileChooser fileChooser = new JFileChooser();

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public synchronized void setFileFilter(CommonFilter filter) {
        this.fileChooser.setFileFilter(filter);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showSaveDialog(Component component) {
        return this.fileChooser.showSaveDialog(component);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public File getSelectedFile() {
        return this.fileChooser.getSelectedFile();
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setSelectedFile(File file) {
        this.fileChooser.setSelectedFile(file);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setCurrentDirectory(File dir) {
        if (dir == null) {
            dir = FileSystemView.getFileSystemView().getDefaultDirectory();
        }
        this.fileChooser.setCurrentDirectory(dir);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showDialog(Component parent) {
        return showDialog(parent, UIManager.getString("FileChooser.directoryOpenButtonText"));
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public File[] getSelectedFiles() {
        File[] selectedFiles = this.fileChooser.getSelectedFiles();
        if (selectedFiles.length > 0) {
            return selectedFiles;
        }
        File selectedFile = this.fileChooser.getSelectedFile();
        if (selectedFile == null) {
            return null;
        }
        return new File[]{selectedFile};
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setMultiSelectionEnabled(boolean b) {
        this.fileChooser.setMultiSelectionEnabled(b);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public int showDialog(Component component, String s) {
        return this.fileChooser.showDialog(component, s);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setDialogTitle(String s) {
        this.fileChooser.setDialogTitle(s);
    }

    @Override // org.tlauncher.tlauncher.ui.explorer.FileChooser
    public void setFileSelectionMode(int mode) {
        this.fileChooser.setFileSelectionMode(mode);
    }
}
