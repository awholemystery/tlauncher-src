package org.apache.commons.compress.archivers.tar;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.EntryStreamOffsets;
import org.apache.commons.compress.archivers.sevenz.NID;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.cookie.ClientCookie;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarArchiveEntry.class */
public class TarArchiveEntry implements ArchiveEntry, TarConstants, EntryStreamOffsets {
    private static final TarArchiveEntry[] EMPTY_TAR_ARCHIVE_ENTRY_ARRAY = new TarArchiveEntry[0];
    public static final long UNKNOWN = -1;
    public static final int MAX_NAMELEN = 31;
    public static final int DEFAULT_DIR_MODE = 16877;
    public static final int DEFAULT_FILE_MODE = 33188;
    public static final int MILLIS_PER_SECOND = 1000;
    private String name;
    private final boolean preserveAbsolutePath;
    private int mode;
    private long userId;
    private long groupId;
    private long size;
    private FileTime mTime;
    private FileTime cTime;
    private FileTime aTime;
    private FileTime birthTime;
    private boolean checkSumOK;
    private byte linkFlag;
    private String linkName;
    private String magic;
    private String version;
    private String userName;
    private String groupName;
    private int devMajor;
    private int devMinor;
    private List<TarArchiveStructSparse> sparseHeaders;
    private boolean isExtended;
    private long realSize;
    private boolean paxGNUSparse;
    private boolean paxGNU1XSparse;
    private boolean starSparse;
    private final Path file;
    private final LinkOption[] linkOptions;
    private final Map<String, String> extraPaxHeaders;
    private long dataOffset;

    private static FileTime fileTimeFromOptionalSeconds(long seconds) {
        if (seconds <= 0) {
            return null;
        }
        return FileTime.from(seconds, TimeUnit.SECONDS);
    }

    private static String normalizeFileName(String fileName, boolean preserveAbsolutePath) {
        String fileName2;
        String property;
        int colon;
        if (!preserveAbsolutePath && (property = System.getProperty("os.name")) != null) {
            String osName = property.toLowerCase(Locale.ROOT);
            if (osName.startsWith("windows")) {
                if (fileName.length() > 2) {
                    char ch1 = fileName.charAt(0);
                    char ch2 = fileName.charAt(1);
                    if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
                        fileName = fileName.substring(2);
                    }
                }
            } else if (osName.contains("netware") && (colon = fileName.indexOf(58)) != -1) {
                fileName = fileName.substring(colon + 1);
            }
        }
        String replace = fileName.replace(File.separatorChar, '/');
        while (true) {
            fileName2 = replace;
            if (preserveAbsolutePath || !fileName2.startsWith("/")) {
                break;
            }
            replace = fileName2.substring(1);
        }
        return fileName2;
    }

    private static Instant parseInstantFromDecimalSeconds(String value) {
        BigDecimal epochSeconds = new BigDecimal(value);
        long seconds = epochSeconds.longValue();
        long nanos = epochSeconds.remainder(BigDecimal.ONE).movePointRight(9).longValue();
        return Instant.ofEpochSecond(seconds, nanos);
    }

    private TarArchiveEntry(boolean preserveAbsolutePath) {
        this.name = CoreConstants.EMPTY_STRING;
        this.linkName = CoreConstants.EMPTY_STRING;
        this.magic = "ustar��";
        this.version = TarConstants.VERSION_POSIX;
        this.groupName = CoreConstants.EMPTY_STRING;
        this.extraPaxHeaders = new HashMap();
        this.dataOffset = -1L;
        String user = System.getProperty("user.name", CoreConstants.EMPTY_STRING);
        this.userName = user.length() > 31 ? user.substring(0, 31) : user;
        this.file = null;
        this.linkOptions = IOUtils.EMPTY_LINK_OPTIONS;
        this.preserveAbsolutePath = preserveAbsolutePath;
    }

    public TarArchiveEntry(byte[] headerBuf) {
        this(false);
        parseTarHeader(headerBuf);
    }

    public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding) throws IOException {
        this(headerBuf, encoding, false);
    }

    public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding, boolean lenient) throws IOException {
        this(Collections.emptyMap(), headerBuf, encoding, lenient);
    }

    public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding, boolean lenient, long dataOffset) throws IOException {
        this(headerBuf, encoding, lenient);
        setDataOffset(dataOffset);
    }

    public TarArchiveEntry(File file) {
        this(file, file.getPath());
    }

    public TarArchiveEntry(File file, String fileName) {
        this.name = CoreConstants.EMPTY_STRING;
        this.linkName = CoreConstants.EMPTY_STRING;
        this.magic = "ustar��";
        this.version = TarConstants.VERSION_POSIX;
        this.groupName = CoreConstants.EMPTY_STRING;
        this.extraPaxHeaders = new HashMap();
        this.dataOffset = -1L;
        String normalizedName = normalizeFileName(fileName, false);
        this.file = file.toPath();
        this.linkOptions = IOUtils.EMPTY_LINK_OPTIONS;
        try {
            readFileMode(this.file, normalizedName, new LinkOption[0]);
        } catch (IOException e) {
            if (!file.isDirectory()) {
                this.size = file.length();
            }
        }
        this.userName = CoreConstants.EMPTY_STRING;
        try {
            readOsSpecificProperties(this.file, new LinkOption[0]);
        } catch (IOException e2) {
            this.mTime = FileTime.fromMillis(file.lastModified());
        }
        this.preserveAbsolutePath = false;
    }

    public TarArchiveEntry(Map<String, String> globalPaxHeaders, byte[] headerBuf, ZipEncoding encoding, boolean lenient) throws IOException {
        this(false);
        parseTarHeader(globalPaxHeaders, headerBuf, encoding, false, lenient);
    }

    public TarArchiveEntry(Map<String, String> globalPaxHeaders, byte[] headerBuf, ZipEncoding encoding, boolean lenient, long dataOffset) throws IOException {
        this(globalPaxHeaders, headerBuf, encoding, lenient);
        setDataOffset(dataOffset);
    }

    public TarArchiveEntry(Path file) throws IOException {
        this(file, file.toString(), new LinkOption[0]);
    }

    public TarArchiveEntry(Path file, String fileName, LinkOption... linkOptions) throws IOException {
        this.name = CoreConstants.EMPTY_STRING;
        this.linkName = CoreConstants.EMPTY_STRING;
        this.magic = "ustar��";
        this.version = TarConstants.VERSION_POSIX;
        this.groupName = CoreConstants.EMPTY_STRING;
        this.extraPaxHeaders = new HashMap();
        this.dataOffset = -1L;
        String normalizedName = normalizeFileName(fileName, false);
        this.file = file;
        this.linkOptions = linkOptions == null ? IOUtils.EMPTY_LINK_OPTIONS : linkOptions;
        readFileMode(file, normalizedName, linkOptions);
        this.userName = CoreConstants.EMPTY_STRING;
        readOsSpecificProperties(file, new LinkOption[0]);
        this.preserveAbsolutePath = false;
    }

    public TarArchiveEntry(String name) {
        this(name, false);
    }

    public TarArchiveEntry(String name, boolean preserveAbsolutePath) {
        this(preserveAbsolutePath);
        String name2 = normalizeFileName(name, preserveAbsolutePath);
        boolean isDir = name2.endsWith("/");
        this.name = name2;
        this.mode = isDir ? DEFAULT_DIR_MODE : DEFAULT_FILE_MODE;
        this.linkFlag = isDir ? (byte) 53 : (byte) 48;
        this.mTime = FileTime.from(Instant.now());
        this.userName = CoreConstants.EMPTY_STRING;
    }

    public TarArchiveEntry(String name, byte linkFlag) {
        this(name, linkFlag, false);
    }

    public TarArchiveEntry(String name, byte linkFlag, boolean preserveAbsolutePath) {
        this(name, preserveAbsolutePath);
        this.linkFlag = linkFlag;
        if (linkFlag == 76) {
            this.magic = TarConstants.MAGIC_GNU;
            this.version = TarConstants.VERSION_GNU_SPACE;
        }
    }

    public void addPaxHeader(String name, String value) {
        try {
            processPaxHeader(name, value);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Invalid input", ex);
        }
    }

    public void clearExtraPaxHeaders() {
        this.extraPaxHeaders.clear();
    }

    public boolean equals(Object it) {
        if (it == null || getClass() != it.getClass()) {
            return false;
        }
        return equals((TarArchiveEntry) it);
    }

    public boolean equals(TarArchiveEntry it) {
        return it != null && getName().equals(it.getName());
    }

    private int evaluateType(Map<String, String> globalPaxHeaders, byte[] header) {
        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_GNU, header, TarConstants.MAGIC_OFFSET, 6)) {
            return 2;
        }
        if (ArchiveUtils.matchAsciiBuffer("ustar��", header, TarConstants.MAGIC_OFFSET, 6)) {
            if (isXstar(globalPaxHeaders, header)) {
                return 4;
            }
            return 3;
        }
        return 0;
    }

    private int fill(byte value, int offset, byte[] outbuf, int length) {
        for (int i = 0; i < length; i++) {
            outbuf[offset + i] = value;
        }
        return offset + length;
    }

    private int fill(int value, int offset, byte[] outbuf, int length) {
        return fill((byte) value, offset, outbuf, length);
    }

    void fillGNUSparse0xData(Map<String, String> headers) {
        this.paxGNUSparse = true;
        this.realSize = Integer.parseInt(headers.get("GNU.sparse.size"));
        if (headers.containsKey("GNU.sparse.name")) {
            this.name = headers.get("GNU.sparse.name");
        }
    }

    void fillGNUSparse1xData(Map<String, String> headers) throws IOException {
        this.paxGNUSparse = true;
        this.paxGNU1XSparse = true;
        if (headers.containsKey("GNU.sparse.name")) {
            this.name = headers.get("GNU.sparse.name");
        }
        if (headers.containsKey("GNU.sparse.realsize")) {
            try {
                this.realSize = Integer.parseInt(headers.get("GNU.sparse.realsize"));
            } catch (NumberFormatException e) {
                throw new IOException("Corrupted TAR archive. GNU.sparse.realsize header for " + this.name + " contains non-numeric value");
            }
        }
    }

    void fillStarSparseData(Map<String, String> headers) throws IOException {
        this.starSparse = true;
        if (headers.containsKey("SCHILY.realsize")) {
            try {
                this.realSize = Long.parseLong(headers.get("SCHILY.realsize"));
            } catch (NumberFormatException e) {
                throw new IOException("Corrupted TAR archive. SCHILY.realsize header for " + this.name + " contains non-numeric value");
            }
        }
    }

    public FileTime getCreationTime() {
        return this.birthTime;
    }

    @Override // org.apache.commons.compress.archivers.EntryStreamOffsets
    public long getDataOffset() {
        return this.dataOffset;
    }

    public int getDevMajor() {
        return this.devMajor;
    }

    public int getDevMinor() {
        return this.devMinor;
    }

    public TarArchiveEntry[] getDirectoryEntries() {
        if (this.file == null || !isDirectory()) {
            return EMPTY_TAR_ARCHIVE_ENTRY_ARRAY;
        }
        List<TarArchiveEntry> entries = new ArrayList<>();
        try {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(this.file);
            for (Path p : dirStream) {
                entries.add(new TarArchiveEntry(p));
            }
            if (dirStream != null) {
                if (0 != 0) {
                    dirStream.close();
                } else {
                    dirStream.close();
                }
            }
            return (TarArchiveEntry[]) entries.toArray(EMPTY_TAR_ARCHIVE_ENTRY_ARRAY);
        } catch (IOException e) {
            return EMPTY_TAR_ARCHIVE_ENTRY_ARRAY;
        }
    }

    public String getExtraPaxHeader(String name) {
        return this.extraPaxHeaders.get(name);
    }

    public Map<String, String> getExtraPaxHeaders() {
        return Collections.unmodifiableMap(this.extraPaxHeaders);
    }

    public File getFile() {
        if (this.file == null) {
            return null;
        }
        return this.file.toFile();
    }

    @Deprecated
    public int getGroupId() {
        return (int) (this.groupId & (-1));
    }

    public String getGroupName() {
        return this.groupName;
    }

    public FileTime getLastAccessTime() {
        return this.aTime;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public Date getLastModifiedDate() {
        return getModTime();
    }

    public FileTime getLastModifiedTime() {
        return this.mTime;
    }

    public String getLinkName() {
        return this.linkName;
    }

    public long getLongGroupId() {
        return this.groupId;
    }

    public long getLongUserId() {
        return this.userId;
    }

    public int getMode() {
        return this.mode;
    }

    public Date getModTime() {
        return new Date(this.mTime.toMillis());
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public String getName() {
        return this.name;
    }

    public List<TarArchiveStructSparse> getOrderedSparseHeaders() throws IOException {
        if (this.sparseHeaders == null || this.sparseHeaders.isEmpty()) {
            return Collections.emptyList();
        }
        List<TarArchiveStructSparse> orderedAndFiltered = (List) this.sparseHeaders.stream().filter(s -> {
            return s.getOffset() > 0 || s.getNumbytes() > 0;
        }).sorted(Comparator.comparingLong((v0) -> {
            return v0.getOffset();
        })).collect(Collectors.toList());
        int numberOfHeaders = orderedAndFiltered.size();
        for (int i = 0; i < numberOfHeaders; i++) {
            TarArchiveStructSparse str = orderedAndFiltered.get(i);
            if (i + 1 < numberOfHeaders && str.getOffset() + str.getNumbytes() > orderedAndFiltered.get(i + 1).getOffset()) {
                throw new IOException("Corrupted TAR archive. Sparse blocks for " + getName() + " overlap each other.");
            }
            if (str.getOffset() + str.getNumbytes() < 0) {
                throw new IOException("Unreadable TAR archive. Offset and numbytes for sparse block in " + getName() + " too large.");
            }
        }
        if (!orderedAndFiltered.isEmpty()) {
            TarArchiveStructSparse last = orderedAndFiltered.get(numberOfHeaders - 1);
            if (last.getOffset() + last.getNumbytes() > getRealSize()) {
                throw new IOException("Corrupted TAR archive. Sparse block extends beyond real size of the entry");
            }
        }
        return orderedAndFiltered;
    }

    public Path getPath() {
        return this.file;
    }

    public long getRealSize() {
        if (!isSparse()) {
            return getSize();
        }
        return this.realSize;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public long getSize() {
        return this.size;
    }

    public List<TarArchiveStructSparse> getSparseHeaders() {
        return this.sparseHeaders;
    }

    public FileTime getStatusChangeTime() {
        return this.cTime;
    }

    @Deprecated
    public int getUserId() {
        return (int) (this.userId & (-1));
    }

    public String getUserName() {
        return this.userName;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean isBlockDevice() {
        return this.linkFlag == 52;
    }

    public boolean isCharacterDevice() {
        return this.linkFlag == 51;
    }

    public boolean isCheckSumOK() {
        return this.checkSumOK;
    }

    public boolean isDescendent(TarArchiveEntry desc) {
        return desc.getName().startsWith(getName());
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public boolean isDirectory() {
        if (this.file != null) {
            return Files.isDirectory(this.file, this.linkOptions);
        }
        if (this.linkFlag == 53) {
            return true;
        }
        return (isPaxHeader() || isGlobalPaxHeader() || !getName().endsWith("/")) ? false : true;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public boolean isFIFO() {
        return this.linkFlag == 54;
    }

    public boolean isFile() {
        if (this.file != null) {
            return Files.isRegularFile(this.file, this.linkOptions);
        }
        return this.linkFlag == 0 || this.linkFlag == 48 || !getName().endsWith("/");
    }

    public boolean isGlobalPaxHeader() {
        return this.linkFlag == 103;
    }

    public boolean isGNULongLinkEntry() {
        return this.linkFlag == 75;
    }

    public boolean isGNULongNameEntry() {
        return this.linkFlag == 76;
    }

    public boolean isGNUSparse() {
        return isOldGNUSparse() || isPaxGNUSparse();
    }

    private boolean isInvalidPrefix(byte[] header) {
        if (header[475] != 0) {
            if (header[156] == 77) {
                if ((header[464] & 128) == 0 && header[475] != 32) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean isInvalidXtarTime(byte[] buffer, int offset, int length) {
        if ((buffer[offset] & 128) == 0) {
            int lastIndex = length - 1;
            for (int i = 0; i < lastIndex; i++) {
                byte b = buffer[offset + i];
                if (b < 48 || b > 55) {
                    return true;
                }
            }
            byte b2 = buffer[offset + lastIndex];
            if (b2 != 32 && b2 != 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isLink() {
        return this.linkFlag == 49;
    }

    public boolean isOldGNUSparse() {
        return this.linkFlag == 83;
    }

    public boolean isPaxGNU1XSparse() {
        return this.paxGNU1XSparse;
    }

    public boolean isPaxGNUSparse() {
        return this.paxGNUSparse;
    }

    public boolean isPaxHeader() {
        return this.linkFlag == 120 || this.linkFlag == 88;
    }

    public boolean isSparse() {
        return isGNUSparse() || isStarSparse();
    }

    public boolean isStarSparse() {
        return this.starSparse;
    }

    @Override // org.apache.commons.compress.archivers.EntryStreamOffsets
    public boolean isStreamContiguous() {
        return true;
    }

    public boolean isSymbolicLink() {
        return this.linkFlag == 50;
    }

    private boolean isXstar(Map<String, String> globalPaxHeaders, byte[] header) {
        if (ArchiveUtils.matchAsciiBuffer(TarConstants.MAGIC_XSTAR, header, TarConstants.XSTAR_MAGIC_OFFSET, 4)) {
            return true;
        }
        String archType = globalPaxHeaders.get("SCHILY.archtype");
        if (archType != null) {
            return "xustar".equals(archType) || "exustar".equals(archType);
        } else if (isInvalidPrefix(header) || isInvalidXtarTime(header, TarConstants.XSTAR_ATIME_OFFSET, 12) || isInvalidXtarTime(header, TarConstants.XSTAR_CTIME_OFFSET, 12)) {
            return false;
        } else {
            return true;
        }
    }

    private long parseOctalOrBinary(byte[] header, int offset, int length, boolean lenient) {
        if (lenient) {
            try {
                return TarUtils.parseOctalOrBinary(header, offset, length);
            } catch (IllegalArgumentException e) {
                return -1L;
            }
        }
        return TarUtils.parseOctalOrBinary(header, offset, length);
    }

    public void parseTarHeader(byte[] header) {
        try {
            parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
        } catch (IOException e) {
            try {
                parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true, false);
            } catch (IOException ex2) {
                throw new UncheckedIOException(ex2);
            }
        }
    }

    public void parseTarHeader(byte[] header, ZipEncoding encoding) throws IOException {
        parseTarHeader(header, encoding, false, false);
    }

    private void parseTarHeader(byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
        parseTarHeader(Collections.emptyMap(), header, encoding, oldStyle, lenient);
    }

    private void parseTarHeader(Map<String, String> globalPaxHeaders, byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
        try {
            parseTarHeaderUnwrapped(globalPaxHeaders, header, encoding, oldStyle, lenient);
        } catch (IllegalArgumentException ex) {
            throw new IOException("Corrupted TAR archive.", ex);
        }
    }

    private void parseTarHeaderUnwrapped(Map<String, String> globalPaxHeaders, byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
        String parseName;
        int offset;
        int offset2;
        int offset3;
        String parseName2;
        String parseName3;
        if (oldStyle) {
            parseName = TarUtils.parseName(header, 0, 100);
        } else {
            parseName = TarUtils.parseName(header, 0, 100, encoding);
        }
        this.name = parseName;
        int offset4 = 0 + 100;
        this.mode = (int) parseOctalOrBinary(header, offset4, 8, lenient);
        this.userId = (int) parseOctalOrBinary(header, offset, 8, lenient);
        this.groupId = (int) parseOctalOrBinary(header, offset2, 8, lenient);
        int offset5 = offset4 + 8 + 8 + 8;
        this.size = TarUtils.parseOctalOrBinary(header, offset5, 12);
        if (this.size < 0) {
            throw new IOException("broken archive, entry with negative size");
        }
        int offset6 = offset5 + 12;
        this.mTime = FileTime.from(parseOctalOrBinary(header, offset6, 12, lenient), TimeUnit.SECONDS);
        this.checkSumOK = TarUtils.verifyCheckSum(header);
        int offset7 = offset6 + 12 + 8;
        int offset8 = offset7 + 1;
        this.linkFlag = header[offset7];
        this.linkName = oldStyle ? TarUtils.parseName(header, offset8, 100) : TarUtils.parseName(header, offset8, 100, encoding);
        int offset9 = offset8 + 100;
        this.magic = TarUtils.parseName(header, offset9, 6);
        int offset10 = offset9 + 6;
        this.version = TarUtils.parseName(header, offset10, 2);
        int offset11 = offset10 + 2;
        this.userName = oldStyle ? TarUtils.parseName(header, offset11, 32) : TarUtils.parseName(header, offset11, 32, encoding);
        int offset12 = offset11 + 32;
        this.groupName = oldStyle ? TarUtils.parseName(header, offset12, 32) : TarUtils.parseName(header, offset12, 32, encoding);
        int offset13 = offset12 + 32;
        if (this.linkFlag == 51 || this.linkFlag == 52) {
            this.devMajor = (int) parseOctalOrBinary(header, offset13, 8, lenient);
            int offset14 = offset13 + 8;
            this.devMinor = (int) parseOctalOrBinary(header, offset14, 8, lenient);
            offset3 = offset14 + 8;
        } else {
            offset3 = offset13 + 16;
        }
        int type = evaluateType(globalPaxHeaders, header);
        switch (type) {
            case 2:
                this.aTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset3, 12, lenient));
                int offset15 = offset3 + 12;
                this.cTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset15, 12, lenient));
                int offset16 = offset15 + 12 + 12 + 4 + 1;
                this.sparseHeaders = new ArrayList(TarUtils.readSparseStructs(header, offset16, 4));
                int offset17 = offset16 + 96;
                this.isExtended = TarUtils.parseBoolean(header, offset17);
                int offset18 = offset17 + 1;
                this.realSize = TarUtils.parseOctal(header, offset18, 12);
                int i = offset18 + 12;
                return;
            case 3:
            default:
                if (oldStyle) {
                    parseName2 = TarUtils.parseName(header, offset3, TarConstants.PREFIXLEN);
                } else {
                    parseName2 = TarUtils.parseName(header, offset3, TarConstants.PREFIXLEN, encoding);
                }
                String prefix = parseName2;
                int i2 = offset3 + TarConstants.PREFIXLEN;
                if (isDirectory() && !this.name.endsWith("/")) {
                    this.name += "/";
                }
                if (!prefix.isEmpty()) {
                    this.name = prefix + "/" + this.name;
                    return;
                }
                return;
            case 4:
                if (oldStyle) {
                    parseName3 = TarUtils.parseName(header, offset3, TarConstants.PREFIXLEN_XSTAR);
                } else {
                    parseName3 = TarUtils.parseName(header, offset3, TarConstants.PREFIXLEN_XSTAR, encoding);
                }
                String xstarPrefix = parseName3;
                int offset19 = offset3 + TarConstants.PREFIXLEN_XSTAR;
                if (!xstarPrefix.isEmpty()) {
                    this.name = xstarPrefix + "/" + this.name;
                }
                this.aTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset19, 12, lenient));
                int offset20 = offset19 + 12;
                this.cTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset20, 12, lenient));
                int i3 = offset20 + 12;
                return;
        }
    }

    private void processPaxHeader(String key, String val) throws IOException {
        processPaxHeader(key, val, this.extraPaxHeaders);
    }

    private void processPaxHeader(String key, String val, Map<String, String> headers) throws IOException {
        boolean z = true;
        switch (key.hashCode()) {
            case -1916861932:
                if (key.equals("SCHILY.devmajor")) {
                    z = true;
                    break;
                }
                break;
            case -1916619760:
                if (key.equals("SCHILY.devminor")) {
                    z = true;
                    break;
                }
                break;
            case -277496563:
                if (key.equals("GNU.sparse.realsize")) {
                    z = true;
                    break;
                }
                break;
            case -160380561:
                if (key.equals("GNU.sparse.size")) {
                    z = true;
                    break;
                }
                break;
            case 102338:
                if (key.equals("gid")) {
                    z = true;
                    break;
                }
                break;
            case 115792:
                if (key.equals("uid")) {
                    z = true;
                    break;
                }
                break;
            case 3433509:
                if (key.equals(ClientCookie.PATH_ATTR)) {
                    z = false;
                    break;
                }
                break;
            case 3530753:
                if (key.equals("size")) {
                    z = true;
                    break;
                }
                break;
            case 93141678:
                if (key.equals("atime")) {
                    z = true;
                    break;
                }
                break;
            case 94988720:
                if (key.equals("ctime")) {
                    z = true;
                    break;
                }
                break;
            case 98496370:
                if (key.equals("gname")) {
                    z = true;
                    break;
                }
                break;
            case 104223930:
                if (key.equals("mtime")) {
                    z = true;
                    break;
                }
                break;
            case 111425664:
                if (key.equals("uname")) {
                    z = true;
                    break;
                }
                break;
            case 304222685:
                if (key.equals("LIBARCHIVE.creationtime")) {
                    z = true;
                    break;
                }
                break;
            case 530706950:
                if (key.equals("SCHILY.filetype")) {
                    z = true;
                    break;
                }
                break;
            case 1195018015:
                if (key.equals("linkpath")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                setName(val);
                return;
            case true:
                setLinkName(val);
                return;
            case true:
                setGroupId(Long.parseLong(val));
                return;
            case true:
                setGroupName(val);
                return;
            case true:
                setUserId(Long.parseLong(val));
                return;
            case true:
                setUserName(val);
                return;
            case true:
                long size = Long.parseLong(val);
                if (size < 0) {
                    throw new IOException("Corrupted TAR archive. Entry size is negative");
                }
                setSize(size);
                return;
            case true:
                setLastModifiedTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
                return;
            case true:
                setLastAccessTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
                return;
            case true:
                setStatusChangeTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
                return;
            case true:
                setCreationTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
                return;
            case true:
                int devMinor = Integer.parseInt(val);
                if (devMinor < 0) {
                    throw new IOException("Corrupted TAR archive. Dev-Minor is negative");
                }
                setDevMinor(devMinor);
                return;
            case true:
                int devMajor = Integer.parseInt(val);
                if (devMajor < 0) {
                    throw new IOException("Corrupted TAR archive. Dev-Major is negative");
                }
                setDevMajor(devMajor);
                return;
            case true:
                fillGNUSparse0xData(headers);
                return;
            case NID.kEmptyStream /* 14 */:
                fillGNUSparse1xData(headers);
                return;
            case NID.kEmptyFile /* 15 */:
                if ("sparse".equals(val)) {
                    fillStarSparseData(headers);
                    return;
                }
                return;
            default:
                this.extraPaxHeaders.put(key, val);
                return;
        }
    }

    private void readFileMode(Path file, String normalizedName, LinkOption... options) throws IOException {
        if (Files.isDirectory(file, options)) {
            this.mode = DEFAULT_DIR_MODE;
            this.linkFlag = (byte) 53;
            int nameLength = normalizedName.length();
            if (nameLength == 0 || normalizedName.charAt(nameLength - 1) != '/') {
                this.name = normalizedName + "/";
                return;
            } else {
                this.name = normalizedName;
                return;
            }
        }
        this.mode = DEFAULT_FILE_MODE;
        this.linkFlag = (byte) 48;
        this.name = normalizedName;
        this.size = Files.size(file);
    }

    private void readOsSpecificProperties(Path file, LinkOption... options) throws IOException {
        Set<String> availableAttributeViews = file.getFileSystem().supportedFileAttributeViews();
        if (availableAttributeViews.contains("posix")) {
            PosixFileAttributes posixFileAttributes = (PosixFileAttributes) Files.readAttributes(file, PosixFileAttributes.class, options);
            setLastModifiedTime(posixFileAttributes.lastModifiedTime());
            setCreationTime(posixFileAttributes.creationTime());
            setLastAccessTime(posixFileAttributes.lastAccessTime());
            this.userName = posixFileAttributes.owner().getName();
            this.groupName = posixFileAttributes.group().getName();
            if (availableAttributeViews.contains("unix")) {
                this.userId = ((Number) Files.getAttribute(file, "unix:uid", options)).longValue();
                this.groupId = ((Number) Files.getAttribute(file, "unix:gid", options)).longValue();
                try {
                    setStatusChangeTime((FileTime) Files.getAttribute(file, "unix:ctime", options));
                } catch (IllegalArgumentException e) {
                }
            }
        } else if (availableAttributeViews.contains("dos")) {
            DosFileAttributes dosFileAttributes = (DosFileAttributes) Files.readAttributes(file, DosFileAttributes.class, options);
            setLastModifiedTime(dosFileAttributes.lastModifiedTime());
            setCreationTime(dosFileAttributes.creationTime());
            setLastAccessTime(dosFileAttributes.lastAccessTime());
            this.userName = Files.getOwner(file, options).getName();
        } else {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class, options);
            setLastModifiedTime(basicFileAttributes.lastModifiedTime());
            setCreationTime(basicFileAttributes.creationTime());
            setLastAccessTime(basicFileAttributes.lastAccessTime());
            this.userName = Files.getOwner(file, options).getName();
        }
    }

    public void setCreationTime(FileTime time) {
        this.birthTime = time;
    }

    public void setDataOffset(long dataOffset) {
        if (dataOffset < 0) {
            throw new IllegalArgumentException("The offset can not be smaller than 0");
        }
        this.dataOffset = dataOffset;
    }

    public void setDevMajor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Major device number is out of range: " + devNo);
        }
        this.devMajor = devNo;
    }

    public void setDevMinor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
        }
        this.devMinor = devNo;
    }

    public void setGroupId(int groupId) {
        setGroupId(groupId);
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setIds(int userId, int groupId) {
        setUserId(userId);
        setGroupId(groupId);
    }

    public void setLastAccessTime(FileTime time) {
        this.aTime = time;
    }

    public void setLastModifiedTime(FileTime time) {
        this.mTime = (FileTime) Objects.requireNonNull(time, "Time must not be null");
    }

    public void setLinkName(String link) {
        this.linkName = link;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setModTime(Date time) {
        setLastModifiedTime(FileTime.fromMillis(time.getTime()));
    }

    public void setModTime(FileTime time) {
        setLastModifiedTime(time);
    }

    public void setModTime(long time) {
        setLastModifiedTime(FileTime.fromMillis(time));
    }

    public void setName(String name) {
        this.name = normalizeFileName(name, this.preserveAbsolutePath);
    }

    public void setNames(String userName, String groupName) {
        setUserName(userName);
        setGroupName(groupName);
    }

    public void setSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size is out of range: " + size);
        }
        this.size = size;
    }

    public void setSparseHeaders(List<TarArchiveStructSparse> sparseHeaders) {
        this.sparseHeaders = sparseHeaders;
    }

    public void setStatusChangeTime(FileTime time) {
        this.cTime = time;
    }

    public void setUserId(int userId) {
        setUserId(userId);
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateEntryFromPaxHeaders(Map<String, String> headers) throws IOException {
        for (Map.Entry<String, String> ent : headers.entrySet()) {
            processPaxHeader(ent.getKey(), ent.getValue(), headers);
        }
    }

    public void writeEntryHeader(byte[] outbuf) {
        try {
            writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
        } catch (IOException e) {
            try {
                writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
            } catch (IOException ex2) {
                throw new UncheckedIOException(ex2);
            }
        }
    }

    public void writeEntryHeader(byte[] outbuf, ZipEncoding encoding, boolean starMode) throws IOException {
        int offset = TarUtils.formatNameBytes(this.name, outbuf, 0, 100, encoding);
        int offset2 = writeEntryHeaderField(this.mTime.to(TimeUnit.SECONDS), outbuf, writeEntryHeaderField(this.size, outbuf, writeEntryHeaderField(this.groupId, outbuf, writeEntryHeaderField(this.userId, outbuf, writeEntryHeaderField(this.mode, outbuf, offset, 8, starMode), 8, starMode), 8, starMode), 12, starMode), 12, starMode);
        int offset3 = fill((byte) 32, offset2, outbuf, 8);
        outbuf[offset3] = this.linkFlag;
        int offset4 = writeEntryHeaderField(this.devMinor, outbuf, writeEntryHeaderField(this.devMajor, outbuf, TarUtils.formatNameBytes(this.groupName, outbuf, TarUtils.formatNameBytes(this.userName, outbuf, TarUtils.formatNameBytes(this.version, outbuf, TarUtils.formatNameBytes(this.magic, outbuf, TarUtils.formatNameBytes(this.linkName, outbuf, offset3 + 1, 100, encoding), 6), 2), 32, encoding), 32, encoding), 8, starMode), 8, starMode);
        if (starMode) {
            offset4 = fill(0, fill(0, writeEntryHeaderOptionalTimeField(this.cTime, writeEntryHeaderOptionalTimeField(this.aTime, fill(0, offset4, outbuf, TarConstants.PREFIXLEN_XSTAR), outbuf, 12), outbuf, 12), outbuf, 8), outbuf, 4);
        }
        fill(0, offset4, outbuf, outbuf.length - offset4);
        long chk = TarUtils.computeCheckSum(outbuf);
        TarUtils.formatCheckSumOctalBytes(chk, outbuf, offset2, 8);
    }

    private int writeEntryHeaderField(long value, byte[] outbuf, int offset, int length, boolean starMode) {
        if (!starMode && (value < 0 || value >= (1 << (3 * (length - 1))))) {
            return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
        }
        return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
    }

    private int writeEntryHeaderOptionalTimeField(FileTime time, int offset, byte[] outbuf, int fieldLength) {
        int offset2;
        if (time != null) {
            offset2 = writeEntryHeaderField(time.to(TimeUnit.SECONDS), outbuf, offset, fieldLength, true);
        } else {
            offset2 = fill(0, offset, outbuf, fieldLength);
        }
        return offset2;
    }
}
