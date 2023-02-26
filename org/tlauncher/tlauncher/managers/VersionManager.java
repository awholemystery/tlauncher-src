package org.tlauncher.tlauncher.managers;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.launcher.updater.LatestVersionSyncInfo;
import net.minecraft.launcher.updater.LocalVersionList;
import net.minecraft.launcher.updater.RemoteVersionList;
import net.minecraft.launcher.updater.VersionFilter;
import net.minecraft.launcher.updater.VersionList;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.ReleaseType;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.component.ComponentDependence;
import org.tlauncher.tlauncher.component.InterruptibleComponent;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.Time;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncObject;
import org.tlauncher.util.async.AsyncObjectContainer;
import org.tlauncher.util.async.AsyncObjectGotErrorException;
import org.tlauncher.util.async.AsyncThread;

@ComponentDependence({AssetsManager.class, VersionLists.class, TLauncherManager.class})
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionManager.class */
public class VersionManager extends InterruptibleComponent {
    private final LocalVersionList localList;
    private final RemoteVersionList[] remoteLists;
    private final List<VersionManagerListener> listeners;
    private final Object versionFlushLock;
    private Map<ReleaseType, Version> latestVersions;
    private boolean hadRemote;
    private boolean localRefresh;
    private final List<VersionSyncInfo> currentSyncVersions;

    public VersionManager(ComponentManager manager) throws Exception {
        super(manager);
        this.currentSyncVersions = new ArrayList();
        VersionLists list = (VersionLists) manager.getComponent(VersionLists.class);
        this.localList = list.getLocal();
        this.remoteLists = list.getRemoteLists();
        this.latestVersions = new LinkedHashMap();
        this.listeners = Collections.synchronizedList(new ArrayList());
        this.versionFlushLock = new Object();
    }

    public void addListener(VersionManagerListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.listeners.add(listener);
    }

    public LocalVersionList getLocalList() {
        LocalVersionList localVersionList;
        synchronized (this.versionFlushLock) {
            localVersionList = this.localList;
        }
        return localVersionList;
    }

    public Map<ReleaseType, Version> getLatestVersions() {
        Map<ReleaseType, Version> unmodifiableMap;
        synchronized (this.versionFlushLock) {
            unmodifiableMap = Collections.unmodifiableMap(this.latestVersions);
        }
        return unmodifiableMap;
    }

    boolean refresh(int refreshID, boolean local) {
        this.refreshList[refreshID] = true;
        this.localRefresh = local;
        boolean local2 = local | (!this.manager.getLauncher().getConfiguration().getBoolean("minecraft.versions.sub.remote"));
        this.hadRemote |= !local2;
        if (local2) {
            log("Refreshing versions locally...");
        } else {
            log("Refreshing versions remotely...");
            this.latestVersions.clear();
            synchronized (this.listeners) {
                for (VersionManagerListener listener : this.listeners) {
                    listener.onVersionsRefreshing(this);
                }
            }
        }
        Object lock = new Object();
        Time.start(lock);
        Map<AsyncObject<VersionList.RawVersionList>, VersionList.RawVersionList> result = null;
        Throwable e = null;
        try {
            result = refreshVersions(local2);
        } catch (Throwable e0) {
            e = e0;
        }
        if (isCancelled(refreshID)) {
            log("Version refresh has been cancelled (" + Time.stop(lock) + " ms)");
            return false;
        } else if (e != null) {
            synchronized (this.listeners) {
                for (VersionManagerListener listener2 : this.listeners) {
                    listener2.onVersionsRefreshingFailed(this);
                }
            }
            log("Cannot refresh versions (" + Time.stop(lock) + " ms)", e);
            return true;
        } else {
            if (result != null) {
                synchronized (this.versionFlushLock) {
                    for (AsyncObject<VersionList.RawVersionList> object : result.keySet()) {
                        VersionList.RawVersionList rawList = result.get(object);
                        if (rawList != null) {
                            AsyncRawVersionListObject listObject = (AsyncRawVersionListObject) object;
                            RemoteVersionList versionList = listObject.getVersionList();
                            versionList.refreshVersions(rawList);
                            this.latestVersions.putAll(versionList.getLatestVersions());
                        }
                    }
                }
            }
            this.latestVersions = U.sortMap(this.latestVersions, ReleaseType.values());
            List<VersionSyncInfo> l1 = getVersions0();
            this.currentSyncVersions.clear();
            this.currentSyncVersions.addAll(l1);
            log("Versions has been refreshed (" + Time.stop(lock) + " ms)");
            this.refreshList[refreshID] = false;
            synchronized (this.listeners) {
                for (VersionManagerListener listener3 : this.listeners) {
                    listener3.onVersionsRefreshed(this);
                }
            }
            return true;
        }
    }

    @Override // org.tlauncher.tlauncher.component.InterruptibleComponent
    protected boolean refresh(int queueID) {
        return refresh(queueID, false);
    }

    public void startRefresh(boolean local) {
        refresh(nextID(), local);
    }

    @Override // org.tlauncher.tlauncher.component.InterruptibleComponent
    public synchronized void stopRefresh() {
        super.stopRefresh();
        startRefresh(true);
    }

    public void asyncRefresh(boolean local) {
        AsyncThread.execute(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.managers.VersionManager A[D('this' org.tlauncher.tlauncher.managers.VersionManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'local' boolean A[D('local' boolean), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.managers.VersionManager), (r1 I:boolean) type: DIRECT call: org.tlauncher.tlauncher.managers.VersionManager.lambda$asyncRefresh$0(boolean):void)
             type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.VersionManager.asyncRefresh(boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionManager.class
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
                r0.lambda$asyncRefresh$0(r1);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.VersionManager.asyncRefresh(boolean):void");
    }

    @Override // org.tlauncher.tlauncher.component.RefreshableComponent
    public void asyncRefresh() {
        asyncRefresh(false);
    }

    private Map<AsyncObject<VersionList.RawVersionList>, VersionList.RawVersionList> refreshVersions(boolean local) {
        RemoteVersionList[] remoteVersionListArr;
        this.localList.refreshVersions();
        if (local) {
            return null;
        }
        AsyncObjectContainer<VersionList.RawVersionList> container = new AsyncObjectContainer<>();
        for (RemoteVersionList remoteList : this.remoteLists) {
            container.add(new AsyncRawVersionListObject(remoteList));
        }
        return container.execute();
    }

    public void updateVersionList() {
        if (!this.hadRemote) {
            asyncRefresh();
            return;
        }
        for (VersionManagerListener listener : this.listeners) {
            listener.onVersionsRefreshed(this);
        }
    }

    public VersionSyncInfo getVersionSyncInfo(Version version) {
        return getVersionSyncInfo(version.getID());
    }

    public VersionSyncInfo getVersionSyncInfo(String name) {
        if (name == null) {
            throw new NullPointerException("Cannot get sync info of NULL!");
        }
        if (name.startsWith("latest-")) {
            String realID = name.substring(7);
            name = null;
            Iterator<Map.Entry<ReleaseType, Version>> it = this.latestVersions.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<ReleaseType, Version> entry = it.next();
                if (entry.getKey().toString().equals(realID)) {
                    name = entry.getValue().getID();
                    break;
                }
            }
            if (name == null) {
                return null;
            }
        }
        Version localVersion = this.localList.getVersion(name);
        if ((localVersion instanceof CompleteVersion) && ((CompleteVersion) localVersion).getInheritsFrom() != null) {
            try {
                localVersion = ((CompleteVersion) localVersion).resolve(this, false);
            } catch (IOException ioE) {
                U.log(ioE);
                localVersion = null;
            }
        }
        Version remoteVersion = null;
        RemoteVersionList[] remoteVersionListArr = this.remoteLists;
        int length = remoteVersionListArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            RemoteVersionList list = remoteVersionListArr[i];
            Version currentVersion = list.getVersion(name);
            if (currentVersion == null) {
                i++;
            } else {
                remoteVersion = currentVersion;
                break;
            }
        }
        if (localVersion == null && remoteVersion == null) {
            return null;
        }
        return new VersionSyncInfo(localVersion, remoteVersion);
    }

    public LatestVersionSyncInfo getLatestVersionSyncInfo(Version version) {
        if (version == null) {
            throw new NullPointerException("Cannot get latest sync info of NULL!");
        }
        VersionSyncInfo syncInfo = getVersionSyncInfo(version);
        return new LatestVersionSyncInfo(version.getReleaseType(), syncInfo);
    }

    public List<VersionSyncInfo> getVersions(VersionFilter filter, boolean includeLatest) {
        return (List) this.currentSyncVersions.stream().filter(v -> {
            if (!includeLatest && (v instanceof LatestVersionSyncInfo)) {
                return false;
            }
            return true;
        }).filter(v2 -> {
            if (Objects.isNull(filter)) {
                return true;
            }
            return filter.satisfies(v2.getAvailableVersion());
        }).collect(Collectors.toList());
    }

    public List<VersionSyncInfo> getVersions(boolean includeLatest) {
        return getVersions(TLauncher.getInstance() == null ? null : TLauncher.getInstance().getConfiguration().getVersionFilter(), includeLatest);
    }

    public synchronized List<VersionSyncInfo> getVersions() {
        return getVersions(true);
    }

    private synchronized List<VersionSyncInfo> getVersions0() {
        RemoteVersionList[] remoteVersionListArr;
        ArrayList arrayList = new ArrayList();
        List<VersionSyncInfo> result = new ArrayList<>();
        Map<String, VersionSyncInfo> lookup = new HashMap<>();
        for (Version version : this.latestVersions.values()) {
            LatestVersionSyncInfo syncInfo = getLatestVersionSyncInfo(version);
            if (!result.contains(syncInfo)) {
                result.add(syncInfo);
            }
        }
        Iterator it = Lists.newArrayList(this.localList.getVersions()).iterator();
        while (it.hasNext()) {
            Version v = (Version) it.next();
            VersionSyncInfo syncInfo2 = getVersionSyncInfo(v);
            if (syncInfo2 != null) {
                lookup.put(v.getID(), syncInfo2);
                arrayList.add(syncInfo2);
            }
        }
        for (RemoteVersionList remoteList : this.remoteLists) {
            if (Objects.isNull(remoteList)) {
                U.log("remote list is null");
            }
            remoteList.getVersions().stream().filter(version2 -> {
                return !lookup.containsKey(version2.getID());
            }).forEach(version3 -> {
                VersionSyncInfo syncInfo3 = getVersionSyncInfo(arrayList);
                lookup.put(arrayList.getID(), syncInfo3);
                lookup.add(syncInfo3);
            });
        }
        arrayList.sort(a, b -> {
            Date aDate = a.getLatestVersion().getReleaseTime();
            Date bDate = b.getLatestVersion().getReleaseTime();
            if (aDate == null || bDate == null) {
                return 1;
            }
            return bDate.compareTo(aDate);
        });
        result.addAll(arrayList);
        return result;
    }

    public List<VersionSyncInfo> getInstalledVersions(VersionFilter filter) {
        List<VersionSyncInfo> result = new ArrayList<>();
        for (Version version : this.localList.getVersions()) {
            result.add(getVersionSyncInfo(version));
        }
        return result;
    }

    public List<VersionSyncInfo> getInstalledVersions() {
        return getInstalledVersions(TLauncher.getInstance() == null ? null : TLauncher.getInstance().getConfiguration().getVersionFilter());
    }

    public VersionSyncInfoContainer downloadVersion(VersionSyncInfo syncInfo, boolean tlauncher, boolean force) throws IOException {
        String jarFile;
        String saveFile;
        VersionSyncInfoContainer container = new VersionSyncInfoContainer(syncInfo);
        CompleteVersion completeVersion = syncInfo.getCompleteVersion(force);
        File baseDirectory = this.localList.getBaseDirectory();
        Set<Downloadable> required = syncInfo.getRequiredDownloadables(baseDirectory, force, tlauncher);
        container.addAll(required);
        log("Required for version " + syncInfo.getID() + ':', required);
        String originalId = completeVersion.getJar();
        Repo repo = ClientInstanceRepo.OFFICIAL_VERSION_REPO;
        String id = completeVersion.getID();
        if (originalId == null) {
            if (Objects.nonNull(syncInfo.getRemote())) {
                repo = syncInfo.getRemote().getSource();
            }
            jarFile = "versions/" + id + "/" + id + ".jar";
            saveFile = jarFile;
        } else {
            repo = ClientInstanceRepo.OFFICIAL_VERSION_REPO;
            jarFile = "versions/" + originalId + "/" + originalId + ".jar";
            saveFile = "versions/" + id + "/" + id + ".jar";
        }
        File versionFile = new File(baseDirectory, saveFile);
        if (Objects.nonNull(completeVersion.getDownloads()) && Objects.nonNull(completeVersion.getDownloads().get("client"))) {
            CompleteVersion completeVersion1 = TLauncher.getInstance().getTLauncherManager().addFilesForDownloading(completeVersion, false);
            boolean containsKey = Objects.nonNull(completeVersion1.getDownloads().get("client_tl_manager"));
            boolean tlAccount = TLauncher.getInstance().getTLauncherManager().applyTLauncherAccountLib(completeVersion);
            addedMinecraftClient(force, container, completeVersion.getDownloads().get("client"), baseDirectory, versionFile, (containsKey && tlAccount) ? false : true, completeVersion.getModifiedVersion().isSkipHashsumValidation());
            if (containsKey) {
                addedMinecraftClient(force, container, completeVersion1.getDownloads().get("client_tl_manager"), baseDirectory, versionFile, containsKey && tlAccount, completeVersion.getModifiedVersion().isSkipHashsumValidation());
            }
        } else if (!Files.exists(new File(baseDirectory, saveFile).toPath(), new LinkOption[0])) {
            String cacheRelatedFolder = "libraries/v1/" + jarFile;
            MetadataDTO client = new MetadataDTO();
            client.setPath(cacheRelatedFolder);
            client.setUrl(jarFile);
            client.setLocalDestination(new File(baseDirectory, cacheRelatedFolder));
            Downloadable d = new Downloadable(repo, client, force);
            d.addAdditionalDestination(new File(baseDirectory, saveFile));
            log("Jar for " + syncInfo.getID() + ':', d);
            container.add(d);
        }
        return container;
    }

    public boolean isLocalRefresh() {
        return this.localRefresh;
    }

    private VersionSyncInfoContainer addedMinecraftClient(boolean force, VersionSyncInfoContainer container, MetadataDTO client, File baseDirectory, File versionFile, boolean additionalCopy, boolean skipHashumValidation) throws IOException {
        Repo repo = ClientInstanceRepo.EMPTY_REPO;
        String cacheRelatedFolder = "libraries" + new URL(client.getUrl()).getPath();
        File cacheFile = new File(baseDirectory, cacheRelatedFolder);
        client.setLocalDestination(cacheFile);
        if (versionFile.exists() && (FileUtil.getChecksum(versionFile).equals(client.getSha1()) || skipHashumValidation)) {
            return container;
        }
        if (cacheFile.exists() && (FileUtil.getChecksum(cacheFile).equals(client.getSha1()) || skipHashumValidation)) {
            FileUtil.copyFile(cacheFile.getAbsoluteFile(), versionFile, true);
            return container;
        }
        Downloadable d = new Downloadable(repo, client, force);
        container.add(d);
        if (additionalCopy) {
            d.addAdditionalDestination(versionFile);
        }
        return container;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionManager$AsyncRawVersionListObject.class */
    public class AsyncRawVersionListObject extends AsyncObject<VersionList.RawVersionList> {
        private final RemoteVersionList remoteList;

        AsyncRawVersionListObject(RemoteVersionList remoteList) {
            this.remoteList = remoteList;
        }

        RemoteVersionList getVersionList() {
            return this.remoteList;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.tlauncher.util.async.AsyncObject
        public VersionList.RawVersionList execute() throws AsyncObjectGotErrorException {
            try {
                return this.remoteList.getRawList();
            } catch (Exception e) {
                VersionManager.this.log("Error refreshing version list:", e);
                throw new AsyncObjectGotErrorException(this, e);
            }
        }
    }
}
