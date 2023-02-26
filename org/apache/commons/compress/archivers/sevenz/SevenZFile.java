package org.apache.commons.compress.archivers.sevenz;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.apache.commons.compress.MemoryLimitException;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.ByteUtils;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/SevenZFile.class */
public class SevenZFile implements Closeable {
    static final int SIGNATURE_HEADER_SIZE = 32;
    private static final String DEFAULT_FILE_NAME = "unknown archive";
    private final String fileName;
    private SeekableByteChannel channel;
    private final Archive archive;
    private int currentEntryIndex;
    private int currentFolderIndex;
    private InputStream currentFolderInputStream;
    private byte[] password;
    private final SevenZFileOptions options;
    private long compressedBytesReadFromCurrentEntry;
    private long uncompressedBytesReadFromCurrentEntry;
    private final ArrayList<InputStream> deferredBlockStreams;
    static final byte[] sevenZSignature = {55, 122, -68, -81, 39, 28};

    public SevenZFile(File fileName, char[] password) throws IOException {
        this(fileName, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(File fileName, char[] password, SevenZFileOptions options) throws IOException {
        this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), new FileAttribute[0]), fileName.getAbsolutePath(), utf16Decode(password), true, options);
    }

    @Deprecated
    public SevenZFile(File fileName, byte[] password) throws IOException {
        this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), new FileAttribute[0]), fileName.getAbsolutePath(), password, true, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel) throws IOException {
        this(channel, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, SevenZFileOptions options) throws IOException {
        this(channel, DEFAULT_FILE_NAME, null, options);
    }

    public SevenZFile(SeekableByteChannel channel, char[] password) throws IOException {
        this(channel, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, char[] password, SevenZFileOptions options) throws IOException {
        this(channel, DEFAULT_FILE_NAME, password, options);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, char[] password) throws IOException {
        this(channel, fileName, password, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, char[] password, SevenZFileOptions options) throws IOException {
        this(channel, fileName, utf16Decode(password), false, options);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName) throws IOException {
        this(channel, fileName, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(SeekableByteChannel channel, String fileName, SevenZFileOptions options) throws IOException {
        this(channel, fileName, null, false, options);
    }

    @Deprecated
    public SevenZFile(SeekableByteChannel channel, byte[] password) throws IOException {
        this(channel, DEFAULT_FILE_NAME, password);
    }

    @Deprecated
    public SevenZFile(SeekableByteChannel channel, String fileName, byte[] password) throws IOException {
        this(channel, fileName, password, false, SevenZFileOptions.DEFAULT);
    }

    private SevenZFile(SeekableByteChannel channel, String filename, byte[] password, boolean closeOnError, SevenZFileOptions options) throws IOException {
        this.currentEntryIndex = -1;
        this.currentFolderIndex = -1;
        this.deferredBlockStreams = new ArrayList<>();
        boolean succeeded = false;
        this.channel = channel;
        this.fileName = filename;
        this.options = options;
        try {
            this.archive = readHeaders(password);
            if (password != null) {
                this.password = Arrays.copyOf(password, password.length);
            } else {
                this.password = null;
            }
            succeeded = true;
        } finally {
            if (!succeeded && closeOnError) {
                this.channel.close();
            }
        }
    }

    public SevenZFile(File fileName) throws IOException {
        this(fileName, SevenZFileOptions.DEFAULT);
    }

    public SevenZFile(File fileName, SevenZFileOptions options) throws IOException {
        this(fileName, (char[]) null, options);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.channel != null) {
            try {
                this.channel.close();
            } finally {
                this.channel = null;
                if (this.password != null) {
                    Arrays.fill(this.password, (byte) 0);
                }
                this.password = null;
            }
        }
    }

    public SevenZArchiveEntry getNextEntry() throws IOException {
        if (this.currentEntryIndex >= this.archive.files.length - 1) {
            return null;
        }
        this.currentEntryIndex++;
        SevenZArchiveEntry entry = this.archive.files[this.currentEntryIndex];
        if (entry.getName() == null && this.options.getUseDefaultNameForUnnamedEntries()) {
            entry.setName(getDefaultName());
        }
        buildDecodingStream(this.currentEntryIndex, false);
        this.compressedBytesReadFromCurrentEntry = 0L;
        this.uncompressedBytesReadFromCurrentEntry = 0L;
        return entry;
    }

    public Iterable<SevenZArchiveEntry> getEntries() {
        return new ArrayList(Arrays.asList(this.archive.files));
    }

    private Archive readHeaders(byte[] password) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
        readFully(buf);
        byte[] signature = new byte[6];
        buf.get(signature);
        if (!Arrays.equals(signature, sevenZSignature)) {
            throw new IOException("Bad 7z signature");
        }
        byte archiveVersionMajor = buf.get();
        byte archiveVersionMinor = buf.get();
        if (archiveVersionMajor != 0) {
            throw new IOException(String.format("Unsupported 7z version (%d,%d)", Byte.valueOf(archiveVersionMajor), Byte.valueOf(archiveVersionMinor)));
        }
        boolean headerLooksValid = false;
        long startHeaderCrc = 4294967295L & buf.getInt();
        if (startHeaderCrc == 0) {
            long currentPosition = this.channel.position();
            ByteBuffer peekBuf = ByteBuffer.allocate(20);
            readFully(peekBuf);
            this.channel.position(currentPosition);
            while (true) {
                if (!peekBuf.hasRemaining()) {
                    break;
                } else if (peekBuf.get() != 0) {
                    headerLooksValid = true;
                    break;
                }
            }
        } else {
            headerLooksValid = true;
        }
        if (headerLooksValid) {
            return initializeArchive(readStartHeader(startHeaderCrc), password, true);
        }
        if (this.options.getTryToRecoverBrokenArchives()) {
            return tryToLocateEndHeader(password);
        }
        throw new IOException("archive seems to be invalid.\nYou may want to retry and enable the tryToRecoverBrokenArchives if the archive could be a multi volume archive that has been closed prematurely.");
    }

    private Archive tryToLocateEndHeader(byte[] password) throws IOException {
        long minPos;
        ByteBuffer nidBuf = ByteBuffer.allocate(1);
        long previousDataSize = this.channel.position() + 20;
        if (this.channel.position() + 1048576 > this.channel.size()) {
            minPos = this.channel.position();
        } else {
            minPos = this.channel.size() - 1048576;
        }
        long pos = this.channel.size() - 1;
        while (pos > minPos) {
            pos--;
            this.channel.position(pos);
            nidBuf.rewind();
            if (this.channel.read(nidBuf) < 1) {
                throw new EOFException();
            }
            byte b = nidBuf.array()[0];
            if (b == 23 || b == 1) {
                try {
                    StartHeader startHeader = new StartHeader();
                    startHeader.nextHeaderOffset = pos - previousDataSize;
                    startHeader.nextHeaderSize = this.channel.size() - pos;
                    Archive result = initializeArchive(startHeader, password, false);
                    if (result.packSizes.length > 0 && result.files.length > 0) {
                        return result;
                    }
                } catch (Exception e) {
                }
            }
        }
        throw new IOException("Start header corrupt and unable to guess end header");
    }

    private Archive initializeArchive(StartHeader startHeader, byte[] password, boolean verifyCrc) throws IOException {
        assertFitsIntoNonNegativeInt("nextHeaderSize", startHeader.nextHeaderSize);
        int nextHeaderSizeInt = (int) startHeader.nextHeaderSize;
        this.channel.position(32 + startHeader.nextHeaderOffset);
        if (verifyCrc) {
            long position = this.channel.position();
            CheckedInputStream cis = new CheckedInputStream(Channels.newInputStream(this.channel), new CRC32());
            if (cis.skip(nextHeaderSizeInt) != nextHeaderSizeInt) {
                throw new IOException("Problem computing NextHeader CRC-32");
            }
            if (startHeader.nextHeaderCrc != cis.getChecksum().getValue()) {
                throw new IOException("NextHeader CRC-32 mismatch");
            }
            this.channel.position(position);
        }
        Archive archive = new Archive();
        ByteBuffer buf = ByteBuffer.allocate(nextHeaderSizeInt).order(ByteOrder.LITTLE_ENDIAN);
        readFully(buf);
        int nid = getUnsignedByte(buf);
        if (nid == 23) {
            buf = readEncodedHeader(buf, archive, password);
            archive = new Archive();
            nid = getUnsignedByte(buf);
        }
        if (nid != 1) {
            throw new IOException("Broken or unsupported archive: no Header");
        }
        readHeader(buf, archive);
        archive.subStreamsInfo = null;
        return archive;
    }

    private StartHeader readStartHeader(long startHeaderCrc) throws IOException {
        StartHeader startHeader = new StartHeader();
        DataInputStream dataInputStream = new DataInputStream(new CRC32VerifyingInputStream(new BoundedSeekableByteChannelInputStream(this.channel, 20L), 20L, startHeaderCrc));
        Throwable th = null;
        try {
            startHeader.nextHeaderOffset = Long.reverseBytes(dataInputStream.readLong());
            if (startHeader.nextHeaderOffset < 0 || startHeader.nextHeaderOffset + 32 > this.channel.size()) {
                throw new IOException("nextHeaderOffset is out of bounds");
            }
            startHeader.nextHeaderSize = Long.reverseBytes(dataInputStream.readLong());
            long nextHeaderEnd = startHeader.nextHeaderOffset + startHeader.nextHeaderSize;
            if (nextHeaderEnd < startHeader.nextHeaderOffset || nextHeaderEnd + 32 > this.channel.size()) {
                throw new IOException("nextHeaderSize is out of bounds");
            }
            startHeader.nextHeaderCrc = 4294967295L & Integer.reverseBytes(dataInputStream.readInt());
            if (dataInputStream != null) {
                if (0 != 0) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    dataInputStream.close();
                }
            }
            return startHeader;
        } finally {
        }
    }

    private void readHeader(ByteBuffer header, Archive archive) throws IOException {
        int pos = header.position();
        ArchiveStatistics stats = sanityCheckAndCollectStatistics(header);
        stats.assertValidity(this.options.getMaxMemoryLimitInKb());
        header.position(pos);
        int nid = getUnsignedByte(header);
        if (nid == 2) {
            readArchiveProperties(header);
            nid = getUnsignedByte(header);
        }
        if (nid == 3) {
            throw new IOException("Additional streams unsupported");
        }
        if (nid == 4) {
            readStreamsInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid == 5) {
            readFilesInfo(header, archive);
            getUnsignedByte(header);
        }
    }

    private ArchiveStatistics sanityCheckAndCollectStatistics(ByteBuffer header) throws IOException {
        ArchiveStatistics stats = new ArchiveStatistics();
        int nid = getUnsignedByte(header);
        if (nid == 2) {
            sanityCheckArchiveProperties(header);
            nid = getUnsignedByte(header);
        }
        if (nid == 3) {
            throw new IOException("Additional streams unsupported");
        }
        if (nid == 4) {
            sanityCheckStreamsInfo(header, stats);
            nid = getUnsignedByte(header);
        }
        if (nid == 5) {
            sanityCheckFilesInfo(header, stats);
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated header, found " + nid);
        }
        return stats;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0006 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readArchiveProperties(java.nio.ByteBuffer r4) throws java.io.IOException {
        /*
            r3 = this;
            r0 = r4
            int r0 = getUnsignedByte(r0)
            r5 = r0
        L5:
            r0 = r5
            if (r0 == 0) goto L22
            r0 = r4
            long r0 = readUint64(r0)
            r6 = r0
            r0 = r6
            int r0 = (int) r0
            byte[] r0 = new byte[r0]
            r8 = r0
            r0 = r4
            r1 = r8
            get(r0, r1)
            r0 = r4
            int r0 = getUnsignedByte(r0)
            r5 = r0
            goto L5
        L22:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.readArchiveProperties(java.nio.ByteBuffer):void");
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0006 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void sanityCheckArchiveProperties(java.nio.ByteBuffer r6) throws java.io.IOException {
        /*
            r5 = this;
            r0 = r6
            int r0 = getUnsignedByte(r0)
            r7 = r0
        L5:
            r0 = r7
            if (r0 == 0) goto L31
            java.lang.String r0 = "propertySize"
            r1 = r6
            long r1 = readUint64(r1)
            int r0 = assertFitsIntoNonNegativeInt(r0, r1)
            r8 = r0
            r0 = r6
            r1 = r8
            long r1 = (long) r1
            long r0 = skipBytesFully(r0, r1)
            r1 = r8
            long r1 = (long) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L29
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "invalid property size"
            r1.<init>(r2)
            throw r0
        L29:
            r0 = r6
            int r0 = getUnsignedByte(r0)
            r7 = r0
            goto L5
        L31:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.sanityCheckArchiveProperties(java.nio.ByteBuffer):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ByteBuffer readEncodedHeader(ByteBuffer header, Archive archive, byte[] password) throws IOException {
        int pos = header.position();
        ArchiveStatistics stats = new ArchiveStatistics();
        sanityCheckStreamsInfo(header, stats);
        stats.assertValidity(this.options.getMaxMemoryLimitInKb());
        header.position(pos);
        readStreamsInfo(header, archive);
        if (archive.folders == null || archive.folders.length == 0) {
            throw new IOException("no folders, can't read encoded header");
        }
        if (archive.packSizes == null || archive.packSizes.length == 0) {
            throw new IOException("no packed streams, can't read encoded header");
        }
        Folder folder = archive.folders[0];
        long folderOffset = 32 + archive.packPos + 0;
        this.channel.position(folderOffset);
        InputStream inputStreamStack = new BoundedSeekableByteChannelInputStream(this.channel, archive.packSizes[0]);
        for (Coder coder : folder.getOrderedCoders()) {
            if (coder.numInStreams != 1 || coder.numOutStreams != 1) {
                throw new IOException("Multi input/output stream coders are not yet supported");
            }
            inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder.getUnpackSizeForCoder(coder), coder, password, this.options.getMaxMemoryLimitInKb());
        }
        if (folder.hasCrc) {
            inputStreamStack = new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc);
        }
        int unpackSize = assertFitsIntoNonNegativeInt("unpackSize", folder.getUnpackSize());
        byte[] nextHeader = IOUtils.readRange(inputStreamStack, unpackSize);
        if (nextHeader.length < unpackSize) {
            throw new IOException("premature end of stream");
        }
        inputStreamStack.close();
        return ByteBuffer.wrap(nextHeader).order(ByteOrder.LITTLE_ENDIAN);
    }

    private void sanityCheckStreamsInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid == 6) {
            sanityCheckPackInfo(header, stats);
            nid = getUnsignedByte(header);
        }
        if (nid == 7) {
            sanityCheckUnpackInfo(header, stats);
            nid = getUnsignedByte(header);
        }
        if (nid == 8) {
            sanityCheckSubStreamsInfo(header, stats);
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated StreamsInfo");
        }
    }

    private void readStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid == 6) {
            readPackInfo(header, archive);
            nid = getUnsignedByte(header);
        }
        if (nid == 7) {
            readUnpackInfo(header, archive);
            nid = getUnsignedByte(header);
        } else {
            archive.folders = Folder.EMPTY_FOLDER_ARRAY;
        }
        if (nid == 8) {
            readSubStreamsInfo(header, archive);
            getUnsignedByte(header);
        }
    }

    private void sanityCheckPackInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        long packPos = readUint64(header);
        if (packPos < 0 || 32 + packPos > this.channel.size() || 32 + packPos < 0) {
            throw new IOException("packPos (" + packPos + ") is out of range");
        }
        long numPackStreams = readUint64(header);
        stats.numberOfPackedStreams = assertFitsIntoNonNegativeInt("numPackStreams", numPackStreams);
        int nid = getUnsignedByte(header);
        if (nid == 9) {
            long totalPackSizes = 0;
            for (int i = 0; i < stats.numberOfPackedStreams; i++) {
                long packSize = readUint64(header);
                totalPackSizes += packSize;
                long endOfPackStreams = 32 + packPos + totalPackSizes;
                if (packSize < 0 || endOfPackStreams > this.channel.size() || endOfPackStreams < packPos) {
                    throw new IOException("packSize (" + packSize + ") is out of range");
                }
            }
            nid = getUnsignedByte(header);
        }
        if (nid == 10) {
            int crcsDefined = readAllOrBits(header, stats.numberOfPackedStreams).cardinality();
            if (skipBytesFully(header, 4 * crcsDefined) < 4 * crcsDefined) {
                throw new IOException("invalid number of CRCs in PackInfo");
            }
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated PackInfo (" + nid + ")");
        }
    }

    private void readPackInfo(ByteBuffer header, Archive archive) throws IOException {
        archive.packPos = readUint64(header);
        int numPackStreamsInt = (int) readUint64(header);
        int nid = getUnsignedByte(header);
        if (nid == 9) {
            archive.packSizes = new long[numPackStreamsInt];
            for (int i = 0; i < archive.packSizes.length; i++) {
                archive.packSizes[i] = readUint64(header);
            }
            nid = getUnsignedByte(header);
        }
        if (nid == 10) {
            archive.packCrcsDefined = readAllOrBits(header, numPackStreamsInt);
            archive.packCrcs = new long[numPackStreamsInt];
            for (int i2 = 0; i2 < numPackStreamsInt; i2++) {
                if (archive.packCrcsDefined.get(i2)) {
                    archive.packCrcs[i2] = 4294967295L & getInt(header);
                }
            }
            getUnsignedByte(header);
        }
    }

    private void sanityCheckUnpackInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        int nid = getUnsignedByte(header);
        if (nid != 11) {
            throw new IOException("Expected kFolder, got " + nid);
        }
        long numFolders = readUint64(header);
        stats.numberOfFolders = assertFitsIntoNonNegativeInt("numFolders", numFolders);
        int external = getUnsignedByte(header);
        if (external != 0) {
            throw new IOException("External unsupported");
        }
        List<Integer> numberOfOutputStreamsPerFolder = new LinkedList<>();
        for (int i = 0; i < stats.numberOfFolders; i++) {
            numberOfOutputStreamsPerFolder.add(Integer.valueOf(sanityCheckFolder(header, stats)));
        }
        long totalNumberOfBindPairs = stats.numberOfOutStreams - stats.numberOfFolders;
        long packedStreamsRequiredByFolders = stats.numberOfInStreams - totalNumberOfBindPairs;
        if (packedStreamsRequiredByFolders < stats.numberOfPackedStreams) {
            throw new IOException("archive doesn't contain enough packed streams");
        }
        int nid2 = getUnsignedByte(header);
        if (nid2 != 12) {
            throw new IOException("Expected kCodersUnpackSize, got " + nid2);
        }
        for (Integer num : numberOfOutputStreamsPerFolder) {
            int numberOfOutputStreams = num.intValue();
            for (int i2 = 0; i2 < numberOfOutputStreams; i2++) {
                long unpackSize = readUint64(header);
                if (unpackSize < 0) {
                    throw new IllegalArgumentException("negative unpackSize");
                }
            }
        }
        int nid3 = getUnsignedByte(header);
        if (nid3 == 10) {
            stats.folderHasCrc = readAllOrBits(header, stats.numberOfFolders);
            int crcsDefined = stats.folderHasCrc.cardinality();
            if (skipBytesFully(header, 4 * crcsDefined) < 4 * crcsDefined) {
                throw new IOException("invalid number of CRCs in UnpackInfo");
            }
            nid3 = getUnsignedByte(header);
        }
        if (nid3 != 0) {
            throw new IOException("Badly terminated UnpackInfo");
        }
    }

    private void readUnpackInfo(ByteBuffer header, Archive archive) throws IOException {
        getUnsignedByte(header);
        int numFoldersInt = (int) readUint64(header);
        Folder[] folders = new Folder[numFoldersInt];
        archive.folders = folders;
        getUnsignedByte(header);
        for (int i = 0; i < numFoldersInt; i++) {
            folders[i] = readFolder(header);
        }
        getUnsignedByte(header);
        for (Folder folder : folders) {
            assertFitsIntoNonNegativeInt("totalOutputStreams", folder.totalOutputStreams);
            folder.unpackSizes = new long[(int) folder.totalOutputStreams];
            for (int i2 = 0; i2 < folder.totalOutputStreams; i2++) {
                folder.unpackSizes[i2] = readUint64(header);
            }
        }
        int nid = getUnsignedByte(header);
        if (nid == 10) {
            BitSet crcsDefined = readAllOrBits(header, numFoldersInt);
            for (int i3 = 0; i3 < numFoldersInt; i3++) {
                if (crcsDefined.get(i3)) {
                    folders[i3].hasCrc = true;
                    folders[i3].crc = 4294967295L & getInt(header);
                } else {
                    folders[i3].hasCrc = false;
                }
            }
            getUnsignedByte(header);
        }
    }

    private void sanityCheckSubStreamsInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        int nid = getUnsignedByte(header);
        List<Integer> numUnpackSubStreamsPerFolder = new LinkedList<>();
        if (nid == 13) {
            for (int i = 0; i < stats.numberOfFolders; i++) {
                numUnpackSubStreamsPerFolder.add(Integer.valueOf(assertFitsIntoNonNegativeInt("numStreams", readUint64(header))));
            }
            stats.numberOfUnpackSubStreams = numUnpackSubStreamsPerFolder.stream().mapToLong((v0) -> {
                return v0.longValue();
            }).sum();
            nid = getUnsignedByte(header);
        } else {
            stats.numberOfUnpackSubStreams = stats.numberOfFolders;
        }
        assertFitsIntoNonNegativeInt("totalUnpackStreams", stats.numberOfUnpackSubStreams);
        if (nid == 9) {
            for (Integer num : numUnpackSubStreamsPerFolder) {
                int numUnpackSubStreams = num.intValue();
                if (numUnpackSubStreams != 0) {
                    for (int i2 = 0; i2 < numUnpackSubStreams - 1; i2++) {
                        long size = readUint64(header);
                        if (size < 0) {
                            throw new IOException("negative unpackSize");
                        }
                    }
                    continue;
                }
            }
            nid = getUnsignedByte(header);
        }
        int numDigests = 0;
        if (!numUnpackSubStreamsPerFolder.isEmpty()) {
            int folderIdx = 0;
            for (Integer num2 : numUnpackSubStreamsPerFolder) {
                int numUnpackSubStreams2 = num2.intValue();
                if (numUnpackSubStreams2 == 1 && stats.folderHasCrc != null) {
                    int i3 = folderIdx;
                    folderIdx++;
                    if (!stats.folderHasCrc.get(i3)) {
                    }
                }
                numDigests += numUnpackSubStreams2;
            }
        } else {
            numDigests = stats.folderHasCrc == null ? stats.numberOfFolders : stats.numberOfFolders - stats.folderHasCrc.cardinality();
        }
        if (nid == 10) {
            assertFitsIntoNonNegativeInt("numDigests", numDigests);
            int missingCrcs = readAllOrBits(header, numDigests).cardinality();
            if (skipBytesFully(header, 4 * missingCrcs) < 4 * missingCrcs) {
                throw new IOException("invalid number of missing CRCs in SubStreamInfo");
            }
            nid = getUnsignedByte(header);
        }
        if (nid != 0) {
            throw new IOException("Badly terminated SubStreamsInfo");
        }
    }

    private void readSubStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
        Folder[] folderArr;
        Folder[] folderArr2;
        Folder[] folderArr3;
        Folder[] folderArr4;
        for (Folder folder : archive.folders) {
            folder.numUnpackSubStreams = 1;
        }
        long unpackStreamsCount = archive.folders.length;
        int nid = getUnsignedByte(header);
        if (nid == 13) {
            unpackStreamsCount = 0;
            for (Folder folder2 : archive.folders) {
                long numStreams = readUint64(header);
                folder2.numUnpackSubStreams = (int) numStreams;
                unpackStreamsCount += numStreams;
            }
            nid = getUnsignedByte(header);
        }
        int totalUnpackStreams = (int) unpackStreamsCount;
        SubStreamsInfo subStreamsInfo = new SubStreamsInfo();
        subStreamsInfo.unpackSizes = new long[totalUnpackStreams];
        subStreamsInfo.hasCrc = new BitSet(totalUnpackStreams);
        subStreamsInfo.crcs = new long[totalUnpackStreams];
        int nextUnpackStream = 0;
        for (Folder folder3 : archive.folders) {
            if (folder3.numUnpackSubStreams != 0) {
                long sum = 0;
                if (nid == 9) {
                    for (int i = 0; i < folder3.numUnpackSubStreams - 1; i++) {
                        long size = readUint64(header);
                        int i2 = nextUnpackStream;
                        nextUnpackStream++;
                        subStreamsInfo.unpackSizes[i2] = size;
                        sum += size;
                    }
                }
                if (sum > folder3.getUnpackSize()) {
                    throw new IOException("sum of unpack sizes of folder exceeds total unpack size");
                }
                int i3 = nextUnpackStream;
                nextUnpackStream++;
                subStreamsInfo.unpackSizes[i3] = folder3.getUnpackSize() - sum;
            }
        }
        if (nid == 9) {
            nid = getUnsignedByte(header);
        }
        int numDigests = 0;
        for (Folder folder4 : archive.folders) {
            if (folder4.numUnpackSubStreams != 1 || !folder4.hasCrc) {
                numDigests += folder4.numUnpackSubStreams;
            }
        }
        if (nid == 10) {
            BitSet hasMissingCrc = readAllOrBits(header, numDigests);
            long[] missingCrcs = new long[numDigests];
            for (int i4 = 0; i4 < numDigests; i4++) {
                if (hasMissingCrc.get(i4)) {
                    missingCrcs[i4] = 4294967295L & getInt(header);
                }
            }
            int nextCrc = 0;
            int nextMissingCrc = 0;
            for (Folder folder5 : archive.folders) {
                if (folder5.numUnpackSubStreams == 1 && folder5.hasCrc) {
                    subStreamsInfo.hasCrc.set(nextCrc, true);
                    subStreamsInfo.crcs[nextCrc] = folder5.crc;
                    nextCrc++;
                } else {
                    for (int i5 = 0; i5 < folder5.numUnpackSubStreams; i5++) {
                        subStreamsInfo.hasCrc.set(nextCrc, hasMissingCrc.get(nextMissingCrc));
                        subStreamsInfo.crcs[nextCrc] = missingCrcs[nextMissingCrc];
                        nextCrc++;
                        nextMissingCrc++;
                    }
                }
            }
            getUnsignedByte(header);
        }
        archive.subStreamsInfo = subStreamsInfo;
    }

    private int sanityCheckFolder(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        int numCoders = assertFitsIntoNonNegativeInt("numCoders", readUint64(header));
        if (numCoders == 0) {
            throw new IOException("Folder without coders");
        }
        stats.numberOfCoders += numCoders;
        long totalOutStreams = 0;
        long totalInStreams = 0;
        for (int i = 0; i < numCoders; i++) {
            int bits = getUnsignedByte(header);
            int idSize = bits & 15;
            get(header, new byte[idSize]);
            boolean isSimple = (bits & 16) == 0;
            boolean hasAttributes = (bits & 32) != 0;
            boolean moreAlternativeMethods = (bits & 128) != 0;
            if (moreAlternativeMethods) {
                throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either.");
            }
            if (isSimple) {
                totalInStreams++;
                totalOutStreams++;
            } else {
                totalInStreams += assertFitsIntoNonNegativeInt("numInStreams", readUint64(header));
                totalOutStreams += assertFitsIntoNonNegativeInt("numOutStreams", readUint64(header));
            }
            if (hasAttributes) {
                int propertiesSize = assertFitsIntoNonNegativeInt("propertiesSize", readUint64(header));
                if (skipBytesFully(header, propertiesSize) < propertiesSize) {
                    throw new IOException("invalid propertiesSize in folder");
                }
            }
        }
        assertFitsIntoNonNegativeInt("totalInStreams", totalInStreams);
        assertFitsIntoNonNegativeInt("totalOutStreams", totalOutStreams);
        stats.numberOfOutStreams += totalOutStreams;
        stats.numberOfInStreams += totalInStreams;
        if (totalOutStreams == 0) {
            throw new IOException("Total output streams can't be 0");
        }
        int numBindPairs = assertFitsIntoNonNegativeInt("numBindPairs", totalOutStreams - 1);
        if (totalInStreams < numBindPairs) {
            throw new IOException("Total input streams can't be less than the number of bind pairs");
        }
        BitSet inStreamsBound = new BitSet((int) totalInStreams);
        for (int i2 = 0; i2 < numBindPairs; i2++) {
            int inIndex = assertFitsIntoNonNegativeInt("inIndex", readUint64(header));
            if (totalInStreams <= inIndex) {
                throw new IOException("inIndex is bigger than number of inStreams");
            }
            inStreamsBound.set(inIndex);
            int outIndex = assertFitsIntoNonNegativeInt("outIndex", readUint64(header));
            if (totalOutStreams <= outIndex) {
                throw new IOException("outIndex is bigger than number of outStreams");
            }
        }
        int numPackedStreams = assertFitsIntoNonNegativeInt("numPackedStreams", totalInStreams - numBindPairs);
        if (numPackedStreams == 1) {
            if (inStreamsBound.nextClearBit(0) == -1) {
                throw new IOException("Couldn't find stream's bind pair index");
            }
        } else {
            for (int i3 = 0; i3 < numPackedStreams; i3++) {
                int packedStreamIndex = assertFitsIntoNonNegativeInt("packedStreamIndex", readUint64(header));
                if (packedStreamIndex >= totalInStreams) {
                    throw new IOException("packedStreamIndex is bigger than number of totalInStreams");
                }
            }
        }
        return (int) totalOutStreams;
    }

    private Folder readFolder(ByteBuffer header) throws IOException {
        Folder folder = new Folder();
        long numCoders = readUint64(header);
        Coder[] coders = new Coder[(int) numCoders];
        long totalInStreams = 0;
        long totalOutStreams = 0;
        for (int i = 0; i < coders.length; i++) {
            coders[i] = new Coder();
            int bits = getUnsignedByte(header);
            int idSize = bits & 15;
            boolean isSimple = (bits & 16) == 0;
            boolean hasAttributes = (bits & 32) != 0;
            boolean moreAlternativeMethods = (bits & 128) != 0;
            coders[i].decompressionMethodId = new byte[idSize];
            get(header, coders[i].decompressionMethodId);
            if (isSimple) {
                coders[i].numInStreams = 1L;
                coders[i].numOutStreams = 1L;
            } else {
                coders[i].numInStreams = readUint64(header);
                coders[i].numOutStreams = readUint64(header);
            }
            totalInStreams += coders[i].numInStreams;
            totalOutStreams += coders[i].numOutStreams;
            if (hasAttributes) {
                long propertiesSize = readUint64(header);
                coders[i].properties = new byte[(int) propertiesSize];
                get(header, coders[i].properties);
            }
            if (moreAlternativeMethods) {
                throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either.");
            }
        }
        folder.coders = coders;
        folder.totalInputStreams = totalInStreams;
        folder.totalOutputStreams = totalOutStreams;
        long numBindPairs = totalOutStreams - 1;
        BindPair[] bindPairs = new BindPair[(int) numBindPairs];
        for (int i2 = 0; i2 < bindPairs.length; i2++) {
            bindPairs[i2] = new BindPair();
            bindPairs[i2].inIndex = readUint64(header);
            bindPairs[i2].outIndex = readUint64(header);
        }
        folder.bindPairs = bindPairs;
        long numPackedStreams = totalInStreams - numBindPairs;
        long[] packedStreams = new long[(int) numPackedStreams];
        if (numPackedStreams == 1) {
            int i3 = 0;
            while (i3 < ((int) totalInStreams) && folder.findBindPairForInStream(i3) >= 0) {
                i3++;
            }
            packedStreams[0] = i3;
        } else {
            for (int i4 = 0; i4 < ((int) numPackedStreams); i4++) {
                packedStreams[i4] = readUint64(header);
            }
        }
        folder.packedStreams = packedStreams;
        return folder;
    }

    private BitSet readAllOrBits(ByteBuffer header, int size) throws IOException {
        BitSet bits;
        int areAllDefined = getUnsignedByte(header);
        if (areAllDefined != 0) {
            bits = new BitSet(size);
            for (int i = 0; i < size; i++) {
                bits.set(i, true);
            }
        } else {
            bits = readBits(header, size);
        }
        return bits;
    }

    private BitSet readBits(ByteBuffer header, int size) throws IOException {
        BitSet bits = new BitSet(size);
        int mask = 0;
        int cache = 0;
        for (int i = 0; i < size; i++) {
            if (mask == 0) {
                mask = 128;
                cache = getUnsignedByte(header);
            }
            bits.set(i, (cache & mask) != 0);
            mask >>>= 1;
        }
        return bits;
    }

    private void sanityCheckFilesInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
        stats.numberOfEntries = assertFitsIntoNonNegativeInt("numFiles", readUint64(header));
        int emptyStreams = -1;
        while (true) {
            int propertyType = getUnsignedByte(header);
            if (propertyType != 0) {
                long size = readUint64(header);
                switch (propertyType) {
                    case NID.kEmptyStream /* 14 */:
                        emptyStreams = readBits(header, stats.numberOfEntries).cardinality();
                        break;
                    case NID.kEmptyFile /* 15 */:
                        if (emptyStreams == -1) {
                            throw new IOException("Header format error: kEmptyStream must appear before kEmptyFile");
                        }
                        readBits(header, emptyStreams);
                        break;
                    case 16:
                        if (emptyStreams == -1) {
                            throw new IOException("Header format error: kEmptyStream must appear before kAnti");
                        }
                        readBits(header, emptyStreams);
                        break;
                    case 17:
                        int external = getUnsignedByte(header);
                        if (external != 0) {
                            throw new IOException("Not implemented");
                        }
                        int namesLength = assertFitsIntoNonNegativeInt("file names length", size - 1);
                        if ((namesLength & 1) != 0) {
                            throw new IOException("File names length invalid");
                        }
                        int filesSeen = 0;
                        for (int i = 0; i < namesLength; i += 2) {
                            char c = getChar(header);
                            if (c == 0) {
                                filesSeen++;
                            }
                        }
                        if (filesSeen != stats.numberOfEntries) {
                            throw new IOException("Invalid number of file names (" + filesSeen + " instead of " + stats.numberOfEntries + ")");
                        }
                        break;
                    case NID.kCTime /* 18 */:
                        int timesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
                        int external2 = getUnsignedByte(header);
                        if (external2 != 0) {
                            throw new IOException("Not implemented");
                        }
                        if (skipBytesFully(header, 8 * timesDefined) < 8 * timesDefined) {
                            throw new IOException("invalid creation dates size");
                        }
                        break;
                    case NID.kATime /* 19 */:
                        int timesDefined2 = readAllOrBits(header, stats.numberOfEntries).cardinality();
                        int external3 = getUnsignedByte(header);
                        if (external3 != 0) {
                            throw new IOException("Not implemented");
                        }
                        if (skipBytesFully(header, 8 * timesDefined2) < 8 * timesDefined2) {
                            throw new IOException("invalid access dates size");
                        }
                        break;
                    case 20:
                        int timesDefined3 = readAllOrBits(header, stats.numberOfEntries).cardinality();
                        int external4 = getUnsignedByte(header);
                        if (external4 != 0) {
                            throw new IOException("Not implemented");
                        }
                        if (skipBytesFully(header, 8 * timesDefined3) < 8 * timesDefined3) {
                            throw new IOException("invalid modification dates size");
                        }
                        break;
                    case 21:
                        int attributesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
                        int external5 = getUnsignedByte(header);
                        if (external5 != 0) {
                            throw new IOException("Not implemented");
                        }
                        if (skipBytesFully(header, 4 * attributesDefined) < 4 * attributesDefined) {
                            throw new IOException("invalid windows attributes size");
                        }
                        break;
                    case NID.kComment /* 22 */:
                    case 23:
                    default:
                        if (skipBytesFully(header, size) < size) {
                            throw new IOException("Incomplete property of type " + propertyType);
                        }
                        break;
                    case 24:
                        throw new IOException("kStartPos is unsupported, please report");
                    case NID.kDummy /* 25 */:
                        if (skipBytesFully(header, size) < size) {
                            throw new IOException("Incomplete kDummy property");
                        }
                        break;
                }
            } else {
                stats.numberOfEntriesWithStream = stats.numberOfEntries - Math.max(emptyStreams, 0);
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0123, code lost:
        throw new java.io.IOException("Error parsing file names");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readFilesInfo(java.nio.ByteBuffer r9, org.apache.commons.compress.archivers.sevenz.Archive r10) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 945
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZFile.readFilesInfo(java.nio.ByteBuffer, org.apache.commons.compress.archivers.sevenz.Archive):void");
    }

    private void checkEntryIsInitialized(Map<Integer, SevenZArchiveEntry> archiveEntries, int index) {
        if (archiveEntries.get(Integer.valueOf(index)) == null) {
            archiveEntries.put(Integer.valueOf(index), new SevenZArchiveEntry());
        }
    }

    private void calculateStreamMap(Archive archive) throws IOException {
        StreamMap streamMap = new StreamMap();
        int nextFolderPackStreamIndex = 0;
        int numFolders = archive.folders != null ? archive.folders.length : 0;
        streamMap.folderFirstPackStreamIndex = new int[numFolders];
        for (int i = 0; i < numFolders; i++) {
            streamMap.folderFirstPackStreamIndex[i] = nextFolderPackStreamIndex;
            nextFolderPackStreamIndex += archive.folders[i].packedStreams.length;
        }
        long nextPackStreamOffset = 0;
        int numPackSizes = archive.packSizes.length;
        streamMap.packStreamOffsets = new long[numPackSizes];
        for (int i2 = 0; i2 < numPackSizes; i2++) {
            streamMap.packStreamOffsets[i2] = nextPackStreamOffset;
            nextPackStreamOffset += archive.packSizes[i2];
        }
        streamMap.folderFirstFileIndex = new int[numFolders];
        streamMap.fileFolderIndex = new int[archive.files.length];
        int nextFolderIndex = 0;
        int nextFolderUnpackStreamIndex = 0;
        for (int i3 = 0; i3 < archive.files.length; i3++) {
            if (!archive.files[i3].hasStream() && nextFolderUnpackStreamIndex == 0) {
                streamMap.fileFolderIndex[i3] = -1;
            } else {
                if (nextFolderUnpackStreamIndex == 0) {
                    while (nextFolderIndex < archive.folders.length) {
                        streamMap.folderFirstFileIndex[nextFolderIndex] = i3;
                        if (archive.folders[nextFolderIndex].numUnpackSubStreams > 0) {
                            break;
                        }
                        nextFolderIndex++;
                    }
                    if (nextFolderIndex >= archive.folders.length) {
                        throw new IOException("Too few folders in archive");
                    }
                }
                streamMap.fileFolderIndex[i3] = nextFolderIndex;
                if (archive.files[i3].hasStream()) {
                    nextFolderUnpackStreamIndex++;
                    if (nextFolderUnpackStreamIndex >= archive.folders[nextFolderIndex].numUnpackSubStreams) {
                        nextFolderIndex++;
                        nextFolderUnpackStreamIndex = 0;
                    }
                }
            }
        }
        archive.streamMap = streamMap;
    }

    private void buildDecodingStream(int entryIndex, boolean isRandomAccess) throws IOException {
        if (this.archive.streamMap == null) {
            throw new IOException("Archive doesn't contain stream information to read entries");
        }
        int folderIndex = this.archive.streamMap.fileFolderIndex[entryIndex];
        if (folderIndex < 0) {
            this.deferredBlockStreams.clear();
            return;
        }
        SevenZArchiveEntry file = this.archive.files[entryIndex];
        boolean isInSameFolder = false;
        if (this.currentFolderIndex == folderIndex) {
            if (entryIndex > 0) {
                file.setContentMethods(this.archive.files[entryIndex - 1].getContentMethods());
            }
            if (isRandomAccess && file.getContentMethods() == null) {
                int folderFirstFileIndex = this.archive.streamMap.folderFirstFileIndex[folderIndex];
                SevenZArchiveEntry folderFirstFile = this.archive.files[folderFirstFileIndex];
                file.setContentMethods(folderFirstFile.getContentMethods());
            }
            isInSameFolder = true;
        } else {
            this.currentFolderIndex = folderIndex;
            reopenFolderInputStream(folderIndex, file);
        }
        boolean haveSkippedEntries = false;
        if (isRandomAccess) {
            haveSkippedEntries = skipEntriesWhenNeeded(entryIndex, isInSameFolder, folderIndex);
        }
        if (isRandomAccess && this.currentEntryIndex == entryIndex && !haveSkippedEntries) {
            return;
        }
        InputStream fileStream = new BoundedInputStream(this.currentFolderInputStream, file.getSize());
        if (file.getHasCrc()) {
            fileStream = new CRC32VerifyingInputStream(fileStream, file.getSize(), file.getCrcValue());
        }
        this.deferredBlockStreams.add(fileStream);
    }

    private void reopenFolderInputStream(int folderIndex, SevenZArchiveEntry file) throws IOException {
        this.deferredBlockStreams.clear();
        if (this.currentFolderInputStream != null) {
            this.currentFolderInputStream.close();
            this.currentFolderInputStream = null;
        }
        Folder folder = this.archive.folders[folderIndex];
        int firstPackStreamIndex = this.archive.streamMap.folderFirstPackStreamIndex[folderIndex];
        long folderOffset = 32 + this.archive.packPos + this.archive.streamMap.packStreamOffsets[firstPackStreamIndex];
        this.currentFolderInputStream = buildDecoderStack(folder, folderOffset, firstPackStreamIndex, file);
    }

    private boolean skipEntriesWhenNeeded(int entryIndex, boolean isInSameFolder, int folderIndex) throws IOException {
        SevenZArchiveEntry file = this.archive.files[entryIndex];
        if (this.currentEntryIndex == entryIndex && !hasCurrentEntryBeenRead()) {
            return false;
        }
        int filesToSkipStartIndex = this.archive.streamMap.folderFirstFileIndex[this.currentFolderIndex];
        if (isInSameFolder) {
            if (this.currentEntryIndex < entryIndex) {
                filesToSkipStartIndex = this.currentEntryIndex + 1;
            } else {
                reopenFolderInputStream(folderIndex, file);
            }
        }
        for (int i = filesToSkipStartIndex; i < entryIndex; i++) {
            SevenZArchiveEntry fileToSkip = this.archive.files[i];
            InputStream fileStreamToSkip = new BoundedInputStream(this.currentFolderInputStream, fileToSkip.getSize());
            if (fileToSkip.getHasCrc()) {
                fileStreamToSkip = new CRC32VerifyingInputStream(fileStreamToSkip, fileToSkip.getSize(), fileToSkip.getCrcValue());
            }
            this.deferredBlockStreams.add(fileStreamToSkip);
            fileToSkip.setContentMethods(file.getContentMethods());
        }
        return true;
    }

    private boolean hasCurrentEntryBeenRead() {
        boolean hasCurrentEntryBeenRead = false;
        if (!this.deferredBlockStreams.isEmpty()) {
            InputStream currentEntryInputStream = this.deferredBlockStreams.get(this.deferredBlockStreams.size() - 1);
            if (currentEntryInputStream instanceof CRC32VerifyingInputStream) {
                hasCurrentEntryBeenRead = ((CRC32VerifyingInputStream) currentEntryInputStream).getBytesRemaining() != this.archive.files[this.currentEntryIndex].getSize();
            }
            if (currentEntryInputStream instanceof BoundedInputStream) {
                hasCurrentEntryBeenRead = ((BoundedInputStream) currentEntryInputStream).getBytesRemaining() != this.archive.files[this.currentEntryIndex].getSize();
            }
        }
        return hasCurrentEntryBeenRead;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private InputStream buildDecoderStack(Folder folder, long folderOffset, int firstPackStreamIndex, SevenZArchiveEntry entry) throws IOException {
        this.channel.position(folderOffset);
        InputStream inputStreamStack = new FilterInputStream(new BufferedInputStream(new BoundedSeekableByteChannelInputStream(this.channel, this.archive.packSizes[firstPackStreamIndex]))) { // from class: org.apache.commons.compress.archivers.sevenz.SevenZFile.1
            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read() throws IOException {
                int r = this.in.read();
                if (r >= 0) {
                    count(1);
                }
                return r;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] b) throws IOException {
                return read(b, 0, b.length);
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] b, int off, int len) throws IOException {
                if (len == 0) {
                    return 0;
                }
                int r = this.in.read(b, off, len);
                if (r >= 0) {
                    count(r);
                }
                return r;
            }

            private void count(int c) {
                SevenZFile.this.compressedBytesReadFromCurrentEntry += c;
            }
        };
        LinkedList<SevenZMethodConfiguration> methods = new LinkedList<>();
        for (Coder coder : folder.getOrderedCoders()) {
            if (coder.numInStreams != 1 || coder.numOutStreams != 1) {
                throw new IOException("Multi input/output stream coders are not yet supported");
            }
            SevenZMethod method = SevenZMethod.byId(coder.decompressionMethodId);
            inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder.getUnpackSizeForCoder(coder), coder, this.password, this.options.getMaxMemoryLimitInKb());
            methods.addFirst(new SevenZMethodConfiguration(method, Coders.findByMethod(method).getOptionsFromCoder(coder, inputStreamStack)));
        }
        entry.setContentMethods(methods);
        if (folder.hasCrc) {
            return new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc);
        }
        return inputStreamStack;
    }

    public int read() throws IOException {
        int b = getCurrentStream().read();
        if (b >= 0) {
            this.uncompressedBytesReadFromCurrentEntry++;
        }
        return b;
    }

    private InputStream getCurrentStream() throws IOException {
        if (this.archive.files[this.currentEntryIndex].getSize() == 0) {
            return new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY);
        }
        if (this.deferredBlockStreams.isEmpty()) {
            throw new IllegalStateException("No current 7z entry (call getNextEntry() first).");
        }
        while (this.deferredBlockStreams.size() > 1) {
            InputStream stream = this.deferredBlockStreams.remove(0);
            Throwable th = null;
            try {
                IOUtils.skip(stream, Long.MAX_VALUE);
                if (stream != null) {
                    if (0 != 0) {
                        try {
                            stream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        stream.close();
                    }
                }
                this.compressedBytesReadFromCurrentEntry = 0L;
            } finally {
            }
        }
        return this.deferredBlockStreams.get(0);
    }

    public InputStream getInputStream(SevenZArchiveEntry entry) throws IOException {
        int entryIndex = -1;
        int i = 0;
        while (true) {
            if (i >= this.archive.files.length) {
                break;
            } else if (entry != this.archive.files[i]) {
                i++;
            } else {
                entryIndex = i;
                break;
            }
        }
        if (entryIndex < 0) {
            throw new IllegalArgumentException("Can not find " + entry.getName() + " in " + this.fileName);
        }
        buildDecodingStream(entryIndex, true);
        this.currentEntryIndex = entryIndex;
        this.currentFolderIndex = this.archive.streamMap.fileFolderIndex[entryIndex];
        return getCurrentStream();
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int cnt = getCurrentStream().read(b, off, len);
        if (cnt > 0) {
            this.uncompressedBytesReadFromCurrentEntry += cnt;
        }
        return cnt;
    }

    public InputStreamStatistics getStatisticsForCurrentEntry() {
        return new InputStreamStatistics() { // from class: org.apache.commons.compress.archivers.sevenz.SevenZFile.2
            @Override // org.apache.commons.compress.utils.InputStreamStatistics
            public long getCompressedCount() {
                return SevenZFile.this.compressedBytesReadFromCurrentEntry;
            }

            @Override // org.apache.commons.compress.utils.InputStreamStatistics
            public long getUncompressedCount() {
                return SevenZFile.this.uncompressedBytesReadFromCurrentEntry;
            }
        };
    }

    private static long readUint64(ByteBuffer in) throws IOException {
        long firstByte = getUnsignedByte(in);
        int mask = 128;
        long value = 0;
        for (int i = 0; i < 8; i++) {
            if ((firstByte & mask) == 0) {
                return value | ((firstByte & (mask - 1)) << (8 * i));
            }
            long nextByte = getUnsignedByte(in);
            value |= nextByte << (8 * i);
            mask >>>= 1;
        }
        return value;
    }

    private static char getChar(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 2) {
            throw new EOFException();
        }
        return buf.getChar();
    }

    private static int getInt(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 4) {
            throw new EOFException();
        }
        return buf.getInt();
    }

    private static long getLong(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 8) {
            throw new EOFException();
        }
        return buf.getLong();
    }

    private static void get(ByteBuffer buf, byte[] to) throws IOException {
        if (buf.remaining() < to.length) {
            throw new EOFException();
        }
        buf.get(to);
    }

    private static int getUnsignedByte(ByteBuffer buf) throws IOException {
        if (!buf.hasRemaining()) {
            throw new EOFException();
        }
        return buf.get() & 255;
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < sevenZSignature.length) {
            return false;
        }
        for (int i = 0; i < sevenZSignature.length; i++) {
            if (signature[i] != sevenZSignature[i]) {
                return false;
            }
        }
        return true;
    }

    private static long skipBytesFully(ByteBuffer input, long bytesToSkip) {
        if (bytesToSkip < 1) {
            return 0L;
        }
        int current = input.position();
        int maxSkip = input.remaining();
        if (maxSkip < bytesToSkip) {
            bytesToSkip = maxSkip;
        }
        input.position(current + ((int) bytesToSkip));
        return bytesToSkip;
    }

    private void readFully(ByteBuffer buf) throws IOException {
        buf.rewind();
        IOUtils.readFully(this.channel, buf);
        buf.flip();
    }

    public String toString() {
        return this.archive.toString();
    }

    public String getDefaultName() {
        if (DEFAULT_FILE_NAME.equals(this.fileName) || this.fileName == null) {
            return null;
        }
        String lastSegment = new File(this.fileName).getName();
        int dotPos = lastSegment.lastIndexOf(".");
        if (dotPos > 0) {
            return lastSegment.substring(0, dotPos);
        }
        return lastSegment + "~";
    }

    private static byte[] utf16Decode(char[] chars) {
        if (chars == null) {
            return null;
        }
        ByteBuffer encoded = StandardCharsets.UTF_16LE.encode(CharBuffer.wrap(chars));
        if (encoded.hasArray()) {
            return encoded.array();
        }
        byte[] e = new byte[encoded.remaining()];
        encoded.get(e);
        return e;
    }

    private static int assertFitsIntoNonNegativeInt(String what, long value) throws IOException {
        if (value > 2147483647L || value < 0) {
            throw new IOException("Cannot handle " + what + " " + value);
        }
        return (int) value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/SevenZFile$ArchiveStatistics.class */
    public static class ArchiveStatistics {
        private int numberOfPackedStreams;
        private long numberOfCoders;
        private long numberOfOutStreams;
        private long numberOfInStreams;
        private long numberOfUnpackSubStreams;
        private int numberOfFolders;
        private BitSet folderHasCrc;
        private int numberOfEntries;
        private int numberOfEntriesWithStream;

        private ArchiveStatistics() {
        }

        public String toString() {
            return "Archive with " + this.numberOfEntries + " entries in " + this.numberOfFolders + " folders. Estimated size " + (estimateSize() / 1024) + " kB.";
        }

        long estimateSize() {
            long lowerBound = (16 * this.numberOfPackedStreams) + (this.numberOfPackedStreams / 8) + (this.numberOfFolders * folderSize()) + (this.numberOfCoders * coderSize()) + ((this.numberOfOutStreams - this.numberOfFolders) * bindPairSize()) + (8 * ((this.numberOfInStreams - this.numberOfOutStreams) + this.numberOfFolders)) + (8 * this.numberOfOutStreams) + (this.numberOfEntries * entrySize()) + streamMapSize();
            return 2 * lowerBound;
        }

        void assertValidity(int maxMemoryLimitInKb) throws IOException {
            if (this.numberOfEntriesWithStream > 0 && this.numberOfFolders == 0) {
                throw new IOException("archive with entries but no folders");
            }
            if (this.numberOfEntriesWithStream > this.numberOfUnpackSubStreams) {
                throw new IOException("archive doesn't contain enough substreams for entries");
            }
            long memoryNeededInKb = estimateSize() / 1024;
            if (maxMemoryLimitInKb < memoryNeededInKb) {
                throw new MemoryLimitException(memoryNeededInKb, maxMemoryLimitInKb);
            }
        }

        private long folderSize() {
            return 30L;
        }

        private long coderSize() {
            return 22L;
        }

        private long bindPairSize() {
            return 16L;
        }

        private long entrySize() {
            return 100L;
        }

        private long streamMapSize() {
            return (8 * this.numberOfFolders) + (8 * this.numberOfPackedStreams) + (4 * this.numberOfEntries);
        }
    }
}
