package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.utils.ExactMath;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/RunCodec.class */
public class RunCodec extends Codec {
    private int k;
    private final Codec aCodec;
    private final Codec bCodec;
    private int last;

    public RunCodec(int k, Codec aCodec, Codec bCodec) throws Pack200Exception {
        if (k <= 0) {
            throw new Pack200Exception("Cannot have a RunCodec for a negative number of numbers");
        }
        if (aCodec == null || bCodec == null) {
            throw new Pack200Exception("Must supply both codecs for a RunCodec");
        }
        this.k = k;
        this.aCodec = aCodec;
        this.bCodec = bCodec;
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int decode(InputStream in) throws IOException, Pack200Exception {
        return decode(in, this.last);
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int decode(InputStream in, long last) throws IOException, Pack200Exception {
        int i = this.k - 1;
        this.k = i;
        if (i >= 0) {
            int value = this.aCodec.decode(in, this.last);
            this.last = this.k == 0 ? 0 : value;
            return normalise(value, this.aCodec);
        }
        this.last = this.bCodec.decode(in, this.last);
        return normalise(this.last, this.bCodec);
    }

    private int normalise(int value, Codec codecUsed) {
        if (codecUsed instanceof BHSDCodec) {
            BHSDCodec bhsd = (BHSDCodec) codecUsed;
            if (bhsd.isDelta()) {
                long cardinality = bhsd.cardinality();
                while (value > bhsd.largest()) {
                    value = (int) (value - cardinality);
                }
                while (value < bhsd.smallest()) {
                    value = ExactMath.add(value, cardinality);
                }
            }
        }
        return value;
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int[] decodeInts(int n, InputStream in) throws IOException, Pack200Exception {
        int[] band = new int[n];
        int[] aValues = this.aCodec.decodeInts(this.k, in);
        normalise(aValues, this.aCodec);
        int[] bValues = this.bCodec.decodeInts(n - this.k, in);
        normalise(bValues, this.bCodec);
        System.arraycopy(aValues, 0, band, 0, this.k);
        System.arraycopy(bValues, 0, band, this.k, n - this.k);
        this.lastBandLength = this.aCodec.lastBandLength + this.bCodec.lastBandLength;
        return band;
    }

    private void normalise(int[] band, Codec codecUsed) {
        int i;
        int i2;
        if (codecUsed instanceof BHSDCodec) {
            BHSDCodec bhsd = (BHSDCodec) codecUsed;
            if (bhsd.isDelta()) {
                long cardinality = bhsd.cardinality();
                for (int i3 = 0; i3 < band.length; i3++) {
                    while (band[i3] > bhsd.largest()) {
                        band[i3] = (int) (band[i2] - cardinality);
                    }
                    while (band[i3] < bhsd.smallest()) {
                        band[i3] = ExactMath.add(band[i3], cardinality);
                    }
                }
            }
        } else if (codecUsed instanceof PopulationCodec) {
            PopulationCodec popCodec = (PopulationCodec) codecUsed;
            int[] favoured = (int[]) popCodec.getFavoured().clone();
            Arrays.sort(favoured);
            for (int i4 = 0; i4 < band.length; i4++) {
                boolean favouredValue = Arrays.binarySearch(favoured, band[i4]) > -1;
                Codec theCodec = favouredValue ? popCodec.getFavouredCodec() : popCodec.getUnfavouredCodec();
                if (theCodec instanceof BHSDCodec) {
                    BHSDCodec bhsd2 = (BHSDCodec) theCodec;
                    if (bhsd2.isDelta()) {
                        long cardinality2 = bhsd2.cardinality();
                        while (band[i4] > bhsd2.largest()) {
                            band[i4] = (int) (band[i] - cardinality2);
                        }
                        while (band[i4] < bhsd2.smallest()) {
                            band[i4] = ExactMath.add(band[i4], cardinality2);
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        return "RunCodec[k=" + this.k + ";aCodec=" + this.aCodec + "bCodec=" + this.bCodec + "]";
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public byte[] encode(int value, int last) throws Pack200Exception {
        throw new Pack200Exception("Must encode entire band at once with a RunCodec");
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public byte[] encode(int value) throws Pack200Exception {
        throw new Pack200Exception("Must encode entire band at once with a RunCodec");
    }

    public int getK() {
        return this.k;
    }

    public Codec getACodec() {
        return this.aCodec;
    }

    public Codec getBCodec() {
        return this.bCodec;
    }
}
