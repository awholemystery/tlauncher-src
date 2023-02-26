package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/TableActButton.class */
public abstract class TableActButton extends ModpackActButton {
    protected JButton deniedButton;
    protected BaseModpackFilter<VersionDTO> filter;
    protected ModpackManager manager;

    @Override // org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton
    public abstract void initButton();

    public TableActButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
        super(entity, type, localmodpacks);
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        Color color = new Color(0, 0, 0, 0);
        this.installButton = new ImageUdaterButton(color, "install-game-element.png");
        this.removeButton = new ImageUdaterButton(color, "delete-game-element.png");
        this.processButton = new ImageUdaterButton(color, "refresh.png");
        this.deniedButton = new ImageUdaterButton(color, "modpack-element-denied.png");
        add((Component) this.installButton, ModpackActButton.INSTALL);
        add((Component) this.removeButton, ModpackActButton.REMOVE);
        add((Component) this.processButton, ModpackActButton.PROCESSING);
        add((Component) this.deniedButton, ModpackActButton.DENIED_OPERATION);
        this.deniedButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.loc.modpack.TableActButton.1
            public void actionPerformed(ActionEvent e) {
                Alert.showLocMessageWithoutTitle("modpack.table.denied.button");
            }
        });
    }
}
