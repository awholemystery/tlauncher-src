package org.apache.commons.compress.compressors.zstandard;

import org.apache.commons.compress.utils.OsgiUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/zstandard/ZstdUtils.class */
public class ZstdUtils {
    private static final byte[] ZSTANDARD_FRAME_MAGIC = {40, -75, 47, -3};
    private static final byte[] SKIPPABLE_FRAME_MAGIC = {42, 77, 24};
    private static volatile CachedAvailability cachedZstdAvailability = CachedAvailability.DONT_CACHE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/zstandard/ZstdUtils$CachedAvailability.class */
    public enum CachedAvailability {
        DONT_CACHE,
        CACHED_AVAILABLE,
        CACHED_UNAVAILABLE
    }

    static {
        setCacheZstdAvailablity(!OsgiUtils.isRunningInOsgiEnvironment());
    }

    private ZstdUtils() {
    }

    public static boolean isZstdCompressionAvailable() {
        CachedAvailability cachedResult = cachedZstdAvailability;
        if (cachedResult != CachedAvailability.DONT_CACHE) {
            return cachedResult == CachedAvailability.CACHED_AVAILABLE;
        }
        return internalIsZstdCompressionAvailable();
    }

    private static boolean internalIsZstdCompressionAvailable() {
        try {
            Class.forName("com.github.luben.zstd.ZstdInputStream");
            return true;
        } catch (Exception | NoClassDefFoundError e) {
            return false;
        }
    }

    public static void setCacheZstdAvailablity(boolean doCache) {
        if (!doCache) {
            cachedZstdAvailability = CachedAvailability.DONT_CACHE;
        } else if (cachedZstdAvailability == CachedAvailability.DONT_CACHE) {
            boolean hasZstd = internalIsZstdCompressionAvailable();
            cachedZstdAvailability = hasZstd ? CachedAvailability.CACHED_AVAILABLE : CachedAvailability.CACHED_UNAVAILABLE;
        }
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < ZSTANDARD_FRAME_MAGIC.length) {
            return false;
        }
        boolean isZstandard = true;
        int i = 0;
        while (true) {
            if (i < ZSTANDARD_FRAME_MAGIC.length) {
                if (signature[i] == ZSTANDARD_FRAME_MAGIC[i]) {
                    i++;
                } else {
                    isZstandard = false;
                    break;
                }
            } else {
                break;
            }
        }
        if (isZstandard) {
            return true;
        }
        if (80 == (signature[0] & 240)) {
            for (int i2 = 0; i2 < SKIPPABLE_FRAME_MAGIC.length; i2++) {
                if (signature[i2 + 1] != SKIPPABLE_FRAME_MAGIC[i2]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static CachedAvailability getCachedZstdAvailability() {
        return cachedZstdAvailability;
    }
}
