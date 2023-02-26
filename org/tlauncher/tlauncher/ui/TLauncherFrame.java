package org.tlauncher.tlauncher.ui;

import ch.qos.logback.core.CoreConstants;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.console.Console;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.tlauncher.ui.login.LoginFormHelper;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/TLauncherFrame.class */
public class TLauncherFrame extends JFrame {
    public static final float fontSize;
    public static final Color BLUE_COLOR;
    private final TLauncherFrame instance = this;
    private final TLauncher tlauncher;
    private final Configuration settings;
    private final LangConfiguration lang;
    public final MainPane mp;
    private String brand;

    static {
        fontSize = OS.WINDOWS.isCurrent() ? 12.0f : 14.0f;
        BLUE_COLOR = new Color(60, 170, 232);
    }

    public TLauncherFrame(TLauncher t) {
        this.tlauncher = t;
        this.settings = t.getConfiguration();
        this.lang = t.getLang();
        if (!this.settings.getLocale().getLanguage().equals(new Locale("zh").getLanguage())) {
            SwingUtil.initFont((int) fontSize);
        }
        SwingUtil.setFavicons(this);
        setUILocale();
        setWindowSize();
        setWindowTitle();
        addWindowListener(new WindowAdapter() { // from class: org.tlauncher.tlauncher.ui.TLauncherFrame.1
            public void windowClosing(WindowEvent e) {
                TLauncherFrame.this.instance.setVisible(false);
                TLauncher.kill();
            }
        });
        setDefaultCloseOperation(3);
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.TLauncherFrame.2
            public void componentShown(ComponentEvent e) {
                TLauncherFrame.this.instance.validate();
                TLauncherFrame.this.instance.repaint();
                TLauncherFrame.this.instance.toFront();
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
        addWindowStateListener(e -> {
            int newState = getExtendedStateFor(e.getNewState());
            if (newState == -1) {
                return;
            }
            this.settings.set("gui.window", Integer.valueOf(newState));
        });
        U.setLoadingStep(Bootstrapper.LoadingStep.PREPARING_MAINPANE);
        this.mp = new MainPane(this);
        add(this.mp);
        U.setLoadingStep(Bootstrapper.LoadingStep.POSTINIT_GUI);
    }

    public void afterInitProfile() {
        log("Packing main frame...");
        pack();
        log("Resizing main pane...");
        this.mp.onResize();
        setWindowTitle();
        Object[] objArr = new Object[1];
        objArr[0] = this.tlauncher.getUpdater().isClosed() ? "Updater is already closed. Showing up..." : "Waiting for Updater to close.";
        U.log(objArr);
        while (!this.tlauncher.getUpdater().isClosed()) {
            U.sleepFor(100L);
        }
        U.log("Visibility set.");
        setLocationRelativeTo(null);
        requestFocusInWindow();
    }

    public TLauncher getLauncher() {
        return this.tlauncher;
    }

    public Configuration getConfiguration() {
        return this.settings;
    }

    public void updateLocales() {
        try {
            this.tlauncher.reloadLocale();
            ((Console) TLauncher.getInjector().getInstance(Key.get(Console.class, Names.named("console")))).updateLocale();
            LocalizableMenuItem.updateLocales();
            setWindowTitle();
            setUILocale();
            Localizable.updateContainer(this);
        } catch (Exception e) {
            log("Cannot reload settings!", e);
        }
    }

    private void updateTitle() {
        this.brand = CoreConstants.EMPTY_STRING + TLauncher.getVersion();
    }

    private void setWindowTitle() {
        updateTitle();
        setTitle(String.format("TLauncher %s", this.brand));
    }

    private void setWindowSize() {
        setResizable(false);
    }

    private void setUILocale() {
        UIManager.put("OptionPane.yesButtonText", this.lang.nget("ui.yes"));
        UIManager.put("OptionPane.noButtonText", this.lang.nget("ui.no"));
        UIManager.put("OptionPane.cancelButtonText", this.lang.nget("ui.cancel"));
        UIManager.put("FileChooser.acceptAllFileFilterText", this.lang.nget("explorer.extension.all"));
        UIManager.put("FileChooser.lookInLabelText", this.lang.nget("explorer.lookin"));
        UIManager.put("FileChooser.saveInLabelText", this.lang.nget("explorer.lookin"));
        UIManager.put("FileChooser.fileNameLabelText", this.lang.nget("explorer.input.filename"));
        UIManager.put("FileChooser.folderNameLabelText", this.lang.nget("explorer.input.foldername"));
        UIManager.put("FileChooser.filesOfTypeLabelText", this.lang.nget("explorer.input.type"));
        UIManager.put("FileChooser.upFolderToolTipText", this.lang.nget("explorer.button.up.tip"));
        UIManager.put("FileChooser.upFolderAccessibleName", this.lang.nget("explorer.button.up"));
        UIManager.put("FileChooser.newFolderToolTipText", this.lang.nget("explorer.button.newfolder.tip"));
        UIManager.put("FileChooser.newFolderAccessibleName", this.lang.nget("explorer.button.newfolder"));
        UIManager.put("FileChooser.newFolderButtonToolTipText", this.lang.nget("explorer.button.newfolder.tip"));
        UIManager.put("FileChooser.newFolderButtonText", this.lang.nget("explorer.button.newfolder"));
        UIManager.put("FileChooser.other.newFolder", this.lang.nget("explorer.button.newfolder.name"));
        UIManager.put("FileChooser.other.newFolder.subsequent", this.lang.nget("explorer.button.newfolder.name"));
        UIManager.put("FileChooser.win32.newFolder", this.lang.nget("explorer.button.newfolder.name"));
        UIManager.put("FileChooser.win32.newFolder.subsequent", this.lang.nget("explorer.button.newfolder.name"));
        UIManager.put("FileChooser.homeFolderToolTipText", this.lang.nget("explorer.button.home.tip"));
        UIManager.put("FileChooser.homeFolderAccessibleName", this.lang.nget("explorer.button.home"));
        UIManager.put("FileChooser.listViewButtonToolTipText", this.lang.nget("explorer.button.list.tip"));
        UIManager.put("FileChooser.listViewButtonAccessibleName", this.lang.nget("explorer.button.list"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", this.lang.nget("explorer.button.details.tip"));
        UIManager.put("FileChooser.detailsViewButtonAccessibleName", this.lang.nget("explorer.button.details"));
        UIManager.put("FileChooser.viewMenuButtonToolTipText", this.lang.nget("explorer.button.view.tip"));
        UIManager.put("FileChooser.viewMenuButtonAccessibleName", this.lang.nget("explorer.button.view"));
        UIManager.put("FileChooser.newFolderErrorText", this.lang.nget("explorer.error.newfolder"));
        UIManager.put("FileChooser.newFolderErrorSeparator", ": ");
        UIManager.put("FileChooser.newFolderParentDoesntExistTitleText", this.lang.nget("explorer.error.newfolder-nopath"));
        UIManager.put("FileChooser.newFolderParentDoesntExistText", this.lang.nget("explorer.error.newfolder-nopath"));
        UIManager.put("FileChooser.fileDescriptionText", this.lang.nget("explorer.details.file"));
        UIManager.put("FileChooser.directoryDescriptionText", this.lang.nget("explorer.details.dir"));
        UIManager.put("FileChooser.saveButtonText", this.lang.nget("explorer.button.save"));
        UIManager.put("FileChooser.openButtonText", this.lang.nget("explorer.button.open"));
        UIManager.put("FileChooser.saveDialogTitleText", this.lang.nget("explorer.title.save"));
        UIManager.put("FileChooser.openDialogTitleText", this.lang.nget("explorer.title.open"));
        UIManager.put("FileChooser.cancelButtonText", this.lang.nget("explorer.button.cancel"));
        UIManager.put("FileChooser.updateButtonText", this.lang.nget("explorer.button.update"));
        UIManager.put("FileChooser.helpButtonText", this.lang.nget("explorer.button.help"));
        UIManager.put("FileChooser.directoryOpenButtonText", this.lang.nget("explorer.button.open-dir"));
        UIManager.put("FileChooser.saveButtonToolTipText", this.lang.nget("explorer.title.save.tip"));
        UIManager.put("FileChooser.openButtonToolTipText", this.lang.nget("explorer.title.open.tip"));
        UIManager.put("FileChooser.cancelButtonToolTipText", this.lang.nget("explorer.button.cancel.tip"));
        UIManager.put("FileChooser.updateButtonToolTipText", this.lang.nget("explorer.button.update.tip"));
        UIManager.put("FileChooser.helpButtonToolTipText", this.lang.nget("explorer.title.help.tip"));
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", this.lang.nget("explorer.button.open-dir.tip"));
        UIManager.put("FileChooser.viewMenuLabelText", this.lang.nget("explorer.button.view"));
        UIManager.put("FileChooser.refreshActionLabelText", this.lang.nget("explorer.context.refresh"));
        UIManager.put("FileChooser.newFolderActionLabelText", this.lang.nget("explorer.context.newfolder"));
        UIManager.put("FileChooser.listViewActionLabelText", this.lang.nget("explorer.view.list"));
        UIManager.put("FileChooser.detailsViewActionLabelText", this.lang.nget("explorer.view.details"));
        UIManager.put("FileChooser.filesListAccessibleName", this.lang.nget("explorer.view.list.name"));
        UIManager.put("FileChooser.filesDetailsAccessibleName", this.lang.nget("explorer.view.details.name"));
        UIManager.put("FileChooser.renameErrorTitleText", this.lang.nget("explorer.error.rename.title"));
        UIManager.put("FileChooser.renameErrorText", this.lang.nget("explorer.error.rename") + "\n{0}");
        UIManager.put("FileChooser.renameErrorFileExistsText", this.lang.nget("explorer.error.rename-exists"));
        UIManager.put("FileChooser.readOnly", Boolean.FALSE);
        UIManager.put("TabbedPane.contentOpaque", false);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 8, 6, 8));
        UIManager.put("OptionPane.questionIcon", ImageCache.getIcon("qestion-option-panel.png"));
        UIManager.put("OptionPane.warningIcon", ImageCache.getIcon("warning.png"));
        UIManager.put("OptionPane.informationIcon", ImageCache.getIcon("exclamation-point.png"));
        UIManager.put("TabbedPane.foreground", new Color(60, 170, 232));
        UIManager.put("TabbedPane.font", new Font("Roboto", 1, 16));
    }

    public void setSize(int width, int height) {
        if ((getWidth() == width && getHeight() == height) || getExtendedState() != 0) {
            return;
        }
        boolean show = isVisible();
        if (show) {
            setVisible(false);
        }
        super.setSize(width, height);
        if (show) {
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }

    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    private static int getExtendedStateFor(int state) {
        switch (state) {
            case 0:
            case 2:
            case 4:
            case 6:
                return state;
            case 1:
            case 3:
            case 5:
            default:
                return -1;
        }
    }

    public static URL getRes(String uri) {
        return TLauncherFrame.class.getResource(uri);
    }

    private static void log(Object... o) {
        U.log("[Frame]", o);
    }

    public void showTips() {
        if (this.mp.defaultScene.loginForm.accountPanel.getTypeAccountShow() == Account.AccountType.FREE && this.mp.defaultScene.loginForm.accountPanel.username.getUsername() == null) {
            this.mp.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.SHOWN);
        }
    }
}
