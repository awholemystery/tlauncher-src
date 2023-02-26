package org.tlauncher.tlauncher.ui.accounts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.accounts.UsernameField;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
import org.tlauncher.tlauncher.ui.text.ExtendedPasswordField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountEditor.class */
public class AccountEditor extends CenterPanel implements AuthenticatorListener {
    private static final String passlock = "passlock";
    private final AccountEditorScene scene;
    public final UsernameField username;
    public final BlockablePasswordField password;
    public final ButtonGroup authGroup;
    public final UpdaterButton save;
    public final LinkedHashMap<Account.AccountType, AuthTypeRadio> radioMap;
    public final EditorCheckBox skinCheckBox;
    public final AuthTypeRadio mojangAuth;
    public final AuthTypeRadio tlauncherAuth;
    public final AuthTypeRadio microsoftAuth;
    public final AuthTypeRadio freeAuth;
    private final Configuration configuration;
    private final FlexibleEditorPanel flex;
    private final JProgressBar bar;
    public static final Dimension SIZE = new Dimension(244, 221);
    public static final Color FIELD_COLOR = new Color(149, 149, 149);

    public AccountEditor(final AccountEditorScene sc, FlexibleEditorPanel flex) {
        super(noInsets);
        this.bar = new JProgressBar();
        this.bar.setIndeterminate(true);
        this.bar.setVisible(false);
        this.configuration = TLauncher.getInstance().getConfiguration();
        this.scene = sc;
        this.flex = flex;
        this.username = new UsernameField(this, UsernameField.UsernameState.EMAIL);
        Border compoundBorder = new CompoundBorder(BorderFactory.createLineBorder(FIELD_COLOR, 1), new EmptyBorder(0, 9, 0, 0));
        this.username.setBorder(compoundBorder);
        this.username.setForeground(FIELD_COLOR);
        this.password = new BlockablePasswordField();
        this.password.setBorder(compoundBorder);
        this.password.setEnabled(false);
        this.password.setForeground(FIELD_COLOR);
        this.authGroup = new ButtonGroup();
        this.radioMap = new LinkedHashMap<>();
        this.mojangAuth = new AuthTypeRadio(Account.AccountType.MOJANG);
        this.mojangAuth.setFont(this.mojangAuth.getFont().deriveFont(1));
        this.tlauncherAuth = new AuthTypeRadio(Account.AccountType.TLAUNCHER);
        this.tlauncherAuth.setFont(this.tlauncherAuth.getFont().deriveFont(1));
        this.microsoftAuth = new AuthTypeRadio(Account.AccountType.MICROSOFT);
        this.microsoftAuth.setFont(this.microsoftAuth.getFont().deriveFont(1));
        this.freeAuth = new AuthTypeRadio(Account.AccountType.FREE);
        this.freeAuth.setFont(this.freeAuth.getFont().deriveFont(1));
        this.save = new UpdaterButton(UpdaterButton.GREEN_COLOR, "account.save");
        this.save.setFont(this.save.getFont().deriveFont(1, 16.0f));
        this.save.setForeground(Color.WHITE);
        this.skinCheckBox = new EditorCheckBox("skin.status.checkbox") { // from class: org.tlauncher.tlauncher.ui.accounts.AccountEditor.1
            @Override // org.tlauncher.tlauncher.ui.editor.EditorCheckBox, org.tlauncher.tlauncher.ui.block.Blockable
            public void block(Object reason) {
            }

            @Override // org.tlauncher.tlauncher.ui.editor.EditorCheckBox, org.tlauncher.tlauncher.ui.block.Blockable
            public void unblock(Object reason) {
            }
        };
        this.skinCheckBox.setState(this.configuration.getBoolean("skin.status.checkbox.state"));
        this.skinCheckBox.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.accounts.AccountEditor.2
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!AccountEditor.this.skinCheckBox.getState()) {
                        if (Alert.showLocQuestion("skin.status.title", "skin.status.message")) {
                            AccountEditor.this.configuration.set("skin.status.checkbox.state", false, true);
                            return;
                        } else {
                            AccountEditor.this.skinCheckBox.setState(true);
                            return;
                        }
                    }
                    Alert.showMessage(AccountEditor.this.lang.get("skin.checkbox.switch.on.title"), AccountEditor.this.lang.get("skin.checkbox.switch.on.message"));
                    AccountEditor.this.configuration.set("skin.status.checkbox.state", true, true);
                }
            }
        });
        setPreferredSize(SIZE);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        UsernameField usernameField = this.username;
        springLayout.putConstraint("North", usernameField, 0, "North", this);
        springLayout.putConstraint("South", usernameField, 20, "North", usernameField);
        springLayout.putConstraint("West", usernameField, 0, "West", this);
        springLayout.putConstraint("East", usernameField, SIZE.width, "West", this);
        add((Component) usernameField);
        AuthTypeRadio authTypeRadio = this.tlauncherAuth;
        springLayout.putConstraint("North", authTypeRadio, 12, "South", usernameField);
        springLayout.putConstraint("South", authTypeRadio, 15, "North", authTypeRadio);
        springLayout.putConstraint("West", authTypeRadio, -4, "West", usernameField);
        springLayout.putConstraint("East", authTypeRadio, 8, "East", usernameField);
        add((Component) authTypeRadio);
        AuthTypeRadio authTypeRadio2 = this.microsoftAuth;
        springLayout.putConstraint("North", authTypeRadio2, 12, "South", authTypeRadio);
        springLayout.putConstraint("South", authTypeRadio2, 15, "North", authTypeRadio2);
        springLayout.putConstraint("West", authTypeRadio2, -4, "West", usernameField);
        springLayout.putConstraint("East", authTypeRadio2, 0, "East", usernameField);
        add((Component) authTypeRadio2);
        AuthTypeRadio authTypeRadio3 = this.freeAuth;
        springLayout.putConstraint("North", authTypeRadio3, 12, "South", authTypeRadio2);
        springLayout.putConstraint("South", authTypeRadio3, 15, "North", authTypeRadio3);
        springLayout.putConstraint("West", authTypeRadio3, -4, "West", usernameField);
        springLayout.putConstraint("East", authTypeRadio3, 0, "East", usernameField);
        add((Component) authTypeRadio3);
        AuthTypeRadio authTypeRadio4 = this.mojangAuth;
        springLayout.putConstraint("North", authTypeRadio4, 12, "South", authTypeRadio3);
        springLayout.putConstraint("South", authTypeRadio4, 15, "North", authTypeRadio4);
        springLayout.putConstraint("West", authTypeRadio4, -4, "West", usernameField);
        springLayout.putConstraint("East", authTypeRadio4, 0, "East", usernameField);
        add((Component) authTypeRadio4);
        BlockablePasswordField blockablePasswordField = this.password;
        springLayout.putConstraint("North", blockablePasswordField, 12, "South", authTypeRadio4);
        springLayout.putConstraint("South", blockablePasswordField, 19, "North", blockablePasswordField);
        springLayout.putConstraint("West", blockablePasswordField, 0, "West", usernameField);
        springLayout.putConstraint("East", blockablePasswordField, 0, "East", usernameField);
        add((Component) blockablePasswordField);
        EditorCheckBox editorCheckBox = this.skinCheckBox;
        springLayout.putConstraint("North", editorCheckBox, 12, "South", blockablePasswordField);
        springLayout.putConstraint("South", editorCheckBox, 15, "North", editorCheckBox);
        springLayout.putConstraint("West", editorCheckBox, -4, "West", usernameField);
        springLayout.putConstraint("East", editorCheckBox, 0, "East", usernameField);
        add((Component) editorCheckBox);
        addPanel4(this.save, springLayout, usernameField, editorCheckBox);
        addPanel4(this.bar, springLayout, usernameField, editorCheckBox);
        this.save.addActionListener(e -> {
            defocus();
            Account acc = get();
            if (acc.getUsername() == null && !acc.getType().equals(Account.AccountType.MICROSOFT)) {
                Alert.showLocError("auth.error.email.account");
            } else if (acc.getAccessToken() == null && acc.getPassword() == null && !acc.getType().equals(Account.AccountType.FREE)) {
                Alert.showLocError("auth.error.nopass");
            } else {
                Authenticator.authenticate(acc, this);
            }
        });
        this.microsoftAuth.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.accounts.AccountEditor.3
            public void mouseClicked(MouseEvent e2) {
                if (SwingUtilities.isLeftMouseButton(e2) && AccountEditor.this.microsoftAuth.isEnabled()) {
                    sc.getMainPane().setScene(sc.getMainPane().microsoftAuthScene);
                }
            }
        });
    }

    private void addPanel4(JComponent component, SpringLayout springLayout, JComponent panel, JComponent panel_5) {
        springLayout.putConstraint("North", component, 9, "South", panel_5);
        springLayout.putConstraint("South", component, 26, "North", component);
        springLayout.putConstraint("West", component, 0, "West", panel);
        springLayout.putConstraint("East", component, 0, "East", panel);
        add((Component) component);
    }

    public void setSelectedAccountType(Account.AccountType type) {
        AuthTypeRadio selectable = this.radioMap.get(type);
        if (selectable != null) {
            selectable.setSelected(true);
        }
    }

    public void clear() {
        setSelectedAccountType(Account.AccountType.TLAUNCHER);
        this.username.setText(null);
        this.password.setText(null);
        this.scene.list.setTempAccountType(Account.AccountType.TLAUNCHER);
    }

    public Account get() {
        Account account = this.scene.list.getAccountFromSelected();
        account.setUsername(this.username.getValue());
        switch (account.getType()) {
            case TLAUNCHER:
            case MOJANG:
                if (this.password.hasPassword()) {
                    account.setPassword(new String(this.password.getPassword()));
                    break;
                }
                break;
        }
        return account;
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        super.block(reason);
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        super.unblock(reason);
    }

    public void fillFormBySelectedAccount(Account selectedValue) {
        this.username.setText(selectedValue.getDisplayName());
        this.authGroup.setSelected(this.radioMap.get(selectedValue.getType()).getModel(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountEditor$BlockablePasswordField.class */
    public class BlockablePasswordField extends ExtendedPasswordField implements Blockable {
        private BlockablePasswordField() {
        }

        @Override // org.tlauncher.tlauncher.ui.block.Blockable
        public void block(Object reason) {
            setEnabled(false);
        }

        @Override // org.tlauncher.tlauncher.ui.block.Blockable
        public void unblock(Object reason) {
            setEnabled(true);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountEditor$AuthTypeRadio.class */
    public class AuthTypeRadio extends LocalizableRadioButton {
        private final Account.AccountType type;

        private AuthTypeRadio(final Account.AccountType type) {
            super("account.auth." + type.toString().toLowerCase(Locale.ROOT));
            AccountEditor.this.radioMap.put(type, this);
            AccountEditor.this.authGroup.add(this);
            this.type = type;
            setFocusable(false);
            addItemListener(new CheckBoxListener() { // from class: org.tlauncher.tlauncher.ui.accounts.AccountEditor.AuthTypeRadio.1
                @Override // org.tlauncher.tlauncher.ui.swing.CheckBoxListener
                public void itemStateChanged(boolean newstate) {
                    if (newstate) {
                        AccountEditor.this.scene.list.setTempAccountType(type);
                    }
                    if (newstate && !AccountEditor.this.password.hasPassword()) {
                        AccountEditor.this.password.setText(null);
                    }
                    if (newstate) {
                        AccountEditor.this.scene.tip.setAccountType(type);
                        AccountEditor.this.flex.setText(Localizable.get("auth.tip." + type.name().toLowerCase(Locale.ROOT)));
                    }
                    if (Account.AccountType.FREE == type && newstate) {
                        Blocker.setBlocked(AccountEditor.this.password, AccountEditor.passlock, true);
                    } else {
                        Blocker.setBlocked(AccountEditor.this.password, AccountEditor.passlock, false);
                    }
                    AccountEditor.this.username.setState(newstate ? UsernameField.UsernameState.USERNAME : UsernameField.UsernameState.EMAIL_LOGIN);
                    if (AccountEditor.this.mojangAuth.isSelected()) {
                        AccountEditor.this.username.setState(UsernameField.UsernameState.EMAIL);
                    }
                    AccountEditor.this.defocus();
                }
            });
        }

        public Account.AccountType getAccountType() {
            return this.type;
        }
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassing(Authenticator auth) {
        this.save.setVisible(false);
        this.bar.setVisible(true);
        this.bar.setValue(0);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassingError(Authenticator auth, Exception e) {
        this.bar.setVisible(false);
        this.save.setVisible(true);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassed(Authenticator auth) {
        this.bar.setVisible(false);
        this.save.setVisible(true);
    }
}
