package org.tlauncher.tlauncher.ui.editor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.tlauncher.tlauncher.ui.converter.StringConverter;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorComboBox.class */
public class EditorComboBox<T> extends ExtendedComboBox<T> implements EditorField {
    private static final long serialVersionUID = -2320340434786516374L;
    private final boolean allowNull;

    public EditorComboBox(StringConverter<T> converter, T[] values, boolean allowNull) {
        super(converter);
        this.allowNull = allowNull;
        if (values == null) {
            return;
        }
        for (T value : values) {
            addItem(value);
        }
    }

    public EditorComboBox(StringConverter<T> converter, T[] values) {
        this(converter, values, false);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        T value = getSelectedValue();
        return convert((EditorComboBox<T>) value);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String string) {
        T value = convert(string);
        if (!this.allowNull && string == null) {
            boolean hasNull = false;
            for (int i = 0; i < getItemCount(); i++) {
                if (getItemAt(i) == null) {
                    hasNull = true;
                }
            }
            if (!hasNull) {
                return;
            }
        }
        setSelectedValue((EditorComboBox<T>) value);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        return true;
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.box.TlauncherCustomBox
    protected void init() {
        TlauncherBasicComboBoxUI list = new TlauncherBasicComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.editor.EditorComboBox.1
            @Override // org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI
            protected ComboPopup createPopup() {
                BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.editor.EditorComboBox.1.1
                    protected JScrollPane createScroller() {
                        VersionScrollBarUI barUI = new VersionScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.editor.EditorComboBox.1.1.1
                            @Override // org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI
                            protected Dimension getMinimumThumbSize() {
                                return new Dimension(10, 40);
                            }

                            public Dimension getMaximumSize(JComponent c) {
                                Dimension dim = super.getMaximumSize(c);
                                dim.setSize(10.0d, dim.getHeight());
                                return dim;
                            }

                            public Dimension getPreferredSize(JComponent c) {
                                Dimension dim = super.getPreferredSize(c);
                                dim.setSize(10.0d, dim.getHeight());
                                return dim;
                            }
                        };
                        barUI.setGapThubm(5);
                        JScrollPane scroller = new JScrollPane(this.list, 20, 31);
                        scroller.getVerticalScrollBar().setUI(barUI);
                        return scroller;
                    }
                };
                basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
                return basic;
            }
        };
        setUI(list);
    }
}
