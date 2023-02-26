package org.tlauncher.tlauncher.ui.loc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JMenuItem;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableMenuItem.class */
public class LocalizableMenuItem extends JMenuItem implements LocalizableComponent {
    private static final long serialVersionUID = 1364363532569997394L;
    private static List<LocalizableMenuItem> items = Collections.synchronizedList(new ArrayList());
    private String path;
    private String[] variables;

    public LocalizableMenuItem(String path, Object... vars) {
        items.add(this);
        setText(path, vars);
    }

    public LocalizableMenuItem(String path) {
        this(path, Localizable.EMPTY_VARS);
    }

    public void setText(String path, Object... vars) {
        this.path = path;
        this.variables = Localizable.checkVariables(vars);
        String value = Localizable.get(path);
        for (int i = 0; i < this.variables.length; i++) {
            value = value.replace("%" + i, this.variables[i]);
        }
        super.setText(value);
    }

    public void setText(String path) {
        setText(path, Localizable.EMPTY_VARS);
    }

    public void setVariables(Object... vars) {
        setText(this.path, vars);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setText(this.path, this.variables);
    }

    public static void updateLocales() {
        for (LocalizableMenuItem item : items) {
            if (item != null) {
                item.updateLocale();
            }
        }
    }
}
