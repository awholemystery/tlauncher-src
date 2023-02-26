package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/BandSet.class */
public abstract class BandSet {
    protected final SegmentHeader segmentHeader;
    final int effort;
    private static final int[] effortThresholds = {0, 0, 1000, 500, 100, 100, 100, 100, 100, 0};
    private long[] canonicalLargest;
    private long[] canonicalSmallest;

    public abstract void pack(OutputStream outputStream) throws IOException, Pack200Exception;

    public BandSet(int effort, SegmentHeader header) {
        this.effort = effort;
        this.segmentHeader = header;
    }

    public byte[] encodeScalar(int[] band, BHSDCodec codec) throws Pack200Exception {
        return codec.encode(band);
    }

    public byte[] encodeScalar(int value, BHSDCodec codec) throws Pack200Exception {
        return codec.encode(value);
    }

    public byte[] encodeBandInt(String name, int[] ints, BHSDCodec defaultCodec) throws Pack200Exception {
        int specifier;
        byte[] encodedBand = null;
        if (this.effort > 1 && ints.length >= effortThresholds[this.effort]) {
            BandAnalysisResults results = analyseBand(name, ints, defaultCodec);
            Codec betterCodec = results.betterCodec;
            encodedBand = results.encodedBand;
            if (betterCodec != null) {
                if (betterCodec instanceof BHSDCodec) {
                    int[] specifierBand = CodecEncoding.getSpecifier(betterCodec, defaultCodec);
                    int specifier2 = specifierBand[0];
                    if (specifierBand.length > 1) {
                        for (int i = 1; i < specifierBand.length; i++) {
                            this.segmentHeader.appendBandCodingSpecifier(specifierBand[i]);
                        }
                    }
                    if (defaultCodec.isSigned()) {
                        specifier = (-1) - specifier2;
                    } else {
                        specifier = specifier2 + defaultCodec.getL();
                    }
                    byte[] specifierEncoded = defaultCodec.encode(new int[]{specifier});
                    byte[] band = new byte[specifierEncoded.length + encodedBand.length];
                    System.arraycopy(specifierEncoded, 0, band, 0, specifierEncoded.length);
                    System.arraycopy(encodedBand, 0, band, specifierEncoded.length, encodedBand.length);
                    return band;
                } else if (!(betterCodec instanceof PopulationCodec)) {
                    if (betterCodec instanceof RunCodec) {
                    }
                } else {
                    IntStream of = IntStream.of(results.extraMetadata);
                    SegmentHeader segmentHeader = this.segmentHeader;
                    segmentHeader.getClass();
                    of.forEach(this::appendBandCodingSpecifier);
                    return encodedBand;
                }
            }
        }
        if (ints.length > 0) {
            if (encodedBand == null) {
                encodedBand = defaultCodec.encode(ints);
            }
            int first = ints[0];
            if (defaultCodec.getB() != 1) {
                if (defaultCodec.isSigned() && first >= -256 && first <= -1) {
                    int specifier3 = (-1) - CodecEncoding.getSpecifierForDefaultCodec(defaultCodec);
                    byte[] specifierEncoded2 = defaultCodec.encode(new int[]{specifier3});
                    byte[] band2 = new byte[specifierEncoded2.length + encodedBand.length];
                    System.arraycopy(specifierEncoded2, 0, band2, 0, specifierEncoded2.length);
                    System.arraycopy(encodedBand, 0, band2, specifierEncoded2.length, encodedBand.length);
                    return band2;
                } else if (!defaultCodec.isSigned() && first >= defaultCodec.getL() && first <= defaultCodec.getL() + 255) {
                    int specifier4 = CodecEncoding.getSpecifierForDefaultCodec(defaultCodec) + defaultCodec.getL();
                    byte[] specifierEncoded3 = defaultCodec.encode(new int[]{specifier4});
                    byte[] band3 = new byte[specifierEncoded3.length + encodedBand.length];
                    System.arraycopy(specifierEncoded3, 0, band3, 0, specifierEncoded3.length);
                    System.arraycopy(encodedBand, 0, band3, specifierEncoded3.length, encodedBand.length);
                    return band3;
                }
            }
            return encodedBand;
        }
        return new byte[0];
    }

    private BandAnalysisResults analyseBand(String name, int[] band, BHSDCodec defaultCodec) throws Pack200Exception {
        BandAnalysisResults results = new BandAnalysisResults();
        if (this.canonicalLargest == null) {
            this.canonicalLargest = new long[116];
            this.canonicalSmallest = new long[116];
            for (int i = 1; i < this.canonicalLargest.length; i++) {
                this.canonicalLargest[i] = CodecEncoding.getCanonicalCodec(i).largest();
                this.canonicalSmallest[i] = CodecEncoding.getCanonicalCodec(i).smallest();
            }
        }
        BandData bandData = new BandData(band);
        byte[] encoded = defaultCodec.encode(band);
        results.encodedBand = encoded;
        if (encoded.length <= (band.length + 23) - (2 * this.effort)) {
            return results;
        }
        if (bandData.anyNegatives() || bandData.largest > Codec.BYTE1.largest()) {
            if (this.effort > 3 && !name.equals("POPULATION")) {
                int numDistinctValues = bandData.numDistinctValues();
                float distinctValuesAsProportion = numDistinctValues / band.length;
                if (numDistinctValues < 100 || distinctValuesAsProportion < 0.02d || (this.effort > 6 && distinctValuesAsProportion < 0.04d)) {
                    encodeWithPopulationCodec(name, band, defaultCodec, bandData, results);
                    if (timeToStop(results)) {
                        return results;
                    }
                }
            }
            List<BHSDCodec[]> codecFamiliesToTry = new ArrayList<>();
            if (bandData.mainlyPositiveDeltas() && bandData.mainlySmallDeltas()) {
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs2);
            }
            if (bandData.wellCorrelated()) {
                if (bandData.mainlyPositiveDeltas()) {
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs1);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs3);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs4);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs5);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs1);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs3);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs4);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs5);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs2);
                } else {
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs1);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs3);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs2);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs4);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs5);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs1);
                    codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs2);
                }
            } else if (bandData.anyNegatives()) {
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs1);
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs2);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs1);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs2);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs3);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs4);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs5);
            } else {
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs1);
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs3);
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs4);
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs5);
                codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs2);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs1);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs3);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs4);
                codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs5);
            }
            if (name.equalsIgnoreCase("cpint")) {
                System.out.print(CoreConstants.EMPTY_STRING);
            }
            for (BHSDCodec[] family : codecFamiliesToTry) {
                tryCodecs(name, band, defaultCodec, bandData, results, encoded, family);
                if (timeToStop(results)) {
                    break;
                }
            }
            return results;
        }
        results.encodedBand = Codec.BYTE1.encode(band);
        results.betterCodec = Codec.BYTE1;
        return results;
    }

    private boolean timeToStop(BandAnalysisResults results) {
        return this.effort > 6 ? results.numCodecsTried >= this.effort * 2 : results.numCodecsTried >= this.effort;
    }

    private void tryCodecs(String name, int[] band, BHSDCodec defaultCodec, BandData bandData, BandAnalysisResults results, byte[] encoded, BHSDCodec[] potentialCodecs) throws Pack200Exception {
        for (BHSDCodec potential : potentialCodecs) {
            if (potential.equals(defaultCodec)) {
                return;
            }
            if (potential.isDelta()) {
                if (potential.largest() >= bandData.largestDelta && potential.smallest() <= bandData.smallestDelta && potential.largest() >= bandData.largest && potential.smallest() <= bandData.smallest) {
                    byte[] encoded2 = potential.encode(band);
                    BandAnalysisResults.access$408(results);
                    byte[] specifierEncoded = defaultCodec.encode(CodecEncoding.getSpecifier(potential, null));
                    int saved = (encoded.length - encoded2.length) - specifierEncoded.length;
                    if (saved > results.saved) {
                        results.betterCodec = potential;
                        results.encodedBand = encoded2;
                        results.saved = saved;
                    }
                }
            } else if (potential.largest() >= bandData.largest && potential.smallest() <= bandData.smallest) {
                byte[] encoded22 = potential.encode(band);
                BandAnalysisResults.access$408(results);
                byte[] specifierEncoded2 = defaultCodec.encode(CodecEncoding.getSpecifier(potential, null));
                int saved2 = (encoded.length - encoded22.length) - specifierEncoded2.length;
                if (saved2 > results.saved) {
                    results.betterCodec = potential;
                    results.encodedBand = encoded22;
                    results.saved = saved2;
                }
            }
            if (timeToStop(results)) {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void encodeWithPopulationCodec(String name, int[] band, BHSDCodec defaultCodec, BandData bandData, BandAnalysisResults results) throws Pack200Exception {
        byte[] tokensEncoded;
        int specifier;
        results.numCodecsTried += 3;
        Map<Integer, Integer> distinctValues = bandData.distinctValues;
        List<Integer> favored = new ArrayList<>();
        distinctValues.forEach(k, v -> {
            if (v.intValue() > 2 || distinctValues.size() < 256) {
                favored.add(k);
            }
        });
        if (distinctValues.size() > 255) {
            favored.sort(arg0, arg1 -> {
                return ((Integer) distinctValues.get(arg1)).compareTo((Integer) distinctValues.get(arg0));
            });
        }
        Map<Integer, Integer> favoredToIndex = new HashMap<>();
        for (int i = 0; i < favored.size(); i++) {
            favoredToIndex.put(favored.get(i), Integer.valueOf(i));
        }
        IntList unfavoured = new IntList();
        int[] tokens = new int[band.length];
        for (int i2 = 0; i2 < band.length; i2++) {
            Integer favouredIndex = favoredToIndex.get(Integer.valueOf(band[i2]));
            if (favouredIndex == null) {
                tokens[i2] = 0;
                unfavoured.add(band[i2]);
            } else {
                tokens[i2] = favouredIndex.intValue() + 1;
            }
        }
        favored.add(favored.get(favored.size() - 1));
        int[] favouredBand = integerListToArray(favored);
        int[] unfavouredBand = unfavoured.toArray();
        BandAnalysisResults favouredResults = analyseBand("POPULATION", favouredBand, defaultCodec);
        BandAnalysisResults unfavouredResults = analyseBand("POPULATION", unfavouredBand, defaultCodec);
        int tdefL = 0;
        int l = 0;
        Codec tokenCodec = null;
        int k2 = favored.size() - 1;
        if (k2 < 256) {
            tdefL = 1;
            tokensEncoded = Codec.BYTE1.encode(tokens);
        } else {
            BandAnalysisResults tokenResults = analyseBand("POPULATION", tokens, defaultCodec);
            tokenCodec = tokenResults.betterCodec;
            tokensEncoded = tokenResults.encodedBand;
            if (tokenCodec == null) {
                tokenCodec = defaultCodec;
            }
            l = ((BHSDCodec) tokenCodec).getL();
            int h = ((BHSDCodec) tokenCodec).getH();
            int s = ((BHSDCodec) tokenCodec).getS();
            int b = ((BHSDCodec) tokenCodec).getB();
            int d = ((BHSDCodec) tokenCodec).isDelta() ? 1 : 0;
            if (s == 0 && d == 0) {
                boolean canUseTDefL = true;
                if (b > 1) {
                    BHSDCodec oneLowerB = new BHSDCodec(b - 1, h);
                    if (oneLowerB.largest() >= k2) {
                        canUseTDefL = false;
                    }
                }
                if (canUseTDefL) {
                    switch (l) {
                        case 4:
                            tdefL = 1;
                            break;
                        case 8:
                            tdefL = 2;
                            break;
                        case 16:
                            tdefL = 3;
                            break;
                        case 32:
                            tdefL = 4;
                            break;
                        case 64:
                            tdefL = 5;
                            break;
                        case 128:
                            tdefL = 6;
                            break;
                        case 192:
                            tdefL = 7;
                            break;
                        case 224:
                            tdefL = 8;
                            break;
                        case 240:
                            tdefL = 9;
                            break;
                        case 248:
                            tdefL = 10;
                            break;
                        case 252:
                            tdefL = 11;
                            break;
                    }
                }
            }
        }
        byte[] favouredEncoded = favouredResults.encodedBand;
        byte[] unfavouredEncoded = unfavouredResults.encodedBand;
        Codec favouredCodec = favouredResults.betterCodec;
        Codec unfavouredCodec = unfavouredResults.betterCodec;
        int specifier2 = 141 + (favouredCodec == null ? 1 : 0) + (4 * tdefL) + (unfavouredCodec == null ? 2 : 0);
        IntList extraBandMetadata = new IntList(3);
        if (favouredCodec != null) {
            IntStream of = IntStream.of(CodecEncoding.getSpecifier(favouredCodec, null));
            extraBandMetadata.getClass();
            of.forEach(this::add);
        }
        if (tdefL == 0) {
            IntStream of2 = IntStream.of(CodecEncoding.getSpecifier(tokenCodec, null));
            extraBandMetadata.getClass();
            of2.forEach(this::add);
        }
        if (unfavouredCodec != null) {
            IntStream of3 = IntStream.of(CodecEncoding.getSpecifier(unfavouredCodec, null));
            extraBandMetadata.getClass();
            of3.forEach(this::add);
        }
        int[] extraMetadata = extraBandMetadata.toArray();
        byte[] extraMetadataEncoded = Codec.UNSIGNED5.encode(extraMetadata);
        if (defaultCodec.isSigned()) {
            specifier = (-1) - specifier2;
        } else {
            specifier = specifier2 + defaultCodec.getL();
        }
        byte[] firstValueEncoded = defaultCodec.encode(new int[]{specifier});
        int totalBandLength = firstValueEncoded.length + favouredEncoded.length + tokensEncoded.length + unfavouredEncoded.length;
        if (totalBandLength + extraMetadataEncoded.length >= results.encodedBand.length) {
            return;
        }
        results.saved += results.encodedBand.length - (totalBandLength + extraMetadataEncoded.length);
        byte[] encodedBand = new byte[totalBandLength];
        System.arraycopy(firstValueEncoded, 0, encodedBand, 0, firstValueEncoded.length);
        System.arraycopy(favouredEncoded, 0, encodedBand, firstValueEncoded.length, favouredEncoded.length);
        System.arraycopy(tokensEncoded, 0, encodedBand, firstValueEncoded.length + favouredEncoded.length, tokensEncoded.length);
        System.arraycopy(unfavouredEncoded, 0, encodedBand, firstValueEncoded.length + favouredEncoded.length + tokensEncoded.length, unfavouredEncoded.length);
        results.encodedBand = encodedBand;
        results.extraMetadata = extraMetadata;
        if (l != 0) {
            results.betterCodec = new PopulationCodec(favouredCodec, l, unfavouredCodec);
        } else {
            results.betterCodec = new PopulationCodec(favouredCodec, tokenCodec, unfavouredCodec);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] encodeFlags(String name, long[] flags, BHSDCodec loCodec, BHSDCodec hiCodec, boolean haveHiFlags) throws Pack200Exception {
        if (!haveHiFlags) {
            int[] loBits = new int[flags.length];
            Arrays.setAll(loBits, i -> {
                return (int) flags[i];
            });
            return encodeBandInt(name, loBits, loCodec);
        }
        int[] hiBits = new int[flags.length];
        int[] loBits2 = new int[flags.length];
        for (int i2 = 0; i2 < flags.length; i2++) {
            long l = flags[i2];
            hiBits[i2] = (int) (l >> 32);
            loBits2[i2] = (int) l;
        }
        byte[] hi = encodeBandInt(name, hiBits, hiCodec);
        byte[] lo = encodeBandInt(name, loBits2, loCodec);
        byte[] total = new byte[hi.length + lo.length];
        System.arraycopy(hi, 0, total, 0, hi.length);
        System.arraycopy(lo, 0, total, hi.length + 1, lo.length);
        return total;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int[] integerListToArray(List<Integer> integerList) {
        return integerList.stream().mapToInt((v0) -> {
            return v0.intValue();
        }).toArray();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long[] longListToArray(List<Long> longList) {
        return longList.stream().mapToLong((v0) -> {
            return v0.longValue();
        }).toArray();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int[] cpEntryListToArray(List<? extends ConstantPoolEntry> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i).getIndex();
            if (array[i] < 0) {
                throw new IllegalArgumentException("Index should be > 0");
            }
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int[] cpEntryOrNullListToArray(List<? extends ConstantPoolEntry> list) {
        int[] array = new int[list.size()];
        for (int j = 0; j < array.length; j++) {
            ConstantPoolEntry cpEntry = list.get(j);
            array[j] = cpEntry == null ? 0 : cpEntry.getIndex() + 1;
            if (cpEntry != null && cpEntry.getIndex() < 0) {
                throw new IllegalArgumentException("Index should be > 0");
            }
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] encodeFlags(String name, long[][] flags, BHSDCodec loCodec, BHSDCodec hiCodec, boolean haveHiFlags) throws Pack200Exception {
        return encodeFlags(name, flatten(flags), loCodec, hiCodec, haveHiFlags);
    }

    private long[] flatten(long[][] flags) {
        int totalSize = 0;
        for (long[] flag : flags) {
            totalSize += flag.length;
        }
        long[] flatArray = new long[totalSize];
        int index = 0;
        for (long[] flag2 : flags) {
            for (long element : flag2) {
                flatArray[index] = element;
                index++;
            }
        }
        return flatArray;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/BandSet$BandData.class */
    public class BandData {
        private final int[] band;
        private int smallest;
        private int largest;
        private int smallestDelta;
        private int largestDelta;
        private int deltaIsAscending = 0;
        private int smallDeltaCount = 0;
        private double averageAbsoluteDelta = 0.0d;
        private double averageAbsoluteValue = 0.0d;
        private Map<Integer, Integer> distinctValues;

        public BandData(int[] band) {
            Integer count;
            this.smallest = Integer.MAX_VALUE;
            this.largest = Integer.MIN_VALUE;
            this.band = band;
            for (int i = 0; i < band.length; i++) {
                if (band[i] < this.smallest) {
                    this.smallest = band[i];
                }
                if (band[i] > this.largest) {
                    this.largest = band[i];
                }
                if (i != 0) {
                    int delta = band[i] - band[i - 1];
                    if (delta < this.smallestDelta) {
                        this.smallestDelta = delta;
                    }
                    if (delta > this.largestDelta) {
                        this.largestDelta = delta;
                    }
                    if (delta >= 0) {
                        this.deltaIsAscending++;
                    }
                    this.averageAbsoluteDelta += Math.abs(delta) / (band.length - 1);
                    if (Math.abs(delta) < 256) {
                        this.smallDeltaCount++;
                    }
                } else {
                    this.smallestDelta = band[0];
                    this.largestDelta = band[0];
                }
                this.averageAbsoluteValue += Math.abs(band[i]) / band.length;
                if (BandSet.this.effort > 3) {
                    if (this.distinctValues == null) {
                        this.distinctValues = new HashMap();
                    }
                    Integer value = Integer.valueOf(band[i]);
                    Integer count2 = this.distinctValues.get(value);
                    if (count2 == null) {
                        count = 1;
                    } else {
                        count = Integer.valueOf(count2.intValue() + 1);
                    }
                    this.distinctValues.put(value, count);
                }
            }
        }

        public boolean mainlySmallDeltas() {
            return ((float) this.smallDeltaCount) / ((float) this.band.length) > 0.7f;
        }

        public boolean wellCorrelated() {
            return this.averageAbsoluteDelta * 3.1d < this.averageAbsoluteValue;
        }

        public boolean mainlyPositiveDeltas() {
            return ((float) this.deltaIsAscending) / ((float) this.band.length) > 0.95f;
        }

        public boolean anyNegatives() {
            return this.smallest < 0;
        }

        public int numDistinctValues() {
            if (this.distinctValues == null) {
                return this.band.length;
            }
            return this.distinctValues.size();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/BandSet$BandAnalysisResults.class */
    public class BandAnalysisResults {
        private int numCodecsTried = 0;
        private int saved = 0;
        private int[] extraMetadata;
        private byte[] encodedBand;
        private Codec betterCodec;

        public BandAnalysisResults() {
        }

        static /* synthetic */ int access$408(BandAnalysisResults x0) {
            int i = x0.numCodecsTried;
            x0.numCodecsTried = i + 1;
            return i;
        }
    }
}
