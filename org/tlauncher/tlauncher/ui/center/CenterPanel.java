package org.tlauncher.tlauncher.ui.center;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.BlockablePanel;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.Del;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.extended.UnblockablePanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/CenterPanel.class */
public class CenterPanel extends BlockablePanel {
    private static final long serialVersionUID = -1975869198322761508L;
    public static final CenterPanelTheme defaultTheme = new DefaultCenterPanelTheme();
    public static final CenterPanelTheme tipTheme = new TipPanelTheme();
    public static final CenterPanelTheme loadingTheme = new LoadingPanelTheme();
    public static final CenterPanelTheme settingsTheme = new SettingsPanelTheme();
    public static final CenterPanelTheme updateTheme = new UpdateTheme();
    public static final Insets defaultInsets = new Insets(5, 24, 18, 24);
    public static final Insets squareInsets = new Insets(11, 15, 11, 15);
    public static final Insets ACCOUNT_LOGIN_INSETS = new Insets(11, 19, 11, 0);
    public static final Insets accountInsets = new Insets(0, 20, 1, 10);
    public static final Insets VERSION_LOGIN_INSETS = new Insets(10, 20, 10, 0);
    public static final Insets PLAY_INSETS = new Insets(11, 0, 11, 0);
    public static final Insets SERVER_SQUARE_INSETS = new Insets(1, 10, 10, 10);
    public static final Insets smallSquareInsets = new Insets(7, 7, 7, 7);
    public static final Insets smallSquareNoTopInsets = new Insets(5, 15, 5, 15);
    public static final Insets noInsets = new Insets(0, 0, 0, 0);
    public static final Insets ISETS_20 = new Insets(20, 20, 20, 20);
    public static final Insets MIDDLE_PANEL = new Insets(20, 20, 5, 20);
    protected static final int ARC_SIZE = 24;
    private final Insets insets;
    private final CenterPanelTheme theme;
    protected final ExtendedPanel messagePanel;
    protected final LocalizableLabel messageLabel;
    public final TLauncher tlauncher;
    public final Configuration global;
    public final LangConfiguration lang;

    public CenterPanel() {
        this(null, null);
    }

    public CenterPanel(Insets insets) {
        this(null, insets);
    }

    public CenterPanel(CenterPanelTheme theme) {
        this(theme, null);
    }

    public CenterPanel(CenterPanelTheme theme, Insets insets) {
        this.tlauncher = TLauncher.getInstance();
        this.global = this.tlauncher.getConfiguration();
        this.lang = this.tlauncher.getLang();
        CenterPanelTheme centerPanelTheme = theme == null ? defaultTheme : theme;
        CenterPanelTheme theme2 = centerPanelTheme;
        this.theme = centerPanelTheme;
        this.insets = insets == null ? defaultInsets : insets;
        setLayout(new BoxLayout(this, 3));
        setBackground(theme2.getPanelBackground());
        this.messageLabel = new LocalizableLabel("  ");
        this.messageLabel.setFont(getFont().deriveFont(1));
        this.messageLabel.setVerticalAlignment(0);
        this.messageLabel.setHorizontalTextPosition(0);
        this.messageLabel.setAlignmentX(0.5f);
        this.messagePanel = new ExtendedPanel();
        this.messagePanel.setLayout(new BoxLayout(this.messagePanel, 1));
        this.messagePanel.setAlignmentX(0.5f);
        this.messagePanel.setInsets(new Insets(3, 0, 3, 0));
        this.messagePanel.add((Component) this.messageLabel);
    }

    public CenterPanelTheme getTheme() {
        return this.theme;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
    public Insets getInsets() {
        return this.insets;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Del del(int aligment) {
        return new Del(1, aligment, this.theme.getBorder());
    }

    protected Del del(int aligment, int width, int height) {
        return new Del(1, aligment, width, height, this.theme.getBorder());
    }

    public void defocus() {
        requestFocusInWindow();
    }

    public boolean setError(String message) {
        this.messageLabel.setForeground(this.theme.getFailure());
        this.messageLabel.setText((message == null || message.length() == 0) ? " " : message);
        return false;
    }

    protected boolean setMessage(String message, Object... vars) {
        this.messageLabel.setForeground(this.theme.getFocus());
        this.messageLabel.setText((message == null || message.length() == 0) ? " " : message, vars);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setMessage(String message) {
        return setMessage(message, Localizable.EMPTY_VARS);
    }

    public static BlockablePanel sepPan(LayoutManager manager, Component... components) {
        BlockablePanel panel = new BlockablePanel(manager) { // from class: org.tlauncher.tlauncher.ui.center.CenterPanel.1
            private static final long serialVersionUID = 1;

            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
            public Insets getInsets() {
                return CenterPanel.noInsets;
            }
        };
        panel.add(components);
        return panel;
    }

    public static BlockablePanel sepPan(Component... components) {
        return sepPan(new GridLayout(0, 1), components);
    }

    public static UnblockablePanel uSepPan(LayoutManager manager, Component... components) {
        UnblockablePanel panel = new UnblockablePanel(manager) { // from class: org.tlauncher.tlauncher.ui.center.CenterPanel.2
            private static final long serialVersionUID = 1;

            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
            public Insets getInsets() {
                return CenterPanel.noInsets;
            }
        };
        panel.add(components);
        return panel;
    }

    public static UnblockablePanel uSepPan(Component... components) {
        return uSepPan(new GridLayout(0, 1), components);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void log(Object... o) {
        U.log("[" + getClass().getSimpleName() + "]", o);
    }
}
