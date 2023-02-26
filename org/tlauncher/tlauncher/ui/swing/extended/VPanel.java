package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import org.tlauncher.util.Reflect;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/VPanel.class */
public class VPanel extends ExtendedPanel {
    private VPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setLayout(new BoxLayout(this, 3));
    }

    public VPanel() {
        this(true);
    }

    /* renamed from: getLayout */
    public BoxLayout m689getLayout() {
        return super.getLayout();
    }

    public void setLayout(LayoutManager mgr) {
        if (!(mgr instanceof BoxLayout)) {
            return;
        }
        int axis = ((BoxLayout) Reflect.cast(mgr, BoxLayout.class)).getAxis();
        if (axis == 3 || axis == 1) {
            super.setLayout(mgr);
            return;
        }
        throw new IllegalArgumentException("Illegal BoxLayout axis!");
    }
}
