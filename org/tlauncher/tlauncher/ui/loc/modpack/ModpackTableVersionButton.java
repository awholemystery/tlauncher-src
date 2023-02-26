package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/ModpackTableVersionButton.class */
public class ModpackTableVersionButton extends TableActButton {
    private VersionDTO versionDTO;

    public ModpackTableVersionButton(final GameEntityDTO entity, final GameType type, ModpackComboBox localmodpacks, final VersionDTO versionDTO, BaseModpackFilter<VersionDTO> filter) {
        super(entity, type, localmodpacks);
        this.versionDTO = versionDTO;
        this.filter = filter;
        initButton();
        this.installButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableVersionButton.1
            public void actionPerformed(ActionEvent e) {
                ModpackTableVersionButton.this.manager.installEntity(entity, versionDTO, type, true);
            }
        });
        this.removeButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableVersionButton.2
            public void actionPerformed(ActionEvent e) {
                ModpackTableVersionButton.this.manager.removeEntity(entity, versionDTO, type, false);
            }
        });
    }

    @Override // org.tlauncher.tlauncher.ui.loc.modpack.TableActButton, org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton
    public void initButton() {
        if (findLocal() != null) {
            setTypeButton(ModpackActButton.REMOVE);
        } else if (this.type == GameType.MODPACK) {
            setTypeButton(ModpackActButton.INSTALL);
        } else if (this.filter.isProper(this.versionDTO)) {
            setTypeButton(ModpackActButton.INSTALL);
        } else {
            setTypeButton(ModpackActButton.DENIED_OPERATION);
        }
    }

    private GameEntityDTO findLocal() {
        for (GameEntityDTO e : getSelectedModpackData()) {
            if (this.entity.getId().equals(e.getId()) && e.getVersion().getId().equals(this.versionDTO.getId())) {
                return e;
            }
        }
        return null;
    }
}
