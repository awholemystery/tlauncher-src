package org.apache.commons.compress.harmony.unpack200;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.harmony.pack200.BHSDCodec;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/SegmentHeader.class */
public class SegmentHeader {
    private int archiveMajor;
    private int archiveMinor;
    private long archiveModtime;
    private long archiveSize;
    private int attributeDefinitionCount;
    private InputStream bandHeadersInputStream;
    private int bandHeadersSize;
    private int classCount;
    private int cpClassCount;
    private int cpDescriptorCount;
    private int cpDoubleCount;
    private int cpFieldCount;
    private int cpFloatCount;
    private int cpIMethodCount;
    private int cpIntCount;
    private int cpLongCount;
    private int cpMethodCount;
    private int cpSignatureCount;
    private int cpStringCount;
    private int cpUTF8Count;
    private int defaultClassMajorVersion;
    private int defaultClassMinorVersion;
    private int innerClassCount;
    private int numberOfFiles;
    private int segmentsRemaining;
    private SegmentOptions options;
    private final Segment segment;
    private static final int[] magic = {HttpStatus.SC_ACCEPTED, 254, 208, 13};
    private int archiveSizeOffset;

    public SegmentHeader(Segment segment) {
        this.segment = segment;
    }

    public int getArchiveSizeOffset() {
        return this.archiveSizeOffset;
    }

    public void read(InputStream in) throws IOException, Error, Pack200Exception {
        int[] word = decodeScalar("archive_magic_word", in, Codec.BYTE1, magic.length);
        for (int m = 0; m < magic.length; m++) {
            if (word[m] != magic[m]) {
                throw new Error("Bad header");
            }
        }
        setArchiveMinorVersion(decodeScalar("archive_minver", in, Codec.UNSIGNED5));
        setArchiveMajorVersion(decodeScalar("archive_majver", in, Codec.UNSIGNED5));
        this.options = new SegmentOptions(decodeScalar("archive_options", in, Codec.UNSIGNED5));
        parseArchiveFileCounts(in);
        parseArchiveSpecialCounts(in);
        parseCpCounts(in);
        parseClassCounts(in);
        if (getBandHeadersSize() > 0) {
            byte[] bandHeaders = new byte[getBandHeadersSize()];
            readFully(in, bandHeaders);
            setBandHeadersData(bandHeaders);
        }
        this.archiveSizeOffset -= in.available();
    }

    public void unpack() {
    }

    private void setArchiveMinorVersion(int version) throws Pack200Exception {
        if (version != 7) {
            throw new Pack200Exception("Invalid segment minor version");
        }
        this.archiveMinor = version;
    }

    private void setArchiveMajorVersion(int version) throws Pack200Exception {
        if (version != 150) {
            throw new Pack200Exception("Invalid segment major version: " + version);
        }
        this.archiveMajor = version;
    }

    public long getArchiveModtime() {
        return this.archiveModtime;
    }

    public int getAttributeDefinitionCount() {
        return this.attributeDefinitionCount;
    }

    public int getClassCount() {
        return this.classCount;
    }

    public int getCpClassCount() {
        return this.cpClassCount;
    }

    public int getCpDescriptorCount() {
        return this.cpDescriptorCount;
    }

    public int getCpDoubleCount() {
        return this.cpDoubleCount;
    }

    public int getCpFieldCount() {
        return this.cpFieldCount;
    }

    public int getCpFloatCount() {
        return this.cpFloatCount;
    }

    public int getCpIMethodCount() {
        return this.cpIMethodCount;
    }

    public int getCpIntCount() {
        return this.cpIntCount;
    }

    public int getCpLongCount() {
        return this.cpLongCount;
    }

    public int getCpMethodCount() {
        return this.cpMethodCount;
    }

    public int getCpSignatureCount() {
        return this.cpSignatureCount;
    }

    public int getCpStringCount() {
        return this.cpStringCount;
    }

    public int getCpUTF8Count() {
        return this.cpUTF8Count;
    }

    public int getDefaultClassMajorVersion() {
        return this.defaultClassMajorVersion;
    }

    public int getDefaultClassMinorVersion() {
        return this.defaultClassMinorVersion;
    }

    public int getInnerClassCount() {
        return this.innerClassCount;
    }

    public long getArchiveSize() {
        return this.archiveSize;
    }

    public InputStream getBandHeadersInputStream() {
        if (this.bandHeadersInputStream == null) {
            this.bandHeadersInputStream = new ByteArrayInputStream(new byte[0]);
        }
        return this.bandHeadersInputStream;
    }

    public int getNumberOfFiles() {
        return this.numberOfFiles;
    }

    public int getSegmentsRemaining() {
        return this.segmentsRemaining;
    }

    public SegmentOptions getOptions() {
        return this.options;
    }

    private void parseArchiveFileCounts(InputStream in) throws IOException, Pack200Exception {
        if (this.options.hasArchiveFileCounts()) {
            setArchiveSize((decodeScalar("archive_size_hi", in, Codec.UNSIGNED5) << 32) | decodeScalar("archive_size_lo", in, Codec.UNSIGNED5));
            this.archiveSizeOffset = in.available();
            setSegmentsRemaining(decodeScalar("archive_next_count", in, Codec.UNSIGNED5));
            setArchiveModtime(decodeScalar("archive_modtime", in, Codec.UNSIGNED5));
            this.numberOfFiles = decodeScalar("file_count", in, Codec.UNSIGNED5);
        }
    }

    private void parseArchiveSpecialCounts(InputStream in) throws IOException, Pack200Exception {
        if (getOptions().hasSpecialFormats()) {
            this.bandHeadersSize = decodeScalar("band_headers_size", in, Codec.UNSIGNED5);
            setAttributeDefinitionCount(decodeScalar("attr_definition_count", in, Codec.UNSIGNED5));
        }
    }

    private void parseClassCounts(InputStream in) throws IOException, Pack200Exception {
        this.innerClassCount = decodeScalar("ic_count", in, Codec.UNSIGNED5);
        this.defaultClassMinorVersion = decodeScalar("default_class_minver", in, Codec.UNSIGNED5);
        this.defaultClassMajorVersion = decodeScalar("default_class_majver", in, Codec.UNSIGNED5);
        this.classCount = decodeScalar("class_count", in, Codec.UNSIGNED5);
    }

    private void parseCpCounts(InputStream in) throws IOException, Pack200Exception {
        this.cpUTF8Count = decodeScalar("cp_Utf8_count", in, Codec.UNSIGNED5);
        if (getOptions().hasCPNumberCounts()) {
            this.cpIntCount = decodeScalar("cp_Int_count", in, Codec.UNSIGNED5);
            this.cpFloatCount = decodeScalar("cp_Float_count", in, Codec.UNSIGNED5);
            this.cpLongCount = decodeScalar("cp_Long_count", in, Codec.UNSIGNED5);
            this.cpDoubleCount = decodeScalar("cp_Double_count", in, Codec.UNSIGNED5);
        }
        this.cpStringCount = decodeScalar("cp_String_count", in, Codec.UNSIGNED5);
        this.cpClassCount = decodeScalar("cp_Class_count", in, Codec.UNSIGNED5);
        this.cpSignatureCount = decodeScalar("cp_Signature_count", in, Codec.UNSIGNED5);
        this.cpDescriptorCount = decodeScalar("cp_Descr_count", in, Codec.UNSIGNED5);
        this.cpFieldCount = decodeScalar("cp_Field_count", in, Codec.UNSIGNED5);
        this.cpMethodCount = decodeScalar("cp_Method_count", in, Codec.UNSIGNED5);
        this.cpIMethodCount = decodeScalar("cp_Imethod_count", in, Codec.UNSIGNED5);
    }

    private int[] decodeScalar(String name, InputStream in, BHSDCodec codec, int n) throws IOException, Pack200Exception {
        this.segment.log(2, "Parsed #" + name + " (" + n + ")");
        return codec.decodeInts(n, in);
    }

    private int decodeScalar(String name, InputStream in, BHSDCodec codec) throws IOException, Pack200Exception {
        int ret = codec.decode(in);
        this.segment.log(2, "Parsed #" + name + " as " + ret);
        return ret;
    }

    public void setArchiveModtime(long archiveModtime) {
        this.archiveModtime = archiveModtime;
    }

    public void setArchiveSize(long archiveSize) {
        this.archiveSize = archiveSize;
    }

    private void setAttributeDefinitionCount(long valuie) {
        this.attributeDefinitionCount = (int) valuie;
    }

    private void setBandHeadersData(byte[] bandHeaders) {
        this.bandHeadersInputStream = new ByteArrayInputStream(bandHeaders);
    }

    public void setSegmentsRemaining(long value) {
        this.segmentsRemaining = (int) value;
    }

    private void readFully(InputStream in, byte[] data) throws IOException {
        int total = in.read(data);
        if (total == -1) {
            throw new EOFException("Failed to read any data from input stream");
        }
        while (total < data.length) {
            int delta = in.read(data, total, data.length - total);
            if (delta == -1) {
                throw new EOFException("Failed to read some data from input stream");
            }
            total += delta;
        }
    }

    public int getBandHeadersSize() {
        return this.bandHeadersSize;
    }
}
