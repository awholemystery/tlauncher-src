package org.tlauncher.tlauncher.managers.popup.menu;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingReply;
import ch.qos.logback.core.CoreConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.launcher.updater.VersionFilter;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.ReleaseType;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.entity.ServerInfo;
import org.tlauncher.tlauncher.entity.hot.AdditionalHotServer;
import org.tlauncher.tlauncher.entity.hot.AdditionalHotServers;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.entity.server.Server;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerAdapter;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.login.VersionComboBox;
import org.tlauncher.tlauncher.ui.menu.PopupMenuModel;
import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.statistics.StatisticsUtil;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/popup/menu/HotServerManager.class */
public class HotServerManager extends VersionManagerAdapter implements MinecraftListener {
    private PopupMenuModel current;
    private AdditionalHotServers additionalHotServers;
    private List<ServerInfo> hotServers;
    @Inject
    private InnerMinecraftServer innerMinecraftServer;
    @Inject
    private TLauncher tLauncher;
    private final Map<String, PopupMenuModel> hashMap = Collections.synchronizedMap(new HashMap());
    private boolean serviceAvailable = true;
    private final VersionFilter filter = new VersionFilter().exclude(ReleaseType.SNAPSHOT);

    public void processingEvent(String serverId) {
        if (this.serviceAvailable) {
            this.current = this.hashMap.get(serverId);
            if (this.current != null) {
                PopupMenuView view = this.current.isMainPage() ? TLauncher.getInstance().getFrame().mp.defaultScene.getPopupMenuView() : TLauncher.getInstance().getFrame().mp.additionalHostServerScene.getPopupMenuView();
                view.showSelectedModel(this.current);
                return;
            }
            U.log("server id hasn't found = " + serverId);
        }
    }

    public void launchGame(VersionSyncInfo name) {
        changeVersion(name);
        block();
        TLauncher.getInstance().getFrame().mp.openDefaultScene();
        RemoteServer s = new RemoteServer();
        s.setOfficialAccount(this.current.getInfo().isMojangAccount());
        if (!(this.current.getInfo() instanceof AdditionalHotServer)) {
            try {
                getMinecraftPingReplyAndResolvedAddress(this.current.getInfo());
            } catch (Throwable e) {
                U.log(e);
            }
        }
        s.setAddress(this.current.getResolvedAddress());
        s.setName(this.current.getName());
        sendStat();
        addServerToList(false, name);
        TLauncher.getInstance().getFrame().mp.defaultScene.loginForm.startLauncher(s);
    }

    private void sendStat() {
        String type;
        if (this.current.isMainPage()) {
            type = "main/page";
        } else {
            type = "additional";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(ClientCookie.VERSION_ATTR, this.current.getServerId());
        StatisticsUtil.startSending("save/hot/server/" + type, null, map);
    }

    public void copyAddress() {
        StringSelection selection = new StringSelection(this.current.getAddress());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        Alert.showMessage(CoreConstants.EMPTY_STRING, Localizable.get().get("menu.copy.done"));
    }

    private void changeVersion(VersionSyncInfo version) {
        this.current.setSelected(version);
        this.tLauncher.getFrame().mp.defaultScene.loginForm.versions.setSelectedValue((VersionComboBox) version);
    }

    public void addServerToList(boolean showMessage, VersionSyncInfo v) {
        this.innerMinecraftServer.initInnerServers();
        Server server = new Server();
        server.setAddress(this.current.getAddress());
        server.setName(server.getIp());
        try {
            if (v.isInstalled() && ((CompleteVersion) v.getLocal()).isModpack()) {
                this.innerMinecraftServer.addPageServerToModpack(server, v.getLocal());
            } else {
                this.innerMinecraftServer.addPageServer(server);
            }
        } catch (Throwable e) {
            U.log(e);
        }
        if (showMessage) {
            Alert.showMessage(CoreConstants.EMPTY_STRING, Localizable.get().get("menu.favorite.done"));
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
        block();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
        enablePopup();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        block();
        try {
            if (!this.tLauncher.getLauncher().getVersion().isModpack()) {
                this.innerMinecraftServer.initInnerServers();
                this.innerMinecraftServer.searchRemovedServers();
                this.innerMinecraftServer.prepareInnerServer();
            }
        } catch (Throwable e) {
            U.log(e);
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
        enablePopup();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
        enablePopup();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
        enablePopup();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
        enablePopup();
    }

    public void enablePopup() {
        this.serviceAvailable = true;
    }

    private void block() {
        this.serviceAvailable = false;
    }

    private void addServers(List<? extends ServerInfo> list, boolean mainPage, List<VersionSyncInfo> versions) {
        for (ServerInfo info : list) {
            if (this.hashMap.get(info.getServerId()) != null) {
                U.log("!!!the same id was found: " + info.getServerId());
            } else {
                PopupMenuModel p = new PopupMenuModel(findAvailableVersions(info, versions), info, mainPage);
                this.hashMap.put(info.getServerId(), p);
            }
        }
    }

    private List<VersionSyncInfo> findAvailableVersions(ServerInfo serverInfo, List<VersionSyncInfo> versionList) {
        List<VersionSyncInfo> list = new ArrayList<>();
        for (VersionSyncInfo v : versionList) {
            if (this.filter.satisfies(v.getAvailableVersion()) && !serverInfo.getIgnoreVersions().contains(v.getID())) {
                list.add(v);
                if (Objects.equals(serverInfo.getMinVersion(), v.getID())) {
                    break;
                }
            }
        }
        Stream<VersionSyncInfo> filter = versionList.stream().filter(v2 -> {
            return this.filter.satisfies(v2.getAvailableVersion());
        }).filter(v3 -> {
            return serverInfo.getIncludeVersions().contains(v3.getID());
        });
        list.getClass();
        filter.forEach((v1) -> {
            r1.add(v1);
        });
        return list;
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerAdapter, org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        List<VersionSyncInfo> list = manager.getVersions();
        this.hashMap.clear();
        AsyncThread.execute(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0015: INVOKE  
              (wrap: java.lang.Runnable : 0x0010: INVOKE_CUSTOM (r0v5 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.managers.popup.menu.HotServerManager A[D('this' org.tlauncher.tlauncher.managers.popup.menu.HotServerManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r0v1 'list' java.util.List<net.minecraft.launcher.updater.VersionSyncInfo> A[D('list' java.util.List<net.minecraft.launcher.updater.VersionSyncInfo>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.managers.popup.menu.HotServerManager), (r1 I:java.util.List) type: DIRECT call: org.tlauncher.tlauncher.managers.popup.menu.HotServerManager.lambda$onVersionsRefreshed$2(java.util.List):void)
             type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.popup.menu.HotServerManager.onVersionsRefreshed(org.tlauncher.tlauncher.managers.VersionManager):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/popup/menu/HotServerManager.class
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
            r0 = r4
            java.util.List r0 = r0.getVersions()
            r5 = r0
            r0 = r3
            java.util.Map<java.lang.String, org.tlauncher.tlauncher.ui.menu.PopupMenuModel> r0 = r0.hashMap
            r0.clear()
            r0 = r3
            r1 = r5
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$onVersionsRefreshed$2(r1);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            r0 = r3
            r1 = r5
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$onVersionsRefreshed$3(r1);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.popup.menu.HotServerManager.onVersionsRefreshed(org.tlauncher.tlauncher.managers.VersionManager):void");
    }

    public AdditionalHotServers getAdditionalHotServers() {
        return this.additionalHotServers;
    }

    public boolean isReady() {
        return Objects.nonNull(this.additionalHotServers);
    }

    public void fillServer(AdditionalHotServer s) {
        try {
            MinecraftPingReply data = getMinecraftPingReplyAndResolvedAddress(s);
            s.setOnline(Integer.valueOf(data.getPlayers().getOnline()));
            s.setMax(Integer.valueOf(data.getPlayers().getMax()));
            s.setUpdated(DateUtils.addMinutes(new Date(), 15));
            s.setImage(data.getFavicon());
        } catch (Throwable t) {
            U.log(t);
        }
    }

    private MinecraftPingReply getMinecraftPingReplyAndResolvedAddress(ServerInfo s) throws IOException {
        MinecraftPingReply data;
        String[] serverConfig = s.getAddress().split(":");
        MinecraftPing p = new MinecraftPing();
        MinecraftPingOptions options = new MinecraftPingOptions().setHostname(serverConfig[0]).setPort(Integer.parseInt(serverConfig[1]));
        try {
            data = p.getPing(options);
        } catch (Throwable th) {
            p.resolveDNS(options);
            s.setRedirectAddress(String.format("%s:%s", options.getHostname(), Integer.valueOf(options.getPort())));
            data = p.getPing(options);
        }
        return data;
    }
}
