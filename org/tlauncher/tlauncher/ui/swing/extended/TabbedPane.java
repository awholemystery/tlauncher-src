package org.tlauncher.tlauncher.ui.swing.extended;

import javax.swing.JTabbedPane;
import org.tlauncher.tlauncher.ui.swing.util.Orientation;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/TabbedPane.class */
public class TabbedPane extends JTabbedPane {
    public TabbedPane(Orientation tabLocation, TabLayout layout) {
        setTabLocation(tabLocation == null ? Orientation.TOP : tabLocation);
        setTabLayout(layout == null ? TabLayout.SCROLL : layout);
    }

    public TabbedPane() {
        this(null, null);
    }

    public ExtendedUI getExtendedUI() {
        ExtendedUI ui = getUI();
        if (ui instanceof ExtendedUI) {
            return ui;
        }
        return null;
    }

    public void setTabLocation(Orientation direction) {
        if (direction == null) {
            throw new NullPointerException();
        }
        setTabPlacement(direction.getSwingAlias());
    }

    public TabLayout getTabLayout() {
        return TabLayout.fromSwingConstant(getTabLayoutPolicy());
    }

    public void setTabLayout(TabLayout layout) {
        if (layout == null) {
            throw new NullPointerException();
        }
        setTabLayoutPolicy(layout.getSwingAlias());
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/TabbedPane$TabLayout.class */
    public enum TabLayout {
        WRAP(0),
        SCROLL(1);
        
        private final int swingAlias;

        TabLayout(int swingAlias) {
            this.swingAlias = swingAlias;
        }

        public int getSwingAlias() {
            return this.swingAlias;
        }

        public static TabLayout fromSwingConstant(int orientation) {
            TabLayout[] values;
            for (TabLayout current : values()) {
                if (orientation == current.getSwingAlias()) {
                    return current;
                }
            }
            return null;
        }
    }
}
