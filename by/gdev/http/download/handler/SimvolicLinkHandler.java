package by.gdev.http.download.handler;

import by.gdev.http.upload.download.downloader.DownloadElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/handler/SimvolicLinkHandler.class */
public class SimvolicLinkHandler implements PostHandler {
    private static final Logger log = LoggerFactory.getLogger(SimvolicLinkHandler.class);

    @Override // by.gdev.http.download.handler.PostHandler
    public void postProcessDownloadElement(DownloadElement e) {
        if (!StringUtils.isEmpty(e.getMetadata().getLink())) {
            try {
                Path target = Paths.get(e.getPathToDownload(), e.getMetadata().getLink());
                Path link = Paths.get(e.getPathToDownload(), e.getMetadata().getPath());
                if (Files.exists(link, new LinkOption[0])) {
                    Files.delete(link);
                }
                Files.createSymbolicLink(link.toAbsolutePath(), target.toAbsolutePath(), new FileAttribute[0]);
            } catch (IOException ex) {
                log.error("Error to create simvolic link", (Throwable) ex);
            }
        }
    }
}
