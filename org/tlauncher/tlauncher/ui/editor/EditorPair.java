package org.tlauncher.tlauncher.ui.editor;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.extended.VPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorPair.class */
public class EditorPair {
    private final LocalizableLabel label;
    private final List<? extends EditorHandler> handlers;
    private final JComponent[] fields;
    private final VPanel panel;

    public EditorPair(String labelPath, List<? extends EditorHandler> handlers) {
        this.label = new LocalizableLabel(labelPath);
        this.label.setFont(this.label.getFont().deriveFont(1));
        int num = handlers.size();
        this.fields = new JComponent[num];
        for (int i = 0; i < num; i++) {
            this.fields[i] = handlers.get(i).getComponent();
            this.fields[i].setAlignmentX(0.0f);
        }
        this.handlers = handlers;
        this.panel = new VPanel();
        this.panel.add((Component[]) this.fields);
    }

    public EditorPair(String labelPath, EditorHandler... handlers) {
        this(labelPath, Arrays.asList(handlers));
    }

    public List<? extends EditorHandler> getHandlers() {
        return this.handlers;
    }

    public LocalizableLabel getLabel() {
        return this.label;
    }

    public Component[] getFields() {
        return this.fields;
    }

    public VPanel getPanel() {
        return this.panel;
    }
}
