package org.tlauncher.tlauncher.ui.ui;

import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/CreationModpackGameVersionComboboxUI.class */
public class CreationModpackGameVersionComboboxUI extends CreationModpackComboBoxUI {
    @Override // org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI
    public String getText(Object ob) {
        GameVersionDTO gv = (GameVersionDTO) ob;
        Long l = 0L;
        if (l.equals(gv.getId())) {
            return Localizable.get("modpack.version.any");
        }
        return gv.getName();
    }
}
