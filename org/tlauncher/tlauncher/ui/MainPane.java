package org.tlauncher.tlauncher.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.progress.login.LauncherProgress;
import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
import org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
import org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
import org.tlauncher.tlauncher.ui.scenes.SettingsScene;
import org.tlauncher.tlauncher.ui.scenes.VersionManagerScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
import org.tlauncher.tlauncher.ui.swing.progress.ProgressBarPanel;
import org.tlauncher.tlauncher.ui.ui.FancyProgressBar;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/MainPane.class */
public class MainPane extends ExtendedLayeredPane {
    private static final long serialVersionUID = 1577003831121606643L;
    public static final Dimension SIZE = new Dimension(1050, 655);
    public final BrowserHolder browser;
    public final DefaultScene defaultScene;
    public final VersionManagerScene versionManager;
    public final SettingsScene settingsScene;
    public final ModpackScene modpackScene;
    public final CompleteSubEntityScene completeSubEntityScene;
    public final ModpackEnitityScene modpackEnitityScene;
    public final AdditionalHostServerScene additionalHostServerScene;
    public final SideNotifier notifier;
    public final AccountEditorScene accountEditor;
    public final MicrosoftAuthScene microsoftAuthScene;
    private final TLauncherFrame rootFrame;
    private PseudoScene scene;
    private final ProgressBarPanel barPanel;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MainPane(TLauncherFrame frame) {
        super(null);
        setPreferredSize(SIZE);
        setMaximumSize(SIZE);
        this.rootFrame = frame;
        this.browser = BrowserHolder.getInstance();
        this.browser.setPane(this);
        add((Component) this.browser);
        this.notifier = new SideNotifier();
        this.notifier.setLocation(10, 10);
        this.notifier.setSize(32, 32);
        add((Component) this.notifier);
        this.defaultScene = new DefaultScene(this);
        add((Component) this.defaultScene);
        this.versionManager = new VersionManagerScene(this);
        add((Component) this.versionManager);
        this.modpackScene = new ModpackScene(this);
        add((Component) this.modpackScene);
        this.completeSubEntityScene = new CompleteSubEntityScene(this);
        add((Component) this.completeSubEntityScene);
        this.modpackEnitityScene = new ModpackEnitityScene(this);
        add((Component) this.modpackEnitityScene);
        this.additionalHostServerScene = new AdditionalHostServerScene(this);
        add((Component) this.additionalHostServerScene);
        this.microsoftAuthScene = new MicrosoftAuthScene(this);
        add((Component) this.microsoftAuthScene);
        LauncherProgress bar = new LauncherProgress();
        bar.setBorder(BorderFactory.createEmptyBorder());
        bar.setUI(new FancyProgressBar(ImageCache.getBufferedImage("login-progress-bar.png")));
        bar.setOpaque(false);
        this.barPanel = new ProgressBarPanel(ImageCache.getNativeIcon("speed-icon.png"), ImageCache.getNativeIcon("files-icon.png"), bar);
        this.defaultScene.loginForm.addLoginProcessListener(this.barPanel);
        TLauncher.getInstance().getDownloader().addListener(this.barPanel);
        add((Component) this.barPanel);
        this.accountEditor = new AccountEditorScene(this);
        add((Component) this.accountEditor);
        this.settingsScene = new SettingsScene(this);
        add((Component) this.settingsScene);
        setScene(this.defaultScene, false);
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.MainPane.1
            public void componentResized(ComponentEvent e) {
                MainPane.this.onResize();
            }
        });
    }

    public PseudoScene getScene() {
        return this.scene;
    }

    public void setScene(PseudoScene scene) {
        setScene(scene, true);
    }

    private void setScene(PseudoScene newscene, boolean animate) {
        DefaultScene[] components;
        if (newscene == null) {
            throw new NullPointerException();
        }
        if (newscene.equals(this.scene)) {
            return;
        }
        for (DefaultScene defaultScene : getComponents()) {
            if (newscene != this.additionalHostServerScene) {
                if (!defaultScene.equals(newscene) && (defaultScene instanceof PseudoScene) && defaultScene != this.defaultScene) {
                    defaultScene.setShown(false, animate);
                }
            } else if (!defaultScene.equals(newscene) && (defaultScene instanceof PseudoScene)) {
                defaultScene.setShown(false, animate);
            }
        }
        this.scene = newscene;
        this.scene.setShown(true);
        this.browser.setBrowserContentShown("scene", this.scene.equals(this.defaultScene));
    }

    public void openDefaultScene() {
        setScene(this.defaultScene);
    }

    public TLauncherFrame getRootFrame() {
        return this.rootFrame;
    }

    public ProgressBarPanel getProgress() {
        return this.barPanel;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        this.browser.onResize();
        this.barPanel.setBounds(0, (getHeight() - ProgressBarPanel.SIZE.height) - LoginForm.LOGIN_SIZE.height, ProgressBarPanel.SIZE.width, ProgressBarPanel.SIZE.height);
    }

    public Point getLocationOf(Component comp) {
        return SwingUtil.getRelativeLocation(this, comp);
    }

    public void openAccountEditor() {
        setScene(this.accountEditor);
    }
}
