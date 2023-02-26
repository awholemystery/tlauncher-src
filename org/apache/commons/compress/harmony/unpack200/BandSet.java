package org.apache.commons.compress.harmony.unpack200;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.harmony.pack200.BHSDCodec;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.CodecEncoding;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.pack200.PopulationCodec;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPDouble;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFloat;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPInteger;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPInterfaceMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPLong;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.utils.ExactMath;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/BandSet.class */
public abstract class BandSet {
    protected Segment segment;
    protected SegmentHeader header;

    public abstract void read(InputStream inputStream) throws IOException, Pack200Exception;

    public abstract void unpack() throws IOException, Pack200Exception;

    public void unpack(InputStream in) throws IOException, Pack200Exception {
        read(in);
        unpack();
    }

    public BandSet(Segment segment) {
        this.segment = segment;
        this.header = segment.getSegmentHeader();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int[] decodeBandInt(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] band;
        int[] iArr;
        int i;
        Codec codecUsed = codec;
        if (codec.getB() == 1 || count == 0) {
            return codec.decodeInts(count, in);
        }
        int[] getFirst = codec.decodeInts(1, in);
        if (getFirst.length == 0) {
            return getFirst;
        }
        int first = getFirst[0];
        if (codec.isSigned() && first >= -256 && first <= -1) {
            codecUsed = CodecEncoding.getCodec((-1) - first, this.header.getBandHeadersInputStream(), codec);
            band = codecUsed.decodeInts(count, in);
        } else if (!codec.isSigned() && first >= codec.getL() && first <= codec.getL() + 255) {
            codecUsed = CodecEncoding.getCodec(first - codec.getL(), this.header.getBandHeadersInputStream(), codec);
            band = codecUsed.decodeInts(count, in);
        } else {
            band = codec.decodeInts(count - 1, in, first);
        }
        if (codecUsed instanceof PopulationCodec) {
            PopulationCodec popCodec = (PopulationCodec) codecUsed;
            int[] favoured = (int[]) popCodec.getFavoured().clone();
            Arrays.sort(favoured);
            for (int i2 = 0; i2 < band.length; i2++) {
                boolean favouredValue = Arrays.binarySearch(favoured, band[i2]) > -1;
                Codec theCodec = favouredValue ? popCodec.getFavouredCodec() : popCodec.getUnfavouredCodec();
                if ((theCodec instanceof BHSDCodec) && ((BHSDCodec) theCodec).isDelta()) {
                    BHSDCodec bhsd = (BHSDCodec) theCodec;
                    long cardinality = bhsd.cardinality();
                    while (band[i2] > bhsd.largest()) {
                        band[i2] = (int) (iArr[i] - cardinality);
                    }
                    while (band[i2] < bhsd.smallest()) {
                        band[i2] = ExactMath.add(band[i2], cardinality);
                    }
                }
            }
        }
        return band;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int[], int[][]] */
    public int[][] decodeBandInt(String name, InputStream in, BHSDCodec defaultCodec, int[] counts) throws IOException, Pack200Exception {
        ?? r0 = new int[counts.length];
        int totalCount = 0;
        for (int count : counts) {
            totalCount += count;
        }
        int[] twoDResult = decodeBandInt(name, in, defaultCodec, totalCount);
        int index = 0;
        for (int i = 0; i < r0.length; i++) {
            r0[i] = new int[counts[i]];
            for (int j = 0; j < r0[i].length; j++) {
                r0[i][j] = twoDResult[index];
                index++;
            }
        }
        return r0;
    }

    public long[] parseFlags(String name, InputStream in, int count, BHSDCodec codec, boolean hasHi) throws IOException, Pack200Exception {
        return parseFlags(name, in, new int[]{count}, hasHi ? codec : null, codec)[0];
    }

    public long[][] parseFlags(String name, InputStream in, int[] counts, BHSDCodec codec, boolean hasHi) throws IOException, Pack200Exception {
        return parseFlags(name, in, counts, hasHi ? codec : null, codec);
    }

    public long[] parseFlags(String name, InputStream in, int count, BHSDCodec hiCodec, BHSDCodec loCodec) throws IOException, Pack200Exception {
        return parseFlags(name, in, new int[]{count}, hiCodec, loCodec)[0];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v31, types: [long[], long[][]] */
    /* JADX WARN: Type inference failed for: r0v5, types: [long[], long[][]] */
    public long[][] parseFlags(String name, InputStream in, int[] counts, BHSDCodec hiCodec, BHSDCodec loCodec) throws IOException, Pack200Exception {
        int[] lo;
        int count = counts.length;
        if (count == 0) {
            return new long[]{new long[0]};
        }
        int sum = 0;
        ?? r0 = new long[count];
        for (int i = 0; i < count; i++) {
            r0[i] = new long[counts[i]];
            sum += counts[i];
        }
        int[] hi = null;
        if (hiCodec != null) {
            hi = decodeBandInt(name, in, hiCodec, sum);
            lo = decodeBandInt(name, in, loCodec, sum);
        } else {
            lo = decodeBandInt(name, in, loCodec, sum);
        }
        int index = 0;
        for (int i2 = 0; i2 < r0.length; i2++) {
            for (int j = 0; j < r0[i2].length; j++) {
                if (hi != null) {
                    r0[i2][j] = (hi[index] << 32) | (lo[index] & 4294967295L);
                } else {
                    r0[i2][j] = lo[index];
                }
                index++;
            }
        }
        return r0;
    }

    public String[] parseReferences(String name, InputStream in, BHSDCodec codec, int count, String[] reference) throws IOException, Pack200Exception {
        return parseReferences(name, in, codec, new int[]{count}, reference)[0];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v34, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.String[], java.lang.String[][]] */
    public String[][] parseReferences(String name, InputStream in, BHSDCodec codec, int[] counts, String[] reference) throws IOException, Pack200Exception {
        int count = counts.length;
        if (count == 0) {
            return new String[]{new String[0]};
        }
        ?? r0 = new String[count];
        int sum = 0;
        for (int i = 0; i < count; i++) {
            r0[i] = new String[counts[i]];
            sum += counts[i];
        }
        String[] result1 = new String[sum];
        int[] indices = decodeBandInt(name, in, codec, sum);
        for (int i1 = 0; i1 < sum; i1++) {
            int index = indices[i1];
            if (index < 0 || index >= reference.length) {
                throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
            }
            result1[i1] = reference[index];
        }
        int pos = 0;
        for (int i2 = 0; i2 < count; i2++) {
            int num = counts[i2];
            r0[i2] = new String[num];
            System.arraycopy(result1, pos, r0[i2], 0, num);
            pos += num;
        }
        return r0;
    }

    public CPInteger[] parseCPIntReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] reference = this.segment.getCpBands().getCpInt();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPInteger[] result = new CPInteger[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            int index = indices[i1];
            if (index < 0 || index >= reference.length) {
                throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
            }
            result[i1] = this.segment.getCpBands().cpIntegerValue(index);
        }
        return result;
    }

    public CPDouble[] parseCPDoubleReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPDouble[] result = new CPDouble[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = this.segment.getCpBands().cpDoubleValue(indices[i1]);
        }
        return result;
    }

    public CPFloat[] parseCPFloatReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPFloat[] result = new CPFloat[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = this.segment.getCpBands().cpFloatValue(indices[i1]);
        }
        return result;
    }

    public CPLong[] parseCPLongReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        long[] reference = this.segment.getCpBands().getCpLong();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPLong[] result = new CPLong[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            int index = indices[i1];
            if (index < 0 || index >= reference.length) {
                throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
            }
            result[i1] = this.segment.getCpBands().cpLongValue(index);
        }
        return result;
    }

    public CPUTF8[] parseCPUTF8References(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPUTF8[] result = new CPUTF8[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            int index = indices[i1];
            result[i1] = this.segment.getCpBands().cpUTF8Value(index);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8[], org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8[][]] */
    public CPUTF8[][] parseCPUTF8References(String name, InputStream in, BHSDCodec codec, int[] counts) throws IOException, Pack200Exception {
        ?? r0 = new CPUTF8[counts.length];
        int sum = 0;
        for (int i = 0; i < counts.length; i++) {
            r0[i] = new CPUTF8[counts[i]];
            sum += counts[i];
        }
        CPUTF8[] result1 = new CPUTF8[sum];
        int[] indices = decodeBandInt(name, in, codec, sum);
        for (int i1 = 0; i1 < sum; i1++) {
            int index = indices[i1];
            result1[i1] = this.segment.getCpBands().cpUTF8Value(index);
        }
        int pos = 0;
        for (int i2 = 0; i2 < counts.length; i2++) {
            int num = counts[i2];
            r0[i2] = new CPUTF8[num];
            System.arraycopy(result1, pos, r0[i2], 0, num);
            pos += num;
        }
        return r0;
    }

    public CPString[] parseCPStringReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPString[] result = new CPString[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = this.segment.getCpBands().cpStringValue(indices[i1]);
        }
        return result;
    }

    public CPInterfaceMethodRef[] parseCPInterfaceMethodRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        CpBands cpBands = this.segment.getCpBands();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPInterfaceMethodRef[] result = new CPInterfaceMethodRef[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = cpBands.cpIMethodValue(indices[i1]);
        }
        return result;
    }

    public CPMethodRef[] parseCPMethodRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        CpBands cpBands = this.segment.getCpBands();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPMethodRef[] result = new CPMethodRef[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = cpBands.cpMethodValue(indices[i1]);
        }
        return result;
    }

    public CPFieldRef[] parseCPFieldRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        CpBands cpBands = this.segment.getCpBands();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPFieldRef[] result = new CPFieldRef[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            int index = indices[i1];
            result[i1] = cpBands.cpFieldValue(index);
        }
        return result;
    }

    public CPNameAndType[] parseCPDescriptorReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        CpBands cpBands = this.segment.getCpBands();
        int[] indices = decodeBandInt(name, in, codec, count);
        CPNameAndType[] result = new CPNameAndType[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            int index = indices[i1];
            result[i1] = cpBands.cpNameAndTypeValue(index);
        }
        return result;
    }

    public CPUTF8[] parseCPSignatureReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPUTF8[] result = new CPUTF8[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = this.segment.getCpBands().cpSignatureValue(indices[i1]);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8[], org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8[][]] */
    public CPUTF8[][] parseCPSignatureReferences(String name, InputStream in, BHSDCodec codec, int[] counts) throws IOException, Pack200Exception {
        ?? r0 = new CPUTF8[counts.length];
        int sum = 0;
        for (int i = 0; i < counts.length; i++) {
            r0[i] = new CPUTF8[counts[i]];
            sum += counts[i];
        }
        CPUTF8[] result1 = new CPUTF8[sum];
        int[] indices = decodeBandInt(name, in, codec, sum);
        for (int i1 = 0; i1 < sum; i1++) {
            result1[i1] = this.segment.getCpBands().cpSignatureValue(indices[i1]);
        }
        int pos = 0;
        for (int i2 = 0; i2 < counts.length; i2++) {
            int num = counts[i2];
            r0[i2] = new CPUTF8[num];
            System.arraycopy(result1, pos, r0[i2], 0, num);
            pos += num;
        }
        return r0;
    }

    public CPClass[] parseCPClassReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
        int[] indices = decodeBandInt(name, in, codec, count);
        CPClass[] result = new CPClass[indices.length];
        for (int i1 = 0; i1 < count; i1++) {
            result[i1] = this.segment.getCpBands().cpClassValue(indices[i1]);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String[] getReferences(int[] ints, String[] reference) {
        String[] result = new String[ints.length];
        Arrays.setAll(result, i -> {
            return reference[ints[i]];
        });
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String[], java.lang.String[][]] */
    public String[][] getReferences(int[][] ints, String[] reference) {
        ?? r0 = new String[ints.length];
        for (int i = 0; i < r0.length; i++) {
            r0[i] = new String[ints[i].length];
            for (int j = 0; j < r0[i].length; j++) {
                r0[i][j] = reference[ints[i][j]];
            }
        }
        return r0;
    }
}
