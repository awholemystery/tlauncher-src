package org.apache.commons.compress.harmony.unpack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/SegmentUtils.class */
public final class SegmentUtils {
    public static int countArgs(String descriptor) {
        return countArgs(descriptor, 1);
    }

    public static int countInvokeInterfaceArgs(String descriptor) {
        return countArgs(descriptor, 2);
    }

    protected static int countArgs(String descriptor, int widthOfLongsAndDoubles) {
        int bra = descriptor.indexOf(40);
        int ket = descriptor.indexOf(41);
        if (bra == -1 || ket == -1 || ket < bra) {
            throw new IllegalArgumentException("No arguments");
        }
        boolean inType = false;
        boolean consumingNextType = false;
        int count = 0;
        for (int i = bra + 1; i < ket; i++) {
            char charAt = descriptor.charAt(i);
            if (inType && charAt == ';') {
                inType = false;
                consumingNextType = false;
            } else if (!inType && charAt == 'L') {
                inType = true;
                count++;
            } else if (charAt == '[') {
                consumingNextType = true;
            } else if (!inType) {
                if (consumingNextType) {
                    count++;
                    consumingNextType = false;
                } else if (charAt == 'D' || charAt == 'J') {
                    count += widthOfLongsAndDoubles;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countMatches(long[] flags, IMatcher matcher) {
        int count = 0;
        for (long flag : flags) {
            if (matcher.matches(flag)) {
                count++;
            }
        }
        return count;
    }

    public static int countBit16(int[] flags) {
        int count = 0;
        for (int flag : flags) {
            if ((flag & IcTuple.NESTED_CLASS_FLAG) != 0) {
                count++;
            }
        }
        return count;
    }

    public static int countBit16(long[] flags) {
        int count = 0;
        for (long flag : flags) {
            if ((flag & 65536) != 0) {
                count++;
            }
        }
        return count;
    }

    public static int countBit16(long[][] flags) {
        int count = 0;
        for (long[] flag : flags) {
            for (long element : flag) {
                if ((element & 65536) != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countMatches(long[][] flags, IMatcher matcher) {
        int count = 0;
        for (long[] flag : flags) {
            count += countMatches(flag, matcher);
        }
        return count;
    }
}
