package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.compress.harmony.pack200.Archive;
import org.apache.commons.compress.utils.ExactMath;
import org.objectweb.asm.ClassReader;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/FileBands.class */
public class FileBands extends BandSet {
    private final CPUTF8[] fileName;
    private int[] file_name;
    private final int[] file_modtime;
    private final long[] file_size;
    private final int[] file_options;
    private final byte[][] file_bits;
    private final List<Archive.PackingFile> fileList;
    private final PackingOptions options;
    private final CpBands cpBands;

    /* JADX WARN: Type inference failed for: r1v14, types: [byte[], byte[][]] */
    public FileBands(CpBands cpBands, SegmentHeader segmentHeader, PackingOptions options, Archive.SegmentUnit segmentUnit, int effort) {
        super(effort, segmentHeader);
        this.fileList = segmentUnit.getFileList();
        this.options = options;
        this.cpBands = cpBands;
        int size = this.fileList.size();
        this.fileName = new CPUTF8[size];
        this.file_modtime = new int[size];
        this.file_size = new long[size];
        this.file_options = new int[size];
        int totalSize = 0;
        this.file_bits = new byte[size];
        int archiveModtime = segmentHeader.getArchive_modtime();
        Set<String> classNames = new HashSet<>();
        for (ClassReader reader : segmentUnit.getClassList()) {
            classNames.add(reader.getClassName());
        }
        CPUTF8 emptyString = cpBands.getCPUtf8(CoreConstants.EMPTY_STRING);
        int latestModtime = Integer.MIN_VALUE;
        boolean isLatest = !"keep".equals(options.getModificationTime());
        for (int i = 0; i < size; i++) {
            Archive.PackingFile packingFile = this.fileList.get(i);
            String name = packingFile.getName();
            if (name.endsWith(".class") && !options.isPassFile(name)) {
                int[] iArr = this.file_options;
                int i2 = i;
                iArr[i2] = iArr[i2] | 2;
                if (classNames.contains(name.substring(0, name.length() - 6))) {
                    this.fileName[i] = emptyString;
                } else {
                    this.fileName[i] = cpBands.getCPUtf8(name);
                }
            } else {
                this.fileName[i] = cpBands.getCPUtf8(name);
            }
            if (options.isKeepDeflateHint() && packingFile.isDefalteHint()) {
                int[] iArr2 = this.file_options;
                int i3 = i;
                iArr2[i3] = iArr2[i3] | 1;
            }
            byte[] bytes = packingFile.getContents();
            this.file_size[i] = bytes.length;
            totalSize = ExactMath.add(totalSize, this.file_size[i]);
            long modtime = (packingFile.getModtime() + TimeZone.getDefault().getRawOffset()) / 1000;
            this.file_modtime[i] = (int) (modtime - archiveModtime);
            if (isLatest && latestModtime < this.file_modtime[i]) {
                latestModtime = this.file_modtime[i];
            }
            this.file_bits[i] = packingFile.getContents();
        }
        if (isLatest) {
            Arrays.fill(this.file_modtime, latestModtime);
        }
    }

    public void finaliseBands() {
        this.file_name = new int[this.fileName.length];
        for (int i = 0; i < this.file_name.length; i++) {
            if (this.fileName[i].equals(this.cpBands.getCPUtf8(CoreConstants.EMPTY_STRING))) {
                Archive.PackingFile packingFile = this.fileList.get(i);
                String name = packingFile.getName();
                if (this.options.isPassFile(name)) {
                    this.fileName[i] = this.cpBands.getCPUtf8(name);
                    int[] iArr = this.file_options;
                    int i2 = i;
                    iArr[i2] = iArr[i2] & (-3);
                }
            }
            this.file_name[i] = this.fileName[i].getIndex();
        }
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream out) throws IOException, Pack200Exception {
        PackingUtils.log("Writing file bands...");
        byte[] encodedBand = encodeBandInt("file_name", this.file_name, Codec.UNSIGNED5);
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_name[" + this.file_name.length + "]");
        byte[] encodedBand2 = encodeFlags("file_size", this.file_size, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader.have_file_size_hi());
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from file_size[" + this.file_size.length + "]");
        if (this.segmentHeader.have_file_modtime()) {
            byte[] encodedBand3 = encodeBandInt("file_modtime", this.file_modtime, Codec.DELTA5);
            out.write(encodedBand3);
            PackingUtils.log("Wrote " + encodedBand3.length + " bytes from file_modtime[" + this.file_modtime.length + "]");
        }
        if (this.segmentHeader.have_file_options()) {
            byte[] encodedBand4 = encodeBandInt("file_options", this.file_options, Codec.UNSIGNED5);
            out.write(encodedBand4);
            PackingUtils.log("Wrote " + encodedBand4.length + " bytes from file_options[" + this.file_options.length + "]");
        }
        byte[] encodedBand5 = encodeBandInt("file_bits", flatten(this.file_bits), Codec.BYTE1);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from file_bits[" + this.file_bits.length + "]");
    }

    private int[] flatten(byte[][] bytes) {
        int total = 0;
        for (byte[] element : bytes) {
            total += element.length;
        }
        int[] band = new int[total];
        int index = 0;
        for (byte[] element2 : bytes) {
            for (byte element22 : element2) {
                int i = index;
                index++;
                band[i] = element22 & 255;
            }
        }
        return band;
    }
}
