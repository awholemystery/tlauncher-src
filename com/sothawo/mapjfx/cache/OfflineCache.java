package com.sothawo.mapjfx.cache;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/OfflineCache.class */
public enum OfflineCache {
    INSTANCE;
    
    private static final Logger logger = Logger.getLogger(OfflineCache.class);
    private static final String TILE_OPENSTREETMAP_ORG = "[a-z]\\.tile\\.openstreetmap\\.org";
    private static final int PRELOAD_DATABUFFER_SIZE = 1048576;
    private final Collection<Pattern> noCachePatterns = new ArrayList();
    private final Collection<Pattern> cachePatterns = new ArrayList();
    private boolean urlStreamHandlerFactoryIsInitialized = false;
    private boolean active = false;
    private Path cacheDirectory;

    OfflineCache() {
    }

    static void clearDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new DeletingFileVisitor(path));
    }

    public Collection<String> getNoCacheFilters() {
        return (Collection) this.noCachePatterns.stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.toList());
    }

    public void clearAllCacheFilters() {
        this.cachePatterns.clear();
        this.noCachePatterns.clear();
    }

    public void setCacheFilters(Collection<String> cacheFilters) {
        if (!this.noCachePatterns.isEmpty()) {
            throw new IllegalStateException("cannot set both cacheFilters and noCacheFilters");
        }
        this.cachePatterns.clear();
        if (null != cacheFilters) {
            Stream<R> map = cacheFilters.stream().map(Pattern::compile);
            Collection<Pattern> collection = this.cachePatterns;
            collection.getClass();
            map.forEach((v1) -> {
                r1.add(v1);
            });
        }
    }

    public void setNoCacheFilters(Collection<String> noCacheFilters) {
        if (!this.cachePatterns.isEmpty()) {
            throw new IllegalStateException("cannot set both cacheFilters and noCacheFilters");
        }
        this.noCachePatterns.clear();
        if (null != noCacheFilters) {
            Stream<R> map = noCacheFilters.stream().map(Pattern::compile);
            Collection<Pattern> collection = this.noCachePatterns;
            collection.getClass();
            map.forEach((v1) -> {
                r1.add(v1);
            });
        }
    }

    public Path getCacheDirectory() {
        return this.cacheDirectory;
    }

    public void setCacheDirectory(Path cacheDirectory) {
        Path dir = (Path) Objects.requireNonNull(cacheDirectory);
        if (!Files.isDirectory(dir, new LinkOption[0]) || !Files.isWritable(dir)) {
            throw new IllegalArgumentException("cacheDirectory: " + dir);
        }
        this.cacheDirectory = dir;
    }

    public void setCacheDirectory(String cacheDirectory) {
        setCacheDirectory(FileSystems.getDefault().getPath((String) Objects.requireNonNull(cacheDirectory), new String[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean urlShouldBeCached(URL u) {
        String urlString = u.toString();
        if (!this.noCachePatterns.isEmpty()) {
            return this.noCachePatterns.stream().noneMatch(pattern -> {
                return pattern.matcher(urlString).matches();
            });
        }
        if (!this.cachePatterns.isEmpty()) {
            return this.cachePatterns.stream().anyMatch(pattern2 -> {
                return pattern2.matcher(urlString).matches();
            });
        }
        return true;
    }

    public boolean isNotActive() {
        return !this.active;
    }

    public void setActive(boolean active) {
        if (active && null == this.cacheDirectory) {
            throw new IllegalArgumentException("cannot setActive when no cacheDirectory is set");
        }
        if (active) {
            setupURLStreamHandlerFactory();
        }
        this.active = active;
    }

    private void setupURLStreamHandlerFactory() {
        String msg;
        if (!this.urlStreamHandlerFactoryIsInitialized) {
            try {
                URL.setURLStreamHandlerFactory(new CachingURLStreamHandlerFactory(this));
                this.urlStreamHandlerFactoryIsInitialized = true;
            } catch (Error e) {
                msg = "cannot setup URLStreamFactoryHandler, it is already set in this application. " + e.getMessage();
                if (logger.isTraceEnabled()) {
                    logger.error(msg);
                }
                throw new IllegalStateException(msg);
            } catch (SecurityException e2) {
                msg = "cannot setup URLStreamFactoryHandler. " + e2.getMessage();
                if (logger.isTraceEnabled()) {
                    logger.error(msg);
                }
                throw new IllegalStateException(msg);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCached(URL url) {
        try {
            Path cacheFile = filenameForURL(url);
            if (Files.exists(cacheFile, new LinkOption[0]) && Files.isReadable(cacheFile)) {
                if (Files.size(cacheFile) > 0) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            if (logger.isTraceEnabled()) {
                logger.warn(e.getMessage());
                return false;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Path filenameForURL(URL url) throws UnsupportedEncodingException {
        if (null == this.cacheDirectory) {
            throw new IllegalStateException("cannot resolve filename for url");
        }
        String mappedString = (String) Objects.requireNonNull(doMappings(url.toExternalForm()));
        String encodedString = URLEncoder.encode(mappedString, "UTF-8");
        return this.cacheDirectory.resolve(encodedString);
    }

    private String doMappings(String urlString) {
        if (null == urlString || urlString.isEmpty()) {
            return urlString;
        }
        return urlString.replaceAll(TILE_OPENSTREETMAP_ORG, "x.tile.openstreetmap.org");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveCachedDataInfo(Path cacheFile, CachedDataInfo cachedDataInfo) {
        Path cacheDataFile = Paths.get(cacheFile + ".dataInfo", new String[0]);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheDataFile.toFile()));
            oos.writeObject(cachedDataInfo);
            oos.flush();
            logger.trace("saving dataInfo " + cachedDataInfo);
            logger.trace("saved dataInfo to " + cacheDataFile);
            if (oos != null) {
                if (0 != 0) {
                    oos.close();
                } else {
                    oos.close();
                }
            }
        } catch (Exception e) {
            logger.warn("could not save dataInfo " + cacheDataFile);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CachedDataInfo readCachedDataInfo(Path cacheFile) {
        CachedDataInfo cachedDataInfo = null;
        Path cacheDataFile = Paths.get(cacheFile + ".dataInfo", new String[0]);
        if (Files.exists(cacheDataFile, new LinkOption[0])) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheDataFile.toFile()));
                cachedDataInfo = (CachedDataInfo) ois.readObject();
                if (ois != null) {
                    if (0 != 0) {
                        ois.close();
                    } else {
                        ois.close();
                    }
                }
            } catch (Exception e) {
                logger.warn("could not read dataInfo from " + e.getMessage());
            }
        }
        return cachedDataInfo;
    }

    public void clear() throws IOException {
        if (null != this.cacheDirectory) {
            clearDirectory(this.cacheDirectory);
        }
    }

    public void preloadURLs(Collection<String> urls) {
        preloadURLs(urls, 0);
    }

    public void preloadURLs(Collection<String> urls, int parallelism) {
        if (urls == null || isNotActive()) {
            return;
        }
        ForkJoinPool customThreadPool = new ForkJoinPool(parallelism < 1 ? Runtime.getRuntime().availableProcessors() : parallelism);
        try {
            customThreadPool.submit(() -> {
                urls.parallelStream().forEach(url -> {
                    try {
                        URLConnection urlConnection = new URL(url).openConnection();
                        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:66.0) Gecko/20100101 Firefox/66.0");
                        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        byte[] dataBuffer = new byte[PRELOAD_DATABUFFER_SIZE];
                        while (in.read(dataBuffer, 0, PRELOAD_DATABUFFER_SIZE) != -1) {
                        }
                        if (in != null) {
                            if (0 != 0) {
                                in.close();
                            } else {
                                in.close();
                            }
                        }
                    } catch (Exception e) {
                        logger.debug(String.format("could not load data from url %s %s", url, e));
                    }
                });
            });
            customThreadPool.shutdown();
        } catch (Throwable th) {
            customThreadPool.shutdown();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/OfflineCache$DeletingFileVisitor.class */
    public static class DeletingFileVisitor extends SimpleFileVisitor<Path> {
        private final Path rootDir;

        public DeletingFileVisitor(Path path) {
            this.rootDir = path;
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isDirectory()) {
                Files.delete(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (!dir.equals(this.rootDir)) {
                Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
