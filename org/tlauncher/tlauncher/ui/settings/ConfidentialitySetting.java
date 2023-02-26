package org.tlauncher.tlauncher.ui.settings;

import by.gdev.util.DesktopUtil;
import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.controller.StatisticsController;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.converter.ActionOnLaunchConverter;
import org.tlauncher.tlauncher.ui.converter.BackupFrequencyConverter;
import org.tlauncher.tlauncher.ui.converter.ConnectionQualityConverter;
import org.tlauncher.tlauncher.ui.converter.ConsoleTypeConverter;
import org.tlauncher.tlauncher.ui.converter.LocaleConverter;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.tlauncher.ui.swing.extended.HTMLLabel;
import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/ConfidentialitySetting.class */
public class ConfidentialitySetting extends PageSettings {
    private static final long serialVersionUID = -2705671690811623008L;
    public final TLauncher tlauncher = TLauncher.getInstance();
    public final Configuration global = this.tlauncher.getConfiguration();
    public final LangConfiguration lang = this.tlauncher.getLang();
    private final EditorComboBox<Locale> local;
    private HTMLLabel documentText;
    HtmlTextPane html;

    public ConfidentialitySetting() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        final Color backgroundOldButtonColor = new Color(219, 64, 44);
        final UpdaterButton errorButton = new UpdaterButton(backgroundOldButtonColor, "settings.delete");
        final StatisticsController statisticsController = (StatisticsController) TLauncher.getInjector().getInstance(StatisticsController.class);
        EditorComboBox<ConsoleType> consoleConverter = new EditorComboBox<>(new ConsoleTypeConverter(), ConsoleType.values());
        EditorCheckBox statistics = new EditorCheckBox("statistics.settings.checkbox.name");
        EditorCheckBox guard = new EditorCheckBox("settings.guard");
        EditorCheckBox recommendedServers = new EditorCheckBox("settings.servers.recommendation");
        this.documentText = new HTMLLabel();
        this.documentText.setHorizontalAlignment(0);
        JLabel questionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        EditorComboBox<ConnectionQuality> connQuality = new EditorComboBox<>(new ConnectionQualityConverter(), ConnectionQuality.values());
        EditorComboBox<ActionOnLaunch> launchAction = new EditorComboBox<>(new ActionOnLaunchConverter(), ActionOnLaunch.values());
        EditorComboBox<BackupFrequency> backupFrequency = new EditorComboBox<>(new BackupFrequencyConverter(), BackupFrequency.values());
        this.local = new EditorComboBox<>(new LocaleConverter(), this.global.getLocales());
        setTLauncherBasicComboBoxUI(consoleConverter);
        setTLauncherBasicComboBoxUI(connQuality);
        setTLauncherBasicComboBoxUI(launchAction);
        setTLauncherBasicComboBoxUI(this.local);
        setTLauncherBasicComboBoxUI(backupFrequency);
        SettingElement settingElement_2 = new SettingElement("statistics.settings.title", statistics, 17, -1);
        springLayout.putConstraint("North", settingElement_2, 0, "North", this);
        springLayout.putConstraint("West", settingElement_2, 0, "West", this);
        springLayout.putConstraint("South", settingElement_2, 21, "North", this);
        springLayout.putConstraint("East", settingElement_2, 0, "East", this);
        add((Component) settingElement_2);
        SettingElement settingElement_guard = new SettingElement("settings.guard.title", (JComponent) guard, 19, -1, (JComponent) questionLabel);
        questionLabel.setBounds(0, 0, 20, 19);
        springLayout.putConstraint("North", settingElement_guard, 15, "South", settingElement_2);
        springLayout.putConstraint("West", settingElement_guard, 0, "West", this);
        springLayout.putConstraint("East", settingElement_guard, 0, "East", this);
        springLayout.putConstraint("South", settingElement_guard, 38, "South", settingElement_2);
        add((Component) settingElement_guard);
        this.html = HtmlTextPane.createNew(Localizable.get("settings.servers.analytics"), HttpStatus.SC_BAD_REQUEST);
        SettingElement settingElement_servers = new SettingElement("settings.servers.recommendation.title", recommendedServers, 19, -1);
        springLayout.putConstraint("North", settingElement_servers, 15, "South", settingElement_guard);
        springLayout.putConstraint("West", settingElement_servers, 0, "West", this);
        springLayout.putConstraint("East", settingElement_servers, 0, "East", this);
        springLayout.putConstraint("South", settingElement_servers, 50, "South", settingElement_guard);
        add((Component) settingElement_servers);
        SettingElement settingElement_analytics = new SettingElement("settings.servers.analytics.title", (JComponent) this.html, 19, -1, 1);
        springLayout.putConstraint("North", settingElement_analytics, 15, "South", settingElement_servers);
        springLayout.putConstraint("West", settingElement_analytics, 0, "West", this);
        springLayout.putConstraint("East", settingElement_analytics, 0, "East", this);
        springLayout.putConstraint("South", settingElement_analytics, 180, "South", settingElement_servers);
        add((Component) settingElement_analytics);
        SettingElement settingElement_delete_analytics = new SettingElement(CoreConstants.EMPTY_STRING, errorButton, 19, -1);
        springLayout.putConstraint("North", settingElement_delete_analytics, 15, "South", settingElement_analytics);
        springLayout.putConstraint("West", settingElement_delete_analytics, 245, "West", this);
        springLayout.putConstraint("East", settingElement_delete_analytics, -100, "East", this);
        springLayout.putConstraint("South", settingElement_delete_analytics, 40, "South", settingElement_analytics);
        add((Component) settingElement_delete_analytics);
        SettingElement settingElement_document = new SettingElement("settings.servers.document.title", this.documentText, 19, -1);
        springLayout.putConstraint("North", settingElement_document, 21, "South", settingElement_delete_analytics);
        springLayout.putConstraint("West", settingElement_document, 10, "West", this);
        springLayout.putConstraint("East", settingElement_document, -10, "East", this);
        springLayout.putConstraint("South", settingElement_document, 35, "South", settingElement_delete_analytics);
        add((Component) settingElement_document);
        errorButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.1
            public void mouseEntered(MouseEvent e) {
                errorButton.setBackground(new Color(255, 0, 0));
            }

            public void mouseExited(MouseEvent e) {
                errorButton.setBackground(backgroundOldButtonColor);
            }
        });
        errorButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.2
            public void actionPerformed(ActionEvent e) {
                StatisticsController statisticsController2 = statisticsController;
                CompletableFuture.runAsync(() -> {
                    DesktopUtil.uncheckCall(() -> {
                        LocalizableLabel message = new LocalizableLabel("settings.servers.analytics.delete");
                        String editMessege = String.format(message.getText(), TLauncher.getInstance().getConfiguration().getClient().toString());
                        statisticsController2.removeUserData();
                        Alert.showMessage(CoreConstants.EMPTY_STRING, Localizable.get().get(editMessege));
                        return null;
                    });
                }).exceptionally(t -> {
                    SwingUtilities.invokeLater(() -> {
                        Alert.showLocMessage(CoreConstants.EMPTY_STRING, "modpack.try.later", null);
                    });
                    U.log(t);
                    return null;
                });
            }
        });
        addHandler(new HandlerSettings("gui.statistics.checkbox", statistics));
        addHandler(new HandlerSettings("gui.settings.guard.checkbox", guard));
        addHandler(new HandlerSettings("gui.settings.servers.recommendation", recommendedServers));
        SwingUtil.changeFontFamily(this.documentText, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(errorButton, FontTL.ROBOTO_REGULAR, 14, Color.white);
        SwingUtil.changeFontFamily(this.html, FontTL.ROBOTO_REGULAR, 14);
        this.documentText.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.3
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Object[] objArr = new Object[1];
                    objArr[0] = TLauncher.getInstance().getConfiguration().isUSSRLocale() ? "ru" : "en";
                    String url = String.format("https://ad.tlauncher.org/link/privacy-policy-%s", objArr);
                    OS.openLink(url);
                }
            }
        });
        questionLabel.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.4
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Object[] objArr = new Object[1];
                    objArr[0] = TLauncher.getInstance().getConfiguration().isUSSRLocale() ? "ru" : "en";
                    String url = String.format("https://tlauncher.org/%s/guard.html", objArr);
                    OS.openLink(url);
                }
            }
        });
        ActionListener l = e -> {
            if (this.tlauncher.getProfileManager().isNotPremium()) {
                ((JCheckBox) e.getSource()).setSelected(true);
                Alert.showHtmlMessage(CoreConstants.EMPTY_STRING, Localizable.get("account.premium.not.available"), 1, HttpStatus.SC_BAD_REQUEST);
            }
        };
        recommendedServers.addActionListener(l);
        guard.addActionListener(l);
    }

    public boolean chooseChinaLocal() {
        if (Objects.isNull(this.global.getLocale())) {
            return false;
        }
        return this.local.getSelectedValue().getLanguage().equals(new Locale("zh").getLanguage()) || this.global.getLocale().getLanguage().equals(new Locale("zh").getLanguage());
    }

    private static <T> void setTLauncherBasicComboBoxUI(JComboBox<T> comboBox) {
        comboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
        comboBox.setUI(new TlauncherBasicComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.5
            @Override // org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI
            protected ComboPopup createPopup() {
                BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.5.1
                    protected JScrollPane createScroller() {
                        VersionScrollBarUI barUI = new VersionScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting.5.1.1
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

    @Override // org.tlauncher.tlauncher.ui.settings.PageSettings, org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface
    public void init() {
        super.init();
        this.documentText.setText(Localizable.get("settings.servers.document"));
        this.html.setText(Localizable.get("settings.servers.analytics"));
    }
}
