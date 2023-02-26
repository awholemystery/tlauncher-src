package by.gdev.http.download.service;

import by.gdev.util.model.download.Metadata;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/service/FileCacheService.class */
public interface FileCacheService {
    Path getRawObject(String str, boolean z) throws IOException, NoSuchAlgorithmException;

    Path getRawObject(List<String> list, Metadata metadata, boolean z) throws IOException, NoSuchAlgorithmException;
}
