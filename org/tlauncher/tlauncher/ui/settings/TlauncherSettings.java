package org.tlauncher.tlauncher.ui.settings;

import ch.qos.logback.core.CoreConstants;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
import org.tlauncher.tlauncher.configuration.enums.BackupToolTips;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.console.Console;
import org.tlauncher.tlauncher.ui.converter.ActionOnLaunchConverter;
import org.tlauncher.tlauncher.ui.converter.BackupFrequencyConverter;
import org.tlauncher.tlauncher.ui.converter.ConnectionQualityConverter;
import org.tlauncher.tlauncher.ui.converter.ConsoleTypeConverter;
import org.tlauncher.tlauncher.ui.converter.LocaleConverter;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
import org.tlauncher.tlauncher.ui.editor.EditorTextField;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.SettingsScene;
import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
import org.tlauncher.util.MinecraftUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/TlauncherSettings.class */
public class TlauncherSettings extends PageSettings {
    private static final long serialVersionUID = -555851839208513067L;
    public final TLauncher tlauncher = TLauncher.getInstance();
    public final Configuration global = this.tlauncher.getConfiguration();
    public final LangConfiguration lang = this.tlauncher.getLang();
    private final EditorComboBox<Locale> local;

    public TlauncherSettings() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        EditorComboBox<ConsoleType> consoleConverter = new EditorComboBox<>(new ConsoleTypeConverter(), ConsoleType.values());
        EditorCheckBox doBackup = new EditorCheckBox("ui.yes");
        doBackup.setPreferredSize(new Dimension(70, doBackup.getHeight()));
        doBackup.setSelected(!this.global.getBoolean(BackupSetting.SKIP_USER_BACKUP.toString()));
        JLabel doBackupQuestionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        JLabel backupTitleQuestionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        EditorComboBox<ConnectionQuality> connQuality = new EditorComboBox<>(new ConnectionQualityConverter(), ConnectionQuality.values());
        EditorComboBox<ActionOnLaunch> launchAction = new EditorComboBox<>(new ActionOnLaunchConverter(), ActionOnLaunch.values());
        EditorComboBox<BackupFrequency> backupFrequency = new EditorComboBox<>(new BackupFrequencyConverter(), BackupFrequency.values());
        this.local = new EditorComboBox<>(new LocaleConverter(), this.global.getLocales());
        setTLauncherBasicComboBoxUI(consoleConverter);
        setTLauncherBasicComboBoxUI(connQuality);
        setTLauncherBasicComboBoxUI(launchAction);
        setTLauncherBasicComboBoxUI(this.local);
        setTLauncherBasicComboBoxUI(backupFrequency);
        SettingElement settingElement = new SettingElement("settings.console.label", consoleConverter, 21);
        springLayout.putConstraint("North", settingElement, 0, "North", this);
        springLayout.putConstraint("West", settingElement, 0, "West", this);
        springLayout.putConstraint("South", settingElement, 21, "North", this);
        springLayout.putConstraint("East", settingElement, 0, "East", this);
        add((Component) settingElement);
        SettingElement settingElement_3 = new SettingElement("settings.connection.label", connQuality, 21);
        springLayout.putConstraint("North", settingElement_3, 15, "South", settingElement);
        springLayout.putConstraint("West", settingElement_3, 0, "West", this);
        springLayout.putConstraint("East", settingElement_3, 0, "East", this);
        add((Component) settingElement_3);
        SettingElement settingElement_4 = new SettingElement("settings.launch-action.label", launchAction, 21);
        springLayout.putConstraint("North", settingElement_4, 15, "South", settingElement_3);
        springLayout.putConstraint("West", settingElement_4, 0, "West", this);
        springLayout.putConstraint("East", settingElement_4, 0, "East", this);
        add((Component) settingElement_4);
        SettingElement settingElement_5 = new SettingElement("settings.lang.label", this.local, 21);
        springLayout.putConstraint("North", settingElement_5, 15, "South", settingElement_4);
        springLayout.putConstraint("West", settingElement_5, 0, "West", settingElement);
        springLayout.putConstraint("East", settingElement_5, 0, "East", settingElement);
        add((Component) settingElement_5);
        EditorTextField maxBackupSizeEditorField = new EditorTextField();
        EditorTextField maxTimeForBackupEditorField = new EditorTextField();
        EditorTextField backupRepetitionEditorField = new EditorTextField();
        maxTimeForBackupEditorField.setColumns(5);
        maxBackupSizeEditorField.setColumns(5);
        backupRepetitionEditorField.setText(this.tlauncher.getConfiguration().get(BackupSetting.REPEAT_BACKUP.toString()));
        JPanel backupTitlePanel = new JPanel();
        LocalizableLabel backupTitle = new LocalizableLabel("settings.backup.title");
        backupTitlePanel.setPreferredSize(new Dimension(backupTitlePanel.getWidth(), 27));
        backupTitle.setFont(SettingElement.LABEL_FONT);
        backupTitlePanel.add(backupTitle);
        backupTitlePanel.add(backupTitleQuestionLabel);
        backupTitlePanel.setBackground(SettingsScene.BACKGROUND);
        backupTitleQuestionLabel.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.1
            public void mouseClicked(MouseEvent e) {
                String stringPath = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY) + File.separator + Localizable.get("version.name") + File.separator + PathAppUtil.DIRECTORY_WORLDS;
                Alert.showHtmlMessage(Localizable.get("settings.backup.title"), Localizable.get(BackupToolTips.TITLE.toString(), MinecraftUtil.buildWorkingPath(PathAppUtil.BACKUP_DIRECTORY), TlauncherSettings.this.tlauncher.getConfiguration().get(BackupSetting.FREE_PARTITION_SIZE.toString()), MinecraftUtil.buildWorkingPath(PathAppUtil.DIRECTORY_WORLDS), stringPath, MinecraftUtil.buildWorkingPath(PathAppUtil.BACKUP_DIRECTORY)), 1, 700);
            }
        });
        doBackupQuestionLabel.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.2
            public void mouseClicked(MouseEvent e) {
                Alert.showCustomMonolog(Localizable.get("settings.doBackup").replace(":", CoreConstants.EMPTY_STRING), Localizable.get("settings.doBackup.tooltip"));
            }
        });
        springLayout.putConstraint("North", backupTitlePanel, 5, "South", settingElement_5);
        springLayout.putConstraint("West", backupTitlePanel, 0, "West", this);
        springLayout.putConstraint("East", backupTitlePanel, 0, "East", this);
        add((Component) backupTitlePanel);
        JPanel doBackupPanel = new JPanel();
        EditorTextField doBackupEditorField = new EditorTextField();
        doBackupPanel.setBackground(SettingsScene.BACKGROUND);
        doBackupEditorField.setVisible(false);
        doBackupPanel.add(doBackup);
        doBackupPanel.add(doBackupEditorField);
        doBackup.addActionListener(e -> {
            initBackupCheckbox(doBackup, doBackup, backupFrequency, maxBackupSizeEditorField, maxTimeForBackupEditorField, backupRepetitionEditorField);
        });
        initBackupCheckbox(doBackup, backupFrequency, maxBackupSizeEditorField, maxTimeForBackupEditorField, backupRepetitionEditorField, doBackupEditorField);
        backupFrequency.addActionListener(e2 -> {
            if (backupFrequency.getSelectedValue() != 0) {
                String s = ((BackupFrequency) backupFrequency.getSelectedValue()).toString();
                if (s.equals(BackupFrequency.EVERYTIME.toString())) {
                    backupRepetitionEditorField.setText("0");
                } else if (s.equals(BackupFrequency.OFTEN.toString())) {
                    backupRepetitionEditorField.setText("1");
                }
            }
        });
        SettingElement settingElement_6 = new SettingElement("settings.doBackup", (JComponent) doBackup, 20, -1, (JComponent) doBackupQuestionLabel);
        springLayout.putConstraint("North", settingElement_6, 0, "South", backupTitlePanel);
        springLayout.putConstraint("West", settingElement_6, 0, "West", this);
        springLayout.putConstraint("East", settingElement_6, 0, "East", this);
        add((Component) settingElement_6);
        SettingElement settingElement_7 = new SettingElement("settings.backup.frequency", backupFrequency, 27);
        springLayout.putConstraint("North", settingElement_7, 5, "South", settingElement_6);
        springLayout.putConstraint("West", settingElement_7, 0, "West", this);
        springLayout.putConstraint("East", settingElement_7, 0, "East", this);
        add((Component) settingElement_7);
        JPanel maxBackupSizePanel = getCommonPanel("progress.bar.panel.size", "settings.max.backup.size", BackupToolTips.MAX_BACKUP_SIZE.toString());
        SettingElement settingElement_8 = new SettingElement("settings.max.backup.size", (JComponent) maxBackupSizeEditorField, 27, -1, (JComponent) maxBackupSizePanel);
        springLayout.putConstraint("North", settingElement_8, 4, "South", settingElement_7);
        springLayout.putConstraint("West", settingElement_8, 0, "West", this);
        springLayout.putConstraint("East", settingElement_8, 0, "East", this);
        add((Component) settingElement_8);
        JPanel maxTimeForBackupPanel = getCommonPanel("progress.bar.panel.remaining.time.days", "settings.max.backup.time", BackupToolTips.MAX_BACKUP_SAVE_TIME.toString());
        SettingElement settingElement_9 = new SettingElement("settings.max.backup.time", (JComponent) maxTimeForBackupEditorField, 27, -1, (JComponent) maxTimeForBackupPanel);
        springLayout.putConstraint("North", settingElement_9, 0, "South", settingElement_8);
        springLayout.putConstraint("West", settingElement_9, 0, "West", this);
        springLayout.putConstraint("East", settingElement_9, 0, "East", this);
        add((Component) settingElement_9);
        EditorFieldChangeListener changeListener = new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.3
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldvalue, String newvalue) {
                Console c = (Console) TLauncher.getInjector().getInstance(Key.get(Console.class, Names.named("console")));
                switch (AnonymousClass8.$SwitchMap$org$tlauncher$tlauncher$configuration$enums$ConsoleType[ConsoleType.get(newvalue).ordinal()]) {
                    case 1:
                        c.show(false);
                        return;
                    case 2:
                        c.hide();
                        return;
                    default:
                        throw new IllegalArgumentException("Unknown console type!");
                }
            }
        };
        EditorFieldChangeListener conQualityListener = new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.4
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
                TlauncherSettings.this.tlauncher.getDownloader().setConfiguration(TlauncherSettings.this.global.getConnectionQuality());
            }
        };
        EditorFieldChangeListener localeListener = new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.5
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldvalue, String newvalue) {
                if (TlauncherSettings.this.tlauncher.getFrame() != null) {
                    TlauncherSettings.this.tlauncher.getFrame().updateLocales();
                }
            }
        };
        addHandler(new HandlerSettings("gui.console", consoleConverter, changeListener));
        addHandler(new HandlerSettings("connection", connQuality, conQualityListener));
        addHandler(new HandlerSettings("minecraft.onlaunch", launchAction));
        addHandler(new HandlerSettings("locale", this.local, localeListener));
        addHandler(new HandlerSettings(BackupSetting.MAX_SIZE_FOR_WORLD.toString(), maxBackupSizeEditorField));
        addHandler(new HandlerSettings(BackupSetting.MAX_TIME_FOR_BACKUP.toString(), maxTimeForBackupEditorField));
        addHandler(new HandlerSettings(BackupSetting.REPEAT_BACKUP.toString(), backupFrequency));
        addHandler(new HandlerSettings(BackupSetting.SKIP_USER_BACKUP.toString(), doBackupEditorField));
    }

    /* renamed from: org.tlauncher.tlauncher.ui.settings.TlauncherSettings$8  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/TlauncherSettings$8.class */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$org$tlauncher$tlauncher$configuration$enums$ConsoleType = new int[ConsoleType.values().length];

        static {
            try {
                $SwitchMap$org$tlauncher$tlauncher$configuration$enums$ConsoleType[ConsoleType.GLOBAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$tlauncher$tlauncher$configuration$enums$ConsoleType[ConsoleType.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private JPanel getCommonPanel(String text, final String title, final String toolTipText) {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(SettingsScene.BACKGROUND);
        mainPanel.add(new LocalizableLabel(text));
        JLabel questionMark = new JLabel(new ImageIcon(ImageCache.getImage("qestion-option-panel.png")));
        questionMark.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.6
            public void mouseClicked(MouseEvent e) {
                Alert.showCustomMonolog(Localizable.get(title).replace(":", CoreConstants.EMPTY_STRING), Localizable.get(toolTipText));
            }
        });
        mainPanel.add(questionMark);
        return mainPanel;
    }

    private static <T> void setTLauncherBasicComboBoxUI(JComboBox<T> comboBox) {
        comboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
        comboBox.setUI(new TlauncherBasicComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.7
            @Override // org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI
            protected ComboPopup createPopup() {
                BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.7.1
                    protected JScrollPane createScroller() {
                        VersionScrollBarUI barUI = new VersionScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.settings.TlauncherSettings.7.1.1
                            @Override // org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI
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
                                dim.setSize(13.0d, dim.getHeight());
                                return dim;
                            }
                        };
                        barUI.setGapThubm(5);
                        JScrollPane scroller = new JScrollPane(this.list, 20, 31);
                        scroller.getVerticalScrollBar().setUI(barUI);
                        return scroller;
                    }
                };
                basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
                return basic;
            }
        });
    }

    public boolean chooseChinaLocal() {
        if (Objects.isNull(this.global.getLocale())) {
            return false;
        }
        return this.local.getSelectedValue().getLanguage().equals(new Locale("zh").getLanguage()) || this.global.getLocale().getLanguage().equals(new Locale("zh").getLanguage());
    }

    private void initBackupCheckbox(EditorCheckBox doBackup, EditorComboBox<BackupFrequency> backupFrequency, EditorTextField maxBackupSizeEditorField, EditorTextField maxTimeForBackupEditorField, EditorTextField backupRepetitionEditorField, EditorTextField doBackupEditorField) {
        if (doBackup.isSelected()) {
            maxTimeForBackupEditorField.setEditable(true);
            maxBackupSizeEditorField.setEditable(true);
            backupRepetitionEditorField.setEditable(true);
            backupFrequency.setEnabled(true);
            doBackupEditorField.setText("false");
            return;
        }
        maxTimeForBackupEditorField.setEditable(false);
        maxBackupSizeEditorField.setEditable(false);
        backupRepetitionEditorField.setEditable(false);
        backupFrequency.setEnabled(false);
        doBackupEditorField.setText("true");
    }
}
