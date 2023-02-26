package by.gdev.http.download.handler;

import by.gdev.http.download.exeption.HashSumAndSizeError;
import by.gdev.http.download.model.Headers;
import by.gdev.http.upload.download.downloader.DownloadElement;
import by.gdev.util.DesktopUtil;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/handler/PostHandlerImpl.class */
public class PostHandlerImpl implements PostHandler {
    private static final Logger log = LoggerFactory.getLogger(PostHandlerImpl.class);

    @Override // by.gdev.http.download.handler.PostHandler
    public void postProcessDownloadElement(DownloadElement element) {
        try {
            String shaLocalFile = DesktopUtil.getChecksum(new File(element.getPathToDownload() + element.getMetadata().getPath()), Headers.SHA1.getValue());
            long sizeLocalFile = new File(element.getPathToDownload() + element.getMetadata().getPath()).length();
            if (sizeLocalFile != element.getMetadata().getSize() && StringUtils.isEmpty(element.getMetadata().getLink())) {
                element.setError(new HashSumAndSizeError(element.getMetadata().getRelativeUrl(), element.getPathToDownload() + element.getMetadata().getPath(), "The size should be " + element.getMetadata().getSize()));
            }
            if (!shaLocalFile.equals(element.getMetadata().getSha1()) && StringUtils.isEmpty(element.getMetadata().getLink())) {
                element.setError(new HashSumAndSizeError(element.getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl(), element.getPathToDownload() + element.getMetadata().getPath(), "The hash sum should be " + element.getMetadata().getSha1()));
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Erorr", (Throwable) e);
        }
    }
}
