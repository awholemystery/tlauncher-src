package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.Color;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/CreationMinecraftTypeComboboxRenderer.class */
public class CreationMinecraftTypeComboboxRenderer extends ModpackComboBoxRendererBasic {
    static final int GUP_LEFT = 13;
    public static final Color LINE = new Color(149, 149, 149);
    public static final Color TEXT_COLOR = new Color(25, 25, 25);

    @Override // org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboBoxRendererBasic
    public String getRenderText(Object value) {
        return Localizable.get("modpack.version." + ((NameIdDTO) value).getName());
    }
}
