package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/BorderPanel.class */
public class BorderPanel extends ExtendedPanel {
    private static final long serialVersionUID = -7641580330557833990L;

    private BorderPanel(BorderLayout layout, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setLayout(layout == null ? new BorderLayout() : layout);
    }

    public BorderPanel() {
        this((BorderLayout) null, true);
    }

    public BorderPanel(int hgap, int vgap) {
        this();
        setHgap(hgap);
        setVgap(vgap);
    }

    /* renamed from: getLayout */
    public BorderLayout m685getLayout() {
        return super.getLayout();
    }

    public void setLayout(LayoutManager mgr) {
        if (mgr instanceof BorderLayout) {
            super.setLayout(mgr);
        }
    }

    public int getHgap() {
        return m685getLayout().getHgap();
    }

    public void setHgap(int hgap) {
        m685getLayout().setHgap(hgap);
    }

    public int getVgap() {
        return m685getLayout().getVgap();
    }

    public void setVgap(int vgap) {
        m685getLayout().setVgap(vgap);
    }

    public void setNorth(Component comp) {
        add(comp, "North");
    }

    public void setEast(Component comp) {
        add(comp, "East");
    }

    public void setSouth(Component comp) {
        add(comp, "South");
    }

    public void setWest(Component comp) {
        add(comp, "West");
    }

    public void setCenter(Component comp) {
        add(comp, "Center");
    }
}
