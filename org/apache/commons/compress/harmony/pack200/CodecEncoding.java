package org.apache.commons.compress.harmony.pack200;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.cpio.CpioConstants;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CodecEncoding.class */
public class CodecEncoding {
    private static final BHSDCodec[] canonicalCodec = {null, new BHSDCodec(1, 256), new BHSDCodec(1, 256, 1), new BHSDCodec(1, 256, 0, 1), new BHSDCodec(1, 256, 1, 1), new BHSDCodec(2, 256), new BHSDCodec(2, 256, 1), new BHSDCodec(2, 256, 0, 1), new BHSDCodec(2, 256, 1, 1), new BHSDCodec(3, 256), new BHSDCodec(3, 256, 1), new BHSDCodec(3, 256, 0, 1), new BHSDCodec(3, 256, 1, 1), new BHSDCodec(4, 256), new BHSDCodec(4, 256, 1), new BHSDCodec(4, 256, 0, 1), new BHSDCodec(4, 256, 1, 1), new BHSDCodec(5, 4), new BHSDCodec(5, 4, 1), new BHSDCodec(5, 4, 2), new BHSDCodec(5, 16), new BHSDCodec(5, 16, 1), new BHSDCodec(5, 16, 2), new BHSDCodec(5, 32), new BHSDCodec(5, 32, 1), new BHSDCodec(5, 32, 2), new BHSDCodec(5, 64), new BHSDCodec(5, 64, 1), new BHSDCodec(5, 64, 2), new BHSDCodec(5, 128), new BHSDCodec(5, 128, 1), new BHSDCodec(5, 128, 2), new BHSDCodec(5, 4, 0, 1), new BHSDCodec(5, 4, 1, 1), new BHSDCodec(5, 4, 2, 1), new BHSDCodec(5, 16, 0, 1), new BHSDCodec(5, 16, 1, 1), new BHSDCodec(5, 16, 2, 1), new BHSDCodec(5, 32, 0, 1), new BHSDCodec(5, 32, 1, 1), new BHSDCodec(5, 32, 2, 1), new BHSDCodec(5, 64, 0, 1), new BHSDCodec(5, 64, 1, 1), new BHSDCodec(5, 64, 2, 1), new BHSDCodec(5, 128, 0, 1), new BHSDCodec(5, 128, 1, 1), new BHSDCodec(5, 128, 2, 1), new BHSDCodec(2, 192), new BHSDCodec(2, 224), new BHSDCodec(2, 240), new BHSDCodec(2, 248), new BHSDCodec(2, 252), new BHSDCodec(2, 8, 0, 1), new BHSDCodec(2, 8, 1, 1), new BHSDCodec(2, 16, 0, 1), new BHSDCodec(2, 16, 1, 1), new BHSDCodec(2, 32, 0, 1), new BHSDCodec(2, 32, 1, 1), new BHSDCodec(2, 64, 0, 1), new BHSDCodec(2, 64, 1, 1), new BHSDCodec(2, 128, 0, 1), new BHSDCodec(2, 128, 1, 1), new BHSDCodec(2, 192, 0, 1), new BHSDCodec(2, 192, 1, 1), new BHSDCodec(2, 224, 0, 1), new BHSDCodec(2, 224, 1, 1), new BHSDCodec(2, 240, 0, 1), new BHSDCodec(2, 240, 1, 1), new BHSDCodec(2, 248, 0, 1), new BHSDCodec(2, 248, 1, 1), new BHSDCodec(3, 192), new BHSDCodec(3, 224), new BHSDCodec(3, 240), new BHSDCodec(3, 248), new BHSDCodec(3, 252), new BHSDCodec(3, 8, 0, 1), new BHSDCodec(3, 8, 1, 1), new BHSDCodec(3, 16, 0, 1), new BHSDCodec(3, 16, 1, 1), new BHSDCodec(3, 32, 0, 1), new BHSDCodec(3, 32, 1, 1), new BHSDCodec(3, 64, 0, 1), new BHSDCodec(3, 64, 1, 1), new BHSDCodec(3, 128, 0, 1), new BHSDCodec(3, 128, 1, 1), new BHSDCodec(3, 192, 0, 1), new BHSDCodec(3, 192, 1, 1), new BHSDCodec(3, 224, 0, 1), new BHSDCodec(3, 224, 1, 1), new BHSDCodec(3, 240, 0, 1), new BHSDCodec(3, 240, 1, 1), new BHSDCodec(3, 248, 0, 1), new BHSDCodec(3, 248, 1, 1), new BHSDCodec(4, 192), new BHSDCodec(4, 224), new BHSDCodec(4, 240), new BHSDCodec(4, 248), new BHSDCodec(4, 252), new BHSDCodec(4, 8, 0, 1), new BHSDCodec(4, 8, 1, 1), new BHSDCodec(4, 16, 0, 1), new BHSDCodec(4, 16, 1, 1), new BHSDCodec(4, 32, 0, 1), new BHSDCodec(4, 32, 1, 1), new BHSDCodec(4, 64, 0, 1), new BHSDCodec(4, 64, 1, 1), new BHSDCodec(4, 128, 0, 1), new BHSDCodec(4, 128, 1, 1), new BHSDCodec(4, 192, 0, 1), new BHSDCodec(4, 192, 1, 1), new BHSDCodec(4, 224, 0, 1), new BHSDCodec(4, 224, 1, 1), new BHSDCodec(4, 240, 0, 1), new BHSDCodec(4, 240, 1, 1), new BHSDCodec(4, 248, 0, 1), new BHSDCodec(4, 248, 1, 1)};
    private static Map<BHSDCodec, Integer> canonicalCodecsToSpecifiers;

    public static Codec getCodec(int value, InputStream in, Codec defaultCodec) throws IOException, Pack200Exception {
        Codec aCodec;
        Codec bCodec;
        if (canonicalCodec.length != 116) {
            throw new Error("Canonical encodings have been incorrectly modified");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Encoding cannot be less than zero");
        }
        if (value == 0) {
            return defaultCodec;
        }
        if (value <= 115) {
            return canonicalCodec[value];
        }
        if (value == 116) {
            int code = in.read();
            if (code == -1) {
                throw new EOFException("End of buffer read whilst trying to decode codec");
            }
            int d = code & 1;
            int s = (code >> 1) & 3;
            int b = ((code >> 3) & 7) + 1;
            int code2 = in.read();
            if (code2 == -1) {
                throw new EOFException("End of buffer read whilst trying to decode codec");
            }
            int h = code2 + 1;
            return new BHSDCodec(b, h, s, d);
        } else if (value >= 117 && value <= 140) {
            int offset = value - 117;
            int kx = offset & 3;
            boolean kbflag = ((offset >> 2) & 1) == 1;
            boolean adef = ((offset >> 3) & 1) == 1;
            boolean bdef = ((offset >> 4) & 1) == 1;
            if (adef && bdef) {
                throw new Pack200Exception("ADef and BDef should never both be true");
            }
            int kb = kbflag ? in.read() : 3;
            int k = (kb + 1) * ((int) Math.pow(16.0d, kx));
            if (adef) {
                aCodec = defaultCodec;
            } else {
                aCodec = getCodec(in.read(), in, defaultCodec);
            }
            if (bdef) {
                bCodec = defaultCodec;
            } else {
                bCodec = getCodec(in.read(), in, defaultCodec);
            }
            return new RunCodec(k, aCodec, bCodec);
        } else if (value < 141 || value > 188) {
            throw new Pack200Exception("Invalid codec encoding byte (" + value + ") found");
        } else {
            int offset2 = value - 141;
            boolean fdef = (offset2 & 1) == 1;
            boolean udef = ((offset2 >> 1) & 1) == 1;
            int tdefl = offset2 >> 2;
            boolean tdef = tdefl != 0;
            int[] tdefToL = {0, 4, 8, 16, 32, 64, 128, 192, 224, 240, 248, 252};
            int l = tdefToL[tdefl];
            if (tdef) {
                Codec fCodec = fdef ? defaultCodec : getCodec(in.read(), in, defaultCodec);
                Codec uCodec = udef ? defaultCodec : getCodec(in.read(), in, defaultCodec);
                return new PopulationCodec(fCodec, l, uCodec);
            }
            Codec fCodec2 = fdef ? defaultCodec : getCodec(in.read(), in, defaultCodec);
            Codec tCodec = getCodec(in.read(), in, defaultCodec);
            Codec uCodec2 = udef ? defaultCodec : getCodec(in.read(), in, defaultCodec);
            return new PopulationCodec(fCodec2, tCodec, uCodec2);
        }
    }

    public static int getSpecifierForDefaultCodec(BHSDCodec defaultCodec) {
        return getSpecifier(defaultCodec, null)[0];
    }

    public static int[] getSpecifier(Codec codec, Codec defaultForBand) {
        int kb;
        int kx;
        if (canonicalCodecsToSpecifiers == null) {
            HashMap<BHSDCodec, Integer> reverseMap = new HashMap<>(canonicalCodec.length);
            for (int i = 0; i < canonicalCodec.length; i++) {
                reverseMap.put(canonicalCodec[i], Integer.valueOf(i));
            }
            canonicalCodecsToSpecifiers = reverseMap;
        }
        if (canonicalCodecsToSpecifiers.containsKey(codec)) {
            return new int[]{canonicalCodecsToSpecifiers.get(codec).intValue()};
        }
        if (codec instanceof BHSDCodec) {
            BHSDCodec bhsdCodec = (BHSDCodec) codec;
            int[] specifiers = new int[3];
            specifiers[0] = 116;
            specifiers[1] = (bhsdCodec.isDelta() ? 1 : 0) + (2 * bhsdCodec.getS()) + (8 * (bhsdCodec.getB() - 1));
            specifiers[2] = bhsdCodec.getH() - 1;
            return specifiers;
        } else if (codec instanceof RunCodec) {
            RunCodec runCodec = (RunCodec) codec;
            int k = runCodec.getK();
            if (k <= 256) {
                kb = 0;
                kx = k - 1;
            } else if (k <= 4096) {
                kb = 1;
                kx = (k / 16) - 1;
            } else if (k <= 65536) {
                kb = 2;
                kx = (k / 256) - 1;
            } else {
                kb = 3;
                kx = (k / CpioConstants.C_ISFIFO) - 1;
            }
            Codec aCodec = runCodec.getACodec();
            Codec bCodec = runCodec.getBCodec();
            int abDef = 0;
            if (aCodec.equals(defaultForBand)) {
                abDef = 1;
            } else if (bCodec.equals(defaultForBand)) {
                abDef = 2;
            }
            int first = 117 + kb + (kx == 3 ? 0 : 4) + (8 * abDef);
            int[] aSpecifier = abDef == 1 ? new int[0] : getSpecifier(aCodec, defaultForBand);
            int[] bSpecifier = abDef == 2 ? new int[0] : getSpecifier(bCodec, defaultForBand);
            int[] specifier = new int[1 + (kx == 3 ? 0 : 1) + aSpecifier.length + bSpecifier.length];
            specifier[0] = first;
            int index = 1;
            if (kx != 3) {
                specifier[1] = kx;
                index = 1 + 1;
            }
            for (int element : aSpecifier) {
                specifier[index] = element;
                index++;
            }
            for (int element2 : bSpecifier) {
                specifier[index] = element2;
                index++;
            }
            return specifier;
        } else if (codec instanceof PopulationCodec) {
            PopulationCodec populationCodec = (PopulationCodec) codec;
            Codec tokenCodec = populationCodec.getTokenCodec();
            Codec favouredCodec = populationCodec.getFavouredCodec();
            Codec unfavouredCodec = populationCodec.getUnfavouredCodec();
            int fDef = favouredCodec.equals(defaultForBand) ? 1 : 0;
            int uDef = unfavouredCodec.equals(defaultForBand) ? 1 : 0;
            int tDefL = 0;
            int[] favoured = populationCodec.getFavoured();
            if (favoured != null) {
                if (tokenCodec == Codec.BYTE1) {
                    tDefL = 1;
                } else if (tokenCodec instanceof BHSDCodec) {
                    BHSDCodec tokenBHSD = (BHSDCodec) tokenCodec;
                    if (tokenBHSD.getS() == 0) {
                        int[] possibleLValues = {4, 8, 16, 32, 64, 128, 192, 224, 240, 248, 252};
                        int l = 256 - tokenBHSD.getH();
                        int index2 = Arrays.binarySearch(possibleLValues, l);
                        if (index2 != -1) {
                            int i2 = index2 + 1;
                            tDefL = index2;
                        }
                    }
                }
            }
            int first2 = 141 + fDef + (2 * uDef) + (4 * tDefL);
            int[] favouredSpecifier = fDef == 1 ? new int[0] : getSpecifier(favouredCodec, defaultForBand);
            int[] tokenSpecifier = tDefL != 0 ? new int[0] : getSpecifier(tokenCodec, defaultForBand);
            int[] unfavouredSpecifier = uDef == 1 ? new int[0] : getSpecifier(unfavouredCodec, defaultForBand);
            int[] specifier2 = new int[1 + favouredSpecifier.length + unfavouredSpecifier.length + tokenSpecifier.length];
            specifier2[0] = first2;
            int index3 = 1;
            for (int element3 : favouredSpecifier) {
                specifier2[index3] = element3;
                index3++;
            }
            for (int element4 : tokenSpecifier) {
                specifier2[index3] = element4;
                index3++;
            }
            for (int element5 : unfavouredSpecifier) {
                specifier2[index3] = element5;
                index3++;
            }
            return specifier2;
        } else {
            return null;
        }
    }

    public static BHSDCodec getCanonicalCodec(int i) {
        return canonicalCodec[i];
    }
}
