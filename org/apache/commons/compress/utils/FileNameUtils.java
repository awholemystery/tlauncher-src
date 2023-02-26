package org.apache.commons.compress.utils;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.nio.file.Path;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/FileNameUtils.class */
public class FileNameUtils {
    private static String fileNameToBaseName(String name) {
        int extensionIndex = name.lastIndexOf(46);
        return extensionIndex < 0 ? name : name.substring(0, extensionIndex);
    }

    private static String fileNameToExtension(String name) {
        int extensionIndex = name.lastIndexOf(46);
        return extensionIndex < 0 ? CoreConstants.EMPTY_STRING : name.substring(extensionIndex + 1);
    }

    public static String getBaseName(Path path) {
        if (path == null) {
            return null;
        }
        return fileNameToBaseName(path.getFileName().toString());
    }

    public static String getBaseName(String filename) {
        if (filename == null) {
            return null;
        }
        return fileNameToBaseName(new File(filename).getName());
    }

    public static String getExtension(Path path) {
        if (path == null) {
            return null;
        }
        return fileNameToExtension(path.getFileName().toString());
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        return fileNameToExtension(new File(filename).getName());
    }
}
