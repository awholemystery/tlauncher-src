package org.tlauncher.tlauncher.ui.editor;

import org.tlauncher.util.Range;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorIntegerRangeField.class */
public class EditorIntegerRangeField extends EditorIntegerField {
    private final Range<Integer> range;

    public EditorIntegerRangeField(String placeholder, Range<Integer> range) {
        if (range == null) {
            throw new NullPointerException("range");
        }
        this.range = range;
        setPlaceholder(placeholder);
    }

    public EditorIntegerRangeField(Range<Integer> range) {
        this(null, range);
        setPlaceholder("settings.range", range.getMinValue(), range.getMaxValue());
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorIntegerField, org.tlauncher.tlauncher.ui.editor.EditorTextField, org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        try {
            return this.range.fits(Integer.valueOf(Integer.parseInt(getSettingsValue())));
        } catch (Exception e) {
            return false;
        }
    }
}
