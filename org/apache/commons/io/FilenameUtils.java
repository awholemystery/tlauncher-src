package org.apache.commons.io;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Marker;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/FilenameUtils.class */
public class FilenameUtils {
    private static final String EMPTY_STRING = "";
    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char OTHER_SEPARATOR;
    private static final Pattern IPV4_PATTERN;
    private static final int IPV4_MAX_OCTET_VALUE = 255;
    private static final int IPV6_MAX_HEX_GROUPS = 8;
    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;
    private static final int MAX_UNSIGNED_SHORT = 65535;
    private static final int BASE_16 = 16;
    private static final Pattern REG_NAME_PART_PATTERN;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }
        IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    private static boolean isSeparator(char ch2) {
        return ch2 == '/' || ch2 == '\\';
    }

    public static String normalize(String fileName) {
        return doNormalize(fileName, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String fileName, boolean unixSeparator) {
        char separator = unixSeparator ? '/' : '\\';
        return doNormalize(fileName, separator, true);
    }

    public static String normalizeNoEndSeparator(String fileName) {
        return doNormalize(fileName, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String fileName, boolean unixSeparator) {
        char separator = unixSeparator ? '/' : '\\';
        return doNormalize(fileName, separator, false);
    }

    private static String doNormalize(String fileName, char separator, boolean keepSeparator) {
        if (fileName == null) {
            return null;
        }
        requireNonNullChars(fileName);
        int size = fileName.length();
        if (size == 0) {
            return fileName;
        }
        int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return null;
        }
        char[] array = new char[size + 2];
        fileName.getChars(0, fileName.length(), array, 0);
        char otherSeparator = separator == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == otherSeparator) {
                array[i] = separator;
            }
        }
        boolean lastIsDirectory = true;
        if (array[size - 1] != separator) {
            size++;
            array[size] = separator;
            lastIsDirectory = false;
        }
        int i2 = prefix != 0 ? prefix : 1;
        while (i2 < size) {
            if (array[i2] == separator && array[i2 - 1] == separator) {
                System.arraycopy(array, i2, array, i2 - 1, size - i2);
                size--;
                i2--;
            }
            i2++;
        }
        int i3 = prefix + 1;
        while (i3 < size) {
            if (array[i3] == separator && array[i3 - 1] == '.' && (i3 == prefix + 1 || array[i3 - 2] == separator)) {
                if (i3 == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i3 + 1, array, i3 - 1, size - i3);
                size -= 2;
                i3--;
            }
            i3++;
        }
        int i4 = prefix + 2;
        while (i4 < size) {
            if (array[i4] == separator && array[i4 - 1] == '.' && array[i4 - 2] == '.' && (i4 == prefix + 2 || array[i4 - 3] == separator)) {
                if (i4 == prefix + 2) {
                    return null;
                }
                if (i4 == size - 1) {
                    lastIsDirectory = true;
                }
                int j = i4 - 4;
                while (true) {
                    if (j >= prefix) {
                        if (array[j] == separator) {
                            System.arraycopy(array, i4 + 1, array, j + 1, size - i4);
                            size -= i4 - j;
                            i4 = j + 1;
                            break;
                        }
                        j--;
                    } else {
                        System.arraycopy(array, i4 + 1, array, prefix, size - i4);
                        size -= (i4 + 1) - prefix;
                        i4 = prefix + 1;
                        break;
                    }
                }
            }
            i4++;
        }
        if (size <= 0) {
            return "";
        }
        if (size <= prefix) {
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);
        }
        return new String(array, 0, size - 1);
    }

    public static String concat(String basePath, String fullFileNameToAdd) {
        int prefix = getPrefixLength(fullFileNameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return normalize(fullFileNameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        int len = basePath.length();
        if (len == 0) {
            return normalize(fullFileNameToAdd);
        }
        char ch2 = basePath.charAt(len - 1);
        if (isSeparator(ch2)) {
            return normalize(basePath + fullFileNameToAdd);
        }
        return normalize(basePath + '/' + fullFileNameToAdd);
    }

    public static boolean directoryContains(String canonicalParent, String canonicalChild) {
        Objects.requireNonNull(canonicalParent, "canonicalParent");
        if (canonicalChild == null || IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
            return false;
        }
        return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
    }

    public static String separatorsToUnix(String path) {
        if (path == null || path.indexOf(92) == -1) {
            return path;
        }
        return path.replace('\\', '/');
    }

    public static String separatorsToWindows(String path) {
        if (path == null || path.indexOf(47) == -1) {
            return path;
        }
        return path.replace('/', '\\');
    }

    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        }
        return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
    }

    public static int getPrefixLength(String fileName) {
        if (fileName == null) {
            return -1;
        }
        int len = fileName.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = fileName.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;
            }
            return isSeparator(ch0) ? 1 : 0;
        } else if (ch0 == '~') {
            int posUnix = fileName.indexOf(47, 1);
            int posWin = fileName.indexOf(92, 1);
            if (posUnix == -1 && posWin == -1) {
                return len + 1;
            }
            int posUnix2 = posUnix == -1 ? posWin : posUnix;
            return Math.min(posUnix2, posWin == -1 ? posUnix2 : posWin) + 1;
        } else {
            char ch1 = fileName.charAt(1);
            if (ch1 == ':') {
                char ch02 = Character.toUpperCase(ch0);
                if (ch02 >= 'A' && ch02 <= 'Z') {
                    if (len == 2 && !FileSystem.getCurrent().supportsDriveLetter()) {
                        return 0;
                    }
                    if (len == 2 || !isSeparator(fileName.charAt(2))) {
                        return 2;
                    }
                    return 3;
                } else if (ch02 == '/') {
                    return 1;
                } else {
                    return -1;
                }
            } else if (!isSeparator(ch0) || !isSeparator(ch1)) {
                return isSeparator(ch0) ? 1 : 0;
            } else {
                int posUnix3 = fileName.indexOf(47, 2);
                int posWin2 = fileName.indexOf(92, 2);
                if ((posUnix3 == -1 && posWin2 == -1) || posUnix3 == 2 || posWin2 == 2) {
                    return -1;
                }
                int posUnix4 = posUnix3 == -1 ? posWin2 : posUnix3;
                int pos = Math.min(posUnix4, posWin2 == -1 ? posUnix4 : posWin2) + 1;
                String hostnamePart = fileName.substring(2, pos - 1);
                if (isValidHostName(hostnamePart)) {
                    return pos;
                }
                return -1;
            }
        }
    }

    public static int indexOfLastSeparator(String fileName) {
        if (fileName == null) {
            return -1;
        }
        int lastUnixPos = fileName.lastIndexOf(47);
        int lastWindowsPos = fileName.lastIndexOf(92);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static int indexOfExtension(String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return -1;
        }
        if (isSystemWindows()) {
            int offset = fileName.indexOf(58, getAdsCriticalOffset(fileName));
            if (offset != -1) {
                throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
            }
        }
        int extensionPos = fileName.lastIndexOf(46);
        int lastSeparator = indexOfLastSeparator(fileName);
        if (lastSeparator > extensionPos) {
            return -1;
        }
        return extensionPos;
    }

    public static String getPrefix(String fileName) {
        int len;
        if (fileName == null || (len = getPrefixLength(fileName)) < 0) {
            return null;
        }
        if (len > fileName.length()) {
            requireNonNullChars(fileName + '/');
            return fileName + '/';
        }
        String path = fileName.substring(0, len);
        requireNonNullChars(path);
        return path;
    }

    public static String getPath(String fileName) {
        return doGetPath(fileName, 1);
    }

    public static String getPathNoEndSeparator(String fileName) {
        return doGetPath(fileName, 0);
    }

    private static String doGetPath(String fileName, int separatorAdd) {
        int prefix;
        if (fileName == null || (prefix = getPrefixLength(fileName)) < 0) {
            return null;
        }
        int index = indexOfLastSeparator(fileName);
        int endIndex = index + separatorAdd;
        if (prefix >= fileName.length() || index < 0 || prefix >= endIndex) {
            return "";
        }
        String path = fileName.substring(prefix, endIndex);
        requireNonNullChars(path);
        return path;
    }

    public static String getFullPath(String fileName) {
        return doGetFullPath(fileName, true);
    }

    public static String getFullPathNoEndSeparator(String fileName) {
        return doGetFullPath(fileName, false);
    }

    private static String doGetFullPath(String fileName, boolean includeSeparator) {
        int prefix;
        if (fileName == null || (prefix = getPrefixLength(fileName)) < 0) {
            return null;
        }
        if (prefix >= fileName.length()) {
            if (includeSeparator) {
                return getPrefix(fileName);
            }
            return fileName;
        }
        int index = indexOfLastSeparator(fileName);
        if (index < 0) {
            return fileName.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            end++;
        }
        return fileName.substring(0, end);
    }

    public static String getName(String fileName) {
        if (fileName == null) {
            return null;
        }
        requireNonNullChars(fileName);
        int index = indexOfLastSeparator(fileName);
        return fileName.substring(index + 1);
    }

    private static void requireNonNullChars(String path) {
        if (path.indexOf(0) >= 0) {
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    public static String getBaseName(String fileName) {
        return removeExtension(getName(fileName));
    }

    public static String getExtension(String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return null;
        }
        int index = indexOfExtension(fileName);
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    private static int getAdsCriticalOffset(String fileName) {
        int offset1 = fileName.lastIndexOf(SYSTEM_SEPARATOR);
        int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            if (offset2 == -1) {
                return 0;
            }
            return offset2 + 1;
        } else if (offset2 == -1) {
            return offset1 + 1;
        } else {
            return Math.max(offset1, offset2) + 1;
        }
    }

    public static String removeExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        requireNonNullChars(fileName);
        int index = indexOfExtension(fileName);
        if (index == -1) {
            return fileName;
        }
        return fileName.substring(0, index);
    }

    public static boolean equals(String fileName1, String fileName2) {
        return equals(fileName1, fileName2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String fileName1, String fileName2) {
        return equals(fileName1, fileName2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String fileName1, String fileName2) {
        return equals(fileName1, fileName2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String fileName1, String fileName2) {
        return equals(fileName1, fileName2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String fileName1, String fileName2, boolean normalized, IOCase caseSensitivity) {
        if (fileName1 == null || fileName2 == null) {
            return fileName1 == null && fileName2 == null;
        }
        if (normalized) {
            fileName1 = normalize(fileName1);
            if (fileName1 == null) {
                return false;
            }
            fileName2 = normalize(fileName2);
            if (fileName2 == null) {
                return false;
            }
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        return caseSensitivity.checkEquals(fileName1, fileName2);
    }

    public static boolean isExtension(String fileName, String extension) {
        if (fileName == null) {
            return false;
        }
        requireNonNullChars(fileName);
        if (extension == null || extension.isEmpty()) {
            return indexOfExtension(fileName) == -1;
        }
        String fileExt = getExtension(fileName);
        return fileExt.equals(extension);
    }

    public static boolean isExtension(String fileName, String... extensions) {
        if (fileName == null) {
            return false;
        }
        requireNonNullChars(fileName);
        if (extensions == null || extensions.length == 0) {
            return indexOfExtension(fileName) == -1;
        }
        String fileExt = getExtension(fileName);
        for (String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExtension(String fileName, Collection<String> extensions) {
        if (fileName == null) {
            return false;
        }
        requireNonNullChars(fileName);
        if (extensions == null || extensions.isEmpty()) {
            return indexOfExtension(fileName) == -1;
        }
        String fileExt = getExtension(fileName);
        for (String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean wildcardMatch(String fileName, String wildcardMatcher) {
        return wildcardMatch(fileName, wildcardMatcher, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String fileName, String wildcardMatcher) {
        return wildcardMatch(fileName, wildcardMatcher, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String fileName, String wildcardMatcher, IOCase caseSensitivity) {
        if (fileName == null && wildcardMatcher == null) {
            return true;
        }
        if (fileName == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Deque<int[]> backtrack = new ArrayDeque<>(wcs.length);
        do {
            if (!backtrack.isEmpty()) {
                int[] array = backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            while (wcsIdx < wcs.length) {
                if (wcs[wcsIdx].equals("?")) {
                    textIdx++;
                    if (textIdx > fileName.length()) {
                        break;
                    }
                    anyChars = false;
                    wcsIdx++;
                } else {
                    if (wcs[wcsIdx].equals(Marker.ANY_MARKER)) {
                        anyChars = true;
                        if (wcsIdx == wcs.length - 1) {
                            textIdx = fileName.length();
                        }
                    } else if (anyChars) {
                        textIdx = caseSensitivity.checkIndexOf(fileName, textIdx, wcs[wcsIdx]);
                        if (textIdx == -1) {
                            break;
                        }
                        int repeat = caseSensitivity.checkIndexOf(fileName, textIdx + 1, wcs[wcsIdx]);
                        if (repeat >= 0) {
                            backtrack.push(new int[]{wcsIdx, repeat});
                        }
                        textIdx += wcs[wcsIdx].length();
                        anyChars = false;
                    } else {
                        if (!caseSensitivity.checkRegionMatches(fileName, textIdx, wcs[wcsIdx])) {
                            break;
                        }
                        textIdx += wcs[wcsIdx].length();
                        anyChars = false;
                    }
                    wcsIdx++;
                }
            }
            if (wcsIdx == wcs.length && textIdx == fileName.length()) {
                return true;
            }
        } while (!backtrack.isEmpty());
        return false;
    }

    static String[] splitOnTokens(String text) {
        if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
            return new String[]{text};
        }
        char[] array = text.toCharArray();
        ArrayList<String> list = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        char prevChar = 0;
        for (char ch2 : array) {
            if (ch2 == '?' || ch2 == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (ch2 == '?') {
                    list.add("?");
                } else if (prevChar != '*') {
                    list.add(Marker.ANY_MARKER);
                }
            } else {
                buffer.append(ch2);
            }
            prevChar = ch2;
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }
        return (String[]) list.toArray(EMPTY_STRING_ARRAY);
    }

    private static boolean isValidHostName(String name) {
        return isIPv6Address(name) || isRFC3986HostName(name);
    }

    private static boolean isIPv4Address(String name) {
        Matcher m = IPV4_PATTERN.matcher(name);
        if (!m.matches() || m.groupCount() != 4) {
            return false;
        }
        for (int i = 1; i <= 4; i++) {
            String ipSegment = m.group(i);
            int iIpSegment = Integer.parseInt(ipSegment);
            if (iIpSegment > IPV4_MAX_OCTET_VALUE) {
                return false;
            }
            if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
                return false;
            }
        }
        return true;
    }

    private static boolean isIPv6Address(String inet6Address) {
        boolean containsCompressedZeroes = inet6Address.contains("::");
        if (containsCompressedZeroes && inet6Address.indexOf("::") != inet6Address.lastIndexOf("::")) {
            return false;
        }
        if (!inet6Address.startsWith(":") || inet6Address.startsWith("::")) {
            if (inet6Address.endsWith(":") && !inet6Address.endsWith("::")) {
                return false;
            }
            String[] octets = inet6Address.split(":");
            if (containsCompressedZeroes) {
                List<String> octetList = new ArrayList<>(Arrays.asList(octets));
                if (inet6Address.endsWith("::")) {
                    octetList.add("");
                } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
                    octetList.remove(0);
                }
                octets = (String[]) octetList.toArray(EMPTY_STRING_ARRAY);
            }
            if (octets.length > 8) {
                return false;
            }
            int validOctets = 0;
            int emptyOctets = 0;
            for (int index = 0; index < octets.length; index++) {
                String octet = octets[index];
                if (octet.isEmpty()) {
                    emptyOctets++;
                    if (emptyOctets > 1) {
                        return false;
                    }
                } else {
                    emptyOctets = 0;
                    if (index == octets.length - 1 && octet.contains(".")) {
                        if (!isIPv4Address(octet)) {
                            return false;
                        }
                        validOctets += 2;
                    } else if (octet.length() > 4) {
                        return false;
                    } else {
                        try {
                            int octetInt = Integer.parseInt(octet, 16);
                            if (octetInt < 0 || octetInt > MAX_UNSIGNED_SHORT) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                }
                validOctets++;
            }
            return validOctets <= 8 && (validOctets >= 8 || containsCompressedZeroes);
        }
        return false;
    }

    private static boolean isRFC3986HostName(String name) {
        String[] parts = name.split("\\.", -1);
        int i = 0;
        while (i < parts.length) {
            if (parts[i].isEmpty()) {
                return i == parts.length - 1;
            } else if (REG_NAME_PART_PATTERN.matcher(parts[i]).matches()) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }
}
