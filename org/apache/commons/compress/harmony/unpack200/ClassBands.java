package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.action.Action;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantValueAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.DeprecatedAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.EnclosingMethodAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionsAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.LineNumberTableAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.LocalVariableTableAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.LocalVariableTypeTableAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.SignatureAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.SourceFileAttribute;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/ClassBands.class */
public class ClassBands extends BandSet {
    private int[] classFieldCount;
    private long[] classFlags;
    private long[] classAccessFlags;
    private int[][] classInterfacesInts;
    private int[] classMethodCount;
    private int[] classSuperInts;
    private String[] classThis;
    private int[] classThisInts;
    private ArrayList<Attribute>[] classAttributes;
    private int[] classVersionMajor;
    private int[] classVersionMinor;
    private IcTuple[][] icLocal;
    private List<Attribute>[] codeAttributes;
    private int[] codeHandlerCount;
    private int[] codeMaxNALocals;
    private int[] codeMaxStack;
    private ArrayList<Attribute>[][] fieldAttributes;
    private String[][] fieldDescr;
    private int[][] fieldDescrInts;
    private long[][] fieldFlags;
    private long[][] fieldAccessFlags;
    private ArrayList<Attribute>[][] methodAttributes;
    private String[][] methodDescr;
    private int[][] methodDescrInts;
    private long[][] methodFlags;
    private long[][] methodAccessFlags;
    private final AttributeLayoutMap attrMap;
    private final CpBands cpBands;
    private final SegmentOptions options;
    private final int classCount;
    private int[] methodAttrCalls;
    private int[][] codeHandlerStartP;
    private int[][] codeHandlerEndPO;
    private int[][] codeHandlerCatchPO;
    private int[][] codeHandlerClassRCN;
    private boolean[] codeHasAttributes;

    public ClassBands(Segment segment) {
        super(segment);
        this.attrMap = segment.getAttrDefinitionBands().getAttributeDefinitionMap();
        this.cpBands = segment.getCpBands();
        this.classCount = this.header.getClassCount();
        this.options = this.header.getOptions();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void read(InputStream in) throws IOException, Pack200Exception {
        int classCount = this.header.getClassCount();
        this.classThisInts = decodeBandInt("class_this", in, Codec.DELTA5, classCount);
        this.classThis = getReferences(this.classThisInts, this.cpBands.getCpClass());
        this.classSuperInts = decodeBandInt("class_super", in, Codec.DELTA5, classCount);
        int[] classInterfaceLengths = decodeBandInt("class_interface_count", in, Codec.DELTA5, classCount);
        this.classInterfacesInts = decodeBandInt("class_interface", in, Codec.DELTA5, classInterfaceLengths);
        this.classFieldCount = decodeBandInt("class_field_count", in, Codec.DELTA5, classCount);
        this.classMethodCount = decodeBandInt("class_method_count", in, Codec.DELTA5, classCount);
        parseFieldBands(in);
        parseMethodBands(in);
        parseClassAttrBands(in);
        parseCodeBands(in);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void unpack() {
    }

    private void parseFieldBands(InputStream in) throws IOException, Pack200Exception {
        this.fieldDescrInts = decodeBandInt("field_descr", in, Codec.DELTA5, this.classFieldCount);
        this.fieldDescr = getReferences(this.fieldDescrInts, this.cpBands.getCpDescriptor());
        parseFieldAttrBands(in);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [java.util.ArrayList<org.apache.commons.compress.harmony.unpack200.bytecode.Attribute>[][], java.util.ArrayList[]] */
    private void parseFieldAttrBands(InputStream in) throws IOException, Pack200Exception {
        this.fieldFlags = parseFlags("field_flags", in, this.classFieldCount, Codec.UNSIGNED5, this.options.hasFieldFlagsHi());
        int fieldAttrCount = SegmentUtils.countBit16(this.fieldFlags);
        int[] fieldAttrCounts = decodeBandInt("field_attr_count", in, Codec.UNSIGNED5, fieldAttrCount);
        int[][] fieldAttrIndexes = decodeBandInt("field_attr_indexes", in, Codec.UNSIGNED5, fieldAttrCounts);
        int callCount = getCallCount(fieldAttrIndexes, this.fieldFlags, 1);
        int[] fieldAttrCalls = decodeBandInt("field_attr_calls", in, Codec.UNSIGNED5, callCount);
        this.fieldAttributes = new ArrayList[this.classCount];
        for (int i = 0; i < this.classCount; i++) {
            this.fieldAttributes[i] = new ArrayList[this.fieldFlags[i].length];
            for (int j = 0; j < this.fieldFlags[i].length; j++) {
                this.fieldAttributes[i][j] = new ArrayList<>();
            }
        }
        AttributeLayout constantValueLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_CONSTANT_VALUE, 1);
        int constantCount = SegmentUtils.countMatches(this.fieldFlags, constantValueLayout);
        int[] field_constantValue_KQ = decodeBandInt("field_ConstantValue_KQ", in, Codec.UNSIGNED5, constantCount);
        int constantValueIndex = 0;
        AttributeLayout signatureLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_SIGNATURE, 1);
        int signatureCount = SegmentUtils.countMatches(this.fieldFlags, signatureLayout);
        int[] fieldSignatureRS = decodeBandInt("field_Signature_RS", in, Codec.UNSIGNED5, signatureCount);
        int signatureIndex = 0;
        AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_DEPRECATED, 1);
        for (int i2 = 0; i2 < this.classCount; i2++) {
            for (int j2 = 0; j2 < this.fieldFlags[i2].length; j2++) {
                long flag = this.fieldFlags[i2][j2];
                if (deprecatedLayout.matches(flag)) {
                    this.fieldAttributes[i2][j2].add(new DeprecatedAttribute());
                }
                if (constantValueLayout.matches(flag)) {
                    long result = field_constantValue_KQ[constantValueIndex];
                    String desc = this.fieldDescr[i2][j2];
                    int colon = desc.indexOf(58);
                    String type = desc.substring(colon + 1);
                    type = (type.equals("B") || type.equals("S") || type.equals("C") || type.equals("Z")) ? "I" : "I";
                    ClassFileEntry value = constantValueLayout.getValue(result, type, this.cpBands.getConstantPool());
                    this.fieldAttributes[i2][j2].add(new ConstantValueAttribute(value));
                    constantValueIndex++;
                }
                if (signatureLayout.matches(flag)) {
                    long result2 = fieldSignatureRS[signatureIndex];
                    String desc2 = this.fieldDescr[i2][j2];
                    int colon2 = desc2.indexOf(58);
                    CPUTF8 value2 = (CPUTF8) signatureLayout.getValue(result2, desc2.substring(colon2 + 1), this.cpBands.getConstantPool());
                    this.fieldAttributes[i2][j2].add(new SignatureAttribute(value2));
                    signatureIndex++;
                }
            }
        }
        int backwardsCallIndex = parseFieldMetadataBands(in, fieldAttrCalls);
        int limit = this.options.hasFieldFlagsHi() ? 62 : 31;
        AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
        int[] counts = new int[limit + 1];
        List<Attribute>[] otherAttributes = new List[limit + 1];
        for (int i3 = 0; i3 < limit; i3++) {
            AttributeLayout layout = this.attrMap.getAttributeLayout(i3, 1);
            if (layout != null && !layout.isDefaultLayout()) {
                otherLayouts[i3] = layout;
                counts[i3] = SegmentUtils.countMatches(this.fieldFlags, layout);
            }
        }
        for (int i4 = 0; i4 < counts.length; i4++) {
            if (counts[i4] > 0) {
                NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[i4]);
                otherAttributes[i4] = bands.parseAttributes(in, counts[i4]);
                int numBackwardsCallables = otherLayouts[i4].numBackwardsCallables();
                if (numBackwardsCallables > 0) {
                    int[] backwardsCalls = new int[numBackwardsCallables];
                    System.arraycopy(fieldAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
                    bands.setBackwardsCalls(backwardsCalls);
                    backwardsCallIndex += numBackwardsCallables;
                }
            }
        }
        for (int i5 = 0; i5 < this.classCount; i5++) {
            for (int j3 = 0; j3 < this.fieldFlags[i5].length; j3++) {
                long flag2 = this.fieldFlags[i5][j3];
                int othersAddedAtStart = 0;
                for (int k = 0; k < otherLayouts.length; k++) {
                    if (otherLayouts[k] != null && otherLayouts[k].matches(flag2)) {
                        if (otherLayouts[k].getIndex() < 15) {
                            int i6 = othersAddedAtStart;
                            othersAddedAtStart++;
                            this.fieldAttributes[i5][j3].add(i6, otherAttributes[k].get(0));
                        } else {
                            this.fieldAttributes[i5][j3].add(otherAttributes[k].get(0));
                        }
                        otherAttributes[k].remove(0);
                    }
                }
            }
        }
    }

    private void parseMethodBands(InputStream in) throws IOException, Pack200Exception {
        this.methodDescrInts = decodeBandInt("method_descr", in, Codec.MDELTA5, this.classMethodCount);
        this.methodDescr = getReferences(this.methodDescrInts, this.cpBands.getCpDescriptor());
        parseMethodAttrBands(in);
    }

    /* JADX WARN: Type inference failed for: r1v9, types: [java.util.ArrayList<org.apache.commons.compress.harmony.unpack200.bytecode.Attribute>[][], java.util.ArrayList[]] */
    private void parseMethodAttrBands(InputStream in) throws IOException, Pack200Exception {
        this.methodFlags = parseFlags("method_flags", in, this.classMethodCount, Codec.UNSIGNED5, this.options.hasMethodFlagsHi());
        int methodAttrCount = SegmentUtils.countBit16(this.methodFlags);
        int[] methodAttrCounts = decodeBandInt("method_attr_count", in, Codec.UNSIGNED5, methodAttrCount);
        int[][] methodAttrIndexes = decodeBandInt("method_attr_indexes", in, Codec.UNSIGNED5, methodAttrCounts);
        int callCount = getCallCount(methodAttrIndexes, this.methodFlags, 2);
        this.methodAttrCalls = decodeBandInt("method_attr_calls", in, Codec.UNSIGNED5, callCount);
        this.methodAttributes = new ArrayList[this.classCount];
        for (int i = 0; i < this.classCount; i++) {
            this.methodAttributes[i] = new ArrayList[this.methodFlags[i].length];
            for (int j = 0; j < this.methodFlags[i].length; j++) {
                this.methodAttributes[i][j] = new ArrayList<>();
            }
        }
        AttributeLayout methodExceptionsLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_EXCEPTIONS, 2);
        int count = SegmentUtils.countMatches(this.methodFlags, methodExceptionsLayout);
        int[] numExceptions = decodeBandInt("method_Exceptions_n", in, Codec.UNSIGNED5, count);
        int[][] methodExceptionsRS = decodeBandInt("method_Exceptions_RC", in, Codec.UNSIGNED5, numExceptions);
        AttributeLayout methodSignatureLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_SIGNATURE, 2);
        int count1 = SegmentUtils.countMatches(this.methodFlags, methodSignatureLayout);
        int[] methodSignatureRS = decodeBandInt("method_signature_RS", in, Codec.UNSIGNED5, count1);
        AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_DEPRECATED, 2);
        int methodExceptionsIndex = 0;
        int methodSignatureIndex = 0;
        for (int i2 = 0; i2 < this.methodAttributes.length; i2++) {
            for (int j2 = 0; j2 < this.methodAttributes[i2].length; j2++) {
                long flag = this.methodFlags[i2][j2];
                if (methodExceptionsLayout.matches(flag)) {
                    int n = numExceptions[methodExceptionsIndex];
                    int[] exceptions = methodExceptionsRS[methodExceptionsIndex];
                    CPClass[] exceptionClasses = new CPClass[n];
                    for (int k = 0; k < n; k++) {
                        exceptionClasses[k] = this.cpBands.cpClassValue(exceptions[k]);
                    }
                    this.methodAttributes[i2][j2].add(new ExceptionsAttribute(exceptionClasses));
                    methodExceptionsIndex++;
                }
                if (methodSignatureLayout.matches(flag)) {
                    long result = methodSignatureRS[methodSignatureIndex];
                    String desc = this.methodDescr[i2][j2];
                    int colon = desc.indexOf(58);
                    String type = desc.substring(colon + 1);
                    type = (type.equals("B") || type.equals("H")) ? "I" : "I";
                    CPUTF8 value = (CPUTF8) methodSignatureLayout.getValue(result, type, this.cpBands.getConstantPool());
                    this.methodAttributes[i2][j2].add(new SignatureAttribute(value));
                    methodSignatureIndex++;
                }
                if (deprecatedLayout.matches(flag)) {
                    this.methodAttributes[i2][j2].add(new DeprecatedAttribute());
                }
            }
        }
        int backwardsCallIndex = parseMethodMetadataBands(in, this.methodAttrCalls);
        int limit = this.options.hasMethodFlagsHi() ? 62 : 31;
        AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
        int[] counts = new int[limit + 1];
        for (int i3 = 0; i3 < limit; i3++) {
            AttributeLayout layout = this.attrMap.getAttributeLayout(i3, 2);
            if (layout != null && !layout.isDefaultLayout()) {
                otherLayouts[i3] = layout;
                counts[i3] = SegmentUtils.countMatches(this.methodFlags, layout);
            }
        }
        List<Attribute>[] otherAttributes = new List[limit + 1];
        for (int i4 = 0; i4 < counts.length; i4++) {
            if (counts[i4] > 0) {
                NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[i4]);
                otherAttributes[i4] = bands.parseAttributes(in, counts[i4]);
                int numBackwardsCallables = otherLayouts[i4].numBackwardsCallables();
                if (numBackwardsCallables > 0) {
                    int[] backwardsCalls = new int[numBackwardsCallables];
                    System.arraycopy(this.methodAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
                    bands.setBackwardsCalls(backwardsCalls);
                    backwardsCallIndex += numBackwardsCallables;
                }
            }
        }
        for (int i5 = 0; i5 < this.methodAttributes.length; i5++) {
            for (int j3 = 0; j3 < this.methodAttributes[i5].length; j3++) {
                long flag2 = this.methodFlags[i5][j3];
                int othersAddedAtStart = 0;
                for (int k2 = 0; k2 < otherLayouts.length; k2++) {
                    if (otherLayouts[k2] != null && otherLayouts[k2].matches(flag2)) {
                        if (otherLayouts[k2].getIndex() < 15) {
                            int i6 = othersAddedAtStart;
                            othersAddedAtStart++;
                            this.methodAttributes[i5][j3].add(i6, otherAttributes[k2].get(0));
                        } else {
                            this.methodAttributes[i5][j3].add(otherAttributes[k2].get(0));
                        }
                        otherAttributes[k2].remove(0);
                    }
                }
            }
        }
    }

    private int getCallCount(int[][] methodAttrIndexes, long[][] flags, int context) {
        int callCount = 0;
        for (int i = 0; i < methodAttrIndexes.length; i++) {
            for (int j = 0; j < methodAttrIndexes[i].length; j++) {
                int index = methodAttrIndexes[i][j];
                AttributeLayout layout = this.attrMap.getAttributeLayout(index, context);
                callCount += layout.numBackwardsCallables();
            }
        }
        int layoutsUsed = 0;
        for (int i2 = 0; i2 < flags.length; i2++) {
            for (int j2 = 0; j2 < flags[i2].length; j2++) {
                layoutsUsed = (int) (layoutsUsed | flags[i2][j2]);
            }
        }
        for (int i3 = 0; i3 < 26; i3++) {
            if ((layoutsUsed & (1 << i3)) != 0) {
                AttributeLayout layout2 = this.attrMap.getAttributeLayout(i3, context);
                callCount += layout2.numBackwardsCallables();
            }
        }
        return callCount;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v43, types: [org.apache.commons.compress.harmony.unpack200.IcTuple[], org.apache.commons.compress.harmony.unpack200.IcTuple[][]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [long[], long[][]] */
    private void parseClassAttrBands(InputStream in) throws IOException, Pack200Exception {
        String[] cpUTF8 = this.cpBands.getCpUTF8();
        String[] cpClass = this.cpBands.getCpClass();
        this.classAttributes = new ArrayList[this.classCount];
        Arrays.setAll(this.classAttributes, i -> {
            return new ArrayList();
        });
        this.classFlags = parseFlags("class_flags", in, this.classCount, Codec.UNSIGNED5, this.options.hasClassFlagsHi());
        int classAttrCount = SegmentUtils.countBit16(this.classFlags);
        int[] classAttrCounts = decodeBandInt("class_attr_count", in, Codec.UNSIGNED5, classAttrCount);
        int[][] classAttrIndexes = decodeBandInt("class_attr_indexes", in, Codec.UNSIGNED5, classAttrCounts);
        int callCount = getCallCount(classAttrIndexes, new long[]{this.classFlags}, 0);
        int[] classAttrCalls = decodeBandInt("class_attr_calls", in, Codec.UNSIGNED5, callCount);
        AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_DEPRECATED, 0);
        AttributeLayout sourceFileLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_SOURCE_FILE, 0);
        int sourceFileCount = SegmentUtils.countMatches(this.classFlags, sourceFileLayout);
        int[] classSourceFile = decodeBandInt("class_SourceFile_RUN", in, Codec.UNSIGNED5, sourceFileCount);
        AttributeLayout enclosingMethodLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_ENCLOSING_METHOD, 0);
        int enclosingMethodCount = SegmentUtils.countMatches(this.classFlags, enclosingMethodLayout);
        int[] enclosingMethodRC = decodeBandInt("class_EnclosingMethod_RC", in, Codec.UNSIGNED5, enclosingMethodCount);
        int[] enclosingMethodRDN = decodeBandInt("class_EnclosingMethod_RDN", in, Codec.UNSIGNED5, enclosingMethodCount);
        AttributeLayout signatureLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_SIGNATURE, 0);
        int signatureCount = SegmentUtils.countMatches(this.classFlags, signatureLayout);
        int[] classSignature = decodeBandInt("class_Signature_RS", in, Codec.UNSIGNED5, signatureCount);
        int backwardsCallsUsed = parseClassMetadataBands(in, classAttrCalls);
        AttributeLayout innerClassLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_INNER_CLASSES, 0);
        int innerClassCount = SegmentUtils.countMatches(this.classFlags, innerClassLayout);
        int[] classInnerClassesN = decodeBandInt("class_InnerClasses_N", in, Codec.UNSIGNED5, innerClassCount);
        int[][] classInnerClassesRC = decodeBandInt("class_InnerClasses_RC", in, Codec.UNSIGNED5, classInnerClassesN);
        int[][] classInnerClassesF = decodeBandInt("class_InnerClasses_F", in, Codec.UNSIGNED5, classInnerClassesN);
        int flagsCount = 0;
        for (int i2 = 0; i2 < classInnerClassesF.length; i2++) {
            for (int j = 0; j < classInnerClassesF[i2].length; j++) {
                if (classInnerClassesF[i2][j] != 0) {
                    flagsCount++;
                }
            }
        }
        int[] classInnerClassesOuterRCN = decodeBandInt("class_InnerClasses_outer_RCN", in, Codec.UNSIGNED5, flagsCount);
        int[] classInnerClassesNameRUN = decodeBandInt("class_InnerClasses_name_RUN", in, Codec.UNSIGNED5, flagsCount);
        AttributeLayout versionLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_CLASS_FILE_VERSION, 0);
        int versionCount = SegmentUtils.countMatches(this.classFlags, versionLayout);
        int[] classFileVersionMinorH = decodeBandInt("class_file_version_minor_H", in, Codec.UNSIGNED5, versionCount);
        int[] classFileVersionMajorH = decodeBandInt("class_file_version_major_H", in, Codec.UNSIGNED5, versionCount);
        if (versionCount > 0) {
            this.classVersionMajor = new int[this.classCount];
            this.classVersionMinor = new int[this.classCount];
        }
        int defaultVersionMajor = this.header.getDefaultClassMajorVersion();
        int defaultVersionMinor = this.header.getDefaultClassMinorVersion();
        int backwardsCallIndex = backwardsCallsUsed;
        int limit = this.options.hasClassFlagsHi() ? 62 : 31;
        AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
        int[] counts = new int[limit + 1];
        List<Attribute>[] otherAttributes = new List[limit + 1];
        for (int i3 = 0; i3 < limit; i3++) {
            AttributeLayout layout = this.attrMap.getAttributeLayout(i3, 0);
            if (layout != null && !layout.isDefaultLayout()) {
                otherLayouts[i3] = layout;
                counts[i3] = SegmentUtils.countMatches(this.classFlags, layout);
            }
        }
        for (int i4 = 0; i4 < counts.length; i4++) {
            if (counts[i4] > 0) {
                NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[i4]);
                otherAttributes[i4] = bands.parseAttributes(in, counts[i4]);
                int numBackwardsCallables = otherLayouts[i4].numBackwardsCallables();
                if (numBackwardsCallables > 0) {
                    int[] backwardsCalls = new int[numBackwardsCallables];
                    System.arraycopy(classAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
                    bands.setBackwardsCalls(backwardsCalls);
                    backwardsCallIndex += numBackwardsCallables;
                }
            }
        }
        int sourceFileIndex = 0;
        int enclosingMethodIndex = 0;
        int signatureIndex = 0;
        int innerClassIndex = 0;
        int innerClassC2NIndex = 0;
        int versionIndex = 0;
        this.icLocal = new IcTuple[this.classCount];
        for (int i5 = 0; i5 < this.classCount; i5++) {
            long flag = this.classFlags[i5];
            if (deprecatedLayout.matches(this.classFlags[i5])) {
                this.classAttributes[i5].add(new DeprecatedAttribute());
            }
            if (sourceFileLayout.matches(flag)) {
                long result = classSourceFile[sourceFileIndex];
                ClassFileEntry value = sourceFileLayout.getValue(result, this.cpBands.getConstantPool());
                ClassFileEntry value2 = value;
                if (value == null) {
                    String className = this.classThis[i5].substring(this.classThis[i5].lastIndexOf(47) + 1);
                    String className2 = className.substring(className.lastIndexOf(46) + 1);
                    char[] chars = className2.toCharArray();
                    int index = -1;
                    int j2 = 0;
                    while (true) {
                        if (j2 < chars.length) {
                            if (chars[j2] > '-') {
                                j2++;
                            } else {
                                index = j2;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (index > -1) {
                        className2 = className2.substring(0, index);
                    }
                    value2 = this.cpBands.cpUTF8Value(className2 + ".java", true);
                }
                this.classAttributes[i5].add(new SourceFileAttribute((CPUTF8) value2));
                sourceFileIndex++;
            }
            if (enclosingMethodLayout.matches(flag)) {
                CPClass theClass = this.cpBands.cpClassValue(enclosingMethodRC[enclosingMethodIndex]);
                CPNameAndType theMethod = null;
                if (enclosingMethodRDN[enclosingMethodIndex] != 0) {
                    theMethod = this.cpBands.cpNameAndTypeValue(enclosingMethodRDN[enclosingMethodIndex] - 1);
                }
                this.classAttributes[i5].add(new EnclosingMethodAttribute(theClass, theMethod));
                enclosingMethodIndex++;
            }
            if (signatureLayout.matches(flag)) {
                long result2 = classSignature[signatureIndex];
                this.classAttributes[i5].add(new SignatureAttribute((CPUTF8) signatureLayout.getValue(result2, this.cpBands.getConstantPool())));
                signatureIndex++;
            }
            if (innerClassLayout.matches(flag)) {
                this.icLocal[i5] = new IcTuple[classInnerClassesN[innerClassIndex]];
                for (int j3 = 0; j3 < this.icLocal[i5].length; j3++) {
                    int icTupleCIndex = classInnerClassesRC[innerClassIndex][j3];
                    int icTupleC2Index = -1;
                    int icTupleNIndex = -1;
                    String icTupleC = cpClass[icTupleCIndex];
                    int icTupleF = classInnerClassesF[innerClassIndex][j3];
                    String icTupleC2 = null;
                    String icTupleN = null;
                    if (icTupleF != 0) {
                        icTupleC2Index = classInnerClassesOuterRCN[innerClassC2NIndex];
                        icTupleNIndex = classInnerClassesNameRUN[innerClassC2NIndex];
                        icTupleC2 = cpClass[icTupleC2Index];
                        icTupleN = cpUTF8[icTupleNIndex];
                        innerClassC2NIndex++;
                    } else {
                        IcBands icBands = this.segment.getIcBands();
                        IcTuple[] icAll = icBands.getIcTuples();
                        int k = 0;
                        while (true) {
                            if (k < icAll.length) {
                                if (!icAll[k].getC().equals(icTupleC)) {
                                    k++;
                                } else {
                                    icTupleF = icAll[k].getF();
                                    icTupleC2 = icAll[k].getC2();
                                    icTupleN = icAll[k].getN();
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    IcTuple icTuple = new IcTuple(icTupleC, icTupleF, icTupleC2, icTupleN, icTupleCIndex, icTupleC2Index, icTupleNIndex, j3);
                    this.icLocal[i5][j3] = icTuple;
                }
                innerClassIndex++;
            }
            if (versionLayout.matches(flag)) {
                this.classVersionMajor[i5] = classFileVersionMajorH[versionIndex];
                this.classVersionMinor[i5] = classFileVersionMinorH[versionIndex];
                versionIndex++;
            } else if (this.classVersionMajor != null) {
                this.classVersionMajor[i5] = defaultVersionMajor;
                this.classVersionMinor[i5] = defaultVersionMinor;
            }
            for (int j4 = 0; j4 < otherLayouts.length; j4++) {
                if (otherLayouts[j4] != null && otherLayouts[j4].matches(flag)) {
                    this.classAttributes[i5].add(otherAttributes[j4].get(0));
                    otherAttributes[j4].remove(0);
                }
            }
        }
    }

    private void parseCodeBands(InputStream in) throws Pack200Exception, IOException {
        AttributeLayout layout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_CODE, 2);
        int codeCount = SegmentUtils.countMatches(this.methodFlags, layout);
        int[] codeHeaders = decodeBandInt("code_headers", in, Codec.BYTE1, codeCount);
        boolean allCodeHasFlags = this.segment.getSegmentHeader().getOptions().hasAllCodeFlags();
        if (!allCodeHasFlags) {
            this.codeHasAttributes = new boolean[codeCount];
        }
        int codeSpecialHeader = 0;
        for (int i = 0; i < codeCount; i++) {
            if (codeHeaders[i] == 0) {
                codeSpecialHeader++;
                if (!allCodeHasFlags) {
                    this.codeHasAttributes[i] = true;
                }
            }
        }
        int[] codeMaxStackSpecials = decodeBandInt("code_max_stack", in, Codec.UNSIGNED5, codeSpecialHeader);
        int[] codeMaxNALocalsSpecials = decodeBandInt("code_max_na_locals", in, Codec.UNSIGNED5, codeSpecialHeader);
        int[] codeHandlerCountSpecials = decodeBandInt("code_handler_count", in, Codec.UNSIGNED5, codeSpecialHeader);
        this.codeMaxStack = new int[codeCount];
        this.codeMaxNALocals = new int[codeCount];
        this.codeHandlerCount = new int[codeCount];
        int special = 0;
        for (int i2 = 0; i2 < codeCount; i2++) {
            int header = 255 & codeHeaders[i2];
            if (header < 0) {
                throw new IllegalStateException("Shouldn't get here");
            }
            if (header == 0) {
                this.codeMaxStack[i2] = codeMaxStackSpecials[special];
                this.codeMaxNALocals[i2] = codeMaxNALocalsSpecials[special];
                this.codeHandlerCount[i2] = codeHandlerCountSpecials[special];
                special++;
            } else if (header <= 144) {
                this.codeMaxStack[i2] = (header - 1) % 12;
                this.codeMaxNALocals[i2] = (header - 1) / 12;
                this.codeHandlerCount[i2] = 0;
            } else if (header <= 208) {
                this.codeMaxStack[i2] = (header - 145) % 8;
                this.codeMaxNALocals[i2] = (header - 145) / 8;
                this.codeHandlerCount[i2] = 1;
            } else if (header <= 255) {
                this.codeMaxStack[i2] = (header - 209) % 7;
                this.codeMaxNALocals[i2] = (header - 209) / 7;
                this.codeHandlerCount[i2] = 2;
            } else {
                throw new IllegalStateException("Shouldn't get here either");
            }
        }
        this.codeHandlerStartP = decodeBandInt("code_handler_start_P", in, Codec.BCI5, this.codeHandlerCount);
        this.codeHandlerEndPO = decodeBandInt("code_handler_end_PO", in, Codec.BRANCH5, this.codeHandlerCount);
        this.codeHandlerCatchPO = decodeBandInt("code_handler_catch_PO", in, Codec.BRANCH5, this.codeHandlerCount);
        this.codeHandlerClassRCN = decodeBandInt("code_handler_class_RCN", in, Codec.UNSIGNED5, this.codeHandlerCount);
        int codeFlagsCount = allCodeHasFlags ? codeCount : codeSpecialHeader;
        this.codeAttributes = new List[codeFlagsCount];
        Arrays.setAll(this.codeAttributes, i3 -> {
            return new ArrayList();
        });
        parseCodeAttrBands(in, codeFlagsCount);
    }

    private void parseCodeAttrBands(InputStream in, int codeFlagsCount) throws IOException, Pack200Exception {
        long[] codeFlags = parseFlags("code_flags", in, codeFlagsCount, Codec.UNSIGNED5, this.segment.getSegmentHeader().getOptions().hasCodeFlagsHi());
        int codeAttrCount = SegmentUtils.countBit16(codeFlags);
        int[] codeAttrCounts = decodeBandInt("code_attr_count", in, Codec.UNSIGNED5, codeAttrCount);
        int[][] codeAttrIndexes = decodeBandInt("code_attr_indexes", in, Codec.UNSIGNED5, codeAttrCounts);
        int callCount = 0;
        for (int i = 0; i < codeAttrIndexes.length; i++) {
            for (int j = 0; j < codeAttrIndexes[i].length; j++) {
                int index = codeAttrIndexes[i][j];
                callCount += this.attrMap.getAttributeLayout(index, 3).numBackwardsCallables();
            }
        }
        int[] codeAttrCalls = decodeBandInt("code_attr_calls", in, Codec.UNSIGNED5, callCount);
        AttributeLayout lineNumberTableLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_LINE_NUMBER_TABLE, 3);
        int lineNumberTableCount = SegmentUtils.countMatches(codeFlags, lineNumberTableLayout);
        int[] lineNumberTableN = decodeBandInt("code_LineNumberTable_N", in, Codec.UNSIGNED5, lineNumberTableCount);
        int[][] lineNumberTableBciP = decodeBandInt("code_LineNumberTable_bci_P", in, Codec.BCI5, lineNumberTableN);
        int[][] lineNumberTableLine = decodeBandInt("code_LineNumberTable_line", in, Codec.UNSIGNED5, lineNumberTableN);
        AttributeLayout localVariableTableLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_LOCAL_VARIABLE_TABLE, 3);
        AttributeLayout localVariableTypeTableLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_LOCAL_VARIABLE_TYPE_TABLE, 3);
        int lengthLocalVariableNBand = SegmentUtils.countMatches(codeFlags, localVariableTableLayout);
        int[] localVariableTableN = decodeBandInt("code_LocalVariableTable_N", in, Codec.UNSIGNED5, lengthLocalVariableNBand);
        int[][] localVariableTableBciP = decodeBandInt("code_LocalVariableTable_bci_P", in, Codec.BCI5, localVariableTableN);
        int[][] localVariableTableSpanO = decodeBandInt("code_LocalVariableTable_span_O", in, Codec.BRANCH5, localVariableTableN);
        CPUTF8[][] localVariableTableNameRU = parseCPUTF8References("code_LocalVariableTable_name_RU", in, Codec.UNSIGNED5, localVariableTableN);
        CPUTF8[][] localVariableTableTypeRS = parseCPSignatureReferences("code_LocalVariableTable_type_RS", in, Codec.UNSIGNED5, localVariableTableN);
        int[][] localVariableTableSlot = decodeBandInt("code_LocalVariableTable_slot", in, Codec.UNSIGNED5, localVariableTableN);
        int lengthLocalVariableTypeTableNBand = SegmentUtils.countMatches(codeFlags, localVariableTypeTableLayout);
        int[] localVariableTypeTableN = decodeBandInt("code_LocalVariableTypeTable_N", in, Codec.UNSIGNED5, lengthLocalVariableTypeTableNBand);
        int[][] localVariableTypeTableBciP = decodeBandInt("code_LocalVariableTypeTable_bci_P", in, Codec.BCI5, localVariableTypeTableN);
        int[][] localVariableTypeTableSpanO = decodeBandInt("code_LocalVariableTypeTable_span_O", in, Codec.BRANCH5, localVariableTypeTableN);
        CPUTF8[][] localVariableTypeTableNameRU = parseCPUTF8References("code_LocalVariableTypeTable_name_RU", in, Codec.UNSIGNED5, localVariableTypeTableN);
        CPUTF8[][] localVariableTypeTableTypeRS = parseCPSignatureReferences("code_LocalVariableTypeTable_type_RS", in, Codec.UNSIGNED5, localVariableTypeTableN);
        int[][] localVariableTypeTableSlot = decodeBandInt("code_LocalVariableTypeTable_slot", in, Codec.UNSIGNED5, localVariableTypeTableN);
        int backwardsCallIndex = 0;
        int limit = this.options.hasCodeFlagsHi() ? 62 : 31;
        AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
        int[] counts = new int[limit + 1];
        List<Attribute>[] otherAttributes = new List[limit + 1];
        for (int i2 = 0; i2 < limit; i2++) {
            AttributeLayout layout = this.attrMap.getAttributeLayout(i2, 3);
            if (layout != null && !layout.isDefaultLayout()) {
                otherLayouts[i2] = layout;
                counts[i2] = SegmentUtils.countMatches(codeFlags, layout);
            }
        }
        for (int i3 = 0; i3 < counts.length; i3++) {
            if (counts[i3] > 0) {
                NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[i3]);
                otherAttributes[i3] = bands.parseAttributes(in, counts[i3]);
                int numBackwardsCallables = otherLayouts[i3].numBackwardsCallables();
                if (numBackwardsCallables > 0) {
                    int[] backwardsCalls = new int[numBackwardsCallables];
                    System.arraycopy(codeAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
                    bands.setBackwardsCalls(backwardsCalls);
                    backwardsCallIndex += numBackwardsCallables;
                }
            }
        }
        int lineNumberIndex = 0;
        int lvtIndex = 0;
        int lvttIndex = 0;
        for (int i4 = 0; i4 < codeFlagsCount; i4++) {
            if (lineNumberTableLayout.matches(codeFlags[i4])) {
                LineNumberTableAttribute lnta = new LineNumberTableAttribute(lineNumberTableN[lineNumberIndex], lineNumberTableBciP[lineNumberIndex], lineNumberTableLine[lineNumberIndex]);
                lineNumberIndex++;
                this.codeAttributes[i4].add(lnta);
            }
            if (localVariableTableLayout.matches(codeFlags[i4])) {
                LocalVariableTableAttribute lvta = new LocalVariableTableAttribute(localVariableTableN[lvtIndex], localVariableTableBciP[lvtIndex], localVariableTableSpanO[lvtIndex], localVariableTableNameRU[lvtIndex], localVariableTableTypeRS[lvtIndex], localVariableTableSlot[lvtIndex]);
                lvtIndex++;
                this.codeAttributes[i4].add(lvta);
            }
            if (localVariableTypeTableLayout.matches(codeFlags[i4])) {
                LocalVariableTypeTableAttribute lvtta = new LocalVariableTypeTableAttribute(localVariableTypeTableN[lvttIndex], localVariableTypeTableBciP[lvttIndex], localVariableTypeTableSpanO[lvttIndex], localVariableTypeTableNameRU[lvttIndex], localVariableTypeTableTypeRS[lvttIndex], localVariableTypeTableSlot[lvttIndex]);
                lvttIndex++;
                this.codeAttributes[i4].add(lvtta);
            }
            for (int j2 = 0; j2 < otherLayouts.length; j2++) {
                if (otherLayouts[j2] != null && otherLayouts[j2].matches(codeFlags[i4])) {
                    this.codeAttributes[i4].add(otherAttributes[j2].get(0));
                    otherAttributes[j2].remove(0);
                }
            }
        }
    }

    private int parseFieldMetadataBands(InputStream in, int[] fieldAttrCalls) throws Pack200Exception, IOException {
        int backwardsCallsUsed = 0;
        String[] RxA = {"RVA", "RIA"};
        AttributeLayout rvaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS, 1);
        AttributeLayout riaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS, 1);
        int rvaCount = SegmentUtils.countMatches(this.fieldFlags, rvaLayout);
        int riaCount = SegmentUtils.countMatches(this.fieldFlags, riaLayout);
        int[] RxACount = {rvaCount, riaCount};
        int[] backwardsCalls = {0, 0};
        if (rvaCount > 0) {
            backwardsCalls[0] = fieldAttrCalls[0];
            backwardsCallsUsed = 0 + 1;
            if (riaCount > 0) {
                backwardsCalls[1] = fieldAttrCalls[1];
                backwardsCallsUsed++;
            }
        } else if (riaCount > 0) {
            backwardsCalls[1] = fieldAttrCalls[0];
            backwardsCallsUsed = 0 + 1;
        }
        MetadataBandGroup[] mb = parseMetadata(in, RxA, RxACount, backwardsCalls, "field");
        List<Attribute> rvaAttributes = mb[0].getAttributes();
        List<Attribute> riaAttributes = mb[1].getAttributes();
        int rvaAttributesIndex = 0;
        int riaAttributesIndex = 0;
        for (int i = 0; i < this.fieldFlags.length; i++) {
            for (int j = 0; j < this.fieldFlags[i].length; j++) {
                if (rvaLayout.matches(this.fieldFlags[i][j])) {
                    int i2 = rvaAttributesIndex;
                    rvaAttributesIndex++;
                    this.fieldAttributes[i][j].add(rvaAttributes.get(i2));
                }
                if (riaLayout.matches(this.fieldFlags[i][j])) {
                    int i3 = riaAttributesIndex;
                    riaAttributesIndex++;
                    this.fieldAttributes[i][j].add(riaAttributes.get(i3));
                }
            }
        }
        return backwardsCallsUsed;
    }

    private MetadataBandGroup[] parseMetadata(InputStream in, String[] RxA, int[] RxACount, int[] backwardsCallCounts, String contextName) throws IOException, Pack200Exception {
        MetadataBandGroup[] mbg = new MetadataBandGroup[RxA.length];
        for (int i = 0; i < RxA.length; i++) {
            mbg[i] = new MetadataBandGroup(RxA[i], this.cpBands);
            String rxa = RxA[i];
            if (rxa.indexOf(80) >= 0) {
                mbg[i].param_NB = decodeBandInt(contextName + "_" + rxa + "_param_NB", in, Codec.BYTE1, RxACount[i]);
            }
            int pairCount = 0;
            if (!rxa.equals("AD")) {
                mbg[i].anno_N = decodeBandInt(contextName + "_" + rxa + "_anno_N", in, Codec.UNSIGNED5, RxACount[i]);
                mbg[i].type_RS = parseCPSignatureReferences(contextName + "_" + rxa + "_type_RS", in, Codec.UNSIGNED5, mbg[i].anno_N);
                mbg[i].pair_N = decodeBandInt(contextName + "_" + rxa + "_pair_N", in, Codec.UNSIGNED5, mbg[i].anno_N);
                for (int j = 0; j < mbg[i].pair_N.length; j++) {
                    for (int k = 0; k < mbg[i].pair_N[j].length; k++) {
                        pairCount += mbg[i].pair_N[j][k];
                    }
                }
                mbg[i].name_RU = parseCPUTF8References(contextName + "_" + rxa + "_name_RU", in, Codec.UNSIGNED5, pairCount);
            } else {
                pairCount = RxACount[i];
            }
            mbg[i].T = decodeBandInt(contextName + "_" + rxa + "_T", in, Codec.BYTE1, pairCount + backwardsCallCounts[i]);
            int ICount = 0;
            int DCount = 0;
            int FCount = 0;
            int JCount = 0;
            int cCount = 0;
            int eCount = 0;
            int sCount = 0;
            int arrayCount = 0;
            int atCount = 0;
            for (int j2 = 0; j2 < mbg[i].T.length; j2++) {
                char c = (char) mbg[i].T[j2];
                switch (c) {
                    case '@':
                        atCount++;
                        break;
                    case 'B':
                    case 'C':
                    case 'I':
                    case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
                    case 'Z':
                        ICount++;
                        break;
                    case 'D':
                        DCount++;
                        break;
                    case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
                        FCount++;
                        break;
                    case 'J':
                        JCount++;
                        break;
                    case '[':
                        arrayCount++;
                        break;
                    case 'c':
                        cCount++;
                        break;
                    case HttpStatus.SC_SWITCHING_PROTOCOLS /* 101 */:
                        eCount++;
                        break;
                    case 's':
                        sCount++;
                        break;
                }
            }
            mbg[i].caseI_KI = parseCPIntReferences(contextName + "_" + rxa + "_caseI_KI", in, Codec.UNSIGNED5, ICount);
            mbg[i].caseD_KD = parseCPDoubleReferences(contextName + "_" + rxa + "_caseD_KD", in, Codec.UNSIGNED5, DCount);
            mbg[i].caseF_KF = parseCPFloatReferences(contextName + "_" + rxa + "_caseF_KF", in, Codec.UNSIGNED5, FCount);
            mbg[i].caseJ_KJ = parseCPLongReferences(contextName + "_" + rxa + "_caseJ_KJ", in, Codec.UNSIGNED5, JCount);
            mbg[i].casec_RS = parseCPSignatureReferences(contextName + "_" + rxa + "_casec_RS", in, Codec.UNSIGNED5, cCount);
            mbg[i].caseet_RS = parseReferences(contextName + "_" + rxa + "_caseet_RS", in, Codec.UNSIGNED5, eCount, this.cpBands.getCpSignature());
            mbg[i].caseec_RU = parseReferences(contextName + "_" + rxa + "_caseec_RU", in, Codec.UNSIGNED5, eCount, this.cpBands.getCpUTF8());
            mbg[i].cases_RU = parseCPUTF8References(contextName + "_" + rxa + "_cases_RU", in, Codec.UNSIGNED5, sCount);
            mbg[i].casearray_N = decodeBandInt(contextName + "_" + rxa + "_casearray_N", in, Codec.UNSIGNED5, arrayCount);
            mbg[i].nesttype_RS = parseCPUTF8References(contextName + "_" + rxa + "_nesttype_RS", in, Codec.UNSIGNED5, atCount);
            mbg[i].nestpair_N = decodeBandInt(contextName + "_" + rxa + "_nestpair_N", in, Codec.UNSIGNED5, atCount);
            int nestPairCount = 0;
            for (int j3 = 0; j3 < mbg[i].nestpair_N.length; j3++) {
                nestPairCount += mbg[i].nestpair_N[j3];
            }
            mbg[i].nestname_RU = parseCPUTF8References(contextName + "_" + rxa + "_nestname_RU", in, Codec.UNSIGNED5, nestPairCount);
        }
        return mbg;
    }

    private int parseMethodMetadataBands(InputStream in, int[] methodAttrCalls) throws Pack200Exception, IOException {
        int backwardsCallsUsed = 0;
        String[] RxA = {"RVA", "RIA", "RVPA", "RIPA", "AD"};
        int[] rxaCounts = {0, 0, 0, 0, 0};
        AttributeLayout rvaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS, 2);
        AttributeLayout riaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS, 2);
        AttributeLayout rvpaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS, 2);
        AttributeLayout ripaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, 2);
        AttributeLayout adLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_ANNOTATION_DEFAULT, 2);
        AttributeLayout[] rxaLayouts = {rvaLayout, riaLayout, rvpaLayout, ripaLayout, adLayout};
        Arrays.setAll(rxaCounts, i -> {
            return SegmentUtils.countMatches(this.methodFlags, rxaLayouts[rxaLayouts]);
        });
        int[] backwardsCalls = new int[5];
        int methodAttrIndex = 0;
        for (int i2 = 0; i2 < backwardsCalls.length; i2++) {
            if (rxaCounts[i2] > 0) {
                backwardsCallsUsed++;
                backwardsCalls[i2] = methodAttrCalls[methodAttrIndex];
                methodAttrIndex++;
            } else {
                backwardsCalls[i2] = 0;
            }
        }
        MetadataBandGroup[] mbgs = parseMetadata(in, RxA, rxaCounts, backwardsCalls, "method");
        List<Attribute>[] attributeLists = new List[RxA.length];
        int[] attributeListIndexes = new int[RxA.length];
        for (int i3 = 0; i3 < mbgs.length; i3++) {
            attributeLists[i3] = mbgs[i3].getAttributes();
            attributeListIndexes[i3] = 0;
        }
        for (int i4 = 0; i4 < this.methodFlags.length; i4++) {
            for (int j = 0; j < this.methodFlags[i4].length; j++) {
                for (int k = 0; k < rxaLayouts.length; k++) {
                    if (rxaLayouts[k].matches(this.methodFlags[i4][j])) {
                        ArrayList arrayList = this.methodAttributes[i4][j];
                        List<Attribute> list = attributeLists[k];
                        int i5 = k;
                        int i6 = attributeListIndexes[i5];
                        attributeListIndexes[i5] = i6 + 1;
                        arrayList.add(list.get(i6));
                    }
                }
            }
        }
        return backwardsCallsUsed;
    }

    private int parseClassMetadataBands(InputStream in, int[] classAttrCalls) throws Pack200Exception, IOException {
        int numBackwardsCalls = 0;
        String[] RxA = {"RVA", "RIA"};
        AttributeLayout rvaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_VISIBLE_ANNOTATIONS, 0);
        AttributeLayout riaLayout = this.attrMap.getAttributeLayout(AttributeLayout.ATTRIBUTE_RUNTIME_INVISIBLE_ANNOTATIONS, 0);
        int rvaCount = SegmentUtils.countMatches(this.classFlags, rvaLayout);
        int riaCount = SegmentUtils.countMatches(this.classFlags, riaLayout);
        int[] RxACount = {rvaCount, riaCount};
        int[] backwardsCalls = {0, 0};
        if (rvaCount > 0) {
            numBackwardsCalls = 0 + 1;
            backwardsCalls[0] = classAttrCalls[0];
            if (riaCount > 0) {
                numBackwardsCalls++;
                backwardsCalls[1] = classAttrCalls[1];
            }
        } else if (riaCount > 0) {
            numBackwardsCalls = 0 + 1;
            backwardsCalls[1] = classAttrCalls[0];
        }
        MetadataBandGroup[] mbgs = parseMetadata(in, RxA, RxACount, backwardsCalls, Action.CLASS_ATTRIBUTE);
        List<Attribute> rvaAttributes = mbgs[0].getAttributes();
        List<Attribute> riaAttributes = mbgs[1].getAttributes();
        int rvaAttributesIndex = 0;
        int riaAttributesIndex = 0;
        for (int i = 0; i < this.classFlags.length; i++) {
            if (rvaLayout.matches(this.classFlags[i])) {
                int i2 = rvaAttributesIndex;
                rvaAttributesIndex++;
                this.classAttributes[i].add(rvaAttributes.get(i2));
            }
            if (riaLayout.matches(this.classFlags[i])) {
                int i3 = riaAttributesIndex;
                riaAttributesIndex++;
                this.classAttributes[i].add(riaAttributes.get(i3));
            }
        }
        return numBackwardsCalls;
    }

    public ArrayList<Attribute>[] getClassAttributes() {
        return this.classAttributes;
    }

    public int[] getClassFieldCount() {
        return this.classFieldCount;
    }

    public long[] getRawClassFlags() {
        return this.classFlags;
    }

    public long[] getClassFlags() {
        if (this.classAccessFlags == null) {
            long mask = 32767;
            for (int i = 0; i < 16; i++) {
                AttributeLayout layout = this.attrMap.getAttributeLayout(i, 0);
                if (layout != null && !layout.isDefaultLayout()) {
                    mask &= (1 << i) ^ (-1);
                }
            }
            this.classAccessFlags = new long[this.classFlags.length];
            for (int i2 = 0; i2 < this.classFlags.length; i2++) {
                this.classAccessFlags[i2] = this.classFlags[i2] & mask;
            }
        }
        return this.classAccessFlags;
    }

    public int[][] getClassInterfacesInts() {
        return this.classInterfacesInts;
    }

    public int[] getClassMethodCount() {
        return this.classMethodCount;
    }

    public int[] getClassSuperInts() {
        return this.classSuperInts;
    }

    public int[] getClassThisInts() {
        return this.classThisInts;
    }

    public int[] getCodeMaxNALocals() {
        return this.codeMaxNALocals;
    }

    public int[] getCodeMaxStack() {
        return this.codeMaxStack;
    }

    public ArrayList<Attribute>[][] getFieldAttributes() {
        return this.fieldAttributes;
    }

    public int[][] getFieldDescrInts() {
        return this.fieldDescrInts;
    }

    public int[][] getMethodDescrInts() {
        return this.methodDescrInts;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [long[], long[][]] */
    public long[][] getFieldFlags() {
        if (this.fieldAccessFlags == null) {
            long mask = 32767;
            for (int i = 0; i < 16; i++) {
                AttributeLayout layout = this.attrMap.getAttributeLayout(i, 1);
                if (layout != null && !layout.isDefaultLayout()) {
                    mask &= (1 << i) ^ (-1);
                }
            }
            this.fieldAccessFlags = new long[this.fieldFlags.length];
            for (int i2 = 0; i2 < this.fieldFlags.length; i2++) {
                this.fieldAccessFlags[i2] = new long[this.fieldFlags[i2].length];
                for (int j = 0; j < this.fieldFlags[i2].length; j++) {
                    this.fieldAccessFlags[i2][j] = this.fieldFlags[i2][j] & mask;
                }
            }
        }
        return this.fieldAccessFlags;
    }

    public ArrayList<List<Attribute>> getOrderedCodeAttributes() {
        ArrayList<List<Attribute>> orderedAttributeList = new ArrayList<>(this.codeAttributes.length);
        for (int classIndex = 0; classIndex < this.codeAttributes.length; classIndex++) {
            List<Attribute> currentAttributes = new ArrayList<>(this.codeAttributes[classIndex].size());
            for (int attributeIndex = 0; attributeIndex < this.codeAttributes[classIndex].size(); attributeIndex++) {
                currentAttributes.add(this.codeAttributes[classIndex].get(attributeIndex));
            }
            orderedAttributeList.add(currentAttributes);
        }
        return orderedAttributeList;
    }

    public ArrayList<Attribute>[][] getMethodAttributes() {
        return this.methodAttributes;
    }

    public String[][] getMethodDescr() {
        return this.methodDescr;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [long[], long[][]] */
    public long[][] getMethodFlags() {
        if (this.methodAccessFlags == null) {
            long mask = 32767;
            for (int i = 0; i < 16; i++) {
                AttributeLayout layout = this.attrMap.getAttributeLayout(i, 2);
                if (layout != null && !layout.isDefaultLayout()) {
                    mask &= (1 << i) ^ (-1);
                }
            }
            this.methodAccessFlags = new long[this.methodFlags.length];
            for (int i2 = 0; i2 < this.methodFlags.length; i2++) {
                this.methodAccessFlags[i2] = new long[this.methodFlags[i2].length];
                for (int j = 0; j < this.methodFlags[i2].length; j++) {
                    this.methodAccessFlags[i2][j] = this.methodFlags[i2][j] & mask;
                }
            }
        }
        return this.methodAccessFlags;
    }

    public int[] getClassVersionMajor() {
        return this.classVersionMajor;
    }

    public int[] getClassVersionMinor() {
        return this.classVersionMinor;
    }

    public int[] getCodeHandlerCount() {
        return this.codeHandlerCount;
    }

    public int[][] getCodeHandlerCatchPO() {
        return this.codeHandlerCatchPO;
    }

    public int[][] getCodeHandlerClassRCN() {
        return this.codeHandlerClassRCN;
    }

    public int[][] getCodeHandlerEndPO() {
        return this.codeHandlerEndPO;
    }

    public int[][] getCodeHandlerStartP() {
        return this.codeHandlerStartP;
    }

    public IcTuple[][] getIcLocal() {
        return this.icLocal;
    }

    public boolean[] getCodeHasAttributes() {
        return this.codeHasAttributes;
    }
}
