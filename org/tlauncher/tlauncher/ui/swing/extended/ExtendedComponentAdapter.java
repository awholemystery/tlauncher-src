package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.Component;
import java.awt.event.ComponentEvent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedComponentAdapter.class */
public class ExtendedComponentAdapter extends ExtendedComponentListener {
    public ExtendedComponentAdapter(Component component, int tick) {
        super(component, tick);
    }

    public ExtendedComponentAdapter(Component component) {
        super(component);
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
    public void onComponentResizing(ComponentEvent e) {
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
    public void onComponentResized(ComponentEvent e) {
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
    public void onComponentMoving(ComponentEvent e) {
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
    public void onComponentMoved(ComponentEvent e) {
    }
}
