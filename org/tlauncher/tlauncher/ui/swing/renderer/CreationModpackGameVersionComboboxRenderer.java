package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.Color;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/CreationModpackGameVersionComboboxRenderer.class */
public class CreationModpackGameVersionComboboxRenderer extends ModpackComboBoxRendererBasic {
    private static final long serialVersionUID = 4805241656738015345L;
    static final int GUP_LEFT = 13;
    public static final Color LINE = new Color(149, 149, 149);
    public static final Color TEXT_COLOR = new Color(25, 25, 25);

    @Override // org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboBoxRendererBasic
    public String getRenderText(Object value) {
        GameVersionDTO gv = (GameVersionDTO) value;
        Long l = 0L;
        if (l.equals(gv.getId())) {
            return Localizable.get("modpack.version.any");
        }
        return gv.getName();
    }
}
