package org.tlauncher.tlauncher.ui.editor;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLabel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.IntegerArray;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorResolutionField.class */
public class EditorResolutionField extends ExtendedPanel implements EditorField {
    private static final long serialVersionUID = -5565607141889620750L;
    private EditorIntegerField w;
    private EditorIntegerField h;
    private ExtendedLabel x;
    private final int[] defaults;

    public EditorResolutionField(String promptW, String promptH, int[] defaults, boolean showDefault) {
        if (defaults == null) {
            throw new NullPointerException();
        }
        if (defaults.length != 2) {
            throw new IllegalArgumentException("Illegal array size");
        }
        this.defaults = defaults;
        setLayout(new BoxLayout(this, 0));
        setPreferredSize(new Dimension(161, 21));
        this.w = new EditorIntegerField(promptW);
        this.w.setColumns(4);
        this.w.setHorizontalAlignment(0);
        this.w.setPreferredSize(new Dimension(70, 21));
        this.h = new EditorIntegerField(promptH);
        this.h.setColumns(4);
        this.h.setHorizontalAlignment(0);
        this.h.setPreferredSize(new Dimension(70, 21));
        this.x = new ExtendedLabel("X", 0);
        this.x.setFont(this.x.getFont().deriveFont(1));
        add((Component) this.w);
        add(Box.createHorizontalStrut(6));
        add((Component) this.x);
        add(Box.createHorizontalStrut(6));
        add((Component) this.h);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        return this.w.getSettingsValue() + ';' + this.h.getSettingsValue();
    }

    int[] getResolution() {
        try {
            IntegerArray arr = IntegerArray.parseIntegerArray(getSettingsValue());
            return arr.toArray();
        } catch (Exception e) {
            return new int[2];
        }
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        int[] size = getResolution();
        return size[0] >= 1 && size[1] >= 1;
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
        String width;
        String height;
        try {
            IntegerArray arr = IntegerArray.parseIntegerArray(value);
            width = String.valueOf(arr.get(0));
            height = String.valueOf(arr.get(1));
        } catch (Exception e) {
            width = CoreConstants.EMPTY_STRING;
            height = CoreConstants.EMPTY_STRING;
        }
        this.w.setText(width);
        this.h.setText(height);
    }

    public void setBackground(Color bg) {
        if (this.w != null) {
            this.w.setBackground(bg);
        }
        if (this.h != null) {
            this.h.setBackground(bg);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents(reason, this.w, this.h);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(Blocker.UNIVERSAL_UNBLOCK, this.w, this.h);
    }
}
