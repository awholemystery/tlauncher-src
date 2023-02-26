package by.gdev.http.download.impl;

import by.gdev.http.download.model.Headers;
import by.gdev.http.download.model.RequestMetadata;
import by.gdev.http.download.service.FileCacheService;
import by.gdev.http.download.service.HttpService;
import by.gdev.util.DesktopUtil;
import by.gdev.util.model.download.Metadata;
import by.gdev.utils.service.FileMapperService;
import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/impl/FileCacheServiceImpl.class */
public class FileCacheServiceImpl implements FileCacheService {
    private static final Logger log = LoggerFactory.getLogger(FileCacheServiceImpl.class);
    private HttpService httpService;
    private Gson gson;
    private Charset charset;
    private Path directory;
    private int timeToLife;

    public FileCacheServiceImpl(HttpService httpService, Gson gson, Charset charset, Path directory, int timeToLife) {
        this.httpService = httpService;
        this.gson = gson;
        this.charset = charset;
        this.directory = directory;
        this.timeToLife = timeToLife;
    }

    @Override // by.gdev.http.download.service.FileCacheService
    public Path getRawObject(String url, boolean cache) throws IOException, NoSuchAlgorithmException {
        Path urlPath = Paths.get(this.directory.toString(), url.replaceAll("://", "_").replaceAll("[:?=]", "_"));
        Path metaFile = Paths.get(String.valueOf(urlPath).concat(".metadata"), new String[0]);
        if (cache) {
            return getResourceWithoutHttpHead(url, metaFile, urlPath);
        }
        return getResourceWithHttpHead(url, urlPath, metaFile);
    }

    @Override // by.gdev.http.download.service.FileCacheService
    public Path getRawObject(List<String> urls, Metadata metadata, boolean cache) throws IOException, NoSuchAlgorithmException {
        Iterator<String> it = urls.iterator();
        if (it.hasNext()) {
            String url = it.next();
            return getRawObject(url + metadata.getRelativeUrl(), cache);
        }
        throw new NullPointerException("metadata is empty");
    }

    private Path getResourceWithoutHttpHead(String url, Path metaFile, Path urlPath) throws IOException, NoSuchAlgorithmException {
        long purgeTime = System.currentTimeMillis() - (this.timeToLife * 1000);
        if (urlPath.toFile().lastModified() < purgeTime) {
            Files.deleteIfExists(urlPath);
        }
        if (urlPath.toFile().exists() && Files.exists(metaFile, new LinkOption[0])) {
            RequestMetadata localMetadata = (RequestMetadata) new FileMapperService(this.gson, this.charset, CoreConstants.EMPTY_STRING).read(metaFile.toString(), RequestMetadata.class);
            String sha = DesktopUtil.getChecksum(urlPath.toFile(), Headers.SHA1.getValue());
            if (sha.equals(localMetadata.getSha1())) {
                log.trace("HTTP HEAD -> " + url);
                return urlPath;
            }
            log.trace("not proper hashsum HTTP GET -> " + url);
            RequestMetadata serverMetadata = this.httpService.getRequestByUrlAndSave(url, urlPath);
            createSha1(serverMetadata, urlPath, metaFile);
            return urlPath;
        }
        log.trace("HTTP GET -> " + url);
        this.httpService.getRequestByUrlAndSave(url, urlPath);
        checkMetadataFile(metaFile, url);
        return urlPath;
    }

    private Path getResourceWithHttpHead(String url, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
        boolean fileExists = urlPath.toFile().exists();
        checkMetadataFile(metaFile, url);
        if (fileExists) {
            RequestMetadata serverMetadata = this.httpService.getMetaByUrl(url);
            RequestMetadata localMetadata = (RequestMetadata) new FileMapperService(this.gson, this.charset, CoreConstants.EMPTY_STRING).read(metaFile.toString(), RequestMetadata.class);
            if (StringUtils.equals(serverMetadata.getETag(), localMetadata.getETag()) & StringUtils.equals(serverMetadata.getContentLength(), localMetadata.getContentLength()) & StringUtils.equals(serverMetadata.getLastModified(), localMetadata.getLastModified())) {
                return urlPath;
            }
            this.httpService.getRequestByUrlAndSave(url, urlPath);
            new FileMapperService(this.gson, this.charset, CoreConstants.EMPTY_STRING).write(serverMetadata, metaFile.toString());
            return urlPath;
        }
        createSha1(this.httpService.getRequestByUrlAndSave(url, urlPath), urlPath, metaFile);
        return urlPath;
    }

    private void createSha1(RequestMetadata metadata, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
        metadata.setSha1(DesktopUtil.getChecksum(urlPath.toFile(), MessageDigestAlgorithms.SHA_1));
        new FileMapperService(this.gson, this.charset, CoreConstants.EMPTY_STRING).write(metadata, metaFile.toString());
    }

    private void checkMetadataFile(Path metaFile, String url) throws IOException {
        if (!metaFile.toFile().exists()) {
            RequestMetadata metadata = this.httpService.getMetaByUrl(url);
            new FileMapperService(this.gson, this.charset, CoreConstants.EMPTY_STRING).write(metadata, metaFile.toString());
        }
    }
}
