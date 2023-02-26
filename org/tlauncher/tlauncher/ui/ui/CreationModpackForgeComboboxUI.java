package org.tlauncher.tlauncher.ui.ui;

import org.tlauncher.modpack.domain.client.share.NameIdDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/CreationModpackForgeComboboxUI.class */
public class CreationModpackForgeComboboxUI extends CreationModpackComboBoxUI {
    @Override // org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI
    public String getText(Object ob) {
        return ((NameIdDTO) this.comboBox.getSelectedItem()).getName();
    }
}
