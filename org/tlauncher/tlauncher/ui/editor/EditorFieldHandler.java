package org.tlauncher.tlauncher.ui.editor;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.block.Blocker;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorFieldHandler.class */
public class EditorFieldHandler extends EditorHandler {
    private final EditorField field;
    private final JComponent comp;

    public EditorFieldHandler(String path, JComponent component, FocusListener focus) {
        super(path);
        if (component == null) {
            throw new NullPointerException("comp");
        }
        if (!(component instanceof EditorField)) {
            throw new IllegalArgumentException();
        }
        if (focus != null) {
            addFocus(component, focus);
        }
        this.comp = component;
        this.field = (EditorField) component;
    }

    public EditorFieldHandler(String path, JComponent comp) {
        this(path, comp, null);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorHandler
    public JComponent getComponent() {
        return this.comp;
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorHandler
    public String getValue() {
        return this.field.getSettingsValue();
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorHandler
    protected void setValue0(String s) {
        this.field.setSettingsValue(s);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorHandler
    public boolean isValid() {
        return this.field.isValueValid();
    }

    private void addFocus(Component comp, FocusListener focus) {
        Component[] components;
        comp.addFocusListener(focus);
        if (comp instanceof Container) {
            for (Component curComp : ((Container) comp).getComponents()) {
                addFocus(curComp, focus);
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents(reason, getComponent());
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(reason, getComponent());
    }
}
