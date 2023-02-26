package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/PopulationCodec.class */
public class PopulationCodec extends Codec {
    private final Codec favouredCodec;
    private Codec tokenCodec;
    private final Codec unfavouredCodec;
    private int l;
    private int[] favoured;

    public PopulationCodec(Codec favouredCodec, Codec tokenCodec, Codec unvafouredCodec) {
        this.favouredCodec = favouredCodec;
        this.tokenCodec = tokenCodec;
        this.unfavouredCodec = unvafouredCodec;
    }

    public PopulationCodec(Codec favouredCodec, int l, Codec unfavouredCodec) {
        if (l >= 256 || l <= 0) {
            throw new IllegalArgumentException("L must be between 1..255");
        }
        this.favouredCodec = favouredCodec;
        this.l = l;
        this.unfavouredCodec = unfavouredCodec;
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int decode(InputStream in) throws IOException, Pack200Exception {
        throw new Pack200Exception("Population encoding does not work unless the number of elements are known");
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int decode(InputStream in, long last) throws IOException, Pack200Exception {
        throw new Pack200Exception("Population encoding does not work unless the number of elements are known");
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public int[] decodeInts(int n, InputStream in) throws IOException, Pack200Exception {
        this.lastBandLength = 0;
        this.favoured = new int[n];
        int smallest = Integer.MAX_VALUE;
        int last = 0;
        int k = -1;
        while (true) {
            int value = this.favouredCodec.decode(in, last);
            if (k <= -1 || (value != smallest && value != last)) {
                k++;
                this.favoured[k] = value;
                int absoluteSmallest = Math.abs(smallest);
                int absoluteValue = Math.abs(value);
                if (absoluteSmallest > absoluteValue) {
                    smallest = value;
                } else if (absoluteSmallest == absoluteValue) {
                    smallest = absoluteSmallest;
                }
                last = value;
            }
        }
        this.lastBandLength += k;
        if (this.tokenCodec == null) {
            if (k < 256) {
                this.tokenCodec = Codec.BYTE1;
            } else {
                int b = 1;
                while (true) {
                    b++;
                    if (b >= 5) {
                        break;
                    }
                    BHSDCodec codec = new BHSDCodec(b, 256 - this.l, 0);
                    if (codec.encodes(k)) {
                        this.tokenCodec = codec;
                        break;
                    }
                }
                if (this.tokenCodec == null) {
                    throw new Pack200Exception("Cannot calculate token codec from " + k + " and " + this.l);
                }
            }
        }
        this.lastBandLength += n;
        int[] result = this.tokenCodec.decodeInts(n, in);
        int last2 = 0;
        for (int i = 0; i < n; i++) {
            int index = result[i];
            if (index == 0) {
                this.lastBandLength++;
                int decode = this.unfavouredCodec.decode(in, last2);
                last2 = decode;
                result[i] = decode;
            } else {
                result[i] = this.favoured[index - 1];
            }
        }
        return result;
    }

    public int[] getFavoured() {
        return this.favoured;
    }

    public Codec getFavouredCodec() {
        return this.favouredCodec;
    }

    public Codec getUnfavouredCodec() {
        return this.unfavouredCodec;
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public byte[] encode(int value, int last) throws Pack200Exception {
        throw new Pack200Exception("Population encoding does not work unless the number of elements are known");
    }

    @Override // org.apache.commons.compress.harmony.pack200.Codec
    public byte[] encode(int value) throws Pack200Exception {
        throw new Pack200Exception("Population encoding does not work unless the number of elements are known");
    }

    public byte[] encode(int[] favoured, int[] tokens, int[] unfavoured) throws Pack200Exception {
        int[] favoured2 = new int[favoured.length + 1];
        System.arraycopy(favoured, 0, favoured2, 0, favoured.length);
        favoured2[favoured2.length - 1] = favoured[favoured.length - 1];
        byte[] favouredEncoded = this.favouredCodec.encode(favoured2);
        byte[] tokensEncoded = this.tokenCodec.encode(tokens);
        byte[] unfavouredEncoded = this.unfavouredCodec.encode(unfavoured);
        byte[] band = new byte[favouredEncoded.length + tokensEncoded.length + unfavouredEncoded.length];
        System.arraycopy(favouredEncoded, 0, band, 0, favouredEncoded.length);
        System.arraycopy(tokensEncoded, 0, band, favouredEncoded.length, tokensEncoded.length);
        System.arraycopy(unfavouredEncoded, 0, band, favouredEncoded.length + tokensEncoded.length, unfavouredEncoded.length);
        return band;
    }

    public Codec getTokenCodec() {
        return this.tokenCodec;
    }
}
