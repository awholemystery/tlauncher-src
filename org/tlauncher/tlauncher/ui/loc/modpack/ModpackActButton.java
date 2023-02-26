package org.tlauncher.tlauncher.ui.loc.modpack;

import com.google.common.collect.Lists;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/ModpackActButton.class */
public abstract class ModpackActButton extends ExtendedPanel implements BlockClickListener {
    public static final String INSTALL = "INSTALL";
    public static final String REMOVE = "REMOVE";
    public static final String PROCESSING = "PROCESSING";
    public static final String DENIED_OPERATION = "DENIED";
    protected JButton installButton;
    protected JButton removeButton;
    protected JButton processButton;
    protected String last;
    protected ModpackComboBox localmodpacks;
    protected GameType type;
    protected GameEntityDTO entity;

    public abstract void initButton();

    public ModpackActButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
        this.localmodpacks = localmodpacks;
        this.type = type;
        this.entity = entity;
        setLayout(new CardLayout(0, 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<? extends GameEntityDTO> getSelectedModpackData() {
        if (this.localmodpacks.getSelectedIndex() > 0 && this.type != GameType.MODPACK) {
            return Lists.newArrayList(((ModpackVersionDTO) this.localmodpacks.getSelectedValue().getModpack().getVersion()).getByType(this.type));
        }
        if (this.type == GameType.MODPACK) {
            return Lists.newArrayList(this.localmodpacks.getModpacks());
        }
        return new ArrayList();
    }

    public void setTypeButton(String name) {
        if (!name.equals(PROCESSING)) {
            this.last = name;
        }
        getLayout().show(this, name);
    }

    public void reset() {
        setTypeButton(this.last);
    }

    public GameEntityDTO getEntity() {
        return this.entity;
    }

    public GameType getType() {
        return this.type;
    }
}
