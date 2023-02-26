package org.tlauncher.tlauncher.ui.block;

import java.awt.Container;
import java.awt.LayoutManager;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/block/BlockablePanel.class */
public class BlockablePanel extends ExtendedPanel implements Blockable {
    private static final long serialVersionUID = 1;

    public BlockablePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public BlockablePanel(LayoutManager layout) {
        super(layout);
    }

    public BlockablePanel() {
    }

    public void block(Object reason) {
        setEnabled(false);
        Blocker.blockComponents((Container) this, reason);
    }

    public void unblock(Object reason) {
        setEnabled(true);
        Blocker.unblockComponents((Container) this, reason);
    }
}
