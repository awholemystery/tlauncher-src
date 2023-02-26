package ch.qos.logback.core.util;

import ch.qos.logback.core.CoreConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/util/FileSize.class */
public class FileSize {
    private static final String LENGTH_PART = "([0-9]+)";
    private static final int DOUBLE_GROUP = 1;
    private static final String UNIT_PART = "(|kb|mb|gb)s?";
    private static final int UNIT_GROUP = 2;
    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("([0-9]+)\\s*(|kb|mb|gb)s?", 2);
    public static final long KB_COEFFICIENT = 1024;
    public static final long MB_COEFFICIENT = 1048576;
    public static final long GB_COEFFICIENT = 1073741824;
    final long size;

    public FileSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return this.size;
    }

    public static FileSize valueOf(String fileSizeStr) {
        long coefficient;
        Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr);
        if (matcher.matches()) {
            String lenStr = matcher.group(1);
            String unitStr = matcher.group(2);
            long lenValue = Long.valueOf(lenStr).longValue();
            if (unitStr.equalsIgnoreCase(CoreConstants.EMPTY_STRING)) {
                coefficient = 1;
            } else if (unitStr.equalsIgnoreCase("kb")) {
                coefficient = 1024;
            } else if (unitStr.equalsIgnoreCase("mb")) {
                coefficient = 1048576;
            } else if (unitStr.equalsIgnoreCase("gb")) {
                coefficient = 1073741824;
            } else {
                throw new IllegalStateException("Unexpected " + unitStr);
            }
            return new FileSize(lenValue * coefficient);
        }
        throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
    }

    public String toString() {
        long inKB = this.size / 1024;
        if (inKB == 0) {
            return this.size + " Bytes";
        }
        long inMB = this.size / 1048576;
        if (inMB == 0) {
            return inKB + " KB";
        }
        long inGB = this.size / 1073741824;
        if (inGB == 0) {
            return inMB + " MB";
        }
        return inGB + " GB";
    }
}
