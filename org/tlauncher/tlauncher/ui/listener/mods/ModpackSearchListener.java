package org.tlauncher.tlauncher.ui.listener.mods;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/ModpackSearchListener.class */
public class ModpackSearchListener implements ActionListener {
    private ModpackScene scene;
    private String previousValue;
    private LocalizableTextField field;

    public ModpackSearchListener(ModpackScene scene, String previousValue, LocalizableTextField field) {
        this.scene = scene;
        this.previousValue = previousValue;
        this.field = field;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.scene.isVisible() && !StringUtils.equals(this.previousValue, this.field.getValue())) {
            this.previousValue = this.field.getValue();
            this.scene.fillGameEntitiesPanel(true);
        }
    }
}
