package org.tlauncher.tlauncher.ui.swing.box;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/box/TlauncherCustomBox.class */
public class TlauncherCustomBox<T> extends JComboBox<T> {
    public TlauncherCustomBox() {
        init();
    }

    public TlauncherCustomBox(ComboBoxModel<T> aModel) {
        super(aModel);
        init();
    }

    public TlauncherCustomBox(T[] items) {
        super(items);
        setModel(new DefaultComboBoxModel(items));
        init();
    }

    protected void init() {
        setUI(new TlauncherBasicComboBoxUI());
        setBorder(BorderFactory.createEmptyBorder());
    }
}
