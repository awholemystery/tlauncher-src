package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/UpInstallButton.class */
public class UpInstallButton extends HideInstallButton {
    private ModpackManager manager;

    public UpInstallButton(final GameEntityDTO entity, final GameType type, ModpackComboBox localmodpacks) {
        super(Localizable.get("loginform.enter.install").toUpperCase(), "up-install.png", localmodpacks, type, entity);
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        setBackground(DEFAULT_BACKGROUND);
        setHorizontalTextPosition(4);
        setForeground(Color.WHITE);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.modpack.UpInstallButton.1
            public void mouseEntered(MouseEvent e) {
                UpInstallButton.this.setBackground(HideInstallButton.MOUSE_UNDER);
            }

            public void mouseExited(MouseEvent e) {
                UpInstallButton.this.setBackground(HideInstallButton.DEFAULT_BACKGROUND);
            }

            public void mousePressed(MouseEvent e) {
                UpInstallButton.this.setVisible(false);
                UpInstallButton.this.manager.installEntity(entity, null, type, true);
            }
        });
        init();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.modpack.HideInstallButton
    public void init() {
        switch (this.type) {
            case MODPACK:
                modpackInit();
                return;
            default:
                if (this.localmodpacks.getSelectedIndex() > 0) {
                    for (GameEntityDTO m : ((ModpackVersionDTO) this.localmodpacks.getSelectedValue().getModpack().getVersion()).getByType(this.type)) {
                        if (this.entity.getId().equals(m.getId())) {
                            setVisible(false);
                            return;
                        }
                    }
                }
                setVisible(true);
                return;
        }
    }

    private void modpackInit() {
        List<ModpackDTO> list = this.localmodpacks.getModpacks();
        for (ModpackDTO m : list) {
            if (this.entity.getId().equals(m.getId())) {
                setVisible(false);
                return;
            }
        }
        setVisible(true);
    }

    public void installEntity(GameEntityDTO e, GameType type) {
        if (this.entity.getId().equals(e.getId())) {
            setVisible(false);
        }
    }

    public void removeEntity(GameEntityDTO e) {
        init();
    }
}
