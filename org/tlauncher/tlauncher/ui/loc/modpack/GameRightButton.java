package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
import org.tlauncher.tlauncher.ui.loc.RoundUpdaterButton;
import org.tlauncher.tlauncher.ui.modpack.filter.Filter;
import org.tlauncher.tlauncher.ui.modpack.filter.InstallGameEntityFilter;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/GameRightButton.class */
public abstract class GameRightButton extends ModpackActButton {
    InstallGameEntityFilter installGameEntityFilter;

    public abstract void updateRow();

    public GameRightButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
        super(entity, type, localmodpacks);
        this.installButton = new RoundUpdaterButton("loginform.enter.install") { // from class: org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton.1Button
            {
                Color color = Color.WHITE;
                Color color2 = new Color(0, 183, 81);
                Color color3 = new Color(0, 222, 99);
            }
        };
        this.removeButton = new RoundUpdaterButton("modpack.remove.button") { // from class: org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton.1Button
            {
                Color color = Color.WHITE;
                Color color2 = new Color(0, 183, 81);
                Color color3 = new Color(0, 222, 99);
            }
        };
        this.processButton = new RoundUpdaterButton("modpack.process.button") { // from class: org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton.1Button
            {
                Color color = Color.WHITE;
                Color color2 = new Color(0, 183, 81);
                Color color3 = new Color(0, 222, 99);
            }
        };
        this.installGameEntityFilter = new InstallGameEntityFilter(localmodpacks, type, new Filter[0]);
        add((Component) this.installButton, ModpackActButton.INSTALL);
        add((Component) this.removeButton, ModpackActButton.REMOVE);
        add((Component) this.processButton, ModpackActButton.PROCESSING);
        SwingUtil.changeFontFamily(this.installButton, FontTL.ROBOTO_REGULAR, 12);
        SwingUtil.changeFontFamily(this.removeButton, FontTL.ROBOTO_REGULAR, 12);
        SwingUtil.changeFontFamily(this.processButton, FontTL.ROBOTO_REGULAR, 12);
        initButton();
        ModpackManager manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.removeButton.addActionListener(e -> {
            manager.removeEntity(getEntity(), manager.getVersion(), getType());
        });
        this.installButton.addActionListener(e2 -> {
            manager.installEntity(this.entity, null, getType(), true);
        });
    }

    @Override // org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton
    public void initButton() {
        setTypeButton(ModpackActButton.INSTALL);
        getSelectedModpackData().stream().filter(e -> {
            return this.entity.getId().equals(e.getId());
        }).findFirst().ifPresent(e2 -> {
            setVisible(false);
            setTypeButton(ModpackActButton.REMOVE);
        });
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
    public synchronized void addMouseListener(MouseListener l) {
        if (l instanceof BlockClickListener) {
            return;
        }
        super.addMouseListener(l);
    }
}
