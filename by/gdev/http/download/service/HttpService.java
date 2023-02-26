package by.gdev.http.download.service;

import by.gdev.http.download.model.RequestMetadata;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/service/HttpService.class */
public interface HttpService {
    RequestMetadata getRequestByUrlAndSave(String str, Path path) throws IOException;

    RequestMetadata getMetaByUrl(String str) throws IOException;

    String getRequestByUrl(String str) throws IOException;

    String getRequestByUrl(String str, Map<String, String> map) throws IOException;
}
