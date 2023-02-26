package org.tlauncher.tlauncher.ui.swing.box;

import ch.qos.logback.core.CoreConstants;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/box/ModpackComboBox.class */
public class ModpackComboBox extends ExtendedComboBox<CompleteVersion> implements GameEntityListener, LocalizableComponent {
    private static final long serialVersionUID = 7773875370848584863L;

    public ModpackComboBox() {
        setModel(new LocalModapackBoxModel());
        setRenderer(new ModpackComboxRenderer());
        setUI(new ModpackComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox.1
            @Override // org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI
            protected ComboPopup createPopup() {
                BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox.1.1
                    private static final long serialVersionUID = -4987177491183525990L;

                    protected JScrollPane createScroller() {
                        ModpackScrollBarUI barUI = new ModpackScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox.1.1.1
                            @Override // org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI
                            protected Dimension getMinimumThumbSize() {
                                return new Dimension(10, 40);
                            }

                            public Dimension getPreferredSize(JComponent c) {
                                Dimension dim = super.getPreferredSize(c);
                                dim.setSize(8.0d, dim.getHeight());
                                return dim;
                            }
                        };
                        barUI.setGapThubm(5);
                        JScrollPane scroller = new JScrollPane(this.list, 20, 31);
                        scroller.getVerticalScrollBar().setUI(barUI);
                        scroller.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
                        return scroller;
                    }
                };
                basic.setMaximumSize(new Dimension(172, 149));
                basic.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, ModpackComboxRenderer.LINE));
                return basic;
            }
        });
        setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE, 1));
        addItemListener(e -> {
            TLauncher.getInstance().getConfiguration().set("modpack.combobox.index", Integer.valueOf(getSelectedIndex()));
        });
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationStarted(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activation(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationError(GameEntityDTO e, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void processingStarted(GameEntityDTO e, VersionDTO version) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(GameEntityDTO e, GameType type) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(CompleteVersion e) {
        addItem(e);
        setSelectedItem(e);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeEntity(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void populateStatus(GameEntityDTO status, GameType type, boolean state) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {
        repaint();
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void updateVersionStorageAndScene(CompleteVersion v, CompleteVersion newVersion) {
        removeItem(v);
        installEntity(newVersion);
        repaint();
    }

    public List<ModpackDTO> getModpacks() {
        int size = getModel().getSize();
        List<ModpackDTO> list = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            list.add(((CompleteVersion) getModel().getElementAt(i)).getModpack());
        }
        return list;
    }

    public CompleteVersion findByModpack(ModpackDTO modpackDTO, VersionDTO versionDTO) {
        int size = getModel().getSize();
        for (int i = 1; i < size; i++) {
            ModpackDTO m = ((CompleteVersion) getModel().getElementAt(i)).getModpack();
            if (m.getId().equals(modpackDTO.getId()) && m.getVersion().getId().equals(versionDTO.getId())) {
                return (CompleteVersion) getModel().getElementAt(i);
            }
        }
        U.log(CoreConstants.EMPTY_STRING + modpackDTO.getId() + " " + versionDTO.getId());
        for (int i2 = 1; i2 < size; i2++) {
            ModpackDTO m2 = ((CompleteVersion) getModel().getElementAt(i2)).getModpack();
            U.log("m id =" + m2.getId() + " v id =" + m2.getVersion().getId());
        }
        return null;
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeCompleteVersion(CompleteVersion e) {
        ComboBoxModel<CompleteVersion> model = getModel();
        for (int i = 1; i < model.getSize(); i++) {
            if (((CompleteVersion) model.getElementAt(i)).getID().equals(e.getID())) {
                removeItemAt(i);
                return;
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        ((CompleteVersion) getModel().getElementAt(0)).setID(Localizable.get("modpack.local.box.default"));
    }
}
