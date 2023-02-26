package org.tlauncher.tlauncher.ui.block;

import java.awt.Container;
import javax.swing.JLayeredPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/block/BlockableLayeredPane.class */
public class BlockableLayeredPane extends JLayeredPane implements Blockable {
    private static final long serialVersionUID = 1;

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents((Container) this, reason);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents((Container) this, reason);
    }
}
