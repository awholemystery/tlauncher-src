package org.tlauncher.tlauncher.ui.listener.mods;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JScrollBar;
import javax.swing.JTable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/ModpackAdjustmentListener.class */
public abstract class ModpackAdjustmentListener implements AdjustmentListener {
    private JTable table;

    public abstract void processed();

    public ModpackAdjustmentListener(JTable table) {
        this.table = table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModpackAdjustmentListener) {
            ModpackAdjustmentListener other = (ModpackAdjustmentListener) o;
            if (other.canEqual(this)) {
                Object this$table = getTable();
                Object other$table = other.getTable();
                return this$table == null ? other$table == null : this$table.equals(other$table);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ModpackAdjustmentListener;
    }

    public int hashCode() {
        Object $table = getTable();
        int result = (1 * 59) + ($table == null ? 43 : $table.hashCode());
        return result;
    }

    public String toString() {
        return "ModpackAdjustmentListener(table=" + getTable() + ")";
    }

    public JTable getTable() {
        return this.table;
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (!e.getValueIsAdjusting() && this.table.getModel().getRowCount() != 0) {
            JScrollBar scrollBar = e.getAdjustable();
            int extent = scrollBar.getModel().getExtent();
            int maximum = scrollBar.getModel().getMaximum();
            if (extent + e.getValue() == maximum) {
                processed();
            }
        }
    }
}
