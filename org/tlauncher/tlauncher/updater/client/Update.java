package org.tlauncher.tlauncher.updater.client;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.launcher.process.JavaProcessLauncher;
import org.apache.commons.io.FileUtils;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Update.class */
public class Update {
    private double version;
    private double requiredAtLeastFor;
    protected boolean mandatory;
    private AtomicBoolean stateDownloading;
    private int updaterView;
    private boolean updaterLaterInstall;
    private List<Offer> offers;
    private Offer selectedOffer;
    private int offerDelay;
    private int offerEmptyCheckboxDelay;
    private Map<String, List<Banner>> banners;
    private List<String> rootAccessExe;
    private Double aboveMandatoryVersion;
    @Expose(serialize = false, deserialize = false)
    private Downloadable download;
    @Expose(serialize = false, deserialize = false)
    private final List<UpdateListener> listeners = Collections.synchronizedList(new ArrayList());
    private List<String> jarLinks = new ArrayList();
    private final List<String> triedToDownload = new ArrayList();
    private List<String> exeLinks = new ArrayList();
    protected Map<String, String> description = new HashMap();
    private Set<Double> mandatoryUpdatedVersions = new HashSet();
    @Expose(serialize = false, deserialize = false)
    private boolean userChoose = true;
    @Expose(serialize = false, deserialize = false)
    private boolean freeSpaceEnough = true;
    @Expose(serialize = false, deserialize = false)
    protected State state = State.NONE;
    @Expose(serialize = false, deserialize = false)
    protected Downloader downloader = getDefaultDownloader();

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Update$State.class */
    public enum State {
        NONE,
        DOWNLOADING,
        READY,
        APPLYING,
        ERRORED
    }

    public Offer getSelectedOffer() {
        return this.selectedOffer;
    }

    public void setSelectedOffer(Offer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Update() {
    }

    public Optional<Offer> getOfferByLang(String lang) {
        return this.offers.stream().filter(e -> {
            return e.getTopText().containsKey(lang);
        }).findAny();
    }

    public Update(double version, Map<String, String> description, List<String> jarList, List<String> exeList) {
        this.version = version;
        if (description != null) {
            this.description.putAll(description);
        }
        if (exeList != null) {
            this.exeLinks.addAll(exeList);
        }
        if (jarList != null) {
            this.jarLinks.addAll(jarList);
        }
    }

    public double getVersion() {
        return this.version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public double getRequiredAtLeastFor() {
        return this.requiredAtLeastFor;
    }

    public void setRequiredAtLeastFor(double version) {
        this.requiredAtLeastFor = version;
    }

    public Map<String, String> getDescriptionMap() {
        return this.description;
    }

    public State getState() {
        return this.state;
    }

    protected void setState(State newState) {
        if (newState.ordinal() <= this.state.ordinal() && this.state.ordinal() != State.values().length - 1) {
            throw new IllegalStateException("tried to change from " + this.state + " to " + newState);
        }
        this.state = newState;
        log("Set state:", newState);
    }

    public Downloader getDownloader() {
        return this.downloader;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public boolean isApplicable() {
        return this.freeSpaceEnough && this.userChoose && TLauncher.getVersion() < this.version;
    }

    public boolean isRequired() {
        return this.userChoose && this.requiredAtLeastFor != 0.0d && TLauncher.getVersion() <= this.requiredAtLeastFor;
    }

    public String getDescription(String key) {
        if (this.description == null) {
            return null;
        }
        return this.description.get(key);
    }

    public String getDescription() {
        return getDescription(TLauncher.getInstance().getConfiguration().getLocale().toString());
    }

    private void download0(PackageType packageType, boolean async) {
        setState(State.DOWNLOADING);
        File destination = new File(FileUtil.getRunningJar().getAbsolutePath() + ".update");
        destination.deleteOnExit();
        log("dest", destination);
        onUpdateDownloading();
        individualUpdate(packageType, async, destination);
    }

    private void individualUpdate(PackageType packageType, boolean async, File destination) {
        String pathServer = getLink(packageType);
        MetadataDTO metadataDTO = new MetadataDTO();
        metadataDTO.setUrl(pathServer);
        metadataDTO.setLocalDestination(destination);
        log("url:", pathServer);
        this.download = new Downloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO);
        if (this.triedToDownload.size() == calculateListSize(packageType)) {
            this.download.addHandler(new DownloadableHandlerAdapter() { // from class: org.tlauncher.tlauncher.updater.client.Update.1
                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onAbort(Downloadable d) {
                    Update.this.onUpdateDownloadError(d.getError());
                }

                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onComplete(Downloadable d) {
                    Update.this.onUpdateReady();
                }

                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onError(Downloadable d, Throwable e) {
                    Update.this.onUpdateDownloadError(e);
                }
            });
        } else {
            this.download.addHandler(new DownloadableHandlerAdapter() { // from class: org.tlauncher.tlauncher.updater.client.Update.2
                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onAbort(Downloadable d) {
                    Update.this.onUpdateDownloadError(d.getError());
                }

                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onComplete(Downloadable d) {
                    Update.this.onUpdateReady();
                }

                @Override // org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableHandler
                public void onError(Downloadable d, Throwable e) {
                    Update.this.log(e);
                    Update.this.stateDownloading.set(false);
                }
            });
        }
        this.downloader.add(this.download);
        this.stateDownloading = new AtomicBoolean(true);
        if (async) {
            this.downloader.startDownload();
        } else {
            this.downloader.startDownloadAndWait();
        }
        if (!this.stateDownloading.get() && this.triedToDownload.size() != calculateListSize(packageType)) {
            individualUpdate(packageType, async, destination);
        }
    }

    private String getLink(PackageType packageType) {
        switch (packageType) {
            case EXE:
                return findLink(this.exeLinks);
            case JAR:
                return findLink(this.jarLinks);
            default:
                throw new NullPointerException("incorrect PackageType");
        }
    }

    private int calculateListSize(PackageType packageType) {
        switch (packageType) {
            case EXE:
                return this.exeLinks.size();
            case JAR:
                return this.jarLinks.size();
            default:
                throw new NullPointerException("incorrect PackageType");
        }
    }

    private String findLink(List<String> list) {
        String link;
        Random r = new Random();
        do {
            link = list.get(r.nextInt(list.size()));
        } while (this.triedToDownload.contains(link));
        this.triedToDownload.add(link);
        return link;
    }

    public void download(PackageType type, boolean async) {
        try {
            download0(type, async);
        } catch (Throwable t) {
            onUpdateError(t);
        }
    }

    public void download(boolean async) {
        download(PackageType.CURRENT, async);
    }

    public void download() {
        download(false);
    }

    public void asyncDownload() {
        download(true);
    }

    private void apply0() throws Throwable {
        setState(State.APPLYING);
        JavaProcessLauncher javaProcessLauncher = Bootstrapper.restartLauncher();
        File replace = FileUtil.getRunningJar();
        File replaceWith = this.download.getMetadataDTO().getLocalDestination();
        ProcessBuilder builder = javaProcessLauncher.createProcess();
        onUpdateApplying();
        InputStream in = new FileInputStream(replaceWith);
        OutputStream out = new FileOutputStream(replace);
        byte[] buffer = new byte[2048];
        int read = in.read(buffer);
        while (true) {
            int read2 = read;
            if (read2 > 0) {
                out.write(buffer, 0, read2);
                read = in.read(buffer);
            } else {
                try {
                    break;
                } catch (IOException e) {
                }
            }
        }
        in.close();
        try {
            out.close();
        } catch (IOException e2) {
        }
        try {
            builder.start();
        } catch (Throwable t) {
            log(t);
        }
        TLauncher.kill();
    }

    public void apply() {
        try {
            File file = FileUtil.getRunningJar();
            FileUtils.copyFile(file, new File(file.getParentFile(), "Old-" + file.getName()));
            TlauncherUtil.clearTimeLabel();
            apply0();
        } catch (Throwable t) {
            onUpdateApplyError(t);
        }
    }

    public void addListener(UpdateListener l) {
        this.listeners.add(l);
    }

    public void removeListener(UpdateListener l) {
        this.listeners.remove(l);
    }

    private void onUpdateError(Throwable e) {
        setState(State.ERRORED);
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateError(this, e);
            }
        }
    }

    private void onUpdateDownloading() {
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateDownloading(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateDownloadError(Throwable e) {
        setState(State.ERRORED);
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateDownloadError(this, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateReady() {
        setState(State.READY);
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateReady(this);
            }
        }
    }

    private void onUpdateApplying() {
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateApplying(this);
            }
        }
    }

    private void onUpdateApplyError(Throwable e) {
        setState(State.ERRORED);
        synchronized (this.listeners) {
            for (UpdateListener l : this.listeners) {
                l.onUpdateApplyError(this, e);
            }
        }
    }

    private Downloader getDefaultDownloader() {
        return TLauncher.getInstance().getDownloader();
    }

    protected void log(Object... o) {
        U.log("[Update:" + this.version + "]", o);
    }

    public List<String> getJarLinks() {
        return this.jarLinks;
    }

    public boolean isMandatory() {
        if (this.mandatory) {
            return true;
        }
        Double v1 = Double.valueOf(TLauncher.getVersion());
        if (!Objects.nonNull(this.aboveMandatoryVersion) || this.aboveMandatoryVersion.compareTo(v1) <= 0) {
            return Objects.nonNull(this.mandatoryUpdatedVersions) && this.mandatoryUpdatedVersions.contains(v1);
        }
        return true;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isUserChoose() {
        return this.userChoose;
    }

    public void setUserChoose(boolean userChoose) {
        this.userChoose = userChoose;
    }

    public String getlastDownloadedLink() {
        if (this.triedToDownload.size() > 0) {
            return this.triedToDownload.get(this.triedToDownload.size() - 1);
        }
        return CoreConstants.EMPTY_STRING;
    }

    public boolean isFreeSpaceEnough() {
        return this.freeSpaceEnough;
    }

    public void setFreeSpaceEnough(boolean freeSpaceEnough) {
        this.freeSpaceEnough = freeSpaceEnough;
    }

    public void setJarLinks(List<String> jarLinks) {
        this.jarLinks = jarLinks;
    }

    public List<String> getExeLinks() {
        return this.exeLinks;
    }

    public void setExeLinks(List<String> exeLinks) {
        this.exeLinks = exeLinks;
    }

    public List<Offer> getOffers() {
        return this.offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Map<String, List<Banner>> getBanners() {
        return this.banners;
    }

    public int getUpdaterView() {
        return this.updaterView;
    }

    public void setBanners(Map<String, List<Banner>> banners) {
        this.banners = banners;
    }

    public void setUpdaterView(int updaterView) {
        this.updaterView = updaterView;
    }

    public boolean isUpdaterLaterInstall() {
        return this.updaterLaterInstall;
    }

    public void setUpdaterLaterInstall(boolean updaterLaterInstall) {
        this.updaterLaterInstall = updaterLaterInstall;
    }

    public int getOfferDelay() {
        return this.offerDelay;
    }

    public void setOfferDelay(int offerDelay) {
        this.offerDelay = offerDelay;
    }

    public int getOfferEmptyCheckboxDelay() {
        return this.offerEmptyCheckboxDelay;
    }

    public void setOfferEmptyCheckboxDelay(int offerEmptyCheckboxDelay) {
        this.offerEmptyCheckboxDelay = offerEmptyCheckboxDelay;
    }

    public List<String> getRootAccessExe() {
        return this.rootAccessExe;
    }

    public void setRootAccessExe(List<String> rootAccessExe) {
        this.rootAccessExe = rootAccessExe;
    }

    public Double getAboveMandatoryVersion() {
        return this.aboveMandatoryVersion;
    }

    public void setAboveMandatoryVersion(Double aboveMandatoryVersion) {
        this.aboveMandatoryVersion = aboveMandatoryVersion;
    }

    public Set<Double> getMandatoryUpdatedVersions() {
        return this.mandatoryUpdatedVersions;
    }

    public void setMandatoryUpdatedVersions(Set<Double> mandatoryUpdatedVersions) {
        this.mandatoryUpdatedVersions = mandatoryUpdatedVersions;
    }
}
