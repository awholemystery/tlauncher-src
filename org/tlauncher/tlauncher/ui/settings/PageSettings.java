package org.tlauncher.tlauncher.ui.settings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/PageSettings.class */
public abstract class PageSettings extends ExtendedPanel implements SettingsHandlerInterface {
    private static final long serialVersionUID = 971905170736637142L;
    private final List<HandlerSettings> settingsList = new ArrayList();
    protected TLauncher tlauncher = TLauncher.getInstance();
    protected final Configuration global = this.tlauncher.getConfiguration();

    @Override // org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface
    public boolean validateSettings() {
        for (HandlerSettings handler : this.settingsList) {
            if (!handler.getEditorField().isValueValid()) {
                handler.getEditorField().setBackground(Color.PINK);
                return false;
            }
        }
        return true;
    }

    @Override // org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface
    public void setValues() {
        for (HandlerSettings handler : this.settingsList) {
            String key = handler.getKey();
            String oldValue = this.global.get(key);
            String newValue = handler.getEditorField().getSettingsValue();
            if (!StringUtils.equals(oldValue, newValue)) {
                this.global.set(key, newValue);
                handler.onChange(oldValue, newValue);
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface
    public void setDefaultSettings() {
        for (HandlerSettings handler : this.settingsList) {
            handler.getEditorField().setSettingsValue(this.global.getDefault(handler.getKey()));
        }
    }

    public void addHandler(HandlerSettings handler) {
        addFocus((Component) handler.getEditorField(), new FocusListener() { // from class: org.tlauncher.tlauncher.ui.settings.PageSettings.1
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(Color.white);
            }
        });
        this.settingsList.add(handler);
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

    public void init() {
        for (HandlerSettings handler : this.settingsList) {
            handler.getEditorField().setSettingsValue(this.global.get(handler.getKey()));
        }
    }
}
