package org.tlauncher.tlauncher.ui.swing.box;

import ch.qos.logback.core.CoreConstants;
import javax.swing.DefaultComboBoxModel;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/box/LocalModapackBoxModel.class */
public class LocalModapackBoxModel extends DefaultComboBoxModel<CompleteVersion> {
    private final CompleteVersion DEFAULT = new CompleteVersion();

    public LocalModapackBoxModel() {
        this.DEFAULT.setID(Localizable.get("modpack.local.box.default"));
        ModpackDTO d = new ModpackDTO();
        d.setId(0L);
        d.setName(CoreConstants.EMPTY_STRING);
        this.DEFAULT.setModpackDTO(d);
        addElement(this.DEFAULT);
        setSelectedItem(this.DEFAULT);
    }

    public void removeAllElements() {
        super.removeAllElements();
        addElement(this.DEFAULT);
    }

    public void removeElement(Object anObject) {
        if (anObject == this.DEFAULT) {
            return;
        }
        super.removeElement(anObject);
    }

    public void removeElementAt(int index) {
        if (index == 0) {
            return;
        }
        super.removeElementAt(index);
    }
}
