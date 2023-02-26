package by.gdev.http.download.service;

import by.gdev.http.download.exeption.StatusExeption;
import by.gdev.http.upload.download.downloader.DownloaderContainer;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/service/Downloader.class */
public interface Downloader {
    void addContainer(DownloaderContainer downloaderContainer) throws IOException;

    void startDownload(boolean z) throws InterruptedException, ExecutionException, StatusExeption, IOException;

    void cancelDownload();
}
