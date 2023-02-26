package by.gdev.http.download.handler;

import by.gdev.http.upload.download.downloader.DownloadElement;
import by.gdev.util.DesktopUtil;
import by.gdev.util.OSInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/handler/AccesHandler.class */
public class AccesHandler implements PostHandler {
    private static final Logger log = LoggerFactory.getLogger(AccesHandler.class);

    @Override // by.gdev.http.download.handler.PostHandler
    public void postProcessDownloadElement(DownloadElement e) {
        if (e.getMetadata().isExecutable()) {
            if ((OSInfo.getOSType() == OSInfo.OSType.LINUX) | (OSInfo.getOSType() == OSInfo.OSType.MACOSX)) {
                try {
                    Files.setPosixFilePermissions(Paths.get(e.getPathToDownload() + e.getMetadata().getPath(), new String[0]), DesktopUtil.PERMISSIONS);
                } catch (IOException e1) {
                    log.error("Error set file permission", (Throwable) e1);
                }
            }
        }
    }
}
