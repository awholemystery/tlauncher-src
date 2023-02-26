package org.tlauncher.tlauncher.ui.scenes;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.minecraft.user.MinecraftUser;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.server.BackPanel;
import org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.tlauncher.ui.settings.ResetView;
import org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface;
import org.tlauncher.tlauncher.ui.settings.TlauncherSettings;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.ui.RadioSettingsUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/SettingsScene.class */
public class SettingsScene extends PseudoScene {
    private final MainPane main;
    private ExtendedPanel base;
    private ButtonGroup buttonGroup;
    public final TLauncher tlauncher;
    public final Configuration global;
    private List<SettingsHandlerInterface> pages;
    JButton reset;
    private TlauncherSettings tlauncherSettings;
    private ConfidentialitySetting confidentialitySetting;
    public static final Dimension SIZE = new Dimension(620, 529);
    private static final Color SWITCH_FOREGROUND = new Color(60, 170, 232);
    private static final Color LEFT_BUTTONS_BACKGROUND = new Color(88, 159, 42);
    public static final Color BACKGROUND = new Color(245, 245, 245);
    public static final Insets BUTTON_INSETS = new Insets(9, 19, 20, 19);
    public static final Insets CENTER_INSETS = new Insets(20, 19, 0, 19);
    public static String minecraft = MinecraftUser.TYPE;
    public static String tlaucner = "tlauncher";
    public static String confidentiality = "confidentiality";

    public SettingsScene(MainPane main) {
        super(main);
        this.buttonGroup = new ButtonGroup();
        this.pages = new ArrayList();
        this.main = main;
        this.tlauncher = TLauncher.getInstance();
        this.global = this.tlauncher.getConfiguration();
        Component extendedPanel = new ExtendedPanel();
        LayoutManager springLayout = new SpringLayout();
        this.base = new ExtendedPanel();
        Component backPanel = new BackPanel("settings.title", new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.1
            public void mouseClicked(MouseEvent e) {
                SettingsScene.this.main.openDefaultScene();
            }
        }, ImageCache.getIcon("back-arrow.png"));
        Component extendedPanel2 = new ExtendedPanel();
        Component extendedPanel3 = new ExtendedPanel();
        Component extendedPanel4 = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
        Component component = (MinecraftSettings) TLauncher.getInjector().getInstance(MinecraftSettings.class);
        this.tlauncherSettings = new TlauncherSettings();
        this.confidentialitySetting = new ConfidentialitySetting();
        this.pages.add(this.tlauncherSettings);
        this.pages.add(component);
        this.pages.add(this.confidentialitySetting);
        this.base.setSize(SIZE);
        this.base.setOpaque(true);
        this.base.setBackground(BACKGROUND);
        extendedPanel2.setLayout(new GridLayout(1, 2, 0, 0));
        extendedPanel3.setLayout(new CardLayout(0, 0));
        this.base.setLayout(new BorderLayout(0, 0));
        extendedPanel.setPreferredSize(new Dimension(SIZE.width, 49));
        extendedPanel4.setPreferredSize(new Dimension(SIZE.width, 55));
        extendedPanel3.setInsets(CENTER_INSETS);
        extendedPanel4.setInsets(BUTTON_INSETS);
        extendedPanel.setLayout(springLayout);
        springLayout.putConstraint("North", backPanel, 0, "North", extendedPanel);
        springLayout.putConstraint("West", backPanel, 0, "West", extendedPanel);
        springLayout.putConstraint("South", backPanel, 25, "North", extendedPanel);
        springLayout.putConstraint("East", backPanel, 0, "East", extendedPanel);
        extendedPanel.add(backPanel);
        springLayout.putConstraint("North", extendedPanel2, 0, "South", backPanel);
        springLayout.putConstraint("West", extendedPanel2, 0, "West", extendedPanel);
        springLayout.putConstraint("South", extendedPanel2, 0, "South", extendedPanel);
        springLayout.putConstraint("East", extendedPanel2, 0, "East", extendedPanel);
        extendedPanel.add(extendedPanel2);
        extendedPanel3.add(minecraft, component);
        extendedPanel3.add(tlaucner, this.tlauncherSettings);
        extendedPanel3.add(confidentiality, this.confidentialitySetting);
        Component createRadioButton = createRadioButton("settings.tab.minecraft", e -> {
            CardLayout cl = centerPanel.getLayout();
            cl.show(centerPanel, minecraft);
            repaint();
        });
        Component createRadioButton2 = createRadioButton("settings.tab.tlauncher", e2 -> {
            CardLayout cl = centerPanel.getLayout();
            cl.show(centerPanel, tlaucner);
            repaint();
        });
        Component createRadioButton3 = createRadioButton("settings.tab.confidentiality", e3 -> {
            CardLayout cl = centerPanel.getLayout();
            cl.show(centerPanel, confidentiality);
            repaint();
        });
        createRadioButton.addActionListener(new AbstractAction() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.2
            public void actionPerformed(ActionEvent e4) {
                if (SettingsScene.this.reset.isVisible()) {
                    SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
                }
            }
        });
        createRadioButton2.addActionListener(new AbstractAction() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.3
            public void actionPerformed(ActionEvent e4) {
                if (!SettingsScene.this.reset.isVisible()) {
                    SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
                }
            }
        });
        createRadioButton3.addActionListener(new AbstractAction() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.4
            public void actionPerformed(ActionEvent e4) {
                if (SettingsScene.this.reset.isVisible()) {
                    SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
                }
            }
        });
        createRadioButton.setSelected(true);
        extendedPanel2.add(createRadioButton);
        extendedPanel2.add(createRadioButton2);
        extendedPanel2.add(createRadioButton3);
        fillButtons(extendedPanel4);
        this.base.add(extendedPanel3, "Center");
        this.base.add(extendedPanel, "North");
        this.base.add(extendedPanel4, "South");
        add((Component) this.base);
        SwingUtil.changeFontFamily(backPanel, FontTL.ROBOTO_BOLD);
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.5
            public void componentShown(ComponentEvent e4) {
                for (SettingsHandlerInterface settingsHandlerInterface : SettingsScene.this.pages) {
                    settingsHandlerInterface.init();
                }
            }
        });
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
        this.base.setLocation((int) ((getWidth() / 2) - (SIZE.getWidth() / 2.0d)), (int) (((getHeight() - LoginForm.LOGIN_SIZE.height) / 2) - (SIZE.getHeight() / 2.0d)));
    }

    private LocalizableRadioButton createRadioButton(String name, ActionListener actionListener) {
        LocalizableRadioButton button = new LocalizableRadioButton(name);
        button.setUI(new RadioSettingsUI(ImageCache.getImage("background-tab-off.png")));
        button.setOpaque(true);
        button.addActionListener(actionListener);
        button.setForeground(SWITCH_FOREGROUND);
        button.setFont(button.getFont().deriveFont(1, 16.0f));
        this.buttonGroup.add(button);
        return button;
    }

    private void fillButtons(ExtendedPanel buttons) {
        Font font = new JButton().getFont().deriveFont(1, 13.0f);
        final UpdaterButton saveButton = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
        saveButton.setFont(font);
        saveButton.addActionListener(e -> {
            for (SettingsHandlerInterface settingsHandlerInterface : this.pages) {
                if (!settingsHandlerInterface.validateSettings()) {
                    return;
                }
            }
            boolean restart = restartClient();
            if (this.tlauncherSettings.chooseChinaLocal() && !restart) {
                this.main.openDefaultScene();
                return;
            }
            for (SettingsHandlerInterface settingsHandlerInterface2 : this.pages) {
                settingsHandlerInterface2.setValues();
            }
            this.global.store();
            if (restart) {
                try {
                    Bootstrapper.restartLauncher().start();
                } catch (Throwable e1) {
                    U.log(e1);
                }
                System.exit(0);
            }
            ((ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class)).resetInfoMod();
            this.main.openDefaultScene();
        });
        saveButton.setForeground(Color.WHITE);
        final UpdaterButton defaultButton = new UpdaterButton(UpdaterButton.GREEN_COLOR, "settings.default");
        defaultButton.setFont(font);
        defaultButton.addActionListener(e2 -> {
            if (Alert.showLocQuestion("settings.default.warning")) {
                for (SettingsHandlerInterface settingsHandlerInterface : this.pages) {
                    settingsHandlerInterface.setDefaultSettings();
                }
            }
        });
        defaultButton.setForeground(Color.WHITE);
        defaultButton.setPreferredSize(new Dimension(178, 26));
        saveButton.setPreferredSize(new Dimension(141, 26));
        defaultButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.6
            public void mouseEntered(MouseEvent e3) {
                defaultButton.setBackground(SettingsScene.LEFT_BUTTONS_BACKGROUND);
            }

            public void mouseExited(MouseEvent e3) {
                defaultButton.setBackground(defaultButton.getBackgroundColor());
            }
        });
        saveButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.SettingsScene.7
            public void mouseEntered(MouseEvent e3) {
                saveButton.setBackground(ColorUtil.COLOR_204);
            }

            public void mouseExited(MouseEvent e3) {
                saveButton.setBackground(saveButton.getBackgroundColor());
            }
        });
        this.reset = new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.button");
        SwingUtil.setFontSize(this.reset, 13.0f, 1);
        this.reset.setPreferredSize(new Dimension(141, 26));
        this.reset.addActionListener(e3 -> {
            ResetView view = new ResetView();
            Alert.showMessage(Localizable.get("settings.reset.button"), view, new JButton[]{view.getResetAgain()});
        });
        this.reset.setVisible(false);
        buttons.add((Component) this.reset);
        buttons.add(Box.createHorizontalStrut(100));
        buttons.add((Component) defaultButton);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add((Component) saveButton);
    }

    private boolean restartClient() {
        if (this.tlauncherSettings.chooseChinaLocal()) {
            return Alert.showLocQuestion("tlauncher.restart", "tlauncher.restart.message");
        }
        return false;
    }
}
