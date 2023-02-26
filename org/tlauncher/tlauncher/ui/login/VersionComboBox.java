package org.tlauncher.tlauncher.ui.login;

import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/VersionComboBox.class */
public class VersionComboBox extends ExtendedComboBox<VersionSyncInfo> implements Blockable, VersionManagerListener, LocalizableComponent, LoginProcessListener, GameEntityListener {
    private static final long serialVersionUID = -9122074452728842733L;
    private static final VersionSyncInfo LOADING = VersionCellRenderer.LOADING;
    private static final VersionSyncInfo EMPTY = VersionCellRenderer.EMPTY;
    private final VersionManager manager;
    private final LoginForm loginForm;
    private final SimpleComboBoxModel<VersionSyncInfo> model;
    private String selectedVersion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionComboBox(LoginForm lf) {
        super(new VersionCellRenderer() { // from class: org.tlauncher.tlauncher.ui.login.VersionComboBox.1
            @Override // org.tlauncher.tlauncher.ui.swing.VersionCellRenderer
            public boolean getShowTLauncherVersions() {
                return false;
            }
        });
        this.model = getSimpleModel();
        this.loginForm = lf;
        this.manager = TLauncher.getInstance().getVersionManager();
        this.manager.addListener(this);
        addItemListener(e -> {
            this.loginForm.play.updateState();
            VersionSyncInfo selected = getVersion();
            if (selected != null) {
                this.selectedVersion = selected.getID();
            }
        });
        this.selectedVersion = lf.global.get("login.version.game");
    }

    public VersionSyncInfo getVersion() {
        VersionSyncInfo selected = (VersionSyncInfo) getSelectedItem();
        if (selected == null || selected.equals(LOADING) || selected.equals(EMPTY)) {
            return null;
        }
        return selected;
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        VersionSyncInfo selected = getVersion();
        if (selected == null) {
            throw new LoginWaitException("Version list is empty, refreshing", () -> {
                this.manager.refresh();
                if (getVersion() == null) {
                    Alert.showLocError("versions.notfound");
                }
                throw new LoginException("Giving user a second chance to choose correct version...");
            });
        }
        TLauncher.getInstance().getConfiguration().setForcefully("login.version.game", selected.getID(), false);
        if (!selected.hasRemote() || !selected.isInstalled() || selected.isUpToDate()) {
            return;
        }
        if (!Alert.showLocQuestion("versions.found-update")) {
            try {
                CompleteVersion complete = this.manager.getLocalList().getCompleteVersion(selected.getLocal());
                complete.setUpdatedTime(selected.getLatestVersion().getUpdatedTime());
                this.manager.getLocalList().saveVersion(complete);
                return;
            } catch (IOException e) {
                Alert.showLocError("versions.found-update.error");
                return;
            }
        }
        this.loginForm.versionPanel.forceupdate.setSelected(true);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        updateList(this.manager.getVersions(), null);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager vm) {
        updateList(null, null);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager vm) {
        updateList(this.manager.getVersions(), null);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager vm) {
        updateList(this.manager.getVersions(), null);
    }

    void updateList(List<VersionSyncInfo> list, String select) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: INVOKE  
              (wrap: java.lang.Runnable : 0x0003: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.ui.login.VersionComboBox A[D('this' org.tlauncher.tlauncher.ui.login.VersionComboBox), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r6v0 'select' java.lang.String A[D('select' java.lang.String), DONT_INLINE])
              (r5v0 'list' java.util.List<net.minecraft.launcher.updater.VersionSyncInfo> A[D('list' java.util.List<net.minecraft.launcher.updater.VersionSyncInfo>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.login.VersionComboBox), (r1 I:java.lang.String), (r2 I:java.util.List) type: DIRECT call: org.tlauncher.tlauncher.ui.login.VersionComboBox.lambda$updateList$2(java.lang.String, java.util.List):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.login.VersionComboBox.updateList(java.util.List<net.minecraft.launcher.updater.VersionSyncInfo>, java.lang.String):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/VersionComboBox.class
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 2 out of bounds for length 2
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
            r0 = r4
            r1 = r6
            r2 = r5
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$updateList$2(r1, r2);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.login.VersionComboBox.updateList(java.util.List, java.lang.String):void");
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationStarted(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activation(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationError(GameEntityDTO e, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void processingStarted(GameEntityDTO e, VersionDTO version) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(GameEntityDTO e, GameType type) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(CompleteVersion e) {
        VersionSyncInfo versionSyncInfo = new VersionSyncInfo(e, null);
        this.model.insertElementAt(versionSyncInfo, 0);
        this.model.setSelectedItem(versionSyncInfo);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeEntity(GameEntityDTO e) {
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeCompleteVersion(CompleteVersion e) {
        for (int i = 0; i < this.model.getSize(); i++) {
            if (this.model.getElementAt(i).getLocal() != null && this.model.getElementAt(i).getLocal().getID().equals(e.getID())) {
                this.model.removeElementAt(i);
                return;
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void populateStatus(GameEntityDTO status, GameType type, boolean state) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void updateVersion(CompleteVersion old, CompleteVersion newVersion) {
        for (int i = 0; i < this.model.getSize(); i++) {
            VersionSyncInfo versionSyncInfo = this.model.getElementAt(i);
            if (versionSyncInfo.getLocal() != null && versionSyncInfo.getLocal().getID().equals(old.getID())) {
                versionSyncInfo.setLocal(newVersion);
                repaint();
                return;
            }
        }
    }
}
