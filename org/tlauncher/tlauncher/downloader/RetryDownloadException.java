package org.tlauncher.tlauncher.downloader;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/RetryDownloadException.class */
public class RetryDownloadException extends IOException {
    private static final long serialVersionUID = 2968569164701826930L;

    public RetryDownloadException(String message) {
        super(message);
    }

    public RetryDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
