package org.tlauncher.tlauncher.updater.bootstrapper;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.RetryDownloadException;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;
import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement;
import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaDownloadedElement;
import org.tlauncher.tlauncher.updater.bootstrapper.model.LibraryConfig;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/PreparedEnvironmentComponentImpl.class */
public class PreparedEnvironmentComponentImpl implements PreparedEnvironmentComponent {
    private final LibraryConfig config;
    private final JavaConfig javaConfig;
    private final File workFolder;
    private final File jvmsFolder;
    private Downloader downloader;

    public PreparedEnvironmentComponentImpl(LibraryConfig config, JavaConfig javaConfig, File workFolder, File jvmsFolder, Downloader downloader) {
        this.config = config;
        this.javaConfig = javaConfig;
        this.workFolder = workFolder;
        this.jvmsFolder = jvmsFolder;
        this.downloader = downloader;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponent
    public List<String> getLibrariesForRunning() {
        List<DownloadedElement> list = this.config.getLibraries();
        List<String> result = new ArrayList<>();
        for (DownloadedElement downloadedElement : list) {
            File f = new File(this.workFolder, downloadedElement.getStoragePath());
            result.add(f.getPath());
        }
        return result;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponent
    public DownloadedBootInfo validateLibraryAndJava() {
        List<DownloadedElement> result = new ArrayList<>();
        List<DownloadedElement> list = this.config.getLibraries();
        for (DownloadedElement downloadedElement : list) {
            if (downloadedElement.notExistOrValid(this.workFolder)) {
                result.add(downloadedElement);
            }
        }
        DownloadedBootInfo el = new DownloadedBootInfo();
        el.setLibraries(result);
        JavaDownloadedElement javaDownloadedElement = TlauncherUtil.getProperJavaElement(this.javaConfig);
        if (!javaDownloadedElement.existFolder(this.jvmsFolder)) {
            el.setElement(javaDownloadedElement);
            return el;
        }
        return el;
    }

    @Override // org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponent
    public void preparedLibrariesAndJava(DownloadedBootInfo info) {
        if (info.getLibraries().isEmpty() && Objects.isNull(info.getElement())) {
            return;
        }
        if (Objects.nonNull(info.getElement())) {
            MetadataDTO metadataDTO = new MetadataDTO();
            metadataDTO.setUrl(CoreConstants.EMPTY_STRING);
            metadataDTO.setPath(info.getElement().getStoragePath());
            metadataDTO.setSha1(info.getElement().getShaCode());
            metadataDTO.setSize(info.getElement().getSize());
            metadataDTO.setLocalDestination(new File(this.jvmsFolder, info.getElement().getStoragePath()));
            DownloadableContainer javaDownloadableContainer = new DownloadableContainer();
            DownloadableDownloadElement downloadedJava = new DownloadableDownloadElement(info.getElement(), metadataDTO);
            javaDownloadableContainer.add(downloadedJava);
            javaDownloadableContainer.addHandler(new DownloadableHandler(this.jvmsFolder) { // from class: org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponentImpl.1
                @Override // org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponentImpl.DownloadableHandler, org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
                public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
                    File javaTempFolder;
                    DownloadableDownloadElement el = (DownloadableDownloadElement) d;
                    super.onComplete(c, d);
                    JavaDownloadedElement original = (JavaDownloadedElement) el.getElement();
                    File javaFolder = new File(PreparedEnvironmentComponentImpl.this.jvmsFolder, original.getJavaFolder());
                    boolean originalJVM = original.isOriginalJVM();
                    File tempJVM = new File(javaFolder + "_temp");
                    try {
                        if (javaFolder.exists()) {
                            FileUtil.deleteDirectory(javaFolder);
                        }
                        if (tempJVM.exists()) {
                            FileUtil.deleteDirectory(tempJVM);
                        }
                        PreparedEnvironmentComponentImpl.this.unpack(d, Boolean.valueOf(originalJVM), tempJVM);
                        if (originalJVM) {
                            javaTempFolder = new File(tempJVM, original.getJavaFolder());
                        } else {
                            javaTempFolder = tempJVM;
                        }
                        File java = new File(OS.appendBootstrapperJvm(javaTempFolder.getPath()));
                        if (OS.is(OS.LINUX)) {
                            Files.setPosixFilePermissions(java.toPath(), FileUtil.PERMISSIONS);
                        }
                        if (!javaTempFolder.renameTo(javaFolder)) {
                            FileUtils.copyDirectory(javaTempFolder, javaFolder);
                        }
                        if (tempJVM.exists()) {
                            FileUtil.deleteFile(tempJVM);
                        }
                        FileUtil.deleteFile(d.getMetadataDTO().getLocalDestination());
                    } catch (IOException t) {
                        U.log(t);
                        if (javaFolder.exists()) {
                            FileUtil.deleteDirectory(javaFolder);
                        }
                        throw new RetryDownloadException("cannot unpack archive", t);
                    }
                }
            });
            this.downloader.add(javaDownloadableContainer);
        }
        if (!info.getLibraries().isEmpty()) {
            DownloadableContainer container = new DownloadableContainer();
            for (DownloadedElement e : info.getLibraries()) {
                MetadataDTO metadataDTO2 = new MetadataDTO();
                metadataDTO2.setUrl(CoreConstants.EMPTY_STRING);
                metadataDTO2.setPath(e.getStoragePath());
                metadataDTO2.setSha1(e.getShaCode());
                metadataDTO2.setSize(e.getSize());
                metadataDTO2.setLocalDestination(new File(this.workFolder, e.getStoragePath()));
                container.add(new DownloadableDownloadElement(e, metadataDTO2));
            }
            container.addHandler(new DownloadableHandler(this.workFolder));
            this.downloader.add(container);
        }
        this.downloader.startDownloadAndWait();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unpack(Downloadable d, Boolean originalJVM, File tempJVM) throws IOException {
        if (OS.is(OS.OSX)) {
            try {
                String[] commands = {ArchiveStreamFactory.TAR, "-xf", d.getMetadataDTO().getLocalDestination().getPath()};
                ProcessBuilder p = new ProcessBuilder(commands);
                p.directory(this.jvmsFolder);
                Process process = p.start();
                try {
                    process.waitFor(90L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Throwable e2) {
                TlauncherUtil.showCriticalProblem(e2);
            }
        }
        if (originalJVM.booleanValue()) {
            FileUtil.unTarGz(d.getMetadataDTO().getLocalDestination(), tempJVM, false, false);
        } else {
            FileUtil.unZip(d.getMetadataDTO().getLocalDestination(), this.jvmsFolder, false, false);
        }
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/PreparedEnvironmentComponentImpl$DownloadableDownloadElement.class */
    public static class DownloadableDownloadElement extends Downloadable {
        private DownloadedElement element;

        @Override // org.tlauncher.tlauncher.downloader.Downloadable
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof DownloadableDownloadElement) {
                DownloadableDownloadElement other = (DownloadableDownloadElement) o;
                if (other.canEqual(this) && super.equals(o)) {
                    Object this$element = getElement();
                    Object other$element = other.getElement();
                    return this$element == null ? other$element == null : this$element.equals(other$element);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof DownloadableDownloadElement;
        }

        @Override // org.tlauncher.tlauncher.downloader.Downloadable
        public int hashCode() {
            int result = super.hashCode();
            Object $element = getElement();
            return (result * 59) + ($element == null ? 43 : $element.hashCode());
        }

        public void setElement(DownloadedElement element) {
            this.element = element;
        }

        @Override // org.tlauncher.tlauncher.downloader.Downloadable
        public String toString() {
            return "PreparedEnvironmentComponentImpl.DownloadableDownloadElement(element=" + getElement() + ")";
        }

        public DownloadedElement getElement() {
            return this.element;
        }

        DownloadableDownloadElement(DownloadedElement element, MetadataDTO metadataDTO) {
            super(new Repo((String[]) element.getUrl().toArray(new String[0]), "BOOTSTRAP"), metadataDTO);
            this.element = element;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/PreparedEnvironmentComponentImpl$DownloadableHandler.class */
    private static class DownloadableHandler extends DownloadableContainerHandlerAdapter {
        private File downloadFolder;

        public void setDownloadFolder(File downloadFolder) {
            this.downloadFolder = downloadFolder;
        }

        public String toString() {
            return "PreparedEnvironmentComponentImpl.DownloadableHandler(downloadFolder=" + getDownloadFolder() + ")";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof DownloadableHandler) {
                DownloadableHandler other = (DownloadableHandler) o;
                if (other.canEqual(this) && super.equals(o)) {
                    Object this$downloadFolder = getDownloadFolder();
                    Object other$downloadFolder = other.getDownloadFolder();
                    return this$downloadFolder == null ? other$downloadFolder == null : this$downloadFolder.equals(other$downloadFolder);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof DownloadableHandler;
        }

        public int hashCode() {
            int result = super.hashCode();
            Object $downloadFolder = getDownloadFolder();
            return (result * 59) + ($downloadFolder == null ? 43 : $downloadFolder.hashCode());
        }

        public DownloadableHandler(File downloadFolder) {
            this.downloadFolder = downloadFolder;
        }

        public File getDownloadFolder() {
            return this.downloadFolder;
        }

        @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
        public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
            DownloadableDownloadElement el = (DownloadableDownloadElement) d;
            if (el.getElement().notExistOrValid(this.downloadFolder)) {
                throw new RetryDownloadException("illegal library hash. " + el.getElement());
            }
        }

        @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
        public void onError(DownloadableContainer c, Downloadable d, Throwable e) {
            U.log(e);
            TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("updater.download.fail", CoreConstants.EMPTY_STRING));
            TLauncher.kill();
        }
    }
}
