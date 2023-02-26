package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.Color;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/HideInstallButton.class */
public abstract class HideInstallButton extends UpdaterFullButton {
    private static final long serialVersionUID = -5663659688051961310L;
    public static final Color MOUSE_UNDER = new Color(95, 198, 255);
    public static final Color DEFAULT_BACKGROUND = new Color(63, 186, 255);
    protected ModpackComboBox localmodpacks;
    protected GameType type;
    protected GameEntityDTO entity;

    public abstract void init();

    public HideInstallButton(String text, String image, ModpackComboBox localmodpacks, GameType type, GameEntityDTO entity) {
        super(DEFAULT_BACKGROUND, text, image);
        this.localmodpacks = localmodpacks;
        this.type = type;
        this.entity = entity;
    }

    public HideInstallButton(ModpackComboBox localmodpacks, GameType type, GameEntityDTO entity, String text, boolean remote, String image) {
        super(DEFAULT_BACKGROUND, "loginform.enter.install", image);
        this.localmodpacks = localmodpacks;
        this.type = type;
        this.entity = entity;
    }
}
