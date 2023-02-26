package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/JTableButtonRenderer.class */
public class JTableButtonRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (Component) value;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return (Component) value;
    }

    public Object getCellEditorValue() {
        return null;
    }
}
