package org.tlauncher.tlauncher.managers;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.launcher.updater.AssetIndex;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.component.ComponentDependence;
import org.tlauncher.tlauncher.component.LauncherComponent;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;

@ComponentDependence({VersionManager.class, VersionLists.class})
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/AssetsManager.class */
public class AssetsManager extends LauncherComponent {
    private final Gson gson;
    private final Object assetsFlushLock;

    public AssetsManager(ComponentManager manager) throws Exception {
        super(manager);
        this.gson = TLauncher.getGson();
        this.assetsFlushLock = new Object();
    }

    public DownloadableContainer downloadResources(CompleteVersion version, List<AssetIndex.AssetObject> list, boolean force) {
        File baseDirectory = this.manager.getLauncher().getVersionManager().getLocalList().getBaseDirectory();
        DownloadableContainer container = new DownloadableContainer();
        container.addAll(getResourceFiles(version, baseDirectory, list));
        return container;
    }

    private static Set<Downloadable> getResourceFiles(CompleteVersion version, File baseDirectory, List<AssetIndex.AssetObject> list) {
        Set<Downloadable> result = new HashSet<>();
        File objectsFolder = new File(baseDirectory, "assets/objects");
        for (AssetIndex.AssetObject object : list) {
            String filename = object.getFilename();
            MetadataDTO metadataDTO = new MetadataDTO();
            metadataDTO.setUrl(filename);
            metadataDTO.setPath(filename);
            metadataDTO.setSha1(object.getHash());
            metadataDTO.setSize(object.getSize());
            metadataDTO.setLocalDestination(new File(objectsFolder, filename));
            Downloadable d = new Downloadable(ClientInstanceRepo.ASSETS_REPO, metadataDTO, false, true);
            result.add(d);
        }
        return result;
    }

    private List<AssetIndex.AssetObject> getResourceFiles(CompleteVersion version, File baseDirectory, boolean local) {
        List<AssetIndex.AssetObject> list = null;
        if (!local) {
            try {
                list = getRemoteResourceFilesList(version, baseDirectory, true);
            } catch (Exception e) {
                log("Cannot get remote assets list. Trying to use the local one.", e);
            }
        }
        if (list == null) {
            list = getLocalResourceFilesList(version, baseDirectory);
        }
        if (list == null) {
            try {
                list = getRemoteResourceFilesList(version, baseDirectory, true);
            } catch (Exception e2) {
                log("Gave up trying to get assets list.", e2);
            }
        }
        return list;
    }

    private List<AssetIndex.AssetObject> getLocalResourceFilesList(CompleteVersion version, File baseDirectory) {
        String indexName = version.getAssets();
        File indexesFolder = new File(baseDirectory, "assets/indexes/");
        File indexFile = new File(indexesFolder, indexName + ".json");
        if (Objects.nonNull(version.getAssetIndex())) {
            long size = version.getAssetIndex().getSize();
            if (size != 0 && version.getID().equals("1.14") && indexFile.length() != size) {
                log("not new assets index file");
                return null;
            }
        }
        log("Reading indexes from file", indexFile);
        try {
            String json = FileUtil.readFile(indexFile);
            AssetIndex index = null;
            try {
                index = (AssetIndex) this.gson.fromJson(json, (Class<Object>) AssetIndex.class);
            } catch (JsonSyntaxException e) {
                log("JSON file is invalid", e);
            }
            if (index == null) {
                log("Cannot read data from JSON file.");
                return null;
            }
            return Lists.newArrayList(index.getUniqueObjects());
        } catch (Exception e2) {
            log("Cannot read local resource files list for index:", indexName, e2);
            return null;
        }
    }

    private List<AssetIndex.AssetObject> getRemoteResourceFilesList(CompleteVersion version, File baseDirectory, boolean save) throws IOException {
        String json;
        String indexName = version.getAssets();
        if (indexName == null) {
            indexName = AssetIndex.DEFAULT_ASSET_NAME;
        }
        File assets = new File(baseDirectory, "assets");
        File indexesFolder = new File(assets, "indexes");
        File indexFile = new File(indexesFolder, indexName + ".json");
        log("Reading from repository...");
        if (version.getAssetIndex() != null) {
            json = ClientInstanceRepo.EMPTY_REPO.getUrl(version.getAssetIndex().getUrl());
        } else {
            json = ClientInstanceRepo.OFFICIAL_VERSION_REPO.getUrl("indexes/" + indexName + ".json");
        }
        if (save) {
            synchronized (this.assetsFlushLock) {
                FileUtil.writeFile(indexFile, json);
            }
        }
        return Lists.newArrayList(((AssetIndex) this.gson.fromJson(json, (Class<Object>) AssetIndex.class)).getUniqueObjects());
    }

    private List<AssetIndex.AssetObject> checkResources(CompleteVersion version, File baseDirectory, boolean fast) {
        log("Checking resources...");
        List<AssetIndex.AssetObject> r = new ArrayList<>();
        List<AssetIndex.AssetObject> list = getResourceFiles(version, baseDirectory, fast);
        if (list == null) {
            log("Cannot get assets list. Aborting.");
            return r;
        }
        log("Fast comparing:", Boolean.valueOf(fast));
        for (AssetIndex.AssetObject resource : list) {
            if (!checkResource(baseDirectory, resource, fast)) {
                r.add(resource);
            }
        }
        return r;
    }

    public List<AssetIndex.AssetObject> checkResources(CompleteVersion version, boolean fast) {
        return checkResources(version, ((VersionLists) this.manager.getComponent(VersionLists.class)).getLocal().getBaseDirectory(), fast);
    }

    private static boolean checkResource(File baseDirectory, AssetIndex.AssetObject local, boolean fast) {
        String path = local.getFilename();
        File file = new File(baseDirectory, "assets/objects/" + path);
        long size = file.length();
        if (!file.isFile() || size == 0) {
            return false;
        }
        if (fast || local.getHash() == null) {
            return true;
        }
        return local.getHash().equals(FileUtil.getChecksum(file));
    }
}
