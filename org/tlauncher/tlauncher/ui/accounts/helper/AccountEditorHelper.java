package org.tlauncher.tlauncher.ui.accounts.helper;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Point;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.accounts.AccountEditor;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/helper/AccountEditorHelper.class */
public class AccountEditorHelper extends ExtendedLayeredPane {
    static final int MARGIN = 5;
    static final byte LEFT = 0;
    static final byte UP = 1;
    static final byte RIGHT = 2;
    static final byte DOWN = 3;
    private final MainPane pane;
    private final HelperTip[] tips;

    public AccountEditorHelper(AccountEditorScene scene) {
        super(scene);
        this.pane = scene.getMainPane();
        AccountEditor editor = scene.editor;
        this.tips = new HelperTip[]{new HelperTip("account", editor, editor, (byte) 1, HelperState.PREMIUM, HelperState.FREE), new HelperTip("username", editor.username, editor, (byte) 0, HelperState.PREMIUM, HelperState.FREE), new HelperTip("password", editor.password, editor, (byte) 0, HelperState.PREMIUM), new HelperTip("button", editor.save, editor, (byte) 0, HelperState.PREMIUM, HelperState.FREE)};
        add((Component[]) this.tips);
    }

    public void setState(HelperState state) {
        HelperState[] values;
        HelperTip[] helperTipArr;
        int x;
        int y;
        if (state == null) {
            throw new NullPointerException();
        }
        for (HelperState st : HelperState.values()) {
            st.item.setEnabled(!st.equals(state));
        }
        if (state == HelperState.NONE) {
            for (HelperTip step : this.tips) {
                step.setVisible(false);
            }
            return;
        }
        for (HelperTip step2 : this.tips) {
            LocalizableLabel l = step2.label;
            l.setText("auth.helper." + state.toString() + "." + step2.name);
            Component c = step2.component;
            int cWidth = c.getWidth();
            int cHeight = c.getHeight();
            Point cp = this.pane.getLocationOf(c);
            Component p = step2.parent;
            int pWidth = p.getWidth();
            int pHeight = p.getHeight();
            Point pp = this.pane.getLocationOf(p);
            FontMetrics fm = l.getFontMetrics(l.getFont());
            Insets i = step2.getInsets();
            int height = i.top + i.bottom + fm.getHeight();
            int width = i.left + i.right + fm.stringWidth(l.getText());
            switch (step2.alignment) {
                case 0:
                    x = (pp.x - 5) - width;
                    y = (cp.y + (cHeight / 2)) - (height / 2);
                    break;
                case 1:
                    x = (cp.x + (cWidth / 2)) - (width / 2);
                    y = (pp.y - 5) - height;
                    break;
                case 2:
                    x = pp.x + pWidth + 5;
                    y = (cp.y + (cHeight / 2)) - (height / 2);
                    break;
                case 3:
                    x = (cp.x + (cWidth / 2)) - (width / 2);
                    y = pp.y + pHeight + 5;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown alignment");
            }
            if (x < 0) {
                x = 0;
            } else if (x + width > getWidth()) {
                x = getWidth() - width;
            }
            if (y < 0) {
                y = 0;
            } else if (y + height > getHeight()) {
                y = getHeight() - height;
            }
            step2.setVisible(true);
            step2.setBounds(x, y, width, height);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
    }
}
