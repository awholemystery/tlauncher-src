package org.apache.commons.io;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.compress.archivers.cpio.CpioConstants;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/FileSystem.class */
public enum FileSystem {
    GENERIC(false, false, Integer.MAX_VALUE, Integer.MAX_VALUE, new char[]{0}, new String[0], false),
    LINUX(true, true, 255, CpioConstants.C_ISFIFO, new char[]{0, '/'}, new String[0], false),
    MAC_OSX(true, true, 255, 1024, new char[]{0, '/', ':'}, new String[0], false),
    WINDOWS(false, true, 255, 32000, new char[]{0, 1, 2, 3, 4, 5, 6, 7, '\b', '\t', '\n', 11, '\f', '\r', 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, '\"', '*', '/', ':', '<', '>', '?', '\\', '|'}, new String[]{DateTokenConverter.AUXILIARY_TOKEN, "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "CON", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "NUL", "PRN"}, true);
    
    private static final boolean IS_OS_LINUX = getOsMatchesName("Linux");
    private static final boolean IS_OS_MAC = getOsMatchesName("Mac");
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    private static final boolean IS_OS_WINDOWS = getOsMatchesName(OS_NAME_WINDOWS_PREFIX);
    private final boolean casePreserving;
    private final boolean caseSensitive;
    private final char[] illegalFileNameChars;
    private final int maxFileNameLength;
    private final int maxPathLength;
    private final String[] reservedFileNames;
    private final boolean supportsDriveLetter;

    public static FileSystem getCurrent() {
        if (IS_OS_LINUX) {
            return LINUX;
        }
        if (IS_OS_MAC) {
            return MAC_OSX;
        }
        if (IS_OS_WINDOWS) {
            return WINDOWS;
        }
        return GENERIC;
    }

    private static boolean getOsMatchesName(String osNamePrefix) {
        return isOsNameMatch(getSystemProperty("os.name"), osNamePrefix);
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException e) {
            System.err.println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    private static boolean isOsNameMatch(String osName, String osNamePrefix) {
        if (osName == null) {
            return false;
        }
        return osName.toUpperCase(Locale.ROOT).startsWith(osNamePrefix.toUpperCase(Locale.ROOT));
    }

    FileSystem(boolean caseSensitive, boolean casePreserving, int maxFileLength, int maxPathLength, char[] illegalFileNameChars, String[] reservedFileNames, boolean supportsDriveLetter) {
        this.maxFileNameLength = maxFileLength;
        this.maxPathLength = maxPathLength;
        this.illegalFileNameChars = (char[]) Objects.requireNonNull(illegalFileNameChars, "illegalFileNameChars");
        this.reservedFileNames = (String[]) Objects.requireNonNull(reservedFileNames, "reservedFileNames");
        this.caseSensitive = caseSensitive;
        this.casePreserving = casePreserving;
        this.supportsDriveLetter = supportsDriveLetter;
    }

    public char[] getIllegalFileNameChars() {
        return (char[]) this.illegalFileNameChars.clone();
    }

    public int getMaxFileNameLength() {
        return this.maxFileNameLength;
    }

    public int getMaxPathLength() {
        return this.maxPathLength;
    }

    public String[] getReservedFileNames() {
        return (String[]) this.reservedFileNames.clone();
    }

    public boolean isCasePreserving() {
        return this.casePreserving;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    private boolean isIllegalFileNameChar(char c) {
        return Arrays.binarySearch(this.illegalFileNameChars, c) >= 0;
    }

    public boolean isLegalFileName(CharSequence candidate) {
        if (candidate == null || candidate.length() == 0 || candidate.length() > this.maxFileNameLength || isReservedFileName(candidate)) {
            return false;
        }
        for (int i = 0; i < candidate.length(); i++) {
            if (isIllegalFileNameChar(candidate.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isReservedFileName(CharSequence candidate) {
        return Arrays.binarySearch(this.reservedFileNames, candidate) >= 0;
    }

    public boolean supportsDriveLetter() {
        return this.supportsDriveLetter;
    }

    public String toLegalFileName(String candidate, char replacement) {
        if (isIllegalFileNameChar(replacement)) {
            Object[] objArr = new Object[3];
            objArr[0] = replacement == 0 ? "\\0" : Character.valueOf(replacement);
            objArr[1] = name();
            objArr[2] = Arrays.toString(this.illegalFileNameChars);
            throw new IllegalArgumentException(String.format("The replacement character '%s' cannot be one of the %s illegal characters: %s", objArr));
        }
        String truncated = candidate.length() > this.maxFileNameLength ? candidate.substring(0, this.maxFileNameLength) : candidate;
        boolean changed = false;
        char[] charArray = truncated.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (isIllegalFileNameChar(charArray[i])) {
                charArray[i] = replacement;
                changed = true;
            }
        }
        return changed ? String.valueOf(charArray) : truncated;
    }
}
