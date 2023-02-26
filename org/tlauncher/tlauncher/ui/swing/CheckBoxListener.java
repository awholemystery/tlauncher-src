package org.tlauncher.tlauncher.ui.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/CheckBoxListener.class */
public abstract class CheckBoxListener implements ItemListener {
    public abstract void itemStateChanged(boolean z);

    public void itemStateChanged(ItemEvent e) {
        itemStateChanged(e.getStateChange() == 1);
    }
}
