package org.tlauncher.tlauncher.ui.settings;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.managers.VersionLists;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.converter.ActionOnLaunchConverter;
import org.tlauncher.tlauncher.ui.converter.ConnectionQualityConverter;
import org.tlauncher.tlauncher.ui.converter.ConsoleTypeConverter;
import org.tlauncher.tlauncher.ui.converter.LocaleConverter;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
import org.tlauncher.tlauncher.ui.editor.EditorFieldHandler;
import org.tlauncher.tlauncher.ui.editor.EditorFileField;
import org.tlauncher.tlauncher.ui.editor.EditorGroupHandler;
import org.tlauncher.tlauncher.ui.editor.EditorHandler;
import org.tlauncher.tlauncher.ui.editor.EditorPair;
import org.tlauncher.tlauncher.ui.editor.EditorResolutionField;
import org.tlauncher.tlauncher.ui.editor.EditorTextField;
import org.tlauncher.tlauncher.ui.editor.TabbedEditorPanel;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.explorer.filters.FolderFilter;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.login.LoginException;
import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingsPanel.class */
public class SettingsPanel extends TabbedEditorPanel implements LoginProcessListener, LocalizableComponent {
    private static final long serialVersionUID = -6596485925333261093L;
    private final DefaultScene scene;
    private final TabbedEditorPanel.EditorPanelTab minecraftTab;
    public final EditorFieldHandler directory;
    public final EditorFieldHandler resolution;
    public final EditorFieldHandler fullscreen;
    public final EditorFieldHandler javaArgs;
    public final EditorFieldHandler mcArgs;
    public final EditorFieldHandler memory;
    public final EditorFieldHandler statistics;
    public final EditorGroupHandler versionHandler;
    private final TabbedEditorPanel.EditorPanelTab tlauncherTab;
    public final EditorFieldHandler console;
    public final EditorFieldHandler connQuality;
    public final EditorFieldHandler launchAction;
    public final EditorFieldHandler locale;
    private final TabbedEditorPanel.EditorPanelTab confidentialityTab;
    private final ExtendedPanel minecraftButtons;
    private final ExtendedPanel tlauncherTabButtons;
    private final ExtendedPanel confidentialityTabButtons;
    private final JPopupMenu popup;
    private final LocalizableMenuItem infoItem;
    private final LocalizableMenuItem defaultItem;
    private EditorHandler selectedHandler;

    public SettingsPanel(DefaultScene sc) {
        super(tipTheme, new Insets(5, 10, 10, 10));
        this.confidentialityTabButtons = createButton();
        this.tlauncherTabButtons = createButton();
        this.minecraftButtons = createButton();
        if (this.tabPane.getExtendedUI() != null) {
            this.tabPane.getExtendedUI().setTheme(settingsTheme);
        }
        this.scene = sc;
        FocusListener warning = new FocusListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.1
            public void focusGained(FocusEvent e) {
                SettingsPanel.this.setMessage("settings.warning");
            }

            public void focusLost(FocusEvent e) {
                SettingsPanel.this.setMessage(null);
            }
        };
        FocusListener restart = new FocusListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.2
            public void focusGained(FocusEvent e) {
                SettingsPanel.this.setMessage("settings.restart");
            }

            public void focusLost(FocusEvent e) {
                SettingsPanel.this.setMessage(null);
            }
        };
        this.minecraftTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.minecraft");
        this.minecraftTab.setInsets(new Insets(20, 20, 0, 20));
        FileChooser chooser = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
        chooser.setFileFilter(new FolderFilter());
        this.directory = new EditorFieldHandler("minecraft.gamedir", new EditorFileField("settings.client.gamedir.prompt", chooser), warning);
        this.directory.addListener(new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.3
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
                if (!SettingsPanel.this.tlauncher.isReady()) {
                    return;
                }
                try {
                    ((VersionLists) SettingsPanel.this.tlauncher.getManager().getComponent(VersionLists.class)).updateLocal();
                    SettingsPanel.this.tlauncher.getVersionManager().asyncRefresh();
                    SettingsPanel.this.tlauncher.getProfileManager().asyncRefresh();
                } catch (IOException e) {
                    Alert.showLocError("settings.client.gamedir.noaccess", e);
                }
            }
        });
        this.minecraftTab.add(new EditorPair("settings.client.gamedir.label", this.directory));
        this.resolution = new EditorFieldHandler("minecraft.size", new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height", this.global.getDefaultClientWindowSize(), false), restart);
        this.fullscreen = new EditorFieldHandler("minecraft.fullscreen", new EditorCheckBox("settings.client.resolution.fullscreen"));
        this.minecraftTab.add(new EditorPair("settings.client.resolution.label", this.resolution));
        List<ReleaseType> releaseTypes = ReleaseType.getDefinable();
        List<EditorFieldHandler> versions = new ArrayList<>(releaseTypes.size());
        for (ReleaseType releaseType : ReleaseType.getDefinable()) {
            versions.add(new EditorFieldHandler("minecraft.versions." + releaseType, new EditorCheckBox("settings.versions." + releaseType)));
        }
        versions.add(new EditorFieldHandler("minecraft.versions.sub." + ReleaseType.SubType.OLD_RELEASE, new EditorCheckBox("settings.versions.sub." + ReleaseType.SubType.OLD_RELEASE)));
        this.versionHandler = new EditorGroupHandler(versions);
        this.minecraftTab.add(new EditorPair("settings.versions.label", versions));
        this.minecraftTab.nextPane();
        this.javaArgs = new EditorFieldHandler("minecraft.javaargs", new EditorTextField("settings.java.args.jvm", true), warning);
        this.mcArgs = new EditorFieldHandler("minecraft.args", new EditorTextField("settings.java.args.minecraft", true), warning);
        this.minecraftTab.add(new EditorPair("settings.java.args.label", this.javaArgs, this.mcArgs));
        this.minecraftTab.nextPane();
        this.memory = new EditorFieldHandler(MinecraftSettings.MINECRAFT_SETTING_RAM, new SettingsMemorySlider(), warning);
        this.minecraftTab.add(new EditorPair("settings.java.memory.label", this.memory));
        add(this.minecraftTab);
        this.tlauncherTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.tlauncher");
        this.tlauncherTab.setInsets(CenterPanel.ISETS_20);
        this.console = new EditorFieldHandler("gui.console", new EditorComboBox(new ConsoleTypeConverter(), ConsoleType.values()));
        new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.4
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
            }
        };
        this.tlauncherTab.add(new EditorPair("settings.console.label", this.console));
        this.connQuality = new EditorFieldHandler("connection", new EditorComboBox(new ConnectionQualityConverter(), ConnectionQuality.values()));
        this.connQuality.addListener(new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.5
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
                SettingsPanel.this.tlauncher.getDownloader().setConfiguration(SettingsPanel.this.global.getConnectionQuality());
            }
        });
        this.tlauncherTab.add(new EditorPair("settings.connection.label", this.connQuality));
        this.launchAction = new EditorFieldHandler("minecraft.onlaunch", new EditorComboBox(new ActionOnLaunchConverter(), ActionOnLaunch.values()));
        this.tlauncherTab.add(new EditorPair("settings.launch-action.label", this.launchAction));
        this.tlauncherTab.nextPane();
        this.locale = new EditorFieldHandler("locale", new EditorComboBox(new LocaleConverter(), this.global.getLocales()));
        this.locale.addListener(new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.6
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldvalue, String newvalue) {
                if (SettingsPanel.this.tlauncher.getFrame() != null) {
                    SettingsPanel.this.tlauncher.getFrame().updateLocales();
                }
            }
        });
        this.tlauncherTab.add(new EditorPair("settings.lang.label", this.locale));
        add(this.tlauncherTab);
        this.confidentialityTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.tlauncher");
        this.confidentialityTab.setInsets(CenterPanel.ISETS_20);
        this.statistics = new EditorFieldHandler("gui.statistics.checkbox", new EditorCheckBox("statistics.settings.checkbox.name"));
        this.confidentialityTab.add(new EditorPair("statistics.settings.title", this.statistics));
        this.confidentialityTab.nextPane();
        add(this.confidentialityTab);
        this.tlauncherTab.addVerticalGap(150);
        this.tlauncherTab.addButtons(this.tlauncherTabButtons);
        this.minecraftTab.addButtons(this.minecraftButtons);
        this.confidentialityTab.addVerticalGap(150);
        this.confidentialityTab.addButtons(this.confidentialityTabButtons);
        this.tabPane.addChangeListener(new ChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.7
            private final String aboutBlock = "abouttab";

            public void stateChanged(ChangeEvent e) {
                if ((SettingsPanel.this.tabPane.getSelectedComponent() instanceof TabbedEditorPanel.EditorScrollPane) && !SettingsPanel.this.tabPane.getSelectedComponent().getTab().getSavingEnabled()) {
                    Blocker.blockComponents("abouttab", SettingsPanel.this.tlauncherTabButtons);
                    Blocker.blockComponents("abouttab", SettingsPanel.this.minecraftButtons);
                    Blocker.blockComponents("abouttab", SettingsPanel.this.confidentialityTab);
                    return;
                }
                Blocker.unblockComponents("abouttab", SettingsPanel.this.tlauncherTabButtons);
                Blocker.unblockComponents("abouttab", SettingsPanel.this.minecraftButtons);
                Blocker.unblockComponents("abouttab", SettingsPanel.this.confidentialityTab);
            }
        });
        this.popup = new JPopupMenu();
        this.infoItem = new LocalizableMenuItem("settings.popup.info");
        this.infoItem.setEnabled(false);
        this.popup.add(this.infoItem);
        this.defaultItem = new LocalizableMenuItem("settings.popup.default");
        this.defaultItem.addActionListener(e -> {
            if (this.selectedHandler == null) {
                return;
            }
            resetValue(this.selectedHandler);
        });
        this.popup.add(this.defaultItem);
        for (final EditorHandler handler : this.handlers) {
            handler.getComponent().addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsPanel.8
                public void mouseClicked(MouseEvent e2) {
                    if (e2.getButton() != 3) {
                        return;
                    }
                    SettingsPanel.this.callPopup(e2, handler);
                }
            });
        }
        updateValues();
        updateLocale();
    }

    void updateValues() {
        boolean globalUnSaveable = !this.global.isSaveable();
        for (EditorHandler handler : this.handlers) {
            String path = handler.getPath();
            String value = this.global.get(path);
            handler.updateValue(value);
            setValid(handler, true);
            if (globalUnSaveable || !this.global.isSaveable(path)) {
                Blocker.block(handler, "unsaveable");
            }
        }
    }

    public boolean saveValues() {
        if (!checkValues()) {
            return false;
        }
        for (EditorHandler handler : this.handlers) {
            String path = handler.getPath();
            String value = handler.getValue();
            this.global.set(path, value, false);
            handler.onChange(value);
        }
        this.global.store();
        return true;
    }

    void resetValues() {
        for (EditorHandler handler : this.handlers) {
            resetValue(handler);
        }
    }

    void resetValue(EditorHandler handler) {
        String path = handler.getPath();
        if (!this.global.isSaveable(path)) {
            return;
        }
        String value = this.global.getDefault(path);
        log("Resetting:", handler.getClass().getSimpleName(), path, value);
        handler.setValue(value);
        log("Reset!");
    }

    boolean canReset(EditorHandler handler) {
        String key = handler.getPath();
        return this.global.isSaveable(key) && this.global.getDefault(handler.getPath()) != null;
    }

    void callPopup(MouseEvent e, EditorHandler handler) {
        if (this.popup.isShowing()) {
            this.popup.setVisible(false);
        }
        defocus();
        int x = e.getX();
        int y = e.getY();
        this.selectedHandler = handler;
        updateResetMenu();
        this.infoItem.setVariables(handler.getPath());
        this.popup.show((JComponent) e.getSource(), x, y);
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents((Container) this.minecraftTab, reason);
        updateResetMenu();
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents((Container) this.minecraftTab, reason);
        updateResetMenu();
    }

    private void updateResetMenu() {
        if (this.selectedHandler != null) {
            this.defaultItem.setEnabled(!Blocker.isBlocked(this.selectedHandler));
        }
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        if (checkValues()) {
            return;
        }
        this.scene.setSidePanel(DefaultScene.SidePanel.SETTINGS);
        throw new LoginException("Invalid settings!");
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
    }

    private ExtendedPanel createButton() {
        Component updaterButton = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
        updaterButton.setFont(updaterButton.getFont().deriveFont(1));
        updaterButton.addActionListener(e -> {
            saveValues();
        });
        Component updaterButton2 = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.default");
        updaterButton2.addActionListener(e2 -> {
            if (Alert.showLocQuestion("settings.default.warning")) {
                resetValues();
            }
        });
        Component imageUdaterButton = new ImageUdaterButton(ImageUdaterButton.GREEN_COLOR, "home.png");
        imageUdaterButton.addActionListener(e3 -> {
            updateValues();
            this.scene.setSidePanel(null);
        });
        Dimension size = imageUdaterButton.getPreferredSize();
        if (size != null) {
            imageUdaterButton.setPreferredSize(new Dimension(size.width * 2, size.height));
        }
        ExtendedPanel buttonPanel = new ExtendedPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0d;
        c.insets = new Insets(0, 0, 0, 10);
        buttonPanel.add(updaterButton, c);
        c.gridx++;
        buttonPanel.add(updaterButton2, c);
        c.gridx++;
        buttonPanel.add(Box.createHorizontalStrut(20), c);
        c.gridx++;
        buttonPanel.add(imageUdaterButton, c);
        return buttonPanel;
    }
}
