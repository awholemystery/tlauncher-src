package org.tlauncher.tlauncher.ui.ui;

import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/CreationMinecraftTypeComboboxUI.class */
public class CreationMinecraftTypeComboboxUI extends CreationModpackComboBoxUI {
    @Override // org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI
    public String getText(Object value) {
        return Localizable.get("modpack.version." + ((NameIdDTO) value).getName());
    }
}
