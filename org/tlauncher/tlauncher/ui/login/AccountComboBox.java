package org.tlauncher.tlauncher.ui.login;

import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.entity.profile.ClientProfile;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.managers.ProfileManagerListener;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.swing.AccountCellRenderer;
import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/AccountComboBox.class */
public class AccountComboBox extends ExtendedComboBox<Account> implements Blockable, LoginProcessListener, ProfileManagerListener {
    private static final long serialVersionUID = 6618039863712810645L;
    private static final Account EMPTY = AccountCellRenderer.EMPTY;
    private static final Account MANAGE = AccountCellRenderer.MANAGE;
    private final ProfileManager profileManager;
    private final LoginForm loginForm;
    private final SimpleComboBoxModel<Account> model;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccountComboBox(LoginForm lf) {
        super(new AccountCellRenderer());
        this.loginForm = lf;
        this.model = getSimpleModel();
        this.profileManager = TLauncher.getInstance().getProfileManager();
        this.profileManager.addListener(this);
        addItemListener(e -> {
            Account selected = (Account) getSelectedItem();
            if (selected == null || selected.equals(EMPTY)) {
                return;
            }
            if (selected.equals(MANAGE)) {
                this.loginForm.pane.openAccountEditor();
            } else {
                this.profileManager.selectAccount(selected);
            }
        });
        addItemListener((ItemListener) TLauncher.getInjector().getInstance(ModpackManager.class));
    }

    public Account getAccount() {
        Account value = (Account) getSelectedItem();
        if (value == null || value.equals(EMPTY)) {
            return null;
        }
        return value;
    }

    public void setAccount(Account account) {
        if (account == null || account.equals(getAccount())) {
            return;
        }
        setSelectedItem(account);
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        Account account = getAccount();
        if (account == null || EMPTY == account || MANAGE == account) {
            this.loginForm.pane.openAccountEditor();
            Alert.showLocError("account.empty.error");
            throw new LoginException("Account list is empty!");
        }
        U.log("AccountComboBox.validte pre game launch username " + account.toString() + " " + account.getType());
    }

    public void refreshAccounts(ClientProfile cl) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.login.AccountComboBox A[D('this' org.tlauncher.tlauncher.ui.login.AccountComboBox), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'cl' org.tlauncher.tlauncher.entity.profile.ClientProfile A[D('cl' org.tlauncher.tlauncher.entity.profile.ClientProfile), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.login.AccountComboBox), (r1 I:org.tlauncher.tlauncher.entity.profile.ClientProfile) type: DIRECT call: org.tlauncher.tlauncher.ui.login.AccountComboBox.lambda$refreshAccounts$1(org.tlauncher.tlauncher.entity.profile.ClientProfile):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.login.AccountComboBox.refreshAccounts(org.tlauncher.tlauncher.entity.profile.ClientProfile):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/AccountComboBox.class
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
                r0.lambda$refreshAccounts$1(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.login.AccountComboBox.refreshAccounts(org.tlauncher.tlauncher.entity.profile.ClientProfile):void");
    }

    @Override // org.tlauncher.tlauncher.managers.ProfileManagerListener
    public void fireRefreshed(ClientProfile cp) {
        refreshAccounts(cp);
    }

    @Override // org.tlauncher.tlauncher.managers.ProfileManagerListener
    public void fireClientProfileChanged(ClientProfile cp) {
        fireRefreshed(cp);
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
