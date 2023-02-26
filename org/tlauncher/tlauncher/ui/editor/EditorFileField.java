package org.tlauncher.tlauncher.ui.editor;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorFileField.class */
public class EditorFileField extends BorderPanel implements EditorField {
    private static final long serialVersionUID = 5136327098130653756L;
    public static final char DEFAULT_DELIMITER = ';';
    private final EditorTextField textField;
    private final LocalizableButton explorerButton;
    private final FileChooser explorer;
    private final char delimiterChar;
    private final Pattern delimiterSplitter;

    public EditorFileField(String prompt, boolean canBeEmpty, String button, FileChooser chooser, char delimiter) {
        super(10, 0);
        if (chooser == null) {
            throw new NullPointerException("FileExplorer should be defined!");
        }
        this.textField = new EditorTextField(prompt, canBeEmpty);
        this.explorerButton = new UpdaterButton(UpdaterButton.GRAY_COLOR, button);
        this.explorer = chooser;
        this.delimiterChar = delimiter;
        this.delimiterSplitter = Pattern.compile(String.valueOf(this.delimiterChar), 16);
        this.explorerButton.addActionListener(e -> {
            this.explorerButton.setEnabled(false);
            this.explorer.setCurrentDirectory(getFirstFile());
            int result = this.explorer.showDialog(this);
            if (result == 0) {
                setRawValue(this.explorer.getSelectedFiles());
            }
            this.explorerButton.setEnabled(true);
        });
        add((Component) this.textField, "Center");
        add((Component) this.explorerButton, "East");
    }

    public EditorFileField(String prompt, boolean canBeEmpty, FileChooser chooser) {
        this(prompt, canBeEmpty, "explorer.browse", chooser, ';');
    }

    public EditorFileField(String prompt, FileChooser chooser) {
        this(prompt, false, chooser);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        return getValueFromRaw(getRawValues());
    }

    private File[] getRawValues() {
        String[] paths = getRawSplitValue();
        if (paths == null) {
            return null;
        }
        int len = paths.length;
        File[] files = new File[len];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        return files;
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
        this.textField.setSettingsValue(value);
    }

    private void setRawValue(File[] fileList) {
        setSettingsValue(getValueFromRaw(fileList));
    }

    private String[] getRawSplitValue() {
        return splitString(this.textField.getValue());
    }

    private String getValueFromRaw(File[] files) {
        if (files == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
            String path = file.getAbsolutePath();
            builder.append(this.delimiterChar).append(path);
        }
        return builder.substring(1);
    }

    private String[] splitString(String s) {
        if (s == null) {
            return null;
        }
        String[] split = this.delimiterSplitter.split(s);
        if (split.length == 0) {
            return null;
        }
        return split;
    }

    private File getFirstFile() {
        File[] files = getRawValues();
        if (files == null || files.length == 0) {
            return TLauncher.getDirectory();
        }
        return files[0];
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        return this.textField.isValueValid();
    }

    public void setBackground(Color bg) {
        if (this.textField != null) {
            this.textField.setBackground(bg);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents(reason, this.textField, this.explorerButton);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(Blocker.UNIVERSAL_UNBLOCK, this.textField, this.explorerButton);
    }

    protected void log(Object... w) {
        U.log("[" + getClass().getSimpleName() + "]", w);
    }
}
