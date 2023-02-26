package by.gdev.http.download.impl;

import by.gdev.http.download.service.FileCacheService;
import by.gdev.http.download.service.GsonService;
import by.gdev.http.download.service.HttpService;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/impl/GsonServiceImpl.class */
public class GsonServiceImpl implements GsonService {
    private static final Logger log = LoggerFactory.getLogger(GsonServiceImpl.class);
    private Gson gson;
    private FileCacheService fileService;
    private HttpService httpService;

    public GsonServiceImpl(Gson gson, FileCacheService fileService, HttpService httpService) {
        this.gson = gson;
        this.fileService = fileService;
        this.httpService = httpService;
    }

    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObject(String url, Class<T> class1, boolean cache) throws IOException, NoSuchAlgorithmException {
        Path pathFile = this.fileService.getRawObject(url, cache);
        InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8);
        Throwable th = null;
        try {
            T t = (T) this.gson.fromJson((Reader) read, (Class<Object>) class1);
            if (read != null) {
                if (0 != 0) {
                    try {
                        read.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    read.close();
                }
            }
            return t;
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObjectByUrls(List<String> urls, String urn, Class<T> class1, boolean cache) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        T returnValue = null;
        for (String url : urls) {
            try {
                Path pathFile = this.fileService.getRawObject(url + urn, cache);
                InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8);
                returnValue = this.gson.fromJson((Reader) read, (Class) class1);
                if (read != null) {
                    if (0 != 0) {
                        read.close();
                    } else {
                        read.close();
                    }
                }
            } catch (IOException e) {
                log.error("Error = " + e.getMessage());
            }
        }
        return returnValue;
    }

    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObjectWithoutSaving(String url, Class<T> class1) throws IOException {
        return (T) getObjectWithoutSaving(url, (Class<Object>) class1, (Map<String, String>) null);
    }

    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObjectWithoutSaving(String url, Type type) throws IOException {
        return (T) getObjectWithoutSaving(url, type, (Map<String, String>) null);
    }

    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObjectWithoutSaving(String url, Class<T> class1, Map<String, String> headers) throws IOException {
        return (T) this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), (Class<Object>) class1);
    }

    @Override // by.gdev.http.download.service.GsonService
    public <T> T getObjectWithoutSaving(String url, Type type, Map<String, String> headers) throws IOException {
        return (T) this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), type);
    }
}
