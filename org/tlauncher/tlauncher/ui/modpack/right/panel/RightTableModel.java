package org.tlauncher.tlauncher.ui.modpack.right.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.tlauncher.modpack.domain.client.GameEntityDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/RightTableModel.class */
public class RightTableModel<T extends GameEntityDTO> extends AbstractTableModel {
    private static final long serialVersionUID = -4513723800530818L;
    private List<T> data = new ArrayList();

    public int getRowCount() {
        return this.data.size();
    }

    public int getColumnCount() {
        return 1;
    }

    /* renamed from: getValueAt */
    public T m650getValueAt(int rowIndex, int columnIndex) {
        return this.data.get(rowIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return GameEntityDTO.class;
    }

    public void addElements(List<T> list, boolean clean) {
        if (clean) {
            this.data.clear();
        }
        this.data.addAll(list);
    }
}
