package org.tlauncher.tlauncher.ui.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/TextPopup.class */
public class TextPopup extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
        JPopupMenu popup;
        if (e.getModifiers() != 4) {
            return;
        }
        Object source = e.getSource();
        if (!(source instanceof JTextComponent) || (popup = getPopup(e, (JTextComponent) source)) == null) {
            return;
        }
        popup.show(e.getComponent(), e.getX(), e.getY() - popup.getSize().height);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JPopupMenu getPopup(MouseEvent e, final JTextComponent comp) {
        Action copyAll;
        if (!comp.isEnabled()) {
            return null;
        }
        boolean isEditable = comp.isEditable();
        boolean isSelected = comp.getSelectedText() != null;
        boolean hasValue = StringUtils.isNotEmpty(comp.getText());
        boolean pasteAvailable = isEditable && Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null).isDataFlavorSupported(DataFlavor.stringFlavor);
        JPopupMenu menu = new JPopupMenu();
        Action cut = isEditable ? selectAction(comp, "cut-to-clipboard", "cut") : null;
        final Action copy = selectAction(comp, "copy-to-clipboard", "copy");
        Action paste = pasteAvailable ? selectAction(comp, "paste-from-clipboard", "paste") : null;
        final Action selectAll = hasValue ? selectAction(comp, "select-all", "selectAll") : null;
        if (selectAll != null && copy != null) {
            copyAll = new EmptyAction() { // from class: org.tlauncher.tlauncher.ui.swing.TextPopup.1
                public void actionPerformed(ActionEvent e2) {
                    selectAll.actionPerformed(e2);
                    copy.actionPerformed(e2);
                    comp.setSelectionStart(comp.getSelectionEnd());
                }
            };
        } else {
            copyAll = null;
        }
        if (cut != null) {
            menu.add(cut).setText(Localizable.get("popup.cut"));
        }
        if (isSelected && copy != null) {
            menu.add(copy).setText(Localizable.get("popup.copy"));
        }
        if (paste != null) {
            menu.add(paste).setText(Localizable.get("popup.paste"));
        }
        if (selectAll != null) {
            if (menu.getComponentCount() > 0 && !(menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator)) {
                menu.addSeparator();
            }
            menu.add(selectAll).setText(Localizable.get("popup.selectall"));
        }
        if (copyAll != null) {
            menu.add(copyAll).setText(Localizable.get("popup.copyall"));
        }
        if (menu.getComponentCount() == 0) {
            return null;
        }
        if (menu.getComponent(0) instanceof JPopupMenu.Separator) {
            menu.remove(0);
        }
        if (menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator) {
            menu.remove(menu.getComponentCount() - 1);
        }
        return menu;
    }

    protected static Action selectAction(JTextComponent comp, String general, String fallback) {
        Action action = comp.getActionMap().get(general);
        if (action == null) {
            action = comp.getActionMap().get(fallback);
        }
        return action;
    }
}
