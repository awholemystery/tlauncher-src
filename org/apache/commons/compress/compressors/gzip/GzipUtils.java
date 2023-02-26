package org.apache.commons.compress.compressors.gzip;

import ch.qos.logback.core.CoreConstants;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.compress.compressors.FileNameUtil;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/gzip/GzipUtils.class */
public class GzipUtils {
    private static final FileNameUtil fileNameUtil;

    static {
        Map<String, String> uncompressSuffix = new LinkedHashMap<>();
        uncompressSuffix.put(".tgz", ".tar");
        uncompressSuffix.put(".taz", ".tar");
        uncompressSuffix.put(".svgz", ".svg");
        uncompressSuffix.put(".cpgz", ".cpio");
        uncompressSuffix.put(".wmz", ".wmf");
        uncompressSuffix.put(".emz", ".emf");
        uncompressSuffix.put(".gz", CoreConstants.EMPTY_STRING);
        uncompressSuffix.put(".z", CoreConstants.EMPTY_STRING);
        uncompressSuffix.put("-gz", CoreConstants.EMPTY_STRING);
        uncompressSuffix.put("-z", CoreConstants.EMPTY_STRING);
        uncompressSuffix.put("_z", CoreConstants.EMPTY_STRING);
        fileNameUtil = new FileNameUtil(uncompressSuffix, ".gz");
    }

    private GzipUtils() {
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
