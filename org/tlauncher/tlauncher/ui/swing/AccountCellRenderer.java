package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/AccountCellRenderer.class */
public class AccountCellRenderer implements ListCellRenderer<Account> {
    public static final Account EMPTY = Account.randomAccount();
    public static final Account MANAGE = Account.randomAccount();
    public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
    public static final Color OVER_ITEM = new Color(235, 235, 235);
    private static final Icon MANAGE_ICON = ImageCache.getIcon("gear.png");
    private static final Icon MOJANG_USER_ICON = ImageCache.getIcon("mojang-user.png");
    private static final Icon TLAUNCHER_USER_ICON = ImageCache.getIcon("tlauncher-user.png");
    private static final Icon MICROSOFT_USER_ICON = ImageCache.getIcon("microsoft-user.png");
    private static final Icon FREE_USER_ICON = ImageCache.getIcon("free-user.png");
    private static final Color FOREGROUND_EDITOR = new Color(74, 74, 73);
    private final DefaultListCellRenderer defaultRenderer;
    private AccountCellType type;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/AccountCellRenderer$AccountCellType.class */
    public enum AccountCellType {
        PREVIEW,
        EDITOR
    }

    public /* bridge */ /* synthetic */ Component getListCellRendererComponent(JList jList, Object obj, int i, boolean z, boolean z2) {
        return getListCellRendererComponent((JList<? extends Account>) jList, (Account) obj, i, z, z2);
    }

    public AccountCellRenderer(AccountCellType type) {
        if (type == null) {
            throw new NullPointerException("CellType cannot be NULL!");
        }
        this.defaultRenderer = new DefaultListCellRenderer();
        this.type = type;
    }

    public AccountCellRenderer() {
        this(AccountCellType.PREVIEW);
    }

    public AccountCellType getType() {
        return this.type;
    }

    public void setType(AccountCellType type) {
        if (type == null) {
            throw new NullPointerException("CellType cannot be NULL!");
        }
        this.type = type;
    }

    public Component getListCellRendererComponent(JList<? extends Account> list, Account value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            renderer.setBackground(OVER_ITEM);
        } else {
            renderer.setBackground(Color.white);
        }
        renderer.setForeground(DARK_COLOR_TEXT);
        renderer.setAlignmentY(0.5f);
        renderer.setIconTextGap(7);
        if (value == null || value.equals(EMPTY)) {
            renderer.setText(Localizable.get("account.empty"));
        } else if (value.equals(MANAGE)) {
            renderer.setText(Localizable.get("account.manage"));
            renderer.setIcon(MANAGE_ICON);
        } else {
            Icon icon = null;
            switch (value.getType()) {
                case TLAUNCHER:
                    icon = TLAUNCHER_USER_ICON;
                    break;
                case MOJANG:
                    icon = MOJANG_USER_ICON;
                    break;
                case MICROSOFT:
                    icon = MICROSOFT_USER_ICON;
                    break;
                case FREE:
                    icon = FREE_USER_ICON;
                    break;
            }
            if (icon != null) {
                renderer.setIcon(icon);
                renderer.setFont(renderer.getFont().deriveFont(1));
            }
            switch (this.type) {
                case EDITOR:
                    configEditLabel(renderer, isSelected);
                    if (!value.hasUsername()) {
                        renderer.setText(Localizable.get("account.creating"));
                        break;
                    } else {
                        renderer.setText(value.getDisplayName());
                        break;
                    }
                default:
                    configEditLabel(renderer, isSelected);
                    renderer.setText(value.getDisplayName());
                    break;
            }
        }
        renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
        renderer.setOpaque(true);
        return renderer;
    }

    private void configEditLabel(JLabel renderer, boolean isSelected) {
        renderer.setFont(renderer.getFont().deriveFont(0, 12.0f));
        renderer.setForeground(FOREGROUND_EDITOR);
        renderer.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        if (isSelected) {
            renderer.setBackground(new Color(235, 235, 235));
        }
    }
}
