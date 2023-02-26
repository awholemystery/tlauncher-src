package org.tlauncher.tlauncher.ui.editor;

import java.awt.Color;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.images.ImageIcon;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/AbstractEditorPanel.class */
public abstract class AbstractEditorPanel extends CenterPanel {
    protected final List<EditorHandler> handlers;

    public AbstractEditorPanel(CenterPanelTheme theme, Insets insets) {
        super(theme, insets);
        this.handlers = new ArrayList();
    }

    public AbstractEditorPanel(Insets insets) {
        this(null, insets);
    }

    public AbstractEditorPanel() {
        this(null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean checkValues() {
        boolean allValid = true;
        for (EditorHandler handler : this.handlers) {
            boolean valid = handler.isValid();
            setValid(handler, valid);
            if (!valid) {
                allValid = false;
            }
        }
        return allValid;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValid(EditorHandler handler, boolean valid) {
        Color color = valid ? getTheme().getBackground() : getTheme().getFailure();
        handler.getComponent().setOpaque(!valid);
        handler.getComponent().setBackground(color);
    }

    protected JComponent createTip(String label, boolean warning) {
        LocalizableLabel tip = new LocalizableLabel(label);
        if (warning) {
            ImageIcon.setup(tip, ImageCache.getIcon("warning.png", 16, 16));
        }
        return tip;
    }
}
