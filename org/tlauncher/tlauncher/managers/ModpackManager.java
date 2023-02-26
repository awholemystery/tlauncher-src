package org.tlauncher.tlauncher.managers;

import by.gdev.http.download.service.GsonService;
import by.gdev.util.model.download.Repo;
import ch.qos.logback.core.joran.action.Action;
import com.github.junrar.Archive;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import javax.inject.Named;
import javax.swing.SwingUtilities;
import net.minecraft.launcher.Http;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.tlauncher.exceptions.ParseModPackException;
import org.tlauncher.modpack.domain.client.ForgeVersionDTO;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.PictureType;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.ShaderpackDTO;
import org.tlauncher.modpack.domain.client.SubModpackDTO;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.modpack.domain.client.share.DependencyType;
import org.tlauncher.modpack.domain.client.share.GameEntitySort;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.InfoMod;
import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.share.ParsedElementDTO;
import org.tlauncher.modpack.domain.client.share.StateGameElement;
import org.tlauncher.modpack.domain.client.site.CommonPage;
import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.configuration.InnerConfiguration;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.mods.GameEntityHandler;
import org.tlauncher.tlauncher.downloader.mods.MapDownloader;
import org.tlauncher.tlauncher.downloader.mods.UnzipEntityDownloader;
import org.tlauncher.tlauncher.entity.MinecraftInstance;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.exceptions.GameEntityNotFound;
import org.tlauncher.tlauncher.exceptions.RequiredRemoteVersionError;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SameMapFoldersException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityAdapter;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame;
import org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class */
public class ModpackManager implements VersionManagerListener, MinecraftListener, ItemListener {
    @Inject
    @Named("GsonCompleteVersion")
    private Gson gson;
    @Inject
    private TLauncher tLauncher;
    @Inject
    private CloseableHttpClient closeableHttpClient;
    @Inject
    private GsonService gsonService;
    @Inject
    @Named("anyVersionType")
    private NameIdDTO anyVersionType;
    private InfoMod infoMod;
    private String modpackApiURL;
    @Inject
    private CloseableHttpClient client;
    @Inject
    private RequestConfig requestConfig;
    @Inject
    @Named("modpackExecutorService")
    private ExecutorService modpackExecutorService;
    private final Map<GameType, List<GameEntityListener>> gameListeners = Collections.synchronizedMap(new HashMap<GameType, List<GameEntityListener>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.1
        {
            put(GameType.MAP, Collections.synchronizedList(new ArrayList()));
            put(GameType.MOD, Collections.synchronizedList(new ArrayList()));
            put(GameType.MODPACK, Collections.synchronizedList(new ArrayList()));
            put(GameType.RESOURCEPACK, Collections.synchronizedList(new ArrayList()));
            put(GameType.SHADERPACK, Collections.synchronizedList(new ArrayList()));
        }
    });
    private final File STATUS_MODPACK_FILE = FileUtil.getRelativeConfigFile("status.modpack");
    private final InnerConfiguration innerConfiguration = TLauncher.getInnerSettings();
    private Set<Long> statusModpackElement = new HashSet();
    private final AtomicBoolean addedVersionListener = new AtomicBoolean(false);
    private Map<GameType, List<CategoryDTO>> map = Collections.synchronizedMap(new HashMap());
    private List<NameIdDTO> minecraftVersionTypes = Collections.synchronizedList(new ArrayList());
    private Map<NameIdDTO, List<GameVersionDTO>> gameVersions = Collections.synchronizedMap(new HashMap());
    private Map<String, Set<Long>> favoriteGameEntityIds = Collections.synchronizedMap(new HashMap());

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager$ModpackServerCommand.class */
    public enum ModpackServerCommand {
        UPDATE,
        DOWNLOAD,
        ADD_NEW_GAME_ENTITY
    }

    public ExecutorService getModpackExecutorService() {
        return this.modpackExecutorService;
    }

    public Map<GameType, List<CategoryDTO>> getMap() {
        return this.map;
    }

    public List<NameIdDTO> getMinecraftVersionTypes() {
        return this.minecraftVersionTypes;
    }

    public Map<NameIdDTO, List<GameVersionDTO>> getGameVersions() {
        return this.gameVersions;
    }

    public ModpackManager() {
        GameEntityAdapter listener = new GameEntityAdapter() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.2
            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityAdapter, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void installEntity(CompleteVersion e) {
                update();
            }

            private void update() {
                TLauncher.getInstance().getVersionManager().getLocalList().refreshVersions();
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityAdapter, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void removeCompleteVersion(CompleteVersion e) {
                update();
            }
        };
        this.gameListeners.get(GameType.MODPACK).add(listener);
        this.modpackApiURL = this.innerConfiguration.get("modpack.operation.url");
    }

    private void log(Object... s) {
        U.log("[Modpack] ", s);
    }

    public synchronized void loadInfo() {
        this.infoMod = new InfoMod();
        this.tLauncher.getFrame().mp.modpackScene.prepareView(getModpackVersions());
        if (!this.addedVersionListener.get()) {
            this.addedVersionListener.set(true);
            this.tLauncher.getVersionManager().addListener(this);
        }
        readStatusGameElement();
        if (!this.statusModpackElement.isEmpty()) {
            try {
                importUserGameEntities((List) this.statusModpackElement.stream().map(e -> {
                    GameEntityDTO d = new GameEntityDTO();
                    d.setId(e);
                    return d;
                }).collect(Collectors.toList()));
                SwingUtilities.invokeLater(() -> {
                    Alert.showLocMessage("export.old.favorite.elements");
                });
            } catch (Throwable e2) {
                log("error", e2);
            }
        }
        getFavoriteGameEntities();
    }

    public void fillVersionTypesAndGameVersion() throws IOException {
        if (this.minecraftVersionTypes.size() < 2) {
            this.minecraftVersionTypes.addAll(getMinecraftVersionTypesRemote());
        }
        if (this.gameVersions.isEmpty()) {
            for (NameIdDTO nid : getMinecraftVersionTypes()) {
                List<GameVersionDTO> list = getGameVersionsRemote(nid);
                this.gameVersions.put(nid, list);
                if (nid.getId().equals(1L)) {
                    this.gameVersions.put(this.anyVersionType, list);
                }
            }
        }
    }

    public void fillCategories() throws IOException {
        if (this.map.isEmpty()) {
            this.map.putAll((Map) this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "client/categories", new TypeToken<Map<GameType, List<CategoryDTO>>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.3
            }.getType()));
        }
    }

    public DownloadableContainer getContainer(CompleteVersion version, boolean force) {
        DownloadableContainer container = new DownloadableContainer();
        Path versionFolder = ModpackUtil.getPathByVersion(version);
        if (version.getModpack() != null && version.getModpack().getVersion() != null) {
            ModpackVersionDTO versionDTO = (ModpackVersionDTO) version.getModpack().getVersion();
            List<MetadataDTO> list = checkResources(versionDTO, force, versionFolder);
            for (MetadataDTO m : list) {
                m.setLocalDestination(new File(versionFolder.toFile(), m.getPath()));
                Downloadable d = new Downloadable(ClientInstanceRepo.createModpackRepo(), m, force);
                container.add(d);
            }
            List<MetadataDTO> list2 = checkCompositeResources(versionDTO, force, versionFolder);
            for (MetadataDTO m2 : list2) {
                m2.setLocalDestination(new File(versionFolder.toFile(), m2.getPath()));
                Downloadable d2 = new MapDownloader(force, m2);
                container.add(d2);
            }
            if (Objects.nonNull(versionDTO.getAdditionalFile())) {
                MetadataDTO metadataDTO = versionDTO.getAdditionalFile();
                Path additionalFile = ModpackUtil.getPathByVersion(version, metadataDTO.getPath());
                metadataDTO.setLocalDestination(additionalFile.toFile());
                if (Files.notExists(additionalFile, new LinkOption[0]) || !metadataDTO.getSha1().equals(FileUtil.getChecksum(metadataDTO.getLocalDestination()))) {
                    container.add(new UnzipEntityDownloader(force, metadataDTO));
                }
            }
            container.addHandler(new GameEntityHandler());
        }
        return container;
    }

    private List<MetadataDTO> checkResources(ModpackVersionDTO version, boolean force, Path versionFolder) {
        log("check resources");
        List<MetadataDTO> list = new ArrayList<>();
        for (ModDTO mod : version.getMods()) {
            if (!mod.isUserInstall() && mod.getStateGameElement() != StateGameElement.NO_ACTIVE && notExistOrCorrect(versionFolder, mod, force) && !fillFromCache(GameType.MOD, mod, version, versionFolder)) {
                list.add(mod.getVersion().getMetadata());
            }
        }
        for (ResourcePackDTO resourcePack : version.getResourcePacks()) {
            if (!resourcePack.isUserInstall() && resourcePack.getStateGameElement() != StateGameElement.NO_ACTIVE && notExistOrCorrect(versionFolder, resourcePack, force) && !fillFromCache(GameType.RESOURCEPACK, resourcePack, version, versionFolder)) {
                list.add(resourcePack.getVersion().getMetadata());
            }
        }
        for (ShaderpackDTO shader : version.getShaderpacks()) {
            if (!shader.isUserInstall() && shader.getStateGameElement() != StateGameElement.NO_ACTIVE && notExistOrCorrect(versionFolder, shader, force) && !fillFromCache(GameType.SHADERPACK, shader, version, versionFolder)) {
                list.add(shader.getVersion().getMetadata());
            }
        }
        return list;
    }

    private boolean notExistOrCorrect(Path versionFolder, GameEntityDTO e, boolean hash) {
        Path path = Paths.get(versionFolder.toString(), e.getVersion().getMetadata().getPath());
        if (Files.notExists(path, new LinkOption[0])) {
            return true;
        }
        return hash && !FileUtil.getChecksum(path.toFile()).equals(e.getVersion().getMetadata().getSha1());
    }

    private List<MetadataDTO> checkCompositeResources(ModpackVersionDTO version, boolean force, Path versionFolder) {
        log("check CompositeResources");
        List<MetadataDTO> list = new ArrayList<>();
        for (MapDTO map : version.getMaps()) {
            if (!map.isUserInstall()) {
                list.add(map.getVersion().getMetadata());
            }
        }
        Iterator<MetadataDTO> it = list.iterator();
        while (it.hasNext()) {
            MetadataDTO meta = it.next();
            U.debug(meta);
            if (((MapMetadataDTO) meta).getFolders() != null && ((MapMetadataDTO) meta).getFolders().size() != 0) {
                String folder = versionFolder.toString() + "/saves/" + ((MapMetadataDTO) meta).getFolders().get(0);
                if (new File(folder).exists()) {
                    it.remove();
                }
            }
        }
        return list;
    }

    public void backupModPack(List<CompleteVersion> list, File saveFolder, ModpackBackupFrame.HandleListener handleListener) {
        AsyncThread.execute(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0009: INVOKE  
              (wrap: java.lang.Runnable : 0x0004: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r5v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r6v0 'list' java.util.List<net.minecraft.launcher.versions.CompleteVersion> A[D('list' java.util.List<net.minecraft.launcher.versions.CompleteVersion>), DONT_INLINE])
              (r7v0 'saveFolder' java.io.File A[D('saveFolder' java.io.File), DONT_INLINE])
              (r8v0 'handleListener' org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener A[D('handleListener' org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
              (r1 I:java.util.List)
              (r2 I:java.io.File)
              (r3 I:org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener)
             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$backupModPack$2(java.util.List, java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void)
             type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.backupModPack(java.util.List<net.minecraft.launcher.versions.CompleteVersion>, java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 3
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
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$backupModPack$2(r1, r2, r3);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.backupModPack(java.util.List, java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void");
    }

    private List<File> findCopiedFiles(CompleteVersion completeVersion, File version) {
        IOFileFilter filter = FileFilterUtils.notFileFilter(FileFilterUtils.or(FileFilterUtils.nameFileFilter("natives"), FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.SAVES.toString()), FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.MODS.toString()), FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.RESOURCEPACKS.toString())));
        IOFileFilter filesFilter = FileFilterUtils.notFileFilter(FileFilterUtils.or(FileFilterUtils.nameFileFilter(completeVersion.getID() + ".jar"), FileFilterUtils.nameFileFilter(completeVersion.getID() + ".jar.bak")));
        List<File> list = (List) FileUtils.listFiles(version, filesFilter, filter);
        if (TLauncher.DEBUG) {
            U.log("filter by IOFileFilter");
            for (File f : list) {
                U.log(f);
            }
        }
        ModpackVersionDTO modpackVersion = (ModpackVersionDTO) completeVersion.getModpack().getVersion();
        Iterator<MapDTO> it = modpackVersion.getMaps().iterator();
        while (it.hasNext()) {
            File map = new File(version, "saves/" + FilenameUtils.getBaseName(it.next().getVersion().getMetadata().getPath()));
            if (map.exists()) {
                list.addAll(FileUtils.listFiles(map, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
            }
        }
        for (SubModpackDTO en : modpackVersion.getMods()) {
            if (en.isUserInstall()) {
                addToList(version, list, en);
            }
        }
        for (SubModpackDTO en2 : modpackVersion.getResourcePacks()) {
            if (en2.isUserInstall()) {
                addToList(version, list, en2);
            }
        }
        if (TLauncher.DEBUG) {
            U.log("backed files");
            for (File f2 : list) {
                U.log(f2);
            }
        }
        return list;
    }

    private void addToList(File version, List<File> list, SubModpackDTO en) {
        if (en.getStateGameElement() == StateGameElement.NO_ACTIVE) {
            list.add(new File(version, en.getVersion().getMetadata().getPath() + ".deactivation"));
        } else {
            list.add(new File(version, en.getVersion().getMetadata().getPath()));
        }
    }

    public void installModPack(File file, ModpackBackupFrame.HandleListener handleListener) {
        AsyncThread.execute(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: INVOKE  
              (wrap: java.lang.Runnable : 0x0003: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r5v0 'file' java.io.File A[D('file' java.io.File), DONT_INLINE])
              (r6v0 'handleListener' org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener A[D('handleListener' org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
              (r1 I:java.io.File)
              (r2 I:org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener)
             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$installModPack$3(java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void)
             type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.installModPack(java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
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
            r1 = r5
            r2 = r6
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$installModPack$3(r1, r2);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.installModPack(java.io.File, org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame$HandleListener):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CompleteVersion parseCurse(File versionFolder, String modpackName, File modPackFolder, File version) throws Exception {
        File instance = new File(modPackFolder, "minecraftinstance.json");
        if (Files.exists(instance.toPath(), new LinkOption[0])) {
            File mods = new File(modPackFolder, "mods");
            File resourcepacks = new File(modPackFolder, "resourcepacks");
            File maps = new File(modPackFolder, PathAppUtil.DIRECTORY_WORLDS);
            if (!Files.exists(mods.toPath(), new LinkOption[0]) && !Files.exists(resourcepacks.toPath(), new LinkOption[0]) && !Files.exists(maps.toPath(), new LinkOption[0])) {
                log("modpack doesn't exist");
            }
            MinecraftInstance minecraftInstance = (MinecraftInstance) this.gson.fromJson(FileUtil.readFile(instance), (Class<Object>) MinecraftInstance.class);
            if (minecraftInstance == null) {
                throw new Exception("broken config");
            }
            ModpackDTO modPack = new ModpackDTO();
            modPack.setId(Long.valueOf(-U.n()));
            ModpackVersionDTO modpackVersion = new ModpackVersionDTO();
            String[] formats = {ArchiveStreamFactory.JAR, ArchiveStreamFactory.ZIP};
            CompleteVersion completeVersion = (CompleteVersion) this.gson.fromJson(minecraftInstance.baseModLoader.VersionJson, (Class<Object>) CompleteVersion.class);
            modpackVersion.setForgeVersion(completeVersion.getID());
            modpackVersion.setGameVersion(minecraftInstance.baseModLoader.MinecraftVersion);
            completeVersion.setID(modpackName);
            modPack.setName(completeVersion.getID());
            modpackVersion.setId(Long.valueOf((-U.n()) - 1));
            modpackVersion.setName("1.0");
            modpackVersion.setMods(createHandleGameEntities(mods, formats, ModDTO.class));
            modpackVersion.setResourcePacks(createHandleGameEntities(resourcepacks, formats, ResourcePackDTO.class));
            modpackVersion.setMaps(createMapsByFolder(maps));
            modPack.setVersion(modpackVersion);
            completeVersion.setModpackDTO(modPack);
            FileUtil.writeFile(version, this.gson.toJson(completeVersion));
            return completeVersion;
        }
        throw new Exception("dont' find config file");
    }

    private List<MapDTO> createMapsByFolder(File maps) {
        File[] fileArr;
        List<MapDTO> list = new ArrayList<>();
        FilenameFilter filter = dir, name -> {
            return dir.isDirectory();
        };
        for (File file : (File[]) Objects.requireNonNull(maps.listFiles(filter))) {
            MapDTO map = new MapDTO();
            VersionDTO v = new VersionDTO();
            v.setName("1.0");
            MetadataDTO meta = new MetadataDTO();
            meta.setPath(FileUtil.GameEntityFolder.getPath(GameType.MAP) + "/" + file.getName() + ".zip");
            v.setMetadata(meta);
            map.setUserInstall(true);
            map.setName(file.getName());
            map.setVersion(v);
            list.add(map);
        }
        return list;
    }

    public List<String> analizeArchiver(File file) throws ParseModPackException {
        try {
            String ext = FilenameUtils.getExtension(file.getCanonicalPath());
            List<String> list = new ArrayList<>();
            boolean z = true;
            switch (ext.hashCode()) {
                case 112675:
                    if (ext.equals("rar")) {
                        z = false;
                        break;
                    }
                    break;
                case 120609:
                    if (ext.equals(ArchiveStreamFactory.ZIP)) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    list = FileUtil.topFolders(new Archive(file));
                    break;
                case true:
                    list = FileUtil.topFolders(new ZipFile(file));
                    break;
            }
            if (list.isEmpty()) {
                throw new ParseModPackException("The archive doesn't contain any folders");
            }
            if (!checkNameVersion(list)) {
                throw new ParseModPackException("there is a version with same name");
            }
            return list;
        } catch (Exception e) {
            log("error during analize archiver " + file);
            throw new ParseModPackException(e);
        }
    }

    public boolean checkNameVersion(List<String> list) {
        for (String s : list) {
            if (Objects.nonNull(this.tLauncher.getVersionManager().getVersionSyncInfo(s))) {
                return false;
            }
        }
        return true;
    }

    private List<? extends GameEntityDTO> createHandleGameEntities(File folder, String[] exts, Class<? extends GameEntityDTO> t) {
        List<GameEntityDTO> list = new ArrayList<>();
        List<File> files = (List) FileUtils.listFiles(folder, exts, true);
        for (File f : files) {
            try {
                GameEntityDTO c = createHandleGameEntity(folder, t, f);
                list.add(c);
            } catch (IllegalAccessException | InstantiationException e) {
                log(e);
            }
        }
        return list;
    }

    private GameEntityDTO createHandleGameEntity(File folder, Class<? extends GameEntityDTO> t, File f) throws InstantiationException, IllegalAccessException {
        GameEntityDTO c = t.newInstance();
        c.setId(Long.valueOf(-U.n()));
        c.setName(FilenameUtils.getBaseName(f.getName()));
        c.setUserInstall(true);
        MetadataDTO meta = FileUtil.createMetadata(f, folder, t);
        meta.setPath(FileUtil.GameEntityFolder.getPath(t, true).concat(meta.getPath()));
        meta.setUrl(FileUtil.GameEntityFolder.getPath(t, true).concat(meta.getUrl()));
        VersionDTO standardVersion = new VersionDTO();
        standardVersion.setId(Long.valueOf(-U.n()));
        standardVersion.setName("1.0");
        standardVersion.setMetadata(meta);
        c.setVersion(standardVersion);
        return c;
    }

    public void createModpack(String name, ModpackDTO modpackDTO, boolean usedSkin) {
        try {
            ModpackVersionDTO mvd = (ModpackVersionDTO) modpackDTO.getVersion();
            CompleteVersion completeVersion = getCompleteVersionByMinecraftVersionTypeAndId(mvd.findFirstMinecraftVersionType(), mvd.getMinecraftVersionName());
            completeVersion.setID(name);
            completeVersion.setModpackDTO(modpackDTO);
            this.tLauncher.getVersionManager().getLocalList().saveVersion(completeVersion);
            for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
                l.installEntity(completeVersion);
                l.installEntity(completeVersion.getModpack(), GameType.MODPACK);
            }
            if (usedSkin) {
                installTLSkinCapeMod(mvd);
            }
            if (mvd.getMinecraftVersionTypes().stream().filter(f -> {
                return f.getId().equals(2L);
            }).findAny().isPresent()) {
                ModDTO m = new ModDTO();
                m.setId(ModDTO.FABRIC_API_ID);
                installEntity(m, null, GameType.MOD, null, true);
            }
        } catch (IOException e) {
            U.log(e);
        }
    }

    public void installTLSkinCapeMod(ModpackVersionDTO mvd) {
        ModDTO m = new ModDTO();
        m.setId(ModDTO.TL_SKIN_CAPE_ID);
        installEntity(m, null, GameType.MOD, null, false);
    }

    public CompleteVersion getCompleteVersionByMinecraftVersionTypeAndId(NameIdDTO type, NameIdDTO version) throws IOException, RequiredRemoteVersionError {
        MinecraftVersionDTO minecraftVersion = getCompleteVersion(type, version);
        try {
            CompleteVersion completeVersion = ((CompleteVersion) this.gson.fromJson(minecraftVersion.getValue(), (Class<Object>) CompleteVersion.class)).resolve(this.tLauncher.getVersionManager(), true);
            if (Objects.isNull(completeVersion)) {
                this.tLauncher.getVersionManager().asyncRefresh();
                throw new RequiredRemoteVersionError();
            }
            TlauncherUtil.processRemoteVersionToSave(completeVersion, minecraftVersion.getValue(), this.gson);
            return completeVersion;
        } catch (NullPointerException e) {
            U.log(" type " + Objects.isNull(type));
            U.log(" version " + Objects.isNull(version));
            U.log(" minecraftVersion " + minecraftVersion);
            U.log("error request " + minecraftVersion.getValue() + " " + type.toString() + " " + version.toString());
            throw e;
        }
    }

    private void addEntityToModpack(GameEntityDTO gameEntity, CompleteVersion completeVersion, GameType type) throws IOException {
        ModpackDTO modPack = completeVersion.getModpack();
        ModpackVersionDTO modpackVersion = (ModpackVersionDTO) modPack.getVersion();
        if (modpackVersion == null) {
            modpackVersion = new ModpackVersionDTO();
            modpackVersion.setMaps(new ArrayList());
            modpackVersion.setMods(new ArrayList());
            modpackVersion.setResourcePacks(new ArrayList());
            modPack.setVersion(modpackVersion);
        }
        try {
            GameEntityDTO removedEntity = findAndRemoveGameEntity(completeVersion, gameEntity, type);
            for (GameEntityListener l : this.gameListeners.get(type)) {
                l.removeEntity(removedEntity);
            }
        } catch (GameEntityNotFound e) {
        }
        modpackVersion.add(type, gameEntity);
    }

    private boolean checkAddedElement(VersionDTO version, GameType type, GameEntityDTO entity) {
        ModpackVersionDTO v = (ModpackVersionDTO) version;
        if (v != null && type != GameType.MODPACK) {
            for (GameEntityDTO en : v.getByType(type)) {
                if (en.getId().equals(entity.getId())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public void showFullGameEntity(GameEntityDTO entity, GameType type) {
        CompletableFuture.runAsync(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0014: INVOKE  
              (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x000c: INVOKE  (r0v2 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
              (wrap: java.lang.Runnable : 0x0003: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r6v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
              (r5v0 'entity' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('entity' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
              (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
              (r2 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$showFullGameEntity$8(org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameEntityDTO):void)
              (wrap: java.util.concurrent.ExecutorService : 0x0009: IGET  (r1v2 java.util.concurrent.ExecutorService A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), IMMUTABLE_TYPE, THIS])
             org.tlauncher.tlauncher.managers.ModpackManager.modpackExecutorService java.util.concurrent.ExecutorService)
             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable, java.util.concurrent.Executor):java.util.concurrent.CompletableFuture)
              (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x000f: INVOKE_CUSTOM (r1v3 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
             handle type: INVOKE_STATIC
             lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
             call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$showFullGameEntity$9(java.lang.Throwable):java.lang.Void)
             type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.managers.ModpackManager.showFullGameEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
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
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
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
                r0.lambda$showFullGameEntity$8(r1, r2);
            }
            r1 = r4
            java.util.concurrent.ExecutorService r1 = r1.modpackExecutorService
            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0, r1)
            void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                return lambda$showFullGameEntity$9(v0);
            }
            java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.showFullGameEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void");
    }

    public void showSubModpackElement(GameEntityDTO entity, GameEntityDTO parent, GameType type) throws IOException {
        GameEntityDTO entity1 = getGameEntity(type, entity.getId());
        if (entity1 == null) {
            return;
        }
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001a: INVOKE  
              (wrap: java.lang.Runnable : 0x0015: INVOKE_CUSTOM (r0v4 java.lang.Runnable A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r0v1 'entity1' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('entity1' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
              (r7v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
              (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
              (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$showSubModpackElement$10(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.showSubModpackElement(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
            	... 19 more
            */
        /*
            this = this;
            r0 = r4
            r1 = r7
            r2 = r5
            java.lang.Long r2 = r2.getId()
            org.tlauncher.modpack.domain.client.GameEntityDTO r0 = r0.getGameEntity(r1, r2)
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L11
            return
        L11:
            r0 = r4
            r1 = r8
            r2 = r7
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$showSubModpackElement$10(r1, r2);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.showSubModpackElement(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void");
    }

    public <T> T readFromServer(Class<T> t, GameEntityDTO e, VersionDTO version) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", e.getId());
        map.put("versionId", version.getId());
        String res = Http.performGet(this.innerConfiguration.get("modpack.operation.url") + "read/" + t.getSimpleName().toLowerCase(), map, U.getConnectionTimeout(), this.innerConfiguration.getInteger("modpack.update.time.connect"));
        return (T) this.gson.fromJson(res, (Class<Object>) t);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager manager) {
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager manager) {
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        if (!manager.isLocalRefresh()) {
            loadInfo();
        }
    }

    private void processGameElementByStatus() {
        readStatusGameElement();
    }

    private void readStatusGameElement() {
        try {
            if (this.STATUS_MODPACK_FILE.exists()) {
                HashSet<Long> set = (HashSet) this.gson.fromJson(FileUtil.readFile(this.STATUS_MODPACK_FILE), new TypeToken<HashSet<Long>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.4
                }.getType());
                if (Objects.nonNull(set)) {
                    this.statusModpackElement.addAll(set);
                }
            } else {
                writeStatusGameElement();
            }
        } catch (JsonSyntaxException | IOException e) {
            U.log(e);
            writeStatusGameElement();
        }
    }

    private void writeStatusGameElement() {
        try {
            FileUtil.writeFile(this.STATUS_MODPACK_FILE, this.gson.toJson(this.statusModpackElement));
        } catch (IOException e1) {
            U.log(e1);
        }
    }

    public synchronized InfoMod getInfoMod() {
        return this.infoMod;
    }

    private List<CompleteVersion> getModpackVersions() {
        return (List) Lists.newArrayList(this.tLauncher.getVersionManager().getLocalList().getVersions()).stream().filter(e -> {
            CompleteVersion cv = (CompleteVersion) e;
            return cv.isModpack();
        }).map(e2 -> {
            return (CompleteVersion) e2;
        }).collect(Collectors.toList());
    }

    public void changeModpackElementState(GameEntityDTO entity, GameType type) {
        CompleteVersion completeVersion = this.tLauncher.getFrame().mp.modpackScene.getSelectedCompleteVersion();
        ModpackVersionDTO versionDTO = (ModpackVersionDTO) completeVersion.getModpack().getVersion();
        Optional<? extends GameEntityDTO> op = versionDTO.getByType(type).stream().filter(e -> {
            return e.getId().equals(entity.getId());
        }).findFirst();
        if (!op.isPresent()) {
            return;
        }
        SubModpackDTO en = (SubModpackDTO) op.get();
        AsyncThread.execute(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x005a: INVOKE  
              (wrap: java.lang.Runnable : 0x0055: INVOKE_CUSTOM (r0v21 java.lang.Runnable A[REMOVE]) = 
              (r7v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r9v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
              (r0v19 'en' org.tlauncher.modpack.domain.client.SubModpackDTO A[D('en' org.tlauncher.modpack.domain.client.SubModpackDTO), DONT_INLINE])
              (r0v9 'versionDTO' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO A[D('versionDTO' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO), DONT_INLINE])
              (r0v5 'completeVersion' net.minecraft.launcher.versions.CompleteVersion A[D('completeVersion' net.minecraft.launcher.versions.CompleteVersion), DONT_INLINE])
              (r8v0 'entity' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('entity' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
              (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
              (r2 I:org.tlauncher.modpack.domain.client.SubModpackDTO)
              (r3 I:org.tlauncher.modpack.domain.client.version.ModpackVersionDTO)
              (r4 I:net.minecraft.launcher.versions.CompleteVersion)
              (r5 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$changeModpackElementState$16(org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.version.ModpackVersionDTO, net.minecraft.launcher.versions.CompleteVersion, org.tlauncher.modpack.domain.client.GameEntityDTO):void)
             type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.changeModpackElementState(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 5
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
            	... 19 more
            */
        /*
            this = this;
            r0 = r7
            org.tlauncher.tlauncher.rmo.TLauncher r0 = r0.tLauncher
            org.tlauncher.tlauncher.ui.TLauncherFrame r0 = r0.getFrame()
            org.tlauncher.tlauncher.ui.MainPane r0 = r0.mp
            org.tlauncher.tlauncher.ui.scenes.ModpackScene r0 = r0.modpackScene
            net.minecraft.launcher.versions.CompleteVersion r0 = r0.getSelectedCompleteVersion()
            r10 = r0
            r0 = r10
            org.tlauncher.modpack.domain.client.ModpackDTO r0 = r0.getModpack()
            org.tlauncher.modpack.domain.client.version.VersionDTO r0 = r0.getVersion()
            org.tlauncher.modpack.domain.client.version.ModpackVersionDTO r0 = (org.tlauncher.modpack.domain.client.version.ModpackVersionDTO) r0
            r11 = r0
            r0 = r11
            r1 = r9
            java.util.List r0 = r0.getByType(r1)
            java.util.stream.Stream r0 = r0.stream()
            r1 = r8
            void r1 = (v1) -> { // java.util.function.Predicate.test(java.lang.Object):boolean
                return lambda$changeModpackElementState$13(r1, v1);
            }
            java.util.stream.Stream r0 = r0.filter(r1)
            java.util.Optional r0 = r0.findFirst()
            r12 = r0
            r0 = r12
            boolean r0 = r0.isPresent()
            if (r0 != 0) goto L43
            return
        L43:
            r0 = r12
            java.lang.Object r0 = r0.get()
            org.tlauncher.modpack.domain.client.SubModpackDTO r0 = (org.tlauncher.modpack.domain.client.SubModpackDTO) r0
            r13 = r0
            r0 = r7
            r1 = r9
            r2 = r13
            r3 = r11
            r4 = r10
            r5 = r8
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$changeModpackElementState$16(r1, r2, r3, r4, r5);
            }
            org.tlauncher.util.async.AsyncThread.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.changeModpackElementState(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void");
    }

    private void changeActivation(SubModpackDTO entity, GameType type, CompleteVersion completeVersion) throws IOException {
        File modpackFolder = FileUtil.getRelative("versions/" + completeVersion.getID()).toFile();
        try {
            File target = new File(modpackFolder, entity.getVersion().getMetadata().getPath());
            if (entity.getStateGameElement() == StateGameElement.ACTIVE) {
                Files.move(target.toPath(), Paths.get(target.toString() + ".deactivation", new String[0]), StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(Paths.get(target.toString() + ".deactivation", new String[0]), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (NoSuchFileException e) {
            log(entity.getStateGameElement() + " ", e.getMessage());
        }
        if (entity.getStateGameElement() == StateGameElement.NO_ACTIVE) {
            entity.setStateGameElement(StateGameElement.ACTIVE);
        } else {
            entity.setStateGameElement(StateGameElement.NO_ACTIVE);
        }
        for (GameEntityListener l : this.gameListeners.get(type)) {
            l.activation(entity);
        }
    }

    public synchronized void installEntity(GameEntityDTO e, VersionDTO version, GameType type, GameVersionDTO gameVersion, boolean throwError) {
        GameEntityDTO gameEntity;
        if (type != GameType.MODPACK && !this.tLauncher.getFrame().mp.modpackScene.isSelectedCompleteVersion()) {
            Alert.showLocMessage("modpack.select.modpack");
            return;
        }
        for (GameEntityListener l : this.gameListeners.get(type)) {
            l.processingStarted(e, version);
        }
        try {
            if (GameType.MODPACK.equals(type)) {
                ModpackDTO installed = new ModpackDTO();
                installed.setId(e.getId());
                installed.setName(e.getName());
                ModpackVersionDTO mv = getInstallingModpackVersionDTO(e, version);
                installed.setVersion(mv);
                CompleteVersion cv = getCompleteVersionByMinecraftVersionTypeAndId(mv.findFirstMinecraftVersionType(), mv.getMinecraftVersionName()).resolve(this.tLauncher.getVersionManager(), true);
                cv.setID(installed.getName() + " " + installed.getVersion().getName());
                cv.setModpackDTO(installed);
                gameEntity = installed;
                this.tLauncher.getVersionManager().getLocalList().saveVersion(cv);
                SwingUtilities.invokeLater(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x00f4: INVOKE  
                      (wrap: java.lang.Runnable : 0x00ef: INVOKE_CUSTOM (r0v86 java.lang.Runnable A[REMOVE]) = 
                      (r7v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r10v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
                      (r0v77 'cv' net.minecraft.launcher.versions.CompleteVersion A[D('cv' net.minecraft.launcher.versions.CompleteVersion), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
                      (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
                      (r2 I:net.minecraft.launcher.versions.CompleteVersion)
                     type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$installEntity$17(org.tlauncher.modpack.domain.client.share.GameType, net.minecraft.launcher.versions.CompleteVersion):void)
                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.installEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameVersionDTO, boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:302)
                    	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                    	... 31 more
                    */
                /*
                    Method dump skipped, instructions count: 579
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.installEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameVersionDTO, boolean):void");
            }

            private void preInstallingShader(GameType type, GameEntityDTO installEntity, CompleteVersion completeVersion) throws IOException {
                List<ShaderpackDTO> shaderpacks = ((ModpackVersionDTO) completeVersion.getModpack().getVersion()).getShaderpacks();
                for (ShaderpackDTO d : shaderpacks) {
                    d.setStateGameElement(StateGameElement.NO_ACTIVE);
                    for (GameEntityListener l : this.gameListeners.get(type)) {
                        l.activation(d);
                    }
                }
                ModpackUtil.addOrReplaceShaderConfig(completeVersion, "shaderPack", FileUtil.getFilename(installEntity.getVersion().getMetadata().getPath()));
            }

            private void checkMapFolders(CompleteVersion completeVersion, GameEntityDTO installEntity, GameType type) throws SameMapFoldersException {
                if (type != GameType.MAP) {
                    return;
                }
                List<String> remoteFolders = ((MapMetadataDTO) installEntity.getVersion().getMetadata()).getFolders();
                for (MapDTO mapDTO : ((ModpackVersionDTO) completeVersion.getModpack().getVersion()).getMaps()) {
                    List<String> folders = ((MapMetadataDTO) mapDTO.getVersion().getMetadata()).getFolders();
                    if (folders != null && remoteFolders != null && !Collections.disjoint(folders, remoteFolders)) {
                        throw new SameMapFoldersException(String.format("maps have same folders local: %s remote: %s", folders.toString(), remoteFolders.toString()));
                    }
                }
            }

            public void installEntity(GameEntityDTO e, VersionDTO version, GameType type, boolean async) {
                if (async) {
                    CompletableFuture.runAsync(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001a: INVOKE  
                          (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0012: INVOKE  (r0v4 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                          (wrap: java.lang.Runnable : 0x0009: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                          (r7v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r8v0 'e' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('e' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
                          (r9v0 'version' org.tlauncher.modpack.domain.client.version.VersionDTO A[D('version' org.tlauncher.modpack.domain.client.version.VersionDTO), DONT_INLINE])
                          (r10v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  
                          (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
                          (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                          (r2 I:org.tlauncher.modpack.domain.client.version.VersionDTO)
                          (r3 I:org.tlauncher.modpack.domain.client.share.GameType)
                         type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$installEntity$19(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType):void)
                          (wrap: java.util.concurrent.ExecutorService : 0x000f: IGET  (r1v3 java.util.concurrent.ExecutorService A[REMOVE]) = 
                          (r7v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), IMMUTABLE_TYPE, THIS])
                         org.tlauncher.tlauncher.managers.ModpackManager.modpackExecutorService java.util.concurrent.ExecutorService)
                         type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable, java.util.concurrent.Executor):java.util.concurrent.CompletableFuture)
                          (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0015: INVOKE_CUSTOM (r1v4 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                         handle type: INVOKE_STATIC
                         lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                         call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$installEntity$20(java.lang.Throwable):java.lang.Void)
                         type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.managers.ModpackManager.installEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType, boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
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
                        Caused by: java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 3
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
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	... 21 more
                        */
                    /*
                        this = this;
                        r0 = r11
                        if (r0 == 0) goto L21
                        r0 = r7
                        r1 = r8
                        r2 = r9
                        r3 = r10
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$installEntity$19(r1, r2, r3);
                        }
                        r1 = r7
                        java.util.concurrent.ExecutorService r1 = r1.modpackExecutorService
                        java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0, r1)
                        void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                            return lambda$installEntity$20(v0);
                        }
                        java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                        goto L2a
                    L21:
                        r0 = r7
                        r1 = r8
                        r2 = r9
                        r3 = r10
                        r4 = 0
                        r5 = 1
                        r0.installEntity(r1, r2, r3, r4, r5)
                    L2a:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.installEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType, boolean):void");
                }

                private void addDependencies(GameEntityDTO e, CompleteVersion version) throws IOException {
                    List<GameEntityDependencyDTO> list = e.getDependencies();
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    ModpackVersionDTO mv = (ModpackVersionDTO) version.getModpack().getVersion();
                    for (GameEntityDependencyDTO d : list) {
                        if (d.getDependencyType() != DependencyType.INCOMPATIBLE && d.getDependencyType() != DependencyType.OPTIONAL && !e.getId().equals(d.getGameEntityId())) {
                            GameEntityDTO dto = new GameEntityDTO();
                            dto.setId(d.getGameEntityId());
                            if (Objects.nonNull(findGameFromCollection(dto, d.getGameType(), mv))) {
                                log("it has already added  " + dto.getId());
                            } else {
                                try {
                                    GameEntityDTO dto2 = getInstallingGameEntity(d.getGameType(), dto, null, getGameVersion(mv), mv.findFirstMinecraftVersionType());
                                    addEntityToModpack(dto2, version, d.getGameType());
                                    for (GameEntityListener l : this.gameListeners.get(d.getGameType())) {
                                        l.installEntity(dto2, d.getGameType());
                                    }
                                    addDependencies(dto2, version);
                                } catch (IOException ex) {
                                    if (StringUtils.contains(ex.getMessage(), "404")) {
                                        log(String.format("can't resolve dependency for game entity %s, dependency id %s name %s", e.getName(), d.getGameEntityId(), d.getName()));
                                    } else {
                                        throw ex;
                                    }
                                }
                            }
                        }
                    }
                }

                public void installHandleEntity(File[] files, CompleteVersion completeVersion, GameType type, HandleInstallModpackElementFrame.HandleListener handleListener) {
                    Path folder = FileUtil.getRelative("versions/" + completeVersion.getID() + "/" + FileUtil.GameEntityFolder.getPath(type));
                    folder.toFile().mkdir();
                    try {
                        List<GameEntityDTO> list = new ArrayList<>();
                        for (File f : files) {
                            GameEntityDTO entity = initHanldeEntity(type, f, folder);
                            Path target = Paths.get(folder.toString(), f.getName());
                            if (!checkAddedElement(completeVersion.getModpack().getVersion(), type, entity)) {
                                throw new ParseModPackException("entity exists" + entity);
                            }
                            Files.copy(f.toPath(), target, new CopyOption[0]);
                            if (type == GameType.MAP) {
                                FileUtil.unzipUniversal(target.toFile(), target.toFile().getParentFile());
                                FileUtil.deleteFile(target.toFile());
                            } else if (type == GameType.SHADERPACK) {
                                preInstallingShader(GameType.SHADERPACK, entity, completeVersion);
                            }
                            addEntityToModpack(entity, completeVersion, type);
                            list.add(entity);
                        }
                        for (GameEntityDTO entity2 : list) {
                            for (GameEntityListener l : this.gameListeners.get(type)) {
                                l.installEntity(entity2, type);
                            }
                        }
                        resaveVersion(completeVersion);
                        handleListener.installedSuccess();
                    } catch (Exception e) {
                        U.log(e);
                        handleListener.processError(e);
                    }
                }

                private GameEntityDTO initHanldeEntity(GameType type, File f, Path folder) throws ParseModPackException {
                    SubModpackDTO entity;
                    VersionDTO versionDTO = new VersionDTO();
                    switch (type) {
                        case MOD:
                            SubModpackDTO modDTO = new ModDTO();
                            ModVersionDTO modVersionDTO = new ModVersionDTO();
                            modVersionDTO.setIncompatibleMods(new ArrayList());
                            modVersionDTO.setIncompatibleMods(new ArrayList());
                            versionDTO = modVersionDTO;
                            entity = modDTO;
                            break;
                        case MAP:
                            entity = new MapDTO();
                            break;
                        case RESOURCEPACK:
                            entity = new ResourcePackDTO();
                            break;
                        case SHADERPACK:
                            entity = new ShaderpackDTO();
                            break;
                        default:
                            throw new ParseModPackException("not proper type");
                    }
                    entity.setId(Long.valueOf(-U.n()));
                    versionDTO.setId(Long.valueOf((-U.n()) - 1));
                    entity.setVersion(versionDTO);
                    entity.setStateGameElement(StateGameElement.ACTIVE);
                    entity.setName(FilenameUtils.getBaseName(f.getName()));
                    entity.setUserInstall(true);
                    VersionDTO v = new VersionDTO();
                    v.setName("1.0");
                    MetadataDTO meta = new MetadataDTO();
                    if (type == GameType.MAP) {
                        MapMetadataDTO mapMetadata = new MapMetadataDTO();
                        mapMetadata.setFolders(analizeArchiver(f));
                        meta = mapMetadata;
                    }
                    if (type == GameType.MAP) {
                        meta.setPath("saves/" + f.getName());
                    } else {
                        meta.setPath(type.toString() + "s/" + f.getName());
                    }
                    v.setMetadata(meta);
                    entity.setVersion(v);
                    return entity;
                }

                public synchronized void removeEntity(GameEntityDTO entity, VersionDTO versionDTO, GameType type) {
                    for (GameEntityListener l : this.gameListeners.get(type)) {
                        l.processingStarted(entity, versionDTO);
                    }
                    try {
                        GameEntityDTO removedEntity = entity;
                        switch (type) {
                            case MOD:
                            case MAP:
                            case RESOURCEPACK:
                            case SHADERPACK:
                                removedEntity = findAndRemoveGameEntity(this.tLauncher.getFrame().mp.modpackScene.getSelectedCompleteVersion(), entity, type);
                                break;
                            case MODPACK:
                                CompleteVersion version = this.tLauncher.getFrame().mp.modpackScene.getCompleteVersion((ModpackDTO) entity, versionDTO);
                                if (Objects.isNull(version)) {
                                    return;
                                }
                                this.tLauncher.getVersionManager().getLocalList().deleteVersion(version.getID(), false);
                                for (GameEntityListener l2 : this.gameListeners.get(type)) {
                                    l2.removeCompleteVersion(version);
                                }
                                this.tLauncher.getVersionManager().getLocalList().refreshVersions();
                                removedEntity = version.getModpack();
                                break;
                        }
                        for (GameEntityListener l3 : this.gameListeners.get(type)) {
                            l3.removeEntity(removedEntity);
                        }
                    } catch (Throwable e) {
                        U.log(e);
                        SwingUtilities.invokeLater(()
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x014e: INVOKE  
                              (wrap: java.lang.Runnable : 0x0149: INVOKE_CUSTOM (r0v10 java.lang.Runnable A[REMOVE]) = 
                              (r6v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                              (r9v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
                              (r7v0 'entity' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('entity' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
                              (r8v0 'versionDTO' org.tlauncher.modpack.domain.client.version.VersionDTO A[D('versionDTO' org.tlauncher.modpack.domain.client.version.VersionDTO), DONT_INLINE])
                              (r10v1 'e' java.lang.Throwable A[D('e' java.lang.Throwable), DONT_INLINE])
                            
                             handle type: INVOKE_DIRECT
                             lambda: java.lang.Runnable.run():void
                             call insn: ?: INVOKE  
                              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
                              (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
                              (r2 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                              (r3 I:org.tlauncher.modpack.domain.client.version.VersionDTO)
                              (r4 I:java.lang.Throwable)
                             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$removeEntity$21(org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, java.lang.Throwable):void)
                             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.removeEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                            	at jadx.core.codegen.RegionGen.makeCatchBlock(RegionGen.java:365)
                            	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:317)
                            	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
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
                            Caused by: java.lang.IndexOutOfBoundsException: Index 4 out of bounds for length 4
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
                            	... 22 more
                            */
                        /*
                            Method dump skipped, instructions count: 338
                            To view this dump change 'Code comments level' option to 'DEBUG'
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.removeEntity(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.version.VersionDTO, org.tlauncher.modpack.domain.client.share.GameType):void");
                    }

                    public void removeEntity(GameEntityDTO entity, VersionDTO versionDTO, GameType type, boolean sync) {
                        removeEntity(entity, versionDTO, type);
                    }

                    private GameEntityDTO findAndRemoveGameEntity(CompleteVersion selected, GameEntityDTO entity, GameType type) throws IOException, GameEntityNotFound {
                        ModpackVersionDTO versionDTO = (ModpackVersionDTO) selected.getModpack().getVersion();
                        GameEntityDTO current = findGameFromCollection(entity, type, versionDTO);
                        if (current != null) {
                            MetadataDTO meta = current.getVersion().getMetadata();
                            File removedFile = null;
                            switch (type) {
                                case MOD:
                                case RESOURCEPACK:
                                case SHADERPACK:
                                    removedFile = FileUtil.getRelative("versions/" + selected.getID() + "/" + meta.getPath()).toFile();
                                    break;
                                case MAP:
                                    removedFile = FileUtil.getRelative("versions/" + selected.getID() + FilenameUtils.removeExtension(meta.getPath())).toFile();
                                    break;
                            }
                            if (removedFile != null) {
                                FileUtil.deleteFile(removedFile);
                            }
                            versionDTO.getByType(type).remove(current);
                            if (ModDTO.SKIN_MODS.contains(current.getId())) {
                                selected.setSkinVersion(false);
                            }
                            this.tLauncher.getVersionManager().getLocalList().refreshLocalVersion(selected);
                            return current;
                        }
                        throw new GameEntityNotFound("can't find in complete version: " + selected.getID() + " gameEntity: " + entity.getName());
                    }

                    private GameEntityDTO findGameFromCollection(GameEntityDTO entity, GameType type, ModpackVersionDTO versionDTO) {
                        for (GameEntityDTO dto : versionDTO.getByType(type)) {
                            if (entity.getId().equals(dto.getId())) {
                                return dto;
                            }
                        }
                        return null;
                    }

                    public void addGameListener(GameType type, GameEntityListener listener) {
                        this.gameListeners.get(type).add(listener);
                    }

                    public void removeGameListener(GameType type, GameEntityListener listener) {
                        this.gameListeners.get(type).remove(listener);
                    }

                    public void renameModpack(CompleteVersion version, String newName) {
                        try {
                            CompleteVersion newVersion = this.tLauncher.getVersionManager().getLocalList().renameVersion(version, newName);
                            this.tLauncher.getVersionManager().getLocalList().refreshVersions();
                            for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
                                l.updateVersion(version, newVersion);
                            }
                            version.setID(newName);
                        } catch (IOException e) {
                            U.log(e);
                            Alert.showError(Localizable.get("modpack.rename.exception.title"), Localizable.get("modpack.rename.exception"));
                        }
                    }

                    public void resaveVersion(CompleteVersion completeVersion) {
                        try {
                            TLauncher.getInstance().getVersionManager().getLocalList().refreshLocalVersion(completeVersion);
                            for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
                                l.updateVersion(completeVersion, completeVersion);
                            }
                        } catch (IOException e) {
                            U.log(e);
                            Alert.showError(Localizable.get("modpack.resave.exception.title"), Localizable.get("modpack.resave.exception"));
                        }
                    }

                    public void resaveVersionWithNewForge(CompleteVersion completeVersion) {
                        resaveVersion(completeVersion);
                        for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
                            l.updateVersionStorageAndScene(completeVersion, completeVersion);
                        }
                    }

                    public void checkFolderSubGameEntity(CompleteVersion selectedValue, GameType current) {
                        boolean find;
                        ModpackVersionDTO versionDTO = (ModpackVersionDTO) selectedValue.getModpack().getVersion();
                        Path subFolder = ModpackUtil.getPathByVersion(selectedValue, FileUtil.GameEntityFolder.getPath(current));
                        if (Files.notExists(subFolder, new LinkOption[0])) {
                            return;
                        }
                        switch (current) {
                            case MOD:
                                find = isFind(FileUtils.listFiles(subFolder.toFile(), new String[]{ArchiveStreamFactory.JAR, ArchiveStreamFactory.ZIP}, true), current, versionDTO, subFolder);
                                break;
                            case MAP:
                                find = isFindMap(current, versionDTO, subFolder);
                                break;
                            case RESOURCEPACK:
                            case SHADERPACK:
                                find = isFind(FileUtils.listFiles(subFolder.toFile(), new String[]{ArchiveStreamFactory.ZIP}, true), current, versionDTO, subFolder);
                                break;
                            default:
                                return;
                        }
                        if (find) {
                            resaveVersion(selectedValue);
                        }
                    }

                    private boolean isFindMap(GameType current, ModpackVersionDTO versionDTO, Path subFolder) {
                        String[] strArr;
                        boolean find = false;
                        Set<String> set = new HashSet<>();
                        for (GameEntityDTO d : versionDTO.getByType(current)) {
                            if (((MapMetadataDTO) d.getVersion().getMetadata()).getFolders() != null) {
                                set.addAll(((MapMetadataDTO) d.getVersion().getMetadata()).getFolders());
                            }
                        }
                        String[] array = subFolder.toFile().list(DirectoryFileFilter.DIRECTORY);
                        for (String m : (String[]) Objects.requireNonNull(array)) {
                            if (!set.contains(m)) {
                                try {
                                    GameEntityDTO dto = createHandleGameEntity(subFolder.toFile(), MapDTO.class, new File(subFolder.toFile(), m));
                                    versionDTO.getByType(current).add(dto);
                                } catch (IllegalAccessException | InstantiationException e) {
                                    U.log(e);
                                }
                                find = true;
                            }
                        }
                        return find;
                    }

                    private boolean isFind(Collection<File> list, GameType current, ModpackVersionDTO versionDTO, Path subFolder) {
                        boolean find = false;
                        for (File f : list) {
                            boolean foundFile = false;
                            String name = f.getName();
                            GameEntityDTO gd = null;
                            try {
                                Iterator<? extends GameEntityDTO> it = versionDTO.getByType(current).iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    GameEntityDTO g = it.next();
                                    gd = g;
                                    if (g.getVersion().getMetadata().getPath().endsWith(name)) {
                                        foundFile = true;
                                        break;
                                    }
                                }
                                if (!foundFile) {
                                    try {
                                        GameEntityDTO dto = createHandleGameEntity(subFolder.toFile(), GameType.createDTO(current), f);
                                        versionDTO.getByType(current).add(dto);
                                        find = true;
                                    } catch (Exception e) {
                                        log(e);
                                    }
                                }
                            } catch (NullPointerException n) {
                                U.log("meta is null " + gd.toString());
                                throw n;
                            }
                        }
                        return find;
                    }

                    private void checkShaderStatus(CompleteVersion completeVersion) throws IOException {
                        List<ShaderpackDTO> shaderpacks = ((ModpackVersionDTO) completeVersion.getModpack().getVersion()).getShaderpacks();
                        if (shaderpacks.isEmpty()) {
                            return;
                        }
                        String name = ModpackUtil.readShaderpackConfigField(completeVersion, "shaderPack");
                        Optional<ShaderpackDTO> op = shaderpacks.stream().filter(d -> {
                            return d.getStateGameElement() == StateGameElement.ACTIVE;
                        }).findFirst();
                        if (StringUtils.isEmpty(name) && op.isPresent()) {
                            op.get().setStateGameElement(StateGameElement.NO_ACTIVE);
                        } else if (StringUtils.isNotEmpty(name)) {
                            if (op.isPresent()) {
                                String fileName = FileUtil.getFilename(op.get().getVersion().getMetadata().getPath());
                                if (fileName.equalsIgnoreCase(name)) {
                                    return;
                                }
                            }
                            shaderpacks.stream().filter(d2 -> {
                                return d2.getStateGameElement() == StateGameElement.ACTIVE;
                            }).forEach(d3 -> {
                                d3.setStateGameElement(StateGameElement.NO_ACTIVE);
                                SwingUtilities.invokeLater(()
                                /*  JADX ERROR: Method code generation error
                                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: INVOKE  
                                      (wrap: java.lang.Runnable : 0x0009: INVOKE_CUSTOM (r0v2 java.lang.Runnable A[REMOVE]) = 
                                      (r3v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                      (r4v0 'd3' org.tlauncher.modpack.domain.client.ShaderpackDTO A[D('d' org.tlauncher.modpack.domain.client.ShaderpackDTO), DONT_INLINE])
                                    
                                     handle type: INVOKE_DIRECT
                                     lambda: java.lang.Runnable.run():void
                                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.managers.ModpackManager), (r1 I:org.tlauncher.modpack.domain.client.ShaderpackDTO) type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$null$24(org.tlauncher.modpack.domain.client.ShaderpackDTO):void)
                                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.lambda$checkShaderStatus$25(org.tlauncher.modpack.domain.client.ShaderpackDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                                    	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
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
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                                    	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:156)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:133)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                                    	... 46 more
                                    */
                                /*
                                    this = this;
                                    r0 = r4
                                    org.tlauncher.modpack.domain.client.share.StateGameElement r1 = org.tlauncher.modpack.domain.client.share.StateGameElement.NO_ACTIVE
                                    r0.setStateGameElement(r1)
                                    r0 = r3
                                    r1 = r4
                                    void r0 = () -> { // java.lang.Runnable.run():void
                                        r0.lambda$null$24(r1);
                                    }
                                    javax.swing.SwingUtilities.invokeLater(r0)
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.lambda$checkShaderStatus$25(org.tlauncher.modpack.domain.client.ShaderpackDTO):void");
                            });
                            shaderpacks.stream().filter(d4 -> {
                                return FileUtil.getFilename(d4.getVersion().getMetadata().getPath()).equalsIgnoreCase(name);
                            }).forEach(d5 -> {
                                d5.setStateGameElement(StateGameElement.ACTIVE);
                                SwingUtilities.invokeLater(()
                                /*  JADX ERROR: Method code generation error
                                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: INVOKE  
                                      (wrap: java.lang.Runnable : 0x0009: INVOKE_CUSTOM (r0v2 java.lang.Runnable A[REMOVE]) = 
                                      (r3v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                      (r4v0 'd5' org.tlauncher.modpack.domain.client.ShaderpackDTO A[D('d' org.tlauncher.modpack.domain.client.ShaderpackDTO), DONT_INLINE])
                                    
                                     handle type: INVOKE_DIRECT
                                     lambda: java.lang.Runnable.run():void
                                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.managers.ModpackManager), (r1 I:org.tlauncher.modpack.domain.client.ShaderpackDTO) type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$null$27(org.tlauncher.modpack.domain.client.ShaderpackDTO):void)
                                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.lambda$checkShaderStatus$28(org.tlauncher.modpack.domain.client.ShaderpackDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                                    	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
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
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                                    	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:156)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:133)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                                    	... 46 more
                                    */
                                /*
                                    this = this;
                                    r0 = r4
                                    org.tlauncher.modpack.domain.client.share.StateGameElement r1 = org.tlauncher.modpack.domain.client.share.StateGameElement.ACTIVE
                                    r0.setStateGameElement(r1)
                                    r0 = r3
                                    r1 = r4
                                    void r0 = () -> { // java.lang.Runnable.run():void
                                        r0.lambda$null$27(r1);
                                    }
                                    javax.swing.SwingUtilities.invokeLater(r0)
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.lambda$checkShaderStatus$28(org.tlauncher.modpack.domain.client.ShaderpackDTO):void");
                            });
                        }
                    }

                    public void openModpackFolder(CompleteVersion version) {
                        OS.openFolder(FileUtil.getRelative("versions/" + version.getID()).toFile());
                    }

                    public CompleteVersion getCompleteVersion(String name) throws IOException {
                        try {
                            String value = Http.performGet(this.innerConfiguration.get("modpack.operation.url") + "read/forgeversion?name=" + name);
                            CompleteVersion completeVersion = ((CompleteVersion) this.gson.fromJson(value, (Class<Object>) CompleteVersion.class)).resolve(this.tLauncher.getVersionManager(), true);
                            TlauncherUtil.processRemoteVersionToSave(completeVersion, value, this.gson);
                            return completeVersion;
                        } catch (NullPointerException e) {
                            log("forge version " + name);
                            throw e;
                        }
                    }

                    private boolean fillFromCache(GameType t, GameEntityDTO e, ModpackVersionDTO versionDTO, Path versionFolder) {
                        for (CompleteVersion v : getModpackVersions()) {
                            if (v.getModpack().getVersion() != versionDTO) {
                                for (GameEntityDTO g : ((ModpackVersionDTO) v.getModpack().getVersion()).getByType(t)) {
                                    if (((SubModpackDTO) g).getStateGameElement() != StateGameElement.NO_ACTIVE && !g.isUserInstall() && !e.isUserInstall() && e.getId().equals(g.getId()) && e.getVersion().getId().equals(g.getVersion().getId())) {
                                        Path cachedFolder = ModpackUtil.getPathByVersion(v);
                                        if (!notExistOrCorrect(cachedFolder, e, true)) {
                                            File dest = new File(versionFolder.toFile(), e.getVersion().getMetadata().getPath());
                                            try {
                                                FileUtil.copyFile(new File(cachedFolder.toFile(), e.getVersion().getMetadata().getPath()), dest, true);
                                                return true;
                                            } catch (IOException e1) {
                                                log(e1);
                                                if (dest.exists()) {
                                                    FileUtil.deleteFile(dest);
                                                    return false;
                                                }
                                                return false;
                                            }
                                        }
                                    }
                                }
                                continue;
                            }
                        }
                        return false;
                    }

                    public List<GameEntityDTO> findDependenciesFromGameEntityDTO(GameEntityDTO entityDTO) {
                        GameType[] valuesCustom;
                        List<GameEntityDTO> list = new ArrayList<>();
                        CompleteVersion completeVersion = this.tLauncher.getFrame().mp.modpackScene.getSelectedCompleteVersion();
                        if (completeVersion != null && !entityDTO.isUserInstall() && ((SubModpackDTO) entityDTO).getStateGameElement() != StateGameElement.NO_ACTIVE) {
                            for (GameType t : GameType.valuesCustom()) {
                                for (GameEntityDTO g : ((ModpackVersionDTO) completeVersion.getModpack().getVersion()).getByType(t)) {
                                    if (g.getDependencies() != null && ((SubModpackDTO) g).getStateGameElement() != StateGameElement.NO_ACTIVE) {
                                        for (GameEntityDependencyDTO d : g.getDependencies()) {
                                            if (d.getDependencyType() == DependencyType.REQUIRED && entityDTO.getId().equals(d.getGameEntityId())) {
                                                list.add(g);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return list;
                    }

                    public void resetInfoMod() {
                        this.infoMod = null;
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftPrepare() {
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftAbort() {
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftLaunch() {
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftClose() {
                        try {
                            ModpackScene scene = this.tLauncher.getFrame().mp.modpackScene;
                            if (scene.isSelectedCompleteVersion()) {
                                checkShaderStatus(scene.getSelectedCompleteVersion());
                            }
                        } catch (IOException io) {
                            U.log(io);
                        }
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftError(Throwable e) {
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftKnownError(MinecraftException e) {
                    }

                    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
                    public void onMinecraftCrash(Crash crash) {
                    }

                    public void openModpackElement(Long id, GameType type) {
                        loadInfo();
                        SwingUtilities.invokeLater(()
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000c: INVOKE  
                              (wrap: java.lang.Runnable : 0x0007: INVOKE_CUSTOM (r0v2 java.lang.Runnable A[REMOVE]) = 
                              (r4v0 'this' org.tlauncher.tlauncher.managers.ModpackManager A[D('this' org.tlauncher.tlauncher.managers.ModpackManager), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                              (r5v0 'id' java.lang.Long A[D('id' java.lang.Long), DONT_INLINE])
                              (r6v0 'type' org.tlauncher.modpack.domain.client.share.GameType A[D('type' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
                            
                             handle type: INVOKE_DIRECT
                             lambda: java.lang.Runnable.run():void
                             call insn: ?: INVOKE  
                              (r0 I:org.tlauncher.tlauncher.managers.ModpackManager)
                              (r1 I:java.lang.Long)
                              (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                             type: DIRECT call: org.tlauncher.tlauncher.managers.ModpackManager.lambda$openModpackElement$29(java.lang.Long, org.tlauncher.modpack.domain.client.share.GameType):void)
                             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.managers.ModpackManager.openModpackElement(java.lang.Long, org.tlauncher.modpack.domain.client.share.GameType):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ModpackManager.class
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
                            r0.loadInfo()
                            r0 = r4
                            r1 = r5
                            r2 = r6
                            void r0 = () -> { // java.lang.Runnable.run():void
                                r0.lambda$openModpackElement$29(r1, r2);
                            }
                            javax.swing.SwingUtilities.invokeLater(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.managers.ModpackManager.openModpackElement(java.lang.Long, org.tlauncher.modpack.domain.client.share.GameType):void");
                    }

                    public List<GameVersionDTO> getGameVersionsRemote(NameIdDTO nameIdDTO) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("minecraftVersionType", nameIdDTO.getId());
                        return (List) this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "gameversions", map), new TypeToken<List<GameVersionDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.5
                        }.getType());
                    }

                    public List<CategoryDTO> getCategories(GameType type) throws IOException {
                        return (List) this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "categories/" + type.toString(), new TypeToken<List<CategoryDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.6
                        }.getType());
                    }

                    public CategoryDTO[] getLocalCategories(GameType type) {
                        if (Objects.isNull(this.map.get(type))) {
                            return new CategoryDTO[0];
                        }
                        return (CategoryDTO[]) this.map.get(type).toArray(new CategoryDTO[0]);
                    }

                    public List<NameIdDTO> getVersionsByGameVersionAndMinecraftVersionType(Long id, NameIdDTO minecraftVersionType) throws IOException {
                        return (List) this.gsonService.getObjectWithoutSaving(String.format("%s%s%s%s%s", this.modpackApiURL, "gameversions/", id, "/minecraftversiontypes/", minecraftVersionType.getId()), new TypeToken<List<NameIdDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.7
                        }.getType());
                    }

                    public GameVersionDTO getGameVersionByName(String name) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put(Action.NAME_ATTRIBUTE, name);
                        return (GameVersionDTO) this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "gameversions/search/findbyname", map), (Class<Object>) GameVersionDTO.class);
                    }

                    public MinecraftVersionDTO getCompleteVersion(NameIdDTO type, NameIdDTO version) throws IOException {
                        return (MinecraftVersionDTO) this.gsonService.getObjectWithoutSaving(String.format("%s%s%s%s%s", this.modpackApiURL, "minecraftversiontypes/", type.getId(), "/baseversion/", version.getId()), (Class<Object>) MinecraftVersionDTO.class);
                    }

                    public CommonPage<GameEntityDTO> getGameEntities(GameType type, NameIdDTO minecraftVesionType, GameVersionDTO gameVersion, String search, Set<CategoryDTO> categories, Integer page, GameEntitySort sort, boolean favorite) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        if (Objects.nonNull(minecraftVesionType)) {
                            map.put("minecraftVesionType", minecraftVesionType.getId());
                        }
                        if (Objects.nonNull(gameVersion)) {
                            map.put("gameversion", gameVersion.getId());
                        }
                        if (Objects.nonNull(search)) {
                            map.put("search", search);
                        }
                        if (Objects.nonNull(categories)) {
                            map.put("category", categories.stream().map(e -> {
                                return e.getId().toString();
                            }).collect(Collectors.joining(",")));
                        }
                        if (Objects.nonNull(page)) {
                            map.put("page", page);
                        }
                        map.put("lang", this.tLauncher.getConfiguration().getLocale().toString().toUpperCase());
                        map.put("sort", sort);
                        if (favorite) {
                            try {
                                map.put("uuid", this.tLauncher.getProfileManager().findUniqueTlauncherAccount().getUUID().toString());
                                map.put("favorite", "true");
                            } catch (RequiredTLAccountException | SelectedAnyOneTLAccountException e1) {
                                log("some problem with favorites", e1);
                            }
                        }
                        return (CommonPage) this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "client/gameentities", map), new TypeToken<CommonPage<GameEntityDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.8
                        }.getType());
                    }

                    public GameEntityDTO getGameEntity(GameType type, Long id) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("lang", this.tLauncher.getConfiguration().getLocale().toString().toUpperCase());
                        return (GameEntityDTO) this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "client/gameentities/" + id, map), (Class<Object>) GameType.createDTO(type));
                    }

                    public GameEntityDTO getInstallingGameEntity(GameType type, GameEntityDTO gameEntity, VersionDTO version, GameVersionDTO gameVersion, NameIdDTO minecraftVersionType) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        if (Objects.nonNull(gameVersion)) {
                            map.put("gameversion", gameVersion.getId());
                        }
                        if (Objects.nonNull(minecraftVersionType)) {
                            map.put("minecraftVersionType", minecraftVersionType.getId());
                        }
                        if (Objects.nonNull(version)) {
                            map.put(ClientCookie.VERSION_ATTR, version.getId());
                        }
                        String uri = Http.get(String.format("%sclient/gameentities/%s/installing", this.modpackApiURL, gameEntity.getId()), map);
                        return (GameEntityDTO) this.gsonService.getObjectWithoutSaving(uri, (Class<Object>) GameType.createDTO(type));
                    }

                    public ModpackVersionDTO getInstallingModpackVersionDTO(GameEntityDTO gameEntity, VersionDTO version) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        if (Objects.nonNull(version)) {
                            map.put(ClientCookie.VERSION_ATTR, version.getId());
                        }
                        String uri = Http.get(String.format("%sclient/modpacks/%s/installing", this.modpackApiURL, gameEntity.getId()), map);
                        return (ModpackVersionDTO) this.gsonService.getObjectWithoutSaving(uri, (Class<Object>) ModpackVersionDTO.class);
                    }

                    public Repo getGameEntitiesPictures(GameType type, Long gameEntity, PictureType pictureType) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("picturetype", pictureType);
                        String uri = Http.get(String.format("%sclient/gameentities/%s/pictures", this.modpackApiURL, gameEntity), map);
                        return (Repo) this.gsonService.getObjectWithoutSaving(uri, (Class<Object>) Repo.class);
                    }

                    public CommonPage<VersionDTO> getGameEntityVersions(GameType type, Long gameEntity, Integer page) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("page", page);
                        String uri = Http.get(String.format("%sclient/gameentities/%s/versions", this.modpackApiURL, gameEntity), map);
                        return (CommonPage) this.gsonService.getObjectWithoutSaving(uri, new TypeToken<CommonPage<VersionDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.9
                        }.getType());
                    }

                    public List<GameEntityDTO> getModpackVersionSubElements(GameType type, Long gameEntity) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        String uri = Http.get(String.format("%sclient/modpacks/%s/versions/subelements", this.modpackApiURL, gameEntity), map);
                        return (List) this.gsonService.getObjectWithoutSaving(uri, new TypeToken<List<GameEntityDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.10
                        }.getType());
                    }

                    public void updateOldGameEntity(GameType type, Long gameEntity, String sourceURI) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("url", sourceURI);
                        HttpPut put = new HttpPut(Http.get(String.format("%sclient/gameentities/old/%s", this.modpackApiURL, gameEntity), map));
                        put.setConfig(this.requestConfig);
                        HttpResponse r = this.client.execute((HttpUriRequest) put);
                        HttpEntity entity = r.getEntity();
                        if (r.getStatusLine().getStatusCode() > 400) {
                            EntityUtils.consume(entity);
                            throw new IOException(String.valueOf(r.getStatusLine()));
                        }
                    }

                    public GameVersionDTO getGameVersion(ModpackVersionDTO mv) throws IOException {
                        if (Objects.isNull(mv.getGameVersionDTO())) {
                            mv.setGameVersionDTO(getGameVersionByName(mv.getGameVersion()));
                        }
                        if (Objects.isNull(mv.getMinecraftVersionTypes())) {
                            NameIdDTO mvt = new NameIdDTO();
                            mvt.setId(1L);
                            mvt.setName("forge");
                            mv.setMinecraftVersionTypes(Lists.newArrayList(new NameIdDTO[]{mvt}));
                        }
                        if (Objects.isNull(mv.getMinecraftVersionName()) && Objects.isNull(mv.getForgeVersionDTO())) {
                            new NameIdDTO().setId(1L);
                            List<NameIdDTO> fv = getVersionsByGameVersionAndMinecraftVersionType(mv.getGameVersionDTO().getId(), mv.findFirstMinecraftVersionType());
                            Optional<NameIdDTO> op = fv.stream().filter(e -> {
                                return e.getName().equals(mv.getForgeVersion());
                            }).findFirst();
                            if (op.isPresent()) {
                                NameIdDTO nid = op.get();
                                mv.setMinecraftVersionName(nid);
                            }
                        } else if (Objects.isNull(mv.getMinecraftVersionName()) && Objects.nonNull(mv.getForgeVersionDTO())) {
                            ForgeVersionDTO fvdto = mv.getForgeVersionDTO();
                            NameIdDTO ver = new NameIdDTO();
                            ver.setId(fvdto.getId());
                            ver.setName(fvdto.getName());
                            mv.setMinecraftVersionName(ver);
                        }
                        return mv.getGameVersionDTO();
                    }

                    public GameEntityDTO getGameEntityDescriptions(GameType type, Long id) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("lang", "en");
                        return (GameEntityDTO) this.gsonService.getObjectWithoutSaving(Http.get(String.format("%sclient/gameentities/%s/descriptions", this.modpackApiURL, id), map), (Class<Object>) GameType.createDTO(type));
                    }

                    public GameEntityDTO getGameEntityWithParserField(Long id, GameType type) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        return (GameEntityDTO) this.gsonService.getObjectWithoutSaving(Http.get(String.format("%sclient/gameentities/%s/parser/status", this.modpackApiURL, id), map), (Class<Object>) GameType.createDTO(type));
                    }

                    public ParsedElementDTO labelForParsingGameEntity(Long id, GameType type) throws IOException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        HttpPatch patch = new HttpPatch(Http.get(String.format("%sclient/gameentities/%s/parser", this.modpackApiURL, id), map));
                        HttpResponse hr = null;
                        try {
                            HttpResponse hr2 = this.closeableHttpClient.execute((HttpUriRequest) patch);
                            if (hr2.getStatusLine().getStatusCode() != 200) {
                                U.log("not proper response " + hr2.getStatusLine().toString());
                                throw new IOException("not proper response " + hr2.getStatusLine());
                            }
                            ParsedElementDTO parsedElementDTO = (ParsedElementDTO) this.gson.fromJson(IOUtils.toString(hr2.getEntity().getContent(), StandardCharsets.UTF_8), (Class<Object>) ParsedElementDTO.class);
                            if (Objects.nonNull(hr2)) {
                                EntityUtils.consumeQuietly(hr2.getEntity());
                            }
                            return parsedElementDTO;
                        } catch (Throwable th) {
                            if (Objects.nonNull(null)) {
                                EntityUtils.consumeQuietly(hr.getEntity());
                            }
                            throw th;
                        }
                    }

                    public List<NameIdDTO> getMinecraftVersionTypesRemote() throws IOException {
                        return (List) this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "minecraftversiontypes", new TypeToken<List<NameIdDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.11
                        }.getType());
                    }

                    public String sendRequest(HttpRequestBase http, Object ob, String urn, Map<String, Object> map) throws IOException, ClientProtocolException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
                        if (Objects.nonNull(map)) {
                            urn = Http.get(urn, map);
                        }
                        http.setURI(U.makeURI(urn));
                        if (Objects.nonNull(ob)) {
                            ((HttpEntityEnclosingRequestBase) http).setEntity(new StringEntity(this.gson.toJson(ob), ContentType.APPLICATION_JSON));
                        }
                        http.setConfig(this.requestConfig);
                        TlauncherUtil.addAuthHeaders(http);
                        HttpResponse hr = null;
                        try {
                            HttpResponse hr2 = this.closeableHttpClient.execute((HttpUriRequest) http);
                            if (hr2.getStatusLine().getStatusCode() >= 300) {
                                throw new IOException("not proper code " + hr2.getStatusLine().toString());
                            }
                            String iOUtils = IOUtils.toString(hr2.getEntity().getContent(), StandardCharsets.UTF_8);
                            if (Objects.nonNull(hr2)) {
                                http.abort();
                                EntityUtils.consumeQuietly(hr2.getEntity());
                            }
                            return iOUtils;
                        } catch (Throwable th) {
                            if (Objects.nonNull(null)) {
                                http.abort();
                                EntityUtils.consumeQuietly(hr.getEntity());
                            }
                            throw th;
                        }
                    }

                    public void addFavoriteGameEntities(GameEntityDTO dto, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
                        Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        sendRequest(new HttpPost(), null, String.format("%suser/client/gameentities/%s/favorite/", this.modpackApiURL, dto.getId()), map);
                        Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
                        if (Objects.isNull(set)) {
                            set = Collections.synchronizedSet(new HashSet());
                            this.favoriteGameEntityIds.put(ac.getUUID(), set);
                        }
                        set.add(dto.getId());
                    }

                    public void deleteFavoriteGameEntities(GameEntityDTO dto, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
                        Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        sendRequest(new HttpDelete(), null, String.format("%suser/client/gameentities/%s/favorite/", this.modpackApiURL, dto.getId()), map);
                        Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
                        if (Objects.nonNull(set)) {
                            set.remove(dto.getId());
                        }
                    }

                    public void getFavoriteGameEntities() {
                        log("try to update favorite");
                        HttpGet request = new HttpGet(String.format("%suser/client/gameentities/favorite", this.modpackApiURL));
                        HttpResponse hr = null;
                        try {
                            try {
                                try {
                                    Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
                                    if (this.favoriteGameEntityIds.containsKey(ac.getUUID())) {
                                        if (Objects.nonNull(null)) {
                                            request.abort();
                                            EntityUtils.consumeQuietly(hr.getEntity());
                                            return;
                                        }
                                        return;
                                    }
                                    log("updated favorites for account " + ac.getDisplayName());
                                    request.setConfig(this.requestConfig);
                                    TlauncherUtil.addAuthHeaders(request);
                                    HttpResponse hr2 = this.closeableHttpClient.execute((HttpUriRequest) request);
                                    if (hr2.getStatusLine().getStatusCode() != 200) {
                                        U.log("not proper response " + hr2.getStatusLine().toString());
                                    } else {
                                        List<NameIdDTO> list = (List) this.gson.fromJson(new InputStreamReader(hr2.getEntity().getContent()), new TypeToken<List<NameIdDTO>>() { // from class: org.tlauncher.tlauncher.managers.ModpackManager.12
                                        }.getType());
                                        this.favoriteGameEntityIds.put(ac.getUUID(), Collections.synchronizedSet((Set) list.stream().map(e -> {
                                            return e.getId();
                                        }).collect(Collectors.toSet())));
                                    }
                                    if (Objects.nonNull(hr2)) {
                                        request.abort();
                                        EntityUtils.consumeQuietly(hr2.getEntity());
                                    }
                                } catch (RequiredTLAccountException | SelectedAnyOneTLAccountException e2) {
                                    log("couldn't get favorites for current accounts");
                                    if (Objects.nonNull(null)) {
                                        request.abort();
                                        EntityUtils.consumeQuietly(hr.getEntity());
                                    }
                                }
                            } catch (Exception e3) {
                                log(e3);
                                if (Objects.nonNull(null)) {
                                    request.abort();
                                    EntityUtils.consumeQuietly(hr.getEntity());
                                }
                            }
                        } catch (Throwable th) {
                            if (Objects.nonNull(null)) {
                                request.abort();
                                EntityUtils.consumeQuietly(hr.getEntity());
                            }
                            throw th;
                        }
                    }

                    public void importUserGameEntities(List<GameEntityDTO> list) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
                        readStatusGameElement();
                        if (!this.statusModpackElement.isEmpty()) {
                            sendRequest(new HttpPost(), list, String.format("%suser/client/export/gameentities/favorite/", this.modpackApiURL), null);
                            int size = this.statusModpackElement.size();
                            this.statusModpackElement.clear();
                            writeStatusGameElement();
                            log("exported favorite game entities successfully " + size);
                            this.favoriteGameEntityIds.clear();
                        }
                    }

                    public Set<Long> getFavoriteGameEntitiesByAccount() {
                        try {
                            Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
                            Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
                            if (Objects.nonNull(set)) {
                                return set;
                            }
                        } catch (Throwable th) {
                        }
                        return new HashSet();
                    }

                    public void itemStateChanged(ItemEvent e) {
                        if (1 == e.getStateChange()) {
                            CompletableFuture.runAsync(() -> {
                                PseudoScene s = this.tLauncher.getFrame().mp.getScene();
                                if (s instanceof UpdateFavoriteValueListener) {
                                    getFavoriteGameEntities();
                                    SwingUtilities.invokeLater(() -> {
                                        ((UpdateFavoriteValueListener) s).updateFavoriteValue();
                                    });
                                }
                            });
                        }
                    }
                }
