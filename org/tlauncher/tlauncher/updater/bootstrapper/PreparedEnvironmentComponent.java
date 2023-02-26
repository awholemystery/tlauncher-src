package org.tlauncher.tlauncher.updater.bootstrapper;

import java.io.IOException;
import java.util.List;
import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/PreparedEnvironmentComponent.class */
public interface PreparedEnvironmentComponent {
    List<String> getLibrariesForRunning() throws IOException;

    DownloadedBootInfo validateLibraryAndJava();

    void preparedLibrariesAndJava(DownloadedBootInfo downloadedBootInfo);
}
