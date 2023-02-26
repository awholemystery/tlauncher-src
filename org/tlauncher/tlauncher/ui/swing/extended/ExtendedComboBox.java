package org.tlauncher.tlauncher.ui.swing.extended;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.converter.StringConverter;
import org.tlauncher.tlauncher.ui.swing.DefaultConverterCellRenderer;
import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
import org.tlauncher.tlauncher.ui.swing.box.TlauncherCustomBox;
import org.tlauncher.util.Reflect;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedComboBox.class */
public class ExtendedComboBox<T> extends TlauncherCustomBox<T> {
    private static final long serialVersionUID = -4509947341182373649L;
    private StringConverter<T> converter;

    public ExtendedComboBox(ListCellRenderer<T> renderer) {
        setModel(new SimpleComboBoxModel());
        setRenderer(renderer);
        setOpaque(false);
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
        ((JComponent) Reflect.cast(getEditor().getEditorComponent(), JComponent.class)).setOpaque(false);
    }

    public ExtendedComboBox(StringConverter<T> converter) {
        this(new DefaultConverterCellRenderer(converter));
        this.converter = converter;
    }

    public ExtendedComboBox() {
        this((ListCellRenderer) null);
    }

    public SimpleComboBoxModel<T> getSimpleModel() {
        return getModel();
    }

    public T getValueAt(int i) {
        Object value = getItemAt(i);
        return returnAs(value);
    }

    public T getSelectedValue() {
        Object selected = getSelectedItem();
        return returnAs(selected);
    }

    public void setSelectedValue(T value) {
        setSelectedItem(value);
    }

    public void setSelectedValue(String string) {
        T value = convert(string);
        if (value == null) {
            return;
        }
        setSelectedValue((ExtendedComboBox<T>) value);
    }

    public StringConverter<T> getConverter() {
        return this.converter;
    }

    public void setConverter(StringConverter<T> converter) {
        this.converter = converter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String convert(T obj) {
        T from = returnAs(obj);
        if (this.converter != null) {
            return this.converter.toValue(from);
        }
        if (from == null) {
            return null;
        }
        return from.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T convert(String from) {
        if (this.converter == null) {
            return null;
        }
        return this.converter.fromString(from);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private T returnAs(Object obj) {
        return obj;
    }
}
