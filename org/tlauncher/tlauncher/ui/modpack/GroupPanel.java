package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Component;
import java.awt.GridBagConstraints;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/GroupPanel.class */
public class GroupPanel extends ShadowPanel {
    private static final long serialVersionUID = -408137826817737500L;
    private GridBagConstraints gbc;
    private ButtonGroup group;

    public GroupPanel(int colorStarted) {
        super(colorStarted);
        this.gbc = new GridBagConstraints();
        this.group = new ButtonGroup();
        this.gbc.fill = 1;
        GridBagConstraints gridBagConstraints = this.gbc;
        this.gbc.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.gbc.weightx = 1.0d;
        this.gbc.weighty = 1.0d;
        this.gbc.anchor = 10;
    }

    public void addInGroup(AbstractButton button, int index) {
        this.gbc.gridx = index;
        this.group.add(button);
        add((Component) button, (Object) this.gbc);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
    public Component add(Component comp, int index) {
        this.gbc.gridx = index;
        add(comp, this.gbc);
        return null;
    }
}
