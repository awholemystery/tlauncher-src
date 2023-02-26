package org.tlauncher.tlauncher.ui.accounts;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Locale;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountTip.class */
public class AccountTip extends CenterPanel implements LocalizableComponent {
    public static final int WIDTH = 510;
    public final Tip mojangTip;
    public final Tip tlauncherTip;
    public final Tip microsoftTip;
    public final Tip freeTip;
    private final AccountEditorScene scene;
    private Tip tip;
    private final EditorPane content;

    public AccountTip(AccountEditorScene sc) {
        super(smallSquareInsets);
        this.scene = sc;
        this.content = new EditorPane();
        this.content.addMouseListener(new MouseListener() { // from class: org.tlauncher.tlauncher.ui.accounts.AccountTip.1
            public void mouseClicked(MouseEvent e) {
                if (!AccountTip.this.isVisible()) {
                    e.consume();
                }
            }

            public void mousePressed(MouseEvent e) {
                if (!AccountTip.this.isVisible()) {
                    e.consume();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (!AccountTip.this.isVisible()) {
                    e.consume();
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (!AccountTip.this.isVisible()) {
                    e.consume();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!AccountTip.this.isVisible()) {
                    e.consume();
                }
            }
        });
        add((Component) this.content);
        this.freeTip = new Tip(Account.AccountType.FREE, ImageCache.getRes("free-user.png"));
        this.mojangTip = new Tip(Account.AccountType.MOJANG, ImageCache.getRes("mojang-user.png"));
        this.tlauncherTip = new Tip(Account.AccountType.TLAUNCHER, ImageCache.getRes("tlauncher-user.png"));
        this.microsoftTip = new Tip(Account.AccountType.MICROSOFT, ImageCache.getRes("microsoft-user.png"));
        setTip(null);
    }

    public void setAccountType(Account.AccountType type) {
        if (type != null) {
            switch (type) {
                case TLAUNCHER:
                    setTip(this.tlauncherTip);
                    return;
                case FREE:
                    setTip(this.freeTip);
                    return;
                case MOJANG:
                    setTip(this.mojangTip);
                    return;
                case MICROSOFT:
                    setTip(this.microsoftTip);
                    return;
            }
        }
        setTip(null);
    }

    public Tip getTip() {
        return this.tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
        if (tip == null) {
            setVisible(false);
            return;
        }
        setVisible(true);
        StringBuilder builder = new StringBuilder();
        builder.append("<table width=\"").append((WIDTH - getInsets().left) - getInsets().right).append("\" height=\"").append(tip.getHeight()).append("\"><tr><td align=\"center\" valign=\"center\">");
        if (tip.image != null) {
            builder.append("<img src=\"").append(tip.image).append("\" /></td><td align=\"center\" valign=\"center\" width=\"100%\">");
        }
        builder.append(Localizable.get(tip.path));
        builder.append("</td></tr></table>");
        setContent(builder.toString(), WIDTH, tip.getHeight());
    }

    void setContent(String text, int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException();
        }
        this.content.setText(text);
        if (OS.CURRENT == OS.LINUX) {
            width = (int) (width * 1.2d);
            height = (int) (height * 1.2d);
        }
        setSize(width, height + getInsets().top + getInsets().bottom);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setTip(this.tip);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountTip$Tip.class */
    public class Tip {
        private final Account.AccountType type;
        private final String path;
        private final URL image;

        Tip(Account.AccountType type, URL image) {
            this.type = type;
            this.path = "auth.tip." + type.toString().toLowerCase(Locale.ROOT);
            this.image = image;
        }

        public int getHeight() {
            return AccountTip.this.tlauncher.getLang().getInteger(this.path + ".height");
        }
    }
}
