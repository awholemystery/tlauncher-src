package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/ModpackTableInstallButton.class */
public class ModpackTableInstallButton extends TableActButton {
    private VersionDTO selectedVersion;
    private ModpackManager manager;

    public ModpackTableInstallButton(GameEntityDTO entity, final GameType type, ModpackComboBox localmodpacks) {
        super(entity, type, localmodpacks);
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.installButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableInstallButton.1
            public void actionPerformed(ActionEvent e) {
                ModpackTableInstallButton.this.manager.installEntity(ModpackTableInstallButton.this.getEntity(), ModpackTableInstallButton.this.selectedVersion, type, true);
            }
        });
        this.removeButton.addActionListener(e -> {
            this.manager.removeEntity(entity, this.selectedVersion, entity);
        });
    }

    @Override // org.tlauncher.tlauncher.ui.loc.modpack.TableActButton, org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton
    public void initButton() {
        for (GameEntityDTO e : getSelectedModpackData()) {
            if (this.entity.getId().equals(e.getId())) {
                this.selectedVersion = e.getVersion();
                setTypeButton(ModpackActButton.REMOVE);
                return;
            }
        }
        if (!GameType.MODPACK.equals(this.type)) {
            if (this.localmodpacks.getSelectedIndex() > 0) {
                ModpackVersionDTO modpackVersionDTO = (ModpackVersionDTO) ((CompleteVersion) this.localmodpacks.getSelectedItem()).getModpack().getVersion();
                try {
                    GameVersionDTO gv = this.manager.getGameVersion(modpackVersionDTO);
                    if (this.entity.getVersion().getGameVersionsDTO().contains(gv)) {
                        setTypeButton(ModpackActButton.INSTALL);
                        return;
                    }
                } catch (IOException e1) {
                    U.log(e1);
                }
            }
            setTypeButton(ModpackActButton.DENIED_OPERATION);
            return;
        }
        setTypeButton(ModpackActButton.INSTALL);
    }
}
