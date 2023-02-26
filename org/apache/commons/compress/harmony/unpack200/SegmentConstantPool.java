package org.apache.commons.compress.harmony.unpack200;

import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/SegmentConstantPool.class */
public class SegmentConstantPool {
    private final CpBands bands;
    private final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
    public static final int ALL = 0;
    public static final int UTF_8 = 1;
    public static final int CP_INT = 2;
    public static final int CP_FLOAT = 3;
    public static final int CP_LONG = 4;
    public static final int CP_DOUBLE = 5;
    public static final int CP_STRING = 6;
    public static final int CP_CLASS = 7;
    public static final int SIGNATURE = 8;
    public static final int CP_DESCR = 9;
    public static final int CP_FIELD = 10;
    public static final int CP_METHOD = 11;
    public static final int CP_IMETHOD = 12;
    protected static final String REGEX_MATCH_ALL = ".*";
    protected static final String INITSTRING = "<init>";
    protected static final String REGEX_MATCH_INIT = "^<init>.*";

    public SegmentConstantPool(CpBands bands) {
        this.bands = bands;
    }

    public ClassFileEntry getValue(int cp, long value) throws Pack200Exception {
        int index = (int) value;
        if (index == -1) {
            return null;
        }
        if (index < 0) {
            throw new Pack200Exception("Cannot have a negative range");
        }
        if (cp == 1) {
            return this.bands.cpUTF8Value(index);
        }
        if (cp == 2) {
            return this.bands.cpIntegerValue(index);
        }
        if (cp == 3) {
            return this.bands.cpFloatValue(index);
        }
        if (cp == 4) {
            return this.bands.cpLongValue(index);
        }
        if (cp == 5) {
            return this.bands.cpDoubleValue(index);
        }
        if (cp == 6) {
            return this.bands.cpStringValue(index);
        }
        if (cp == 7) {
            return this.bands.cpClassValue(index);
        }
        if (cp == 8) {
            return this.bands.cpSignatureValue(index);
        }
        if (cp == 9) {
            return this.bands.cpNameAndTypeValue(index);
        }
        throw new Error("Tried to get a value I don't know about: " + cp);
    }

    public ConstantPoolEntry getClassSpecificPoolEntry(int cp, long desiredIndex, String desiredClassName) throws Pack200Exception {
        String[] array;
        int index = (int) desiredIndex;
        if (cp == 10) {
            array = this.bands.getCpFieldClass();
        } else if (cp == 11) {
            array = this.bands.getCpMethodClass();
        } else if (cp == 12) {
            array = this.bands.getCpIMethodClass();
        } else {
            throw new Error("Don't know how to handle " + cp);
        }
        int realIndex = matchSpecificPoolEntryIndex(array, desiredClassName, index);
        return getConstantPoolEntry(cp, realIndex);
    }

    public ConstantPoolEntry getClassPoolEntry(String name) {
        String[] classes = this.bands.getCpClass();
        int index = matchSpecificPoolEntryIndex(classes, name, 0);
        if (index == -1) {
            return null;
        }
        try {
            return getConstantPoolEntry(7, index);
        } catch (Pack200Exception e) {
            throw new Error("Error getting class pool entry");
        }
    }

    public ConstantPoolEntry getInitMethodPoolEntry(int cp, long value, String desiredClassName) throws Pack200Exception {
        if (cp != 11) {
            throw new Error("Nothing but CP_METHOD can be an <init>");
        }
        int realIndex = matchSpecificPoolEntryIndex(this.bands.getCpMethodClass(), this.bands.getCpMethodDescriptor(), desiredClassName, REGEX_MATCH_INIT, (int) value);
        return getConstantPoolEntry(cp, realIndex);
    }

    protected int matchSpecificPoolEntryIndex(String[] nameArray, String compareString, int desiredIndex) {
        return matchSpecificPoolEntryIndex(nameArray, nameArray, compareString, REGEX_MATCH_ALL, desiredIndex);
    }

    protected int matchSpecificPoolEntryIndex(String[] primaryArray, String[] secondaryArray, String primaryCompareString, String secondaryCompareRegex, int desiredIndex) {
        int instanceCount = -1;
        List<Integer> indexList = this.arrayCache.indexesForArrayKey(primaryArray, primaryCompareString);
        if (indexList.isEmpty()) {
            return -1;
        }
        for (Integer element : indexList) {
            int arrayIndex = element.intValue();
            if (regexMatches(secondaryCompareRegex, secondaryArray[arrayIndex])) {
                instanceCount++;
                if (instanceCount == desiredIndex) {
                    return arrayIndex;
                }
            }
        }
        return -1;
    }

    protected static boolean regexMatches(String regexString, String compareString) {
        if (REGEX_MATCH_ALL.equals(regexString)) {
            return true;
        }
        if (REGEX_MATCH_INIT.equals(regexString)) {
            if (compareString.length() < INITSTRING.length()) {
                return false;
            }
            return INITSTRING.equals(compareString.substring(0, INITSTRING.length()));
        }
        throw new Error("regex trying to match a pattern I don't know: " + regexString);
    }

    public ConstantPoolEntry getConstantPoolEntry(int cp, long value) throws Pack200Exception {
        int index = (int) value;
        if (index == -1) {
            return null;
        }
        if (index < 0) {
            throw new Pack200Exception("Cannot have a negative range");
        }
        if (cp == 1) {
            return this.bands.cpUTF8Value(index);
        }
        if (cp == 2) {
            return this.bands.cpIntegerValue(index);
        }
        if (cp == 3) {
            return this.bands.cpFloatValue(index);
        }
        if (cp == 4) {
            return this.bands.cpLongValue(index);
        }
        if (cp == 5) {
            return this.bands.cpDoubleValue(index);
        }
        if (cp == 6) {
            return this.bands.cpStringValue(index);
        }
        if (cp == 7) {
            return this.bands.cpClassValue(index);
        }
        if (cp == 8) {
            throw new Error("I don't know what to do with signatures yet");
        }
        if (cp == 9) {
            throw new Error("I don't know what to do with descriptors yet");
        }
        if (cp == 10) {
            return this.bands.cpFieldValue(index);
        }
        if (cp == 11) {
            return this.bands.cpMethodValue(index);
        }
        if (cp == 12) {
            return this.bands.cpIMethodValue(index);
        }
        throw new Error("Get value incomplete");
    }
}
