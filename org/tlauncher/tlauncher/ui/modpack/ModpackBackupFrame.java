package org.tlauncher.tlauncher.ui.modpack;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;
import org.tlauncher.exceptions.ParseModPackException;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.GameInstallRadioButton;
import org.tlauncher.tlauncher.ui.swing.GameRadioTextButton;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer;
import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackBackupFrame.class */
public class ModpackBackupFrame extends TemlateModpackFrame {
    private static final long serialVersionUID = 6901167695599672717L;
    private static final String RESTORER = "RESTORER";
    private static final String BACKUP = "BACKUP";
    private static final String RECOVER_FOLDER = "backup/modpacks";
    private final ModpackComboBox localModpack;
    private ButtonGroup group;
    FileChooser explorerRecovery;
    private Color listColor;
    private JPanel subEntityPanel;
    final JList<GameEntityDTO> entitiesList;
    JComboBox<CompleteVersion> modpackBox;
    final JComboBox<GameType> modpackElementType;
    private CompleteVersion selectedVersion;
    public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY-HH_mm_ss");
    private static final Dimension DEFAULT_SIZE = new Dimension(572, 470);

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackBackupFrame$HandleListener.class */
    public interface HandleListener {
        void operationSuccess();

        void processError(Exception exc);

        void installedSuccess(List<String> list);
    }

    public ModpackBackupFrame(JFrame parent, ModpackComboBox localModpack) {
        super(parent, "modpack.backup.title", DEFAULT_SIZE);
        this.group = new ButtonGroup();
        this.listColor = new Color(237, 249, 255);
        this.selectedVersion = null;
        this.localModpack = localModpack;
        SpringLayout springRecoverer = new SpringLayout();
        SpringLayout springBackup = new SpringLayout();
        SpringLayout springPanel = new SpringLayout();
        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(springPanel);
        JPanel cardPanel = new JPanel(cardLayout);
        ModpackManager manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        JPanel restoredPanel = new JPanel(springRecoverer);
        JPanel backupModpackPanel = new JPanel(springBackup);
        backupModpackPanel.setBackground(Color.WHITE);
        addCenter(panel);
        try {
            FileUtil.createFolder(FileUtil.getRelative(RECOVER_FOLDER).toFile());
        } catch (IOException e1) {
            U.log(e1);
        }
        this.explorerRecovery = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
        this.explorerRecovery.setCurrentDirectory(FileUtil.getRelative(RECOVER_FOLDER).toFile());
        this.explorerRecovery.setMultiSelectionEnabled(false);
        this.explorerRecovery.setFileFilter(new FilesAndExtentionFilter("zip, rar", ArchiveStreamFactory.ZIP, "rar"));
        this.modpackBox = new JComboBox<>();
        this.entitiesList = new JList<>();
        GameRadioTextButton restoredBackup = new GameInstallRadioButton("modpack.backup.button.restore");
        GameRadioTextButton backupModpack = new GameInstallRadioButton("modpack.backup.button.backup");
        UpdaterButton doButton = new UpdaterButton(BLUE_COLOR, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.backup.button.do");
        UpdaterButton chooseFile = new UpdaterButton(ColorUtil.COLOR_215, "explorer.title");
        LocalizableLabel description = new LocalizableLabel("modpack.backup.message.info");
        LocalizableLabel informationBold = new LocalizableLabel("modpack.backup.info.label.0");
        LocalizableButton restoreButton = new UpdaterButton(BLUE_COLOR, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.backup.down.button.restore");
        ModpackScrollBarUI barUI = new ModpackScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.1
            @Override // org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI
            protected Dimension getMinimumThumbSize() {
                return new Dimension(10, 40);
            }

            public Dimension getMaximumSize(JComponent c) {
                Dimension dim = super.getMaximumSize(c);
                dim.setSize(10.0d, dim.getHeight());
                return dim;
            }

            public Dimension getPreferredSize(JComponent c) {
                Dimension dim = super.getPreferredSize(c);
                dim.setSize(10.0d, dim.getHeight());
                return dim;
            }
        };
        barUI.setGapThubm(5);
        CardLayout cardLayout1 = new CardLayout();
        this.subEntityPanel = new JPanel(cardLayout1);
        this.subEntityPanel.setBackground(this.listColor);
        JScrollPane scroller = new JScrollPane(this.entitiesList, 20, 31);
        this.subEntityPanel.add(scroller, ModpackScene.NOT_EMPTY);
        this.subEntityPanel.add(new EmptyView(GameType.MOD, "modpack.table.empty."), ModpackScene.EMPTY + GameType.MOD.toString());
        this.subEntityPanel.add(new EmptyView(GameType.RESOURCEPACK, "modpack.table.empty."), ModpackScene.EMPTY + GameType.RESOURCEPACK);
        this.subEntityPanel.add(new EmptyView(GameType.MAP, "modpack.table.empty."), ModpackScene.EMPTY + GameType.MAP);
        this.subEntityPanel.add(new EmptyView(GameType.SHADERPACK, "modpack.table.empty."), ModpackScene.EMPTY + GameType.SHADERPACK);
        this.subEntityPanel.add(new EmptyView(GameType.MAP, "modpack.backup.all.elements."), CoreConstants.EMPTY_STRING + GameType.MODPACK + GameType.MAP);
        this.subEntityPanel.add(new EmptyView(GameType.MOD, "modpack.backup.all.elements."), CoreConstants.EMPTY_STRING + GameType.MODPACK + GameType.MOD);
        this.subEntityPanel.add(new EmptyView(GameType.RESOURCEPACK, "modpack.backup.all.elements."), CoreConstants.EMPTY_STRING + GameType.MODPACK + GameType.RESOURCEPACK);
        this.subEntityPanel.add(new EmptyView(GameType.SHADERPACK, "modpack.backup.all.elements."), CoreConstants.EMPTY_STRING + GameType.MODPACK + GameType.SHADERPACK);
        scroller.getVerticalScrollBar().setUI(barUI);
        scroller.getVerticalScrollBar().setBackground(this.listColor);
        this.entitiesList.setSelectionModel(new DefaultListSelectionModel() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.2
            private static final long serialVersionUID = -4763682115998962110L;
            private int i0 = -1;
            private int i1 = -1;

            public void setSelectionInterval(int index0, int index1) {
                if (this.i0 == index0 && this.i1 == index1) {
                    if (getValueIsAdjusting()) {
                        setValueIsAdjusting(false);
                        setSelection(index0, index1);
                        return;
                    }
                    return;
                }
                this.i0 = index0;
                this.i1 = index1;
                setValueIsAdjusting(false);
                setSelection(index0, index1);
            }

            private void setSelection(int index0, int index1) {
                ModpackVersionDTO v = (ModpackVersionDTO) ModpackBackupFrame.this.selectedVersion.getModpack().getVersion();
                if (super.isSelectedIndex(index0)) {
                    v.getByType((GameType) ModpackBackupFrame.this.modpackElementType.getSelectedItem()).remove(ModpackBackupFrame.this.entitiesList.getModel().getElementAt(index0));
                    super.removeSelectionInterval(index0, index1);
                    return;
                }
                GameEntityDTO en = (GameEntityDTO) ModpackBackupFrame.this.entitiesList.getModel().getElementAt(index0);
                v.getByType((GameType) ModpackBackupFrame.this.modpackElementType.getSelectedItem()).add(en);
                super.addSelectionInterval(index0, index1);
            }
        });
        this.entitiesList.setCellRenderer(new DefaultListCellRenderer() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.3
            private static final long serialVersionUID = -4696236449762079444L;

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                GameEntityDTO entity = (GameEntityDTO) value;
                JLabel label = new JLabel(entity.getName());
                SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
                label.setHorizontalTextPosition(4);
                label.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
                if (isSelected) {
                    label.setIcon(ImageCache.getIcon("settings-check-box-on.png"));
                    label.setIconTextGap(15);
                } else {
                    label.setIconTextGap(14);
                    label.setIcon(ImageCache.getIcon("settings-check-box-off.png"));
                }
                label.setPreferredSize(new Dimension(0, 30));
                label.setOpaque(false);
                return label;
            }
        });
        this.modpackElementType = new JComboBox<>(new GameType[]{GameType.MOD, GameType.RESOURCEPACK, GameType.MAP, GameType.SHADERPACK});
        this.modpackBox.setRenderer(new UserCategoryListRenderer() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.4
            private static final long serialVersionUID = 3495501578727016309L;

            @Override // org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer, org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return createElement(index, isSelected, ((CompleteVersion) value).getID());
            }
        });
        this.modpackElementType.setRenderer(new UserCategoryListRenderer() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.5
            private static final long serialVersionUID = -6697788712085514932L;

            @Override // org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer, org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return createElement(index, isSelected, Localizable.get("modpack.button." + value.toString()));
            }
        });
        this.modpackElementType.setUI(new ModpackComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.6
            @Override // org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                paintBackground(g, bounds);
                paintText(g, bounds, Localizable.get("modpack.button." + ModpackBackupFrame.this.modpackElementType.getSelectedItem().toString()));
            }
        });
        this.modpackBox.setUI(new ModpackComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.7
            @Override // org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                paintBackground(g, bounds);
                paintText(g, bounds, ((CompleteVersion) ModpackBackupFrame.this.modpackBox.getSelectedItem()).getID());
            }
        });
        this.modpackBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModpackComboxRenderer.LINE));
        this.modpackElementType.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModpackComboxRenderer.LINE));
        restoredBackup.setActionCommand(RESTORER);
        backupModpack.setActionCommand(BACKUP);
        description.setHorizontalAlignment(0);
        description.setVerticalAlignment(1);
        informationBold.setHorizontalAlignment(0);
        this.group.add(restoredBackup);
        this.group.add(backupModpack);
        SwingUtil.changeFontFamily(restoredBackup, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(backupModpack, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(chooseFile, FontTL.ROBOTO_REGULAR, 12, Color.BLACK);
        SwingUtil.changeFontFamily(description, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(doButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(restoreButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(informationBold, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.modpackBox, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(this.modpackElementType, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(this.entitiesList, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        description.setBackground(ColorUtil.COLOR_244);
        informationBold.setBackground(ColorUtil.COLOR_244);
        restoredPanel.setBackground(Color.WHITE);
        this.entitiesList.setBackground(this.listColor);
        description.setOpaque(true);
        informationBold.setOpaque(true);
        springPanel.putConstraint("West", restoredBackup, 0, "West", panel);
        springPanel.putConstraint("East", restoredBackup, 286, "West", panel);
        springPanel.putConstraint("North", restoredBackup, 0, "North", panel);
        springPanel.putConstraint("South", restoredBackup, 58, "North", panel);
        panel.add(restoredBackup);
        springPanel.putConstraint("West", backupModpack, 286, "West", panel);
        springPanel.putConstraint("East", backupModpack, 572, "West", panel);
        springPanel.putConstraint("North", backupModpack, 0, "North", panel);
        springPanel.putConstraint("South", backupModpack, 58, "North", panel);
        panel.add(backupModpack);
        springPanel.putConstraint("West", cardPanel, 0, "West", panel);
        springPanel.putConstraint("East", cardPanel, 0, "East", panel);
        springPanel.putConstraint("North", cardPanel, 58, "North", panel);
        springPanel.putConstraint("South", cardPanel, 0, "South", panel);
        panel.add(cardPanel);
        cardPanel.add(RESTORER, restoredPanel);
        cardPanel.add(BACKUP, backupModpackPanel);
        springRecoverer.putConstraint("West", chooseFile, 179, "West", restoredPanel);
        springRecoverer.putConstraint("East", chooseFile, -177, "East", restoredPanel);
        springRecoverer.putConstraint("North", chooseFile, 39, "North", restoredPanel);
        springRecoverer.putConstraint("South", chooseFile, 77, "North", restoredPanel);
        restoredPanel.add(chooseFile);
        springRecoverer.putConstraint("West", informationBold, 0, "West", restoredPanel);
        springRecoverer.putConstraint("East", informationBold, 0, "East", restoredPanel);
        springRecoverer.putConstraint("North", informationBold, 113, "North", restoredPanel);
        springRecoverer.putConstraint("South", informationBold, (int) TarConstants.CHKSUM_OFFSET, "North", restoredPanel);
        restoredPanel.add(informationBold);
        springRecoverer.putConstraint("West", description, 0, "West", restoredPanel);
        springRecoverer.putConstraint("East", description, 0, "East", restoredPanel);
        springRecoverer.putConstraint("North", description, 145, "North", restoredPanel);
        springRecoverer.putConstraint("South", description, 276, "North", restoredPanel);
        restoredPanel.add(description);
        springRecoverer.putConstraint("West", restoreButton, (int) HttpStatus.SC_RESET_CONTENT, "West", restoredPanel);
        springRecoverer.putConstraint("East", restoreButton, 368, "West", restoredPanel);
        springRecoverer.putConstraint("North", restoreButton, -68, "South", restoredPanel);
        springRecoverer.putConstraint("South", restoreButton, -29, "South", restoredPanel);
        restoredPanel.add(restoreButton);
        springBackup.putConstraint("West", this.modpackBox, 129, "West", backupModpackPanel);
        springBackup.putConstraint("East", this.modpackBox, -127, "East", backupModpackPanel);
        springBackup.putConstraint("North", this.modpackBox, 21, "North", backupModpackPanel);
        springBackup.putConstraint("South", this.modpackBox, 61, "North", backupModpackPanel);
        backupModpackPanel.add(this.modpackBox);
        springBackup.putConstraint("West", this.modpackElementType, 129, "West", backupModpackPanel);
        springBackup.putConstraint("East", this.modpackElementType, -127, "East", backupModpackPanel);
        springBackup.putConstraint("North", this.modpackElementType, 81, "North", backupModpackPanel);
        springBackup.putConstraint("South", this.modpackElementType, 121, "North", backupModpackPanel);
        backupModpackPanel.add(this.modpackElementType);
        springBackup.putConstraint("West", this.subEntityPanel, 129, "West", backupModpackPanel);
        springBackup.putConstraint("East", this.subEntityPanel, -127, "East", backupModpackPanel);
        springBackup.putConstraint("North", this.subEntityPanel, 121, "North", backupModpackPanel);
        springBackup.putConstraint("South", this.subEntityPanel, 271, "North", backupModpackPanel);
        backupModpackPanel.add(this.subEntityPanel);
        springBackup.putConstraint("West", doButton, 175, "West", backupModpackPanel);
        springBackup.putConstraint("East", doButton, 398, "West", backupModpackPanel);
        springBackup.putConstraint("North", doButton, -62, "South", backupModpackPanel);
        springBackup.putConstraint("South", doButton, -20, "South", backupModpackPanel);
        backupModpackPanel.add(doButton);
        restoredBackup.addActionListener(e -> {
            cardLayout.show(cardPanel, e.getActionCommand());
        });
        backupModpack.addActionListener(e2 -> {
            if (localModpack.getItemCount() < 2) {
                setVisible(false);
                Alert.showLocMessage("modpack.backup.init");
                setVisible(true);
                localModpack.setSelected(true);
                return;
            }
            CompleteVersion defaultVersion = new CompleteVersion();
            defaultVersion.setID(Localizable.get("modpack.backup.modpack.box"));
            this.modpackBox.removeAllItems();
            this.modpackBox.addItem(defaultVersion);
            for (int i = 1; i < localModpack.getItemCount(); i++) {
                this.modpackBox.addItem(localModpack.getItemAt(i));
            }
            this.modpackBox.setSelectedIndex(0);
            restoredBackup.show(cardLayout, cardPanel.getActionCommand());
        });
        this.modpackBox.addItemListener(e3 -> {
            if (1 == e3.getStateChange()) {
                if (this.modpackBox.getSelectedIndex() != 0) {
                    this.selectedVersion = ((CompleteVersion) this.modpackBox.getSelectedItem()).fullCopy(new CompleteVersion());
                } else {
                    this.selectedVersion = null;
                }
                prepareEntityList();
            }
        });
        this.modpackElementType.addItemListener(e4 -> {
            if (1 == e4.getStateChange()) {
                prepareEntityList();
            }
        });
        doButton.addActionListener(e5 -> {
            FileChooser f = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
            String name = this.selectedVersion == null ? "modpacks-" + format.format(new Date()) : this.selectedVersion.getID() + " " + format.format(new Date());
            f.setFileFilter(new FilesAndExtentionFilter("zip format", ArchiveStreamFactory.ZIP));
            f.setDialogTitle(Localizable.get("console.save.popup"));
            f.setSelectedFile(new File(FileUtil.getRelative(RECOVER_FOLDER).toFile(), name + ".zip"));
            f.setMultiSelectionEnabled(false);
            setAlwaysOnTop(false);
            int res = f.showSaveDialog(this);
            if (res != 0) {
                return;
            }
            setAlwaysOnTop(true);
            doButton.setText("modpack.install.process");
            doButton.setEnabled(false);
            List<CompleteVersion> list = new ArrayList<>();
            if (this.selectedVersion == null) {
                for (int i = 1; i < this.modpackBox.getItemCount(); i++) {
                    list.add(((CompleteVersion) this.modpackBox.getItemAt(i)).fullCopy(new CompleteVersion()));
                }
            } else {
                list.add(this.selectedVersion);
            }
            doButton.backupModPack(list, f.getSelectedFile(), new HandleListener() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.8
                @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                public void processError(Exception e5) {
                    doButton.setEnabled(true);
                    doButton.setText("modpack.backup.button.do");
                    ModpackBackupFrame.this.showWarning("modpack.backup.files.error");
                }

                @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                public void installedSuccess(List<String> list2) {
                }

                @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                public void operationSuccess() {
                    doButton.setEnabled(true);
                    doButton.setText("modpack.backup.button.do");
                    ModpackBackupFrame.this.setVisible(false);
                    Alert.showLocMessageWithoutTitle("modpack.backup.files.do");
                    ModpackBackupFrame.this.setVisible(true);
                }
            });
        });
        chooseFile.addActionListener(e6 -> {
            setAlwaysOnTop(false);
            if (this.explorerRecovery.showDialog(this) == 0) {
                chooseFile.setText("explorer.backup.file.chosen");
                File f = this.explorerRecovery.getSelectedFile();
                U.log("selected file " + f.toString());
                try {
                    List<String> list = chooseFile.analizeArchiver(f);
                    manager.setText(buildDescription(list));
                } catch (ParseModPackException e12) {
                    showWarning("modpack.install.files.error");
                    U.log(e12);
                }
            }
            setAlwaysOnTop(true);
        });
        restoreButton.addActionListener(e7 -> {
            File f = this.explorerRecovery.getSelectedFile();
            if (f == null || f.isDirectory()) {
                showWarning("explorer.error.choose.file");
                return;
            }
            try {
                restoreButton.setEnabled(false);
                restoreButton.setText("modpack.install.process");
                restoreButton.installModPack(f, new HandleListener() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.9
                    @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                    public void processError(Exception e7) {
                        restoreButton.setEnabled(true);
                        ModpackBackupFrame.this.showWarning("modpack.install.files.error");
                    }

                    @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                    public void operationSuccess() {
                    }

                    @Override // org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame.HandleListener
                    public void installedSuccess(List<String> list) {
                        restoreButton.setText("modpack.backup.down.button.restore");
                        restoreButton.setEnabled(true);
                        manager.setText(ModpackBackupFrame.this.buildDescription(list));
                        ModpackBackupFrame.this.setVisible(false);
                        Alert.showLocMessage("modpack.install.files.installed");
                    }
                });
            } catch (Exception e12) {
                U.log(e12);
                showWarning("modpack.install.files.error");
            }
        });
        restoredBackup.setSelected(true);
        cardLayout.show(cardPanel, RESTORER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWarning(String value) {
        setVisible(false);
        Alert.showLocWarning(value);
        setVisible(true);
    }

    private void prepareEntityList() {
        DefaultListModel<GameEntityDTO> listModel = new DefaultListModel<>();
        this.entitiesList.setModel(listModel);
        if (this.selectedVersion != null) {
            ModpackVersionDTO version = (ModpackVersionDTO) this.selectedVersion.getModpack().getVersion();
            for (GameEntityDTO en : version.getByType((GameType) this.modpackElementType.getSelectedItem())) {
                listModel.addElement(en);
            }
            this.entitiesList.setEnabled(true);
            if (this.entitiesList.getModel().getSize() == 0) {
                this.subEntityPanel.getLayout().show(this.subEntityPanel, CoreConstants.EMPTY_STRING + ModpackScene.EMPTY + this.modpackElementType.getSelectedItem());
            } else {
                this.subEntityPanel.getLayout().show(this.subEntityPanel, ModpackScene.NOT_EMPTY);
            }
        } else if (containsAnyElement((GameType) this.modpackElementType.getModel().getSelectedItem())) {
            this.subEntityPanel.getLayout().show(this.subEntityPanel, CoreConstants.EMPTY_STRING + GameType.MODPACK + this.modpackElementType.getSelectedItem());
        } else {
            this.subEntityPanel.getLayout().show(this.subEntityPanel, ModpackScene.EMPTY + this.modpackElementType.getSelectedItem());
        }
        int[] array = new int[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) {
            array[i] = i;
        }
        this.entitiesList.setSelectedIndices(array);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String buildDescription(List<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<p style='text-align: center; margin-top:5'>");
        for (String aList : list) {
            builder.append(aList).append("<br>");
        }
        builder.append("</p></html>");
        return builder.toString();
    }

    private boolean containsAnyElement(GameType type) {
        for (ModpackDTO modpackDTO : this.localModpack.getModpacks()) {
            if (((ModpackVersionDTO) modpackDTO.getVersion()).getByType(type).size() > 0) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackBackupFrame$EmptyView.class */
    private class EmptyView extends ExtendedPanel {
        private static final long serialVersionUID = 9216616637184943829L;

        public EmptyView(GameType gameType, String nameLoc) {
            setLayout(new BorderLayout());
            JLabel jLabel = new LocalizableLabel(nameLoc + gameType);
            jLabel.setHorizontalAlignment(0);
            jLabel.setAlignmentY(0.0f);
            SwingUtil.changeFontFamily(jLabel, FontTL.ROBOTO_BOLD, 14);
            jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
            add((Component) jLabel, "Center");
            setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, ColorUtil.COLOR_149));
        }
    }
}
