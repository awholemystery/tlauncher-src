package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
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

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/CpBands.class */
public class CpBands extends BandSet {
    private final SegmentConstantPool pool;
    private String[] cpClass;
    private int[] cpClassInts;
    private int[] cpDescriptorNameInts;
    private int[] cpDescriptorTypeInts;
    private String[] cpDescriptor;
    private double[] cpDouble;
    private String[] cpFieldClass;
    private String[] cpFieldDescriptor;
    private int[] cpFieldClassInts;
    private int[] cpFieldDescriptorInts;
    private float[] cpFloat;
    private String[] cpIMethodClass;
    private String[] cpIMethodDescriptor;
    private int[] cpIMethodClassInts;
    private int[] cpIMethodDescriptorInts;
    private int[] cpInt;
    private long[] cpLong;
    private String[] cpMethodClass;
    private String[] cpMethodDescriptor;
    private int[] cpMethodClassInts;
    private int[] cpMethodDescriptorInts;
    private String[] cpSignature;
    private int[] cpSignatureInts;
    private String[] cpString;
    private int[] cpStringInts;
    private String[] cpUTF8;
    private final Map<String, CPUTF8> stringsToCPUTF8;
    private final Map<String, CPString> stringsToCPStrings;
    private final Map<Long, CPLong> longsToCPLongs;
    private final Map<Integer, CPInteger> integersToCPIntegers;
    private final Map<Float, CPFloat> floatsToCPFloats;
    private final Map<String, CPClass> stringsToCPClass;
    private final Map<Double, CPDouble> doublesToCPDoubles;
    private final Map<String, CPNameAndType> descriptorsToCPNameAndTypes;
    private Map<String, Integer> mapClass;
    private Map<String, Integer> mapDescriptor;
    private Map<String, Integer> mapUTF8;
    private Map<String, Integer> mapSignature;
    private int intOffset;
    private int floatOffset;
    private int longOffset;
    private int doubleOffset;
    private int stringOffset;
    private int classOffset;
    private int signatureOffset;
    private int descrOffset;
    private int fieldOffset;
    private int methodOffset;
    private int imethodOffset;

    public SegmentConstantPool getConstantPool() {
        return this.pool;
    }

    public CpBands(Segment segment) {
        super(segment);
        this.pool = new SegmentConstantPool(this);
        this.stringsToCPUTF8 = new HashMap();
        this.stringsToCPStrings = new HashMap();
        this.longsToCPLongs = new HashMap();
        this.integersToCPIntegers = new HashMap();
        this.floatsToCPFloats = new HashMap();
        this.stringsToCPClass = new HashMap();
        this.doublesToCPDoubles = new HashMap();
        this.descriptorsToCPNameAndTypes = new HashMap();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void read(InputStream in) throws IOException, Pack200Exception {
        parseCpUtf8(in);
        parseCpInt(in);
        parseCpFloat(in);
        parseCpLong(in);
        parseCpDouble(in);
        parseCpString(in);
        parseCpClass(in);
        parseCpSignature(in);
        parseCpDescriptor(in);
        parseCpField(in);
        parseCpMethod(in);
        parseCpIMethod(in);
        this.intOffset = this.cpUTF8.length;
        this.floatOffset = this.intOffset + this.cpInt.length;
        this.longOffset = this.floatOffset + this.cpFloat.length;
        this.doubleOffset = this.longOffset + this.cpLong.length;
        this.stringOffset = this.doubleOffset + this.cpDouble.length;
        this.classOffset = this.stringOffset + this.cpString.length;
        this.signatureOffset = this.classOffset + this.cpClass.length;
        this.descrOffset = this.signatureOffset + this.cpSignature.length;
        this.fieldOffset = this.descrOffset + this.cpDescriptor.length;
        this.methodOffset = this.fieldOffset + this.cpFieldClass.length;
        this.imethodOffset = this.methodOffset + this.cpMethodClass.length;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void unpack() {
    }

    private void parseCpClass(InputStream in) throws IOException, Pack200Exception {
        int cpClassCount = this.header.getCpClassCount();
        this.cpClassInts = decodeBandInt("cp_Class", in, Codec.UDELTA5, cpClassCount);
        this.cpClass = new String[cpClassCount];
        this.mapClass = new HashMap(cpClassCount);
        for (int i = 0; i < cpClassCount; i++) {
            this.cpClass[i] = this.cpUTF8[this.cpClassInts[i]];
            this.mapClass.put(this.cpClass[i], Integer.valueOf(i));
        }
    }

    private void parseCpDescriptor(InputStream in) throws IOException, Pack200Exception {
        int cpDescriptorCount = this.header.getCpDescriptorCount();
        this.cpDescriptorNameInts = decodeBandInt("cp_Descr_name", in, Codec.DELTA5, cpDescriptorCount);
        this.cpDescriptorTypeInts = decodeBandInt("cp_Descr_type", in, Codec.UDELTA5, cpDescriptorCount);
        String[] cpDescriptorNames = getReferences(this.cpDescriptorNameInts, this.cpUTF8);
        String[] cpDescriptorTypes = getReferences(this.cpDescriptorTypeInts, this.cpSignature);
        this.cpDescriptor = new String[cpDescriptorCount];
        this.mapDescriptor = new HashMap(cpDescriptorCount);
        for (int i = 0; i < cpDescriptorCount; i++) {
            this.cpDescriptor[i] = cpDescriptorNames[i] + ":" + cpDescriptorTypes[i];
            this.mapDescriptor.put(this.cpDescriptor[i], Integer.valueOf(i));
        }
    }

    private void parseCpDouble(InputStream in) throws IOException, Pack200Exception {
        int cpDoubleCount = this.header.getCpDoubleCount();
        long[] band = parseFlags("cp_Double", in, cpDoubleCount, Codec.UDELTA5, Codec.DELTA5);
        this.cpDouble = new double[band.length];
        Arrays.setAll(this.cpDouble, i -> {
            return Double.longBitsToDouble(band[i]);
        });
    }

    private void parseCpField(InputStream in) throws IOException, Pack200Exception {
        int cpFieldCount = this.header.getCpFieldCount();
        this.cpFieldClassInts = decodeBandInt("cp_Field_class", in, Codec.DELTA5, cpFieldCount);
        this.cpFieldDescriptorInts = decodeBandInt("cp_Field_desc", in, Codec.UDELTA5, cpFieldCount);
        this.cpFieldClass = new String[cpFieldCount];
        this.cpFieldDescriptor = new String[cpFieldCount];
        for (int i = 0; i < cpFieldCount; i++) {
            this.cpFieldClass[i] = this.cpClass[this.cpFieldClassInts[i]];
            this.cpFieldDescriptor[i] = this.cpDescriptor[this.cpFieldDescriptorInts[i]];
        }
    }

    private void parseCpFloat(InputStream in) throws IOException, Pack200Exception {
        int cpFloatCount = this.header.getCpFloatCount();
        this.cpFloat = new float[cpFloatCount];
        int[] floatBits = decodeBandInt("cp_Float", in, Codec.UDELTA5, cpFloatCount);
        for (int i = 0; i < cpFloatCount; i++) {
            this.cpFloat[i] = Float.intBitsToFloat(floatBits[i]);
        }
    }

    private void parseCpIMethod(InputStream in) throws IOException, Pack200Exception {
        int cpIMethodCount = this.header.getCpIMethodCount();
        this.cpIMethodClassInts = decodeBandInt("cp_Imethod_class", in, Codec.DELTA5, cpIMethodCount);
        this.cpIMethodDescriptorInts = decodeBandInt("cp_Imethod_desc", in, Codec.UDELTA5, cpIMethodCount);
        this.cpIMethodClass = new String[cpIMethodCount];
        this.cpIMethodDescriptor = new String[cpIMethodCount];
        for (int i = 0; i < cpIMethodCount; i++) {
            this.cpIMethodClass[i] = this.cpClass[this.cpIMethodClassInts[i]];
            this.cpIMethodDescriptor[i] = this.cpDescriptor[this.cpIMethodDescriptorInts[i]];
        }
    }

    private void parseCpInt(InputStream in) throws IOException, Pack200Exception {
        int cpIntCount = this.header.getCpIntCount();
        this.cpInt = decodeBandInt("cpInt", in, Codec.UDELTA5, cpIntCount);
    }

    private void parseCpLong(InputStream in) throws IOException, Pack200Exception {
        int cpLongCount = this.header.getCpLongCount();
        this.cpLong = parseFlags("cp_Long", in, cpLongCount, Codec.UDELTA5, Codec.DELTA5);
    }

    private void parseCpMethod(InputStream in) throws IOException, Pack200Exception {
        int cpMethodCount = this.header.getCpMethodCount();
        this.cpMethodClassInts = decodeBandInt("cp_Method_class", in, Codec.DELTA5, cpMethodCount);
        this.cpMethodDescriptorInts = decodeBandInt("cp_Method_desc", in, Codec.UDELTA5, cpMethodCount);
        this.cpMethodClass = new String[cpMethodCount];
        this.cpMethodDescriptor = new String[cpMethodCount];
        for (int i = 0; i < cpMethodCount; i++) {
            this.cpMethodClass[i] = this.cpClass[this.cpMethodClassInts[i]];
            this.cpMethodDescriptor[i] = this.cpDescriptor[this.cpMethodDescriptorInts[i]];
        }
    }

    private void parseCpSignature(InputStream in) throws IOException, Pack200Exception {
        int cpSignatureCount = this.header.getCpSignatureCount();
        this.cpSignatureInts = decodeBandInt("cp_Signature_form", in, Codec.DELTA5, cpSignatureCount);
        String[] cpSignatureForm = getReferences(this.cpSignatureInts, this.cpUTF8);
        this.cpSignature = new String[cpSignatureCount];
        this.mapSignature = new HashMap();
        int lCount = 0;
        for (int i = 0; i < cpSignatureCount; i++) {
            char[] chars = cpSignatureForm[i].toCharArray();
            for (char element : chars) {
                if (element == 'L') {
                    this.cpSignatureInts[i] = -1;
                    lCount++;
                }
            }
        }
        String[] cpSignatureClasses = parseReferences("cp_Signature_classes", in, Codec.UDELTA5, lCount, this.cpClass);
        int index = 0;
        for (int i2 = 0; i2 < cpSignatureCount; i2++) {
            String form = cpSignatureForm[i2];
            int len = form.length();
            StringBuilder signature = new StringBuilder(64);
            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                char c = form.charAt(j);
                signature.append(c);
                if (c == 'L') {
                    String className = cpSignatureClasses[index];
                    list.add(className);
                    signature.append(className);
                    index++;
                }
            }
            this.cpSignature[i2] = signature.toString();
            this.mapSignature.put(signature.toString(), Integer.valueOf(i2));
        }
    }

    private void parseCpString(InputStream in) throws IOException, Pack200Exception {
        int cpStringCount = this.header.getCpStringCount();
        this.cpStringInts = decodeBandInt("cp_String", in, Codec.UDELTA5, cpStringCount);
        this.cpString = new String[cpStringCount];
        Arrays.setAll(this.cpString, i -> {
            return this.cpUTF8[this.cpStringInts[i]];
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void parseCpUtf8(InputStream in) throws IOException, Pack200Exception {
        int cpUTF8Count = this.header.getCpUTF8Count();
        this.cpUTF8 = new String[cpUTF8Count];
        this.mapUTF8 = new HashMap(cpUTF8Count + 1);
        this.cpUTF8[0] = CoreConstants.EMPTY_STRING;
        this.mapUTF8.put(CoreConstants.EMPTY_STRING, 0);
        int[] prefix = decodeBandInt("cpUTF8Prefix", in, Codec.DELTA5, cpUTF8Count - 2);
        int charCount = 0;
        int bigSuffixCount = 0;
        int[] suffix = decodeBandInt("cpUTF8Suffix", in, Codec.UNSIGNED5, cpUTF8Count - 1);
        for (int element : suffix) {
            if (element == 0) {
                bigSuffixCount++;
            } else {
                charCount += element;
            }
        }
        char[] data = new char[charCount];
        int[] dataBand = decodeBandInt("cp_Utf8_chars", in, Codec.CHAR3, charCount);
        for (int i = 0; i < data.length; i++) {
            data[i] = (char) dataBand[i];
        }
        int[] bigSuffixCounts = decodeBandInt("cp_Utf8_big_suffix", in, Codec.DELTA5, bigSuffixCount);
        int[] iArr = new int[bigSuffixCount];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = decodeBandInt("cp_Utf8_big_chars " + i2, in, Codec.DELTA5, bigSuffixCounts[i2]);
        }
        char[] cArr = new char[bigSuffixCount];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            cArr[i3] = new char[iArr[i3].length];
            for (int j = 0; j < iArr[i3].length; j++) {
                cArr[i3][j] = (char) iArr[i3][j];
            }
        }
        int charCount2 = 0;
        int bigSuffixCount2 = 0;
        int i4 = 1;
        while (i4 < cpUTF8Count) {
            String lastString = this.cpUTF8[i4 - 1];
            if (suffix[i4 - 1] == 0) {
                int i5 = bigSuffixCount2;
                bigSuffixCount2++;
                this.cpUTF8[i4] = lastString.substring(0, i4 > 1 ? prefix[i4 - 2] : 0) + new String(cArr[i5]);
                this.mapUTF8.put(this.cpUTF8[i4], Integer.valueOf(i4));
            } else {
                this.cpUTF8[i4] = lastString.substring(0, i4 > 1 ? prefix[i4 - 2] : 0) + new String(data, charCount2, suffix[i4 - 1]);
                charCount2 += suffix[i4 - 1];
                this.mapUTF8.put(this.cpUTF8[i4], Integer.valueOf(i4));
            }
            i4++;
        }
    }

    public String[] getCpClass() {
        return this.cpClass;
    }

    public String[] getCpDescriptor() {
        return this.cpDescriptor;
    }

    public String[] getCpFieldClass() {
        return this.cpFieldClass;
    }

    public String[] getCpIMethodClass() {
        return this.cpIMethodClass;
    }

    public int[] getCpInt() {
        return this.cpInt;
    }

    public long[] getCpLong() {
        return this.cpLong;
    }

    public String[] getCpMethodClass() {
        return this.cpMethodClass;
    }

    public String[] getCpMethodDescriptor() {
        return this.cpMethodDescriptor;
    }

    public String[] getCpSignature() {
        return this.cpSignature;
    }

    public String[] getCpUTF8() {
        return this.cpUTF8;
    }

    public CPUTF8 cpUTF8Value(int index) {
        String string = this.cpUTF8[index];
        CPUTF8 cputf8 = this.stringsToCPUTF8.get(string);
        if (cputf8 == null) {
            cputf8 = new CPUTF8(string, index);
            this.stringsToCPUTF8.put(string, cputf8);
        } else if (cputf8.getGlobalIndex() > index) {
            cputf8.setGlobalIndex(index);
        }
        return cputf8;
    }

    public CPUTF8 cpUTF8Value(String string) {
        return cpUTF8Value(string, true);
    }

    public CPUTF8 cpUTF8Value(String string, boolean searchForIndex) {
        CPUTF8 cputf8 = this.stringsToCPUTF8.get(string);
        if (cputf8 == null) {
            Integer index = null;
            if (searchForIndex) {
                index = this.mapUTF8.get(string);
            }
            if (index != null) {
                return cpUTF8Value(index.intValue());
            }
            if (searchForIndex) {
                index = this.mapSignature.get(string);
            }
            if (index != null) {
                return cpSignatureValue(index.intValue());
            }
            cputf8 = new CPUTF8(string, -1);
            this.stringsToCPUTF8.put(string, cputf8);
        }
        return cputf8;
    }

    public CPString cpStringValue(int index) {
        String string = this.cpString[index];
        int utf8Index = this.cpStringInts[index];
        int globalIndex = this.stringOffset + index;
        CPString cpString = this.stringsToCPStrings.get(string);
        if (cpString == null) {
            cpString = new CPString(cpUTF8Value(utf8Index), globalIndex);
            this.stringsToCPStrings.put(string, cpString);
        }
        return cpString;
    }

    public CPLong cpLongValue(int index) {
        Long l = Long.valueOf(this.cpLong[index]);
        CPLong cpLong = this.longsToCPLongs.get(l);
        if (cpLong == null) {
            cpLong = new CPLong(l, index + this.longOffset);
            this.longsToCPLongs.put(l, cpLong);
        }
        return cpLong;
    }

    public CPInteger cpIntegerValue(int index) {
        Integer i = Integer.valueOf(this.cpInt[index]);
        CPInteger cpInteger = this.integersToCPIntegers.get(i);
        if (cpInteger == null) {
            cpInteger = new CPInteger(i, index + this.intOffset);
            this.integersToCPIntegers.put(i, cpInteger);
        }
        return cpInteger;
    }

    public CPFloat cpFloatValue(int index) {
        Float f = Float.valueOf(this.cpFloat[index]);
        CPFloat cpFloat = this.floatsToCPFloats.get(f);
        if (cpFloat == null) {
            cpFloat = new CPFloat(f, index + this.floatOffset);
            this.floatsToCPFloats.put(f, cpFloat);
        }
        return cpFloat;
    }

    public CPClass cpClassValue(int index) {
        String string = this.cpClass[index];
        int utf8Index = this.cpClassInts[index];
        int globalIndex = this.classOffset + index;
        CPClass cpString = this.stringsToCPClass.get(string);
        if (cpString == null) {
            cpString = new CPClass(cpUTF8Value(utf8Index), globalIndex);
            this.stringsToCPClass.put(string, cpString);
        }
        return cpString;
    }

    public CPClass cpClassValue(String string) {
        CPClass cpString = this.stringsToCPClass.get(string);
        if (cpString == null) {
            Integer index = this.mapClass.get(string);
            if (index != null) {
                return cpClassValue(index.intValue());
            }
            cpString = new CPClass(cpUTF8Value(string, false), -1);
            this.stringsToCPClass.put(string, cpString);
        }
        return cpString;
    }

    public CPDouble cpDoubleValue(int index) {
        Double dbl = Double.valueOf(this.cpDouble[index]);
        CPDouble cpDouble = this.doublesToCPDoubles.get(dbl);
        if (cpDouble == null) {
            cpDouble = new CPDouble(dbl, index + this.doubleOffset);
            this.doublesToCPDoubles.put(dbl, cpDouble);
        }
        return cpDouble;
    }

    public CPNameAndType cpNameAndTypeValue(int index) {
        String descriptor = this.cpDescriptor[index];
        CPNameAndType cpNameAndType = this.descriptorsToCPNameAndTypes.get(descriptor);
        if (cpNameAndType == null) {
            int nameIndex = this.cpDescriptorNameInts[index];
            int descriptorIndex = this.cpDescriptorTypeInts[index];
            CPUTF8 name = cpUTF8Value(nameIndex);
            CPUTF8 descriptorU = cpSignatureValue(descriptorIndex);
            cpNameAndType = new CPNameAndType(name, descriptorU, index + this.descrOffset);
            this.descriptorsToCPNameAndTypes.put(descriptor, cpNameAndType);
        }
        return cpNameAndType;
    }

    public CPInterfaceMethodRef cpIMethodValue(int index) {
        return new CPInterfaceMethodRef(cpClassValue(this.cpIMethodClassInts[index]), cpNameAndTypeValue(this.cpIMethodDescriptorInts[index]), index + this.imethodOffset);
    }

    public CPMethodRef cpMethodValue(int index) {
        return new CPMethodRef(cpClassValue(this.cpMethodClassInts[index]), cpNameAndTypeValue(this.cpMethodDescriptorInts[index]), index + this.methodOffset);
    }

    public CPFieldRef cpFieldValue(int index) {
        return new CPFieldRef(cpClassValue(this.cpFieldClassInts[index]), cpNameAndTypeValue(this.cpFieldDescriptorInts[index]), index + this.fieldOffset);
    }

    public CPUTF8 cpSignatureValue(int index) {
        int globalIndex;
        if (this.cpSignatureInts[index] != -1) {
            globalIndex = this.cpSignatureInts[index];
        } else {
            globalIndex = index + this.signatureOffset;
        }
        String string = this.cpSignature[index];
        CPUTF8 cpUTF8 = this.stringsToCPUTF8.get(string);
        if (cpUTF8 == null) {
            cpUTF8 = new CPUTF8(string, globalIndex);
            this.stringsToCPUTF8.put(string, cpUTF8);
        }
        return cpUTF8;
    }

    public CPNameAndType cpNameAndTypeValue(String descriptor) {
        CPNameAndType cpNameAndType = this.descriptorsToCPNameAndTypes.get(descriptor);
        if (cpNameAndType == null) {
            Integer index = this.mapDescriptor.get(descriptor);
            if (index != null) {
                return cpNameAndTypeValue(index.intValue());
            }
            int colon = descriptor.indexOf(58);
            String nameString = descriptor.substring(0, colon);
            String descriptorString = descriptor.substring(colon + 1);
            CPUTF8 name = cpUTF8Value(nameString, true);
            CPUTF8 descriptorU = cpUTF8Value(descriptorString, true);
            cpNameAndType = new CPNameAndType(name, descriptorU, (-1) + this.descrOffset);
            this.descriptorsToCPNameAndTypes.put(descriptor, cpNameAndType);
        }
        return cpNameAndType;
    }

    public int[] getCpDescriptorNameInts() {
        return this.cpDescriptorNameInts;
    }

    public int[] getCpDescriptorTypeInts() {
        return this.cpDescriptorTypeInts;
    }
}
