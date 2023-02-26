package org.tlauncher.tlauncher.ui.accounts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.io.IOException;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.tlauncher.tlauncher.entity.profile.ClientProfile;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.managers.ProfileManagerListener;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.accounts.AccountEditor;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
import org.tlauncher.tlauncher.ui.swing.AccountCellRenderer;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.scroll.AccountScrollBarUI;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountList.class */
public class AccountList extends CenterPanel implements ProfileManagerListener {
    public static final Dimension SIZE = new Dimension(211, 171);
    private static final long serialVersionUID = 3280495266368287215L;
    public final DefaultListModel<Account> model;
    public final JList<Account> list;
    public final UpdaterButton add;
    public final UpdaterButton remove;
    private final AccountEditorScene scene;
    private final ProfileManager profileManager;
    private final Account temp;

    public AccountList(AccountEditorScene sc) {
        super(noInsets);
        this.temp = new Account();
        setPreferredSize(SIZE);
        this.scene = sc;
        this.profileManager = TLauncher.getInstance().getProfileManager();
        JPanel panel = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        LocalizableLabel label = new LocalizableLabel("account.list");
        SwingUtil.changeFontFamily(label, FontTL.ROBOTO_BOLD);
        label.setFont(label.getFont().deriveFont(14.0f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 13, 0));
        label.setHorizontalAlignment(2);
        label.setVerticalAlignment(1);
        this.model = new DefaultListModel<>();
        this.list = new JList<>(this.model);
        this.list.setCellRenderer(new AccountCellRenderer(AccountCellRenderer.AccountCellType.EDITOR));
        this.list.setSelectionMode(0);
        this.list.setBackground(AccountEditorScene.BACKGROUND_ACCOUNT_COLOR);
        this.list.addListSelectionListener(e -> {
            if (Objects.nonNull(this.list.getSelectedValue())) {
                this.scene.editor.fillFormBySelectedAccount((Account) this.list.getSelectedValue());
            } else {
                this.scene.editor.clear();
            }
            blockOrUnblock();
        });
        JScrollPane scroll = new JScrollPane(this.list);
        scroll.getVerticalScrollBar().setUI(new AccountScrollBarUI());
        scroll.setBorder((Border) null);
        scroll.setHorizontalScrollBarPolicy(31);
        scroll.setVerticalScrollBarPolicy(20);
        JPanel buttons = new ExtendedPanel();
        buttons.setLayout(new FlowLayout(0, 0, 0));
        buttons.setPreferredSize(new Dimension(SIZE.width, 26));
        this.add = new UpdaterButton(ExtendedButton.ORANGE_COLOR, "account.list.add");
        this.add.setFont(this.add.getFont().deriveFont(1, 16.0f));
        this.add.setForeground(Color.WHITE);
        this.add.setPreferredSize(new Dimension(100, 26));
        this.add.addActionListener(e2 -> {
            for (int i = 0; i < this.model.getSize(); i++) {
                if (((Account) this.model.getElementAt(i)).getUsername() == null) {
                    return;
                }
            }
            this.model.addElement(this.temp);
            this.list.setSelectedValue(this.temp, true);
            defocus();
        });
        this.remove = new UpdaterButton(ImageUdaterButton.ORANGE_COLOR, "account.list.remove");
        this.remove.setPreferredSize(new Dimension(100, 26));
        this.remove.setFont(this.remove.getFont().deriveFont(1, 16.0f));
        this.remove.setForeground(Color.WHITE);
        this.remove.addActionListener(e3 -> {
            Account ac = (Account) this.list.getSelectedValue();
            if (Objects.nonNull(ac)) {
                this.model.removeElement(ac);
                try {
                    this.profileManager.remove(ac);
                } catch (IOException exception) {
                    U.log(exception);
                }
                defocus();
            }
        });
        buttons.add(this.add);
        buttons.add(Box.createHorizontalStrut(11));
        buttons.add(this.remove);
        panel.add("South", buttons);
        panel.add("Center", scroll);
        panel.add("North", label);
        add((Component) panel);
    }

    public void addTempToList() {
        this.model.addElement(this.temp);
        this.list.setSelectedValue(this.temp, true);
    }

    @Override // org.tlauncher.tlauncher.managers.ProfileManagerListener
    public void fireRefreshed(ClientProfile cl) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.accounts.AccountList A[D('this' org.tlauncher.tlauncher.ui.accounts.AccountList), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'cl' org.tlauncher.tlauncher.entity.profile.ClientProfile A[D('cl' org.tlauncher.tlauncher.entity.profile.ClientProfile), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.accounts.AccountList), (r1 I:org.tlauncher.tlauncher.entity.profile.ClientProfile) type: DIRECT call: org.tlauncher.tlauncher.ui.accounts.AccountList.lambda$fireRefreshed$3(org.tlauncher.tlauncher.entity.profile.ClientProfile):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.accounts.AccountList.fireRefreshed(org.tlauncher.tlauncher.entity.profile.ClientProfile):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountList.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
            Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
            	at java.base/java.util.Objects.checkIndex(Unknown Source)
            	at java.base/java.util.ArrayList.get(Unknown Source)
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$fireRefreshed$3(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.accounts.AccountList.fireRefreshed(org.tlauncher.tlauncher.entity.profile.ClientProfile):void");
    }

    @Override // org.tlauncher.tlauncher.managers.ProfileManagerListener
    public void fireClientProfileChanged(ClientProfile cl) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.accounts.AccountList A[D('this' org.tlauncher.tlauncher.ui.accounts.AccountList), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'cl' org.tlauncher.tlauncher.entity.profile.ClientProfile A[D('cl' org.tlauncher.tlauncher.entity.profile.ClientProfile), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.accounts.AccountList), (r1 I:org.tlauncher.tlauncher.entity.profile.ClientProfile) type: DIRECT call: org.tlauncher.tlauncher.ui.accounts.AccountList.lambda$fireClientProfileChanged$4(org.tlauncher.tlauncher.entity.profile.ClientProfile):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.accounts.AccountList.fireClientProfileChanged(org.tlauncher.tlauncher.entity.profile.ClientProfile):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/AccountList.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
            Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
            	at java.base/java.util.Objects.checkIndex(Unknown Source)
            	at java.base/java.util.ArrayList.get(Unknown Source)
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$fireClientProfileChanged$4(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.accounts.AccountList.fireClientProfileChanged(org.tlauncher.tlauncher.entity.profile.ClientProfile):void");
    }

    private void blockOrUnblock() {
        if (Objects.isNull(this.list.getSelectedValue())) {
            Blocker.block(LoginForm.AUTH_BLOCK, this.scene.editor);
        } else if (((Account) this.list.getSelectedValue()).equals(this.temp)) {
            Blocker.unblock(LoginForm.AUTH_BLOCK, this.scene.editor);
        } else {
            Blocker.unblock(LoginForm.AUTH_BLOCK, this.scene.editor);
            for (AccountEditor.AuthTypeRadio o : this.scene.editor.radioMap.values()) {
                o.setEnabled(false);
            }
            if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(((Account) this.list.getSelectedValue()).getType())) {
                Blocker.block(LoginForm.AUTH_BLOCK, this.scene.editor);
            }
        }
    }

    public void setTempAccountType(Account.AccountType accountType) {
        this.temp.setType(accountType);
        this.list.revalidate();
        this.list.repaint();
    }

    public Account getAccountFromSelected() {
        if (this.temp.equals(this.list.getSelectedValue())) {
            Account ac = new Account();
            ac.setType(this.temp.getType());
            return ac;
        }
        return (Account) this.list.getSelectedValue();
    }
}
