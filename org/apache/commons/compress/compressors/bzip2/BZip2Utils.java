package org.apache.commons.compress.compressors.bzip2;

import ch.qos.logback.core.CoreConstants;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.compress.compressors.FileNameUtil;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/bzip2/BZip2Utils.class */
public abstract class BZip2Utils {
    private static final FileNameUtil fileNameUtil;

    static {
        Map<String, String> uncompressSuffix = new LinkedHashMap<>();
        uncompressSuffix.put(".tar.bz2", ".tar");
        uncompressSuffix.put(".tbz2", ".tar");
        uncompressSuffix.put(".tbz", ".tar");
        uncompressSuffix.put(".bz2", CoreConstants.EMPTY_STRING);
        uncompressSuffix.put(".bz", CoreConstants.EMPTY_STRING);
        fileNameUtil = new FileNameUtil(uncompressSuffix, ".bz2");
    }

    private BZip2Utils() {
    }

    public static boolean isCompressedFilename(String fileName) {
        return fileNameUtil.isCompressedFilename(fileName);
    }

    public static String getUncompressedFilename(String fileName) {
        return fileNameUtil.getUncompressedFilename(fileName);
    }

    public static String getCompressedFilename(String fileName) {
        return fileNameUtil.getCompressedFilename(fileName);
    }
}
