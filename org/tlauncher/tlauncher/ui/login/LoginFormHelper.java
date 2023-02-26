package org.tlauncher.tlauncher.ui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.LoginHelperTheme;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper.class */
public class LoginFormHelper extends ExtendedLayeredPane implements LocalizableComponent {
    private static final int EDGE = 100;
    private final DefaultScene defaultScene;
    private final LoginForm loginForm;
    private final LoginFormHelperTip[] tips;
    private volatile LoginFormHelperState state;
    private static final LoginHelperTheme loginHelperTheme = new LoginHelperTheme();
    private static final Insets loginHelperInsets = new Insets(6, 10, 6, 10);

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper$BORDER_POS.class */
    public enum BORDER_POS {
        UP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper$LoginFormHelperState.class */
    public enum LoginFormHelperState {
        NONE,
        SHOWN
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper$Position.class */
    public enum Position {
        UP,
        DOWN
    }

    public LoginFormHelper(DefaultScene scene) {
        LoginFormHelperTip[] loginFormHelperTipArr;
        this.defaultScene = scene;
        this.loginForm = scene.loginForm;
        this.tips = new LoginFormHelperTip[]{new LoginFormHelperTip("username", this.loginForm.accountPanel, Position.UP), new LoginFormHelperTip(PathAppUtil.VERSION_DIRECTORY, this.loginForm.versionPanel, Position.UP), new LoginFormHelperTip("play", this.loginForm.playPanel, Position.UP)};
        for (LoginFormHelperTip tip : this.tips) {
            tip.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.login.LoginFormHelper.1
                public void mouseClicked(MouseEvent e) {
                    LoginFormHelper.this.setState(LoginFormHelperState.NONE);
                }
            });
        }
        add((Component[]) this.tips);
        setState(LoginFormHelperState.NONE);
        MouseListener mouseListener = new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.login.LoginFormHelper.2
            public void mousePressed(MouseEvent e) {
                LoginFormHelper.this.setState(LoginFormHelperState.NONE);
            }
        };
        this.loginForm.play.addMouseListener(mouseListener);
        this.loginForm.buttons.refresh.addMouseListener(mouseListener);
        this.loginForm.buttons.folder.addMouseListener(mouseListener);
        this.loginForm.buttons.settings.addMouseListener(mouseListener);
    }

    public LoginFormHelperState getState() {
        return this.state;
    }

    public void setState(LoginFormHelperState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        this.state = state;
        updateTips();
        setVisible(state != LoginFormHelperState.NONE);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        ResizeableComponent[] components;
        setSize(this.loginForm.getWidth(), 100);
        setLocation(this.loginForm.getX(), this.loginForm.getY() - 100);
        for (ResizeableComponent resizeableComponent : getComponents()) {
            if (resizeableComponent instanceof ResizeableComponent) {
                resizeableComponent.onResize();
            }
        }
        updateTips();
    }

    protected void updateTips() {
        LoginFormHelperTip[] loginFormHelperTipArr;
        for (LoginFormHelperTip tip : this.tips) {
            tip.setVisible(false);
            if (this.state != LoginFormHelperState.NONE) {
                tip.label.setText("loginform.helper." + tip.name);
                tip.label.setVerticalAlignment(0);
                tip.label.setHorizontalAlignment(0);
                Dimension dim = tip.getPreferredSize();
                Point pP = SwingUtil.getRelativeLocation(this.loginForm, tip.component);
                int x = (pP.x - dim.width) + tip.component.getPreferredSize().width;
                int y = 100 - dim.height;
                tip.setBounds(x, y, dim.width, dim.height);
                tip.setVisible(true);
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        updateTips();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper$LoginFormHelperTip.class */
    public class LoginFormHelperTip extends CenterPanel {
        private static final int TRIANGGLE_WIDTH = 10;
        final String name;
        final JComponent component;
        final LocalizableLabel label;

        LoginFormHelperTip(String name, JComponent comp, Position pos) {
            super(LoginFormHelper.loginHelperTheme, noInsets);
            setLayout(new BorderLayout(0, 0));
            if (name == null) {
                throw new NullPointerException("Name is NULL!");
            }
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name is empty!");
            }
            if (comp == null) {
                throw new NullPointerException("Component is NULL!");
            }
            if (pos == null) {
                throw new NullPointerException("Position is NULL!");
            }
            this.name = name.toLowerCase();
            this.component = comp;
            ExtendedPanel p = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
            p.setInsets(LoginFormHelper.loginHelperInsets);
            p.setOpaque(true);
            p.setBackground(LoginFormHelper.loginHelperTheme.getBackground());
            this.label = new LocalizableLabel();
            this.label.setFont(new Font(this.label.getFont().getFamily(), 0, 12));
            this.label.setForeground(LoginFormHelper.loginHelperTheme.getForeground());
            p.add((Component) this.label, "Center");
            add((Component) p, "Center");
            add((Component) new TipTriangle(10, LoginFormHelper.loginHelperTheme.getBorder(), BORDER_POS.UP), "South");
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginFormHelper$TipTriangle.class */
    class TipTriangle extends ExtendedPanel {
        public static final int DEFAULT_WIDTH = 10;
        private int width;
        private int gap = 24;
        private final BORDER_POS pos;
        private Color triangleColor;

        public TipTriangle(int width, Color triangle, BORDER_POS pos) {
            this.width = width;
            this.pos = pos;
            this.triangleColor = triangle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2.setColor(this.triangleColor);
            Rectangle rec = getVisibleRect();
            int[] iArr = new int[0];
            int[] iArr2 = new int[0];
            int startX = rec.x + rec.width + (-this.gap);
            int startY = rec.y;
            int[] xT = {startX, startX - this.width, startX - (this.width * 2)};
            int[] yT = {startY, startY + this.width, startY};
            g2.fillPolygon(xT, yT, 3);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        }
    }
}
