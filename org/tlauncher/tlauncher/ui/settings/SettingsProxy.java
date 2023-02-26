package org.tlauncher.tlauncher.ui.settings;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.editor.EditorField;
import org.tlauncher.tlauncher.ui.editor.EditorIntegerRangeField;
import org.tlauncher.tlauncher.ui.editor.EditorTextField;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
import org.tlauncher.util.Range;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingsProxy.class */
public class SettingsProxy extends VPanel implements EditorField {
    private static final String path = "settings.connection.proxy";
    private static final String block = "proxyselect";
    private final ExtendedPanel proxyTypePanel;
    private final LinkedHashMap<Proxy.Type, ProxyLocRadio> typeMap = new LinkedHashMap<>();
    private final ButtonGroup group = new ButtonGroup();
    private final ProxySettingsPanel proxySettingsPanel;
    private final EditorTextField addressField;
    private final EditorTextField portField;

    SettingsProxy() {
        setAlignmentX(0.0f);
        List<Proxy.Type> typeList = Arrays.asList(Proxy.Type.values());
        for (Proxy.Type type : typeList) {
            ProxyLocRadio radio = new ProxyLocRadio();
            radio.setText(type.name().toLowerCase());
            radio.setAlignmentX(0.0f);
            radio.setOpaque(false);
            this.group.add(radio);
            this.typeMap.put(type, radio);
        }
        this.typeMap.get(Proxy.Type.DIRECT).addItemListener(new ItemListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsProxy.1
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == 1;
                if (selected) {
                    Blocker.block(SettingsProxy.this.proxySettingsPanel, SettingsProxy.block);
                }
            }
        });
        this.proxyTypePanel = new ExtendedPanel();
        this.proxyTypePanel.setAlignmentX(0.0f);
        add((Component) this.proxyTypePanel);
        ItemListener listener = new ItemListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsProxy.2
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == 1;
                if (selected) {
                    Blocker.unblock(SettingsProxy.this.proxySettingsPanel, SettingsProxy.block);
                }
            }
        };
        for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
            this.proxyTypePanel.add((Component) en.getValue());
            if (en.getKey() != Proxy.Type.DIRECT) {
                en.getValue().addItemListener(listener);
            }
        }
        this.proxySettingsPanel = new ProxySettingsPanel();
        this.proxySettingsPanel.setAlignmentX(0.0f);
        add((Component) this.proxySettingsPanel);
        this.addressField = new EditorTextField("settings.connection.proxy.address", false);
        this.proxySettingsPanel.setCenter(this.addressField);
        this.portField = new EditorIntegerRangeField("settings.connection.proxy.port", new Range(0, 65535));
        this.portField.setColumns(5);
        this.proxySettingsPanel.setEast(this.portField);
    }

    private Map.Entry<Proxy.Type, ProxyLocRadio> getSelectedType() {
        for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
            if (en.getValue().isSelected()) {
                return en;
            }
        }
        return null;
    }

    private void setSelectedType(Proxy.Type type) {
        for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
            if (en.getKey() == type) {
                en.getValue().setSelected(true);
                return;
            }
        }
        this.typeMap.get(Proxy.Type.DIRECT).setSelected(true);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        Map.Entry<Proxy.Type, ProxyLocRadio> selected = getSelectedType();
        if (selected == null || selected.getKey() == Proxy.Type.DIRECT) {
            U.log("selected is", selected, "so null");
            return null;
        }
        U.log(selected.getKey().name().toLowerCase() + ';' + this.addressField.getValue() + ';' + this.portField.getValue());
        return selected.getKey().name().toLowerCase() + ';' + this.addressField.getValue() + ';' + this.portField.getValue();
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        Map.Entry<Proxy.Type, ProxyLocRadio> selected = getSelectedType();
        if (selected == null || selected.getKey() == Proxy.Type.DIRECT) {
            return true;
        }
        return this.addressField.isValueValid() && this.portField.isValueValid();
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents((Container) this, reason);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents((Container) this, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingsProxy$ProxyLocRadio.class */
    public class ProxyLocRadio extends JRadioButton implements LocalizableComponent {
        private String currentType;

        private ProxyLocRadio() {
        }

        public void setText(String proxyType) {
            this.currentType = proxyType;
            String text = Localizable.get("settings.connection.proxy.type." + proxyType);
            if (StringUtils.isBlank(text)) {
                text = proxyType;
            }
            super.setText(text);
        }

        @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
        public void updateLocale() {
            setText(this.currentType);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingsProxy$ProxySettingsPanel.class */
    private class ProxySettingsPanel extends BorderPanel implements Blockable {
        private ProxySettingsPanel() {
        }

        @Override // org.tlauncher.tlauncher.ui.block.Blockable
        public void block(Object reason) {
            Blocker.blockComponents((Container) this, reason);
        }

        @Override // org.tlauncher.tlauncher.ui.block.Blockable
        public void unblock(Object reason) {
            Blocker.unblockComponents((Container) this, reason);
        }
    }
}
