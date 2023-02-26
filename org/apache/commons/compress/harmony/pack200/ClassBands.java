package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.harmony.pack200.AttributeDefinitionBands;
import org.apache.commons.compress.harmony.pack200.IcBands;
import org.objectweb.asm.Label;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/ClassBands.class */
public class ClassBands extends BandSet {
    private final CpBands cpBands;
    private final AttributeDefinitionBands attrBands;
    private final CPClass[] class_this;
    private final CPClass[] class_super;
    private final CPClass[][] class_interface;
    private final int[] class_interface_count;
    private final int[] major_versions;
    private final long[] class_flags;
    private int[] class_attr_calls;
    private final List<CPUTF8> classSourceFile;
    private final List<ConstantPoolEntry> classEnclosingMethodClass;
    private final List<ConstantPoolEntry> classEnclosingMethodDesc;
    private final List<CPSignature> classSignature;
    private final IntList classFileVersionMinor;
    private final IntList classFileVersionMajor;
    private final int[] class_field_count;
    private final CPNameAndType[][] field_descr;
    private final long[][] field_flags;
    private int[] field_attr_calls;
    private final List<CPConstant<?>> fieldConstantValueKQ;
    private final List<CPSignature> fieldSignature;
    private final int[] class_method_count;
    private final CPNameAndType[][] method_descr;
    private final long[][] method_flags;
    private int[] method_attr_calls;
    private final List<CPSignature> methodSignature;
    private final IntList methodExceptionNumber;
    private final List<CPClass> methodExceptionClasses;
    private int[] codeHeaders;
    private final IntList codeMaxStack;
    private final IntList codeMaxLocals;
    private final IntList codeHandlerCount;
    private final List codeHandlerStartP;
    private final List codeHandlerEndPO;
    private final List codeHandlerCatchPO;
    private final List<CPClass> codeHandlerClass;
    private final List<Long> codeFlags;
    private int[] code_attr_calls;
    private final IntList codeLineNumberTableN;
    private final List codeLineNumberTableBciP;
    private final IntList codeLineNumberTableLine;
    private final IntList codeLocalVariableTableN;
    private final List codeLocalVariableTableBciP;
    private final List codeLocalVariableTableSpanO;
    private final List<ConstantPoolEntry> codeLocalVariableTableNameRU;
    private final List<ConstantPoolEntry> codeLocalVariableTableTypeRS;
    private final IntList codeLocalVariableTableSlot;
    private final IntList codeLocalVariableTypeTableN;
    private final List codeLocalVariableTypeTableBciP;
    private final List codeLocalVariableTypeTableSpanO;
    private final List<ConstantPoolEntry> codeLocalVariableTypeTableNameRU;
    private final List<ConstantPoolEntry> codeLocalVariableTypeTableTypeRS;
    private final IntList codeLocalVariableTypeTableSlot;
    private final MetadataBandGroup class_RVA_bands;
    private final MetadataBandGroup class_RIA_bands;
    private final MetadataBandGroup field_RVA_bands;
    private final MetadataBandGroup field_RIA_bands;
    private final MetadataBandGroup method_RVA_bands;
    private final MetadataBandGroup method_RIA_bands;
    private final MetadataBandGroup method_RVPA_bands;
    private final MetadataBandGroup method_RIPA_bands;
    private final MetadataBandGroup method_AD_bands;
    private final List<NewAttributeBands> classAttributeBands;
    private final List<NewAttributeBands> methodAttributeBands;
    private final List<NewAttributeBands> fieldAttributeBands;
    private final List<NewAttributeBands> codeAttributeBands;
    private final List<Long> tempFieldFlags;
    private final List<CPNameAndType> tempFieldDesc;
    private final List<Long> tempMethodFlags;
    private final List<CPNameAndType> tempMethodDesc;
    private TempParamAnnotation tempMethodRVPA;
    private TempParamAnnotation tempMethodRIPA;
    private boolean anySyntheticClasses;
    private boolean anySyntheticFields;
    private boolean anySyntheticMethods;
    private final Segment segment;
    private final Map<CPClass, Set<CPClass>> classReferencesInnerClass;
    private final boolean stripDebug;
    private int index;
    private int numMethodArgs;
    private int[] class_InnerClasses_N;
    private CPClass[] class_InnerClasses_RC;
    private int[] class_InnerClasses_F;
    private List<CPClass> classInnerClassesOuterRCN;
    private List<CPUTF8> classInnerClassesNameRUN;

    /* JADX WARN: Type inference failed for: r1v62, types: [org.apache.commons.compress.harmony.pack200.CPClass[], org.apache.commons.compress.harmony.pack200.CPClass[][]] */
    /* JADX WARN: Type inference failed for: r1v68, types: [org.apache.commons.compress.harmony.pack200.CPNameAndType[], org.apache.commons.compress.harmony.pack200.CPNameAndType[][]] */
    /* JADX WARN: Type inference failed for: r1v70, types: [long[], long[][]] */
    /* JADX WARN: Type inference failed for: r1v72, types: [org.apache.commons.compress.harmony.pack200.CPNameAndType[], org.apache.commons.compress.harmony.pack200.CPNameAndType[][]] */
    /* JADX WARN: Type inference failed for: r1v74, types: [long[], long[][]] */
    public ClassBands(Segment segment, int numClasses, int effort, boolean stripDebug) throws IOException {
        super(effort, segment.getSegmentHeader());
        this.classSourceFile = new ArrayList();
        this.classEnclosingMethodClass = new ArrayList();
        this.classEnclosingMethodDesc = new ArrayList();
        this.classSignature = new ArrayList();
        this.classFileVersionMinor = new IntList();
        this.classFileVersionMajor = new IntList();
        this.fieldConstantValueKQ = new ArrayList();
        this.fieldSignature = new ArrayList();
        this.methodSignature = new ArrayList();
        this.methodExceptionNumber = new IntList();
        this.methodExceptionClasses = new ArrayList();
        this.codeMaxStack = new IntList();
        this.codeMaxLocals = new IntList();
        this.codeHandlerCount = new IntList();
        this.codeHandlerStartP = new ArrayList();
        this.codeHandlerEndPO = new ArrayList();
        this.codeHandlerCatchPO = new ArrayList();
        this.codeHandlerClass = new ArrayList();
        this.codeFlags = new ArrayList();
        this.codeLineNumberTableN = new IntList();
        this.codeLineNumberTableBciP = new ArrayList();
        this.codeLineNumberTableLine = new IntList();
        this.codeLocalVariableTableN = new IntList();
        this.codeLocalVariableTableBciP = new ArrayList();
        this.codeLocalVariableTableSpanO = new ArrayList();
        this.codeLocalVariableTableNameRU = new ArrayList();
        this.codeLocalVariableTableTypeRS = new ArrayList();
        this.codeLocalVariableTableSlot = new IntList();
        this.codeLocalVariableTypeTableN = new IntList();
        this.codeLocalVariableTypeTableBciP = new ArrayList();
        this.codeLocalVariableTypeTableSpanO = new ArrayList();
        this.codeLocalVariableTypeTableNameRU = new ArrayList();
        this.codeLocalVariableTypeTableTypeRS = new ArrayList();
        this.codeLocalVariableTypeTableSlot = new IntList();
        this.classAttributeBands = new ArrayList();
        this.methodAttributeBands = new ArrayList();
        this.fieldAttributeBands = new ArrayList();
        this.codeAttributeBands = new ArrayList();
        this.tempFieldFlags = new ArrayList();
        this.tempFieldDesc = new ArrayList();
        this.tempMethodFlags = new ArrayList();
        this.tempMethodDesc = new ArrayList();
        this.anySyntheticClasses = false;
        this.anySyntheticFields = false;
        this.anySyntheticMethods = false;
        this.classReferencesInnerClass = new HashMap();
        this.index = 0;
        this.numMethodArgs = 0;
        this.stripDebug = stripDebug;
        this.segment = segment;
        this.cpBands = segment.getCpBands();
        this.attrBands = segment.getAttrBands();
        this.class_this = new CPClass[numClasses];
        this.class_super = new CPClass[numClasses];
        this.class_interface_count = new int[numClasses];
        this.class_interface = new CPClass[numClasses];
        this.class_field_count = new int[numClasses];
        this.class_method_count = new int[numClasses];
        this.field_descr = new CPNameAndType[numClasses];
        this.field_flags = new long[numClasses];
        this.method_descr = new CPNameAndType[numClasses];
        this.method_flags = new long[numClasses];
        for (int i = 0; i < numClasses; i++) {
            this.field_flags[i] = new long[0];
            this.method_flags[i] = new long[0];
        }
        this.major_versions = new int[numClasses];
        this.class_flags = new long[numClasses];
        this.class_RVA_bands = new MetadataBandGroup("RVA", 0, this.cpBands, this.segmentHeader, effort);
        this.class_RIA_bands = new MetadataBandGroup("RIA", 0, this.cpBands, this.segmentHeader, effort);
        this.field_RVA_bands = new MetadataBandGroup("RVA", 1, this.cpBands, this.segmentHeader, effort);
        this.field_RIA_bands = new MetadataBandGroup("RIA", 1, this.cpBands, this.segmentHeader, effort);
        this.method_RVA_bands = new MetadataBandGroup("RVA", 2, this.cpBands, this.segmentHeader, effort);
        this.method_RIA_bands = new MetadataBandGroup("RIA", 2, this.cpBands, this.segmentHeader, effort);
        this.method_RVPA_bands = new MetadataBandGroup("RVPA", 2, this.cpBands, this.segmentHeader, effort);
        this.method_RIPA_bands = new MetadataBandGroup("RIPA", 2, this.cpBands, this.segmentHeader, effort);
        this.method_AD_bands = new MetadataBandGroup("AD", 2, this.cpBands, this.segmentHeader, effort);
        createNewAttributeBands();
    }

    private void createNewAttributeBands() throws IOException {
        for (AttributeDefinitionBands.AttributeDefinition def : this.attrBands.getClassAttributeLayouts()) {
            this.classAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def));
        }
        for (AttributeDefinitionBands.AttributeDefinition def2 : this.attrBands.getMethodAttributeLayouts()) {
            this.methodAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def2));
        }
        for (AttributeDefinitionBands.AttributeDefinition def3 : this.attrBands.getFieldAttributeLayouts()) {
            this.fieldAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def3));
        }
        for (AttributeDefinitionBands.AttributeDefinition def4 : this.attrBands.getCodeAttributeLayouts()) {
            this.codeAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def4));
        }
    }

    public void addClass(int major, int flags, String className, String signature, String superName, String[] interfaces) {
        this.class_this[this.index] = this.cpBands.getCPClass(className);
        this.class_super[this.index] = this.cpBands.getCPClass(superName);
        this.class_interface_count[this.index] = interfaces.length;
        this.class_interface[this.index] = new CPClass[interfaces.length];
        Arrays.setAll(this.class_interface[this.index], i -> {
            return this.cpBands.getCPClass(interfaces[interfaces]);
        });
        this.major_versions[this.index] = major;
        this.class_flags[this.index] = flags;
        if (!this.anySyntheticClasses && (flags & CpioConstants.C_ISFIFO) != 0 && this.segment.getCurrentClassReader().hasSyntheticAttributes()) {
            this.cpBands.addCPUtf8("Synthetic");
            this.anySyntheticClasses = true;
        }
        if ((flags & 131072) != 0) {
            int i2 = (flags & (-131073)) | 1048576;
        }
        if (signature != null) {
            long[] jArr = this.class_flags;
            int i3 = this.index;
            jArr[i3] = jArr[i3] | 524288;
            this.classSignature.add(this.cpBands.getCPSignature(signature));
        }
    }

    public void currentClassReferencesInnerClass(CPClass inner) {
        CPClass currentClass;
        if (this.index < this.class_this.length && (currentClass = this.class_this[this.index]) != null && !currentClass.equals(inner) && !isInnerClassOf(currentClass.toString(), inner)) {
            Set<CPClass> referencedInnerClasses = this.classReferencesInnerClass.get(currentClass);
            if (referencedInnerClasses == null) {
                referencedInnerClasses = new HashSet<>();
                this.classReferencesInnerClass.put(currentClass, referencedInnerClasses);
            }
            referencedInnerClasses.add(inner);
        }
    }

    private boolean isInnerClassOf(String possibleInner, CPClass possibleOuter) {
        if (isInnerClass(possibleInner)) {
            String superClassName = possibleInner.substring(0, possibleInner.lastIndexOf(36));
            if (superClassName.equals(possibleOuter.toString())) {
                return true;
            }
            return isInnerClassOf(superClassName, possibleOuter);
        }
        return false;
    }

    private boolean isInnerClass(String possibleInner) {
        return possibleInner.indexOf(36) != -1;
    }

    public void addField(int flags, String name, String desc, String signature, Object value) {
        int flags2 = flags & 65535;
        this.tempFieldDesc.add(this.cpBands.getCPNameAndType(name, desc));
        if (signature != null) {
            this.fieldSignature.add(this.cpBands.getCPSignature(signature));
            flags2 |= 524288;
        }
        if ((flags2 & 131072) != 0) {
            flags2 = (flags2 & (-131073)) | 1048576;
        }
        if (value != null) {
            this.fieldConstantValueKQ.add(this.cpBands.getConstant(value));
            flags2 |= 131072;
        }
        if (!this.anySyntheticFields && (flags2 & CpioConstants.C_ISFIFO) != 0 && this.segment.getCurrentClassReader().hasSyntheticAttributes()) {
            this.cpBands.addCPUtf8("Synthetic");
            this.anySyntheticFields = true;
        }
        this.tempFieldFlags.add(Long.valueOf(flags2));
    }

    public void finaliseBands() {
        int[] numBackwardsCalls;
        int[] numBackwardsCalls2;
        int[] numBackwardsCalls3;
        int[] numBackwardsCalls4;
        int header;
        int defaultMajorVersion = this.segmentHeader.getDefaultMajorVersion();
        for (int i = 0; i < this.class_flags.length; i++) {
            int major = this.major_versions[i];
            if (major != defaultMajorVersion) {
                long[] jArr = this.class_flags;
                int i2 = i;
                jArr[i2] = jArr[i2] | 16777216;
                this.classFileVersionMajor.add(major);
                this.classFileVersionMinor.add(0);
            }
        }
        this.codeHeaders = new int[this.codeHandlerCount.size()];
        int removed = 0;
        for (int i3 = 0; i3 < this.codeHeaders.length; i3++) {
            int numHandlers = this.codeHandlerCount.get(i3 - removed);
            int maxLocals = this.codeMaxLocals.get(i3 - removed);
            int maxStack = this.codeMaxStack.get(i3 - removed);
            if (numHandlers == 0) {
                int header2 = (maxLocals * 12) + maxStack + 1;
                if (header2 < 145 && maxStack < 12) {
                    this.codeHeaders[i3] = header2;
                }
            } else if (numHandlers == 1) {
                int header3 = (maxLocals * 8) + maxStack + 145;
                if (header3 < 209 && maxStack < 8) {
                    this.codeHeaders[i3] = header3;
                }
            } else if (numHandlers == 2 && (header = (maxLocals * 7) + maxStack + 209) < 256 && maxStack < 7) {
                this.codeHeaders[i3] = header;
            }
            if (this.codeHeaders[i3] != 0) {
                this.codeHandlerCount.remove(i3 - removed);
                this.codeMaxLocals.remove(i3 - removed);
                this.codeMaxStack.remove(i3 - removed);
                removed++;
            } else if (!this.segment.getSegmentHeader().have_all_code_flags()) {
                this.codeFlags.add(0L);
            }
        }
        IntList innerClassesN = new IntList();
        List<IcBands.IcTuple> icLocal = new ArrayList<>();
        for (int i4 = 0; i4 < this.class_this.length; i4++) {
            CPClass cpClass = this.class_this[i4];
            Set<CPClass> referencedInnerClasses = this.classReferencesInnerClass.get(cpClass);
            if (referencedInnerClasses != null) {
                int innerN = 0;
                List<IcBands.IcTuple> innerClasses = this.segment.getIcBands().getInnerClassesForOuter(cpClass.toString());
                if (innerClasses != null) {
                    for (IcBands.IcTuple element : innerClasses) {
                        referencedInnerClasses.remove(element.C);
                    }
                }
                for (CPClass inner : referencedInnerClasses) {
                    IcBands.IcTuple icTuple = this.segment.getIcBands().getIcTuple(inner);
                    if (icTuple != null && !icTuple.isAnonymous()) {
                        icLocal.add(icTuple);
                        innerN++;
                    }
                }
                if (innerN != 0) {
                    innerClassesN.add(innerN);
                    long[] jArr2 = this.class_flags;
                    int i5 = i4;
                    jArr2[i5] = jArr2[i5] | 8388608;
                }
            }
        }
        this.class_InnerClasses_N = innerClassesN.toArray();
        this.class_InnerClasses_RC = new CPClass[icLocal.size()];
        this.class_InnerClasses_F = new int[icLocal.size()];
        this.classInnerClassesOuterRCN = new ArrayList();
        this.classInnerClassesNameRUN = new ArrayList();
        for (int i6 = 0; i6 < this.class_InnerClasses_RC.length; i6++) {
            IcBands.IcTuple icTuple2 = icLocal.get(i6);
            this.class_InnerClasses_RC[i6] = icTuple2.C;
            if (icTuple2.C2 == null && icTuple2.N == null) {
                this.class_InnerClasses_F[i6] = 0;
            } else {
                if (icTuple2.F == 0) {
                    this.class_InnerClasses_F[i6] = 65536;
                } else {
                    this.class_InnerClasses_F[i6] = icTuple2.F;
                }
                this.classInnerClassesOuterRCN.add(icTuple2.C2);
                this.classInnerClassesNameRUN.add(icTuple2.N);
            }
        }
        IntList classAttrCalls = new IntList();
        IntList fieldAttrCalls = new IntList();
        IntList methodAttrCalls = new IntList();
        IntList codeAttrCalls = new IntList();
        if (this.class_RVA_bands.hasContent()) {
            classAttrCalls.add(this.class_RVA_bands.numBackwardsCalls());
        }
        if (this.class_RIA_bands.hasContent()) {
            classAttrCalls.add(this.class_RIA_bands.numBackwardsCalls());
        }
        if (this.field_RVA_bands.hasContent()) {
            fieldAttrCalls.add(this.field_RVA_bands.numBackwardsCalls());
        }
        if (this.field_RIA_bands.hasContent()) {
            fieldAttrCalls.add(this.field_RIA_bands.numBackwardsCalls());
        }
        if (this.method_RVA_bands.hasContent()) {
            methodAttrCalls.add(this.method_RVA_bands.numBackwardsCalls());
        }
        if (this.method_RIA_bands.hasContent()) {
            methodAttrCalls.add(this.method_RIA_bands.numBackwardsCalls());
        }
        if (this.method_RVPA_bands.hasContent()) {
            methodAttrCalls.add(this.method_RVPA_bands.numBackwardsCalls());
        }
        if (this.method_RIPA_bands.hasContent()) {
            methodAttrCalls.add(this.method_RIPA_bands.numBackwardsCalls());
        }
        if (this.method_AD_bands.hasContent()) {
            methodAttrCalls.add(this.method_AD_bands.numBackwardsCalls());
        }
        Comparator<NewAttributeBands> comparator = arg0, arg1 -> {
            return arg0.getFlagIndex() - arg1.getFlagIndex();
        };
        this.classAttributeBands.sort(comparator);
        this.methodAttributeBands.sort(comparator);
        this.fieldAttributeBands.sort(comparator);
        this.codeAttributeBands.sort(comparator);
        for (NewAttributeBands bands : this.classAttributeBands) {
            if (bands.isUsedAtLeastOnce()) {
                for (int backwardsCallCount : bands.numBackwardsCalls()) {
                    classAttrCalls.add(backwardsCallCount);
                }
            }
        }
        for (NewAttributeBands bands2 : this.methodAttributeBands) {
            if (bands2.isUsedAtLeastOnce()) {
                for (int backwardsCallCount2 : bands2.numBackwardsCalls()) {
                    methodAttrCalls.add(backwardsCallCount2);
                }
            }
        }
        for (NewAttributeBands bands3 : this.fieldAttributeBands) {
            if (bands3.isUsedAtLeastOnce()) {
                for (int backwardsCallCount3 : bands3.numBackwardsCalls()) {
                    fieldAttrCalls.add(backwardsCallCount3);
                }
            }
        }
        for (NewAttributeBands bands4 : this.codeAttributeBands) {
            if (bands4.isUsedAtLeastOnce()) {
                for (int backwardsCallCount4 : bands4.numBackwardsCalls()) {
                    codeAttrCalls.add(backwardsCallCount4);
                }
            }
        }
        this.class_attr_calls = classAttrCalls.toArray();
        this.field_attr_calls = fieldAttrCalls.toArray();
        this.method_attr_calls = methodAttrCalls.toArray();
        this.code_attr_calls = codeAttrCalls.toArray();
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream out) throws IOException, Pack200Exception {
        CPClass[][] cPClassArr;
        PackingUtils.log("Writing class bands...");
        byte[] encodedBand = encodeBandInt("class_this", getInts(this.class_this), Codec.DELTA5);
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_this[" + this.class_this.length + "]");
        byte[] encodedBand2 = encodeBandInt("class_super", getInts(this.class_super), Codec.DELTA5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from class_super[" + this.class_super.length + "]");
        byte[] encodedBand3 = encodeBandInt("class_interface_count", this.class_interface_count, Codec.DELTA5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from class_interface_count[" + this.class_interface_count.length + "]");
        int totalInterfaces = sum(this.class_interface_count);
        int[] classInterface = new int[totalInterfaces];
        int k = 0;
        for (CPClass[] element : this.class_interface) {
            if (element != null) {
                for (CPClass cpClass : element) {
                    classInterface[k] = cpClass.getIndex();
                    k++;
                }
            }
        }
        byte[] encodedBand4 = encodeBandInt("class_interface", classInterface, Codec.DELTA5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from class_interface[" + classInterface.length + "]");
        byte[] encodedBand5 = encodeBandInt("class_field_count", this.class_field_count, Codec.DELTA5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from class_field_count[" + this.class_field_count.length + "]");
        byte[] encodedBand6 = encodeBandInt("class_method_count", this.class_method_count, Codec.DELTA5);
        out.write(encodedBand6);
        PackingUtils.log("Wrote " + encodedBand6.length + " bytes from class_method_count[" + this.class_method_count.length + "]");
        int totalFields = sum(this.class_field_count);
        int[] fieldDescr = new int[totalFields];
        int k2 = 0;
        for (int i = 0; i < this.index; i++) {
            for (int j = 0; j < this.field_descr[i].length; j++) {
                CPNameAndType descr = this.field_descr[i][j];
                fieldDescr[k2] = descr.getIndex();
                k2++;
            }
        }
        byte[] encodedBand7 = encodeBandInt("field_descr", fieldDescr, Codec.DELTA5);
        out.write(encodedBand7);
        PackingUtils.log("Wrote " + encodedBand7.length + " bytes from field_descr[" + fieldDescr.length + "]");
        writeFieldAttributeBands(out);
        int totalMethods = sum(this.class_method_count);
        int[] methodDescr = new int[totalMethods];
        int k3 = 0;
        for (int i2 = 0; i2 < this.index; i2++) {
            for (int j2 = 0; j2 < this.method_descr[i2].length; j2++) {
                CPNameAndType descr2 = this.method_descr[i2][j2];
                methodDescr[k3] = descr2.getIndex();
                k3++;
            }
        }
        byte[] encodedBand8 = encodeBandInt("method_descr", methodDescr, Codec.MDELTA5);
        out.write(encodedBand8);
        PackingUtils.log("Wrote " + encodedBand8.length + " bytes from method_descr[" + methodDescr.length + "]");
        writeMethodAttributeBands(out);
        writeClassAttributeBands(out);
        writeCodeBands(out);
    }

    private int sum(int[] ints) {
        int sum = 0;
        for (int j : ints) {
            sum += j;
        }
        return sum;
    }

    private void writeFieldAttributeBands(OutputStream out) throws IOException, Pack200Exception {
        byte[] encodedBand = encodeFlags("field_flags", this.field_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader.have_field_flags_hi());
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from field_flags[" + this.field_flags.length + "]");
        byte[] encodedBand2 = encodeBandInt("field_attr_calls", this.field_attr_calls, Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from field_attr_calls[" + this.field_attr_calls.length + "]");
        byte[] encodedBand3 = encodeBandInt("fieldConstantValueKQ", cpEntryListToArray(this.fieldConstantValueKQ), Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from fieldConstantValueKQ[" + this.fieldConstantValueKQ.size() + "]");
        byte[] encodedBand4 = encodeBandInt("fieldSignature", cpEntryListToArray(this.fieldSignature), Codec.UNSIGNED5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from fieldSignature[" + this.fieldSignature.size() + "]");
        this.field_RVA_bands.pack(out);
        this.field_RIA_bands.pack(out);
        for (NewAttributeBands bands : this.fieldAttributeBands) {
            bands.pack(out);
        }
    }

    private void writeMethodAttributeBands(OutputStream out) throws IOException, Pack200Exception {
        byte[] encodedBand = encodeFlags("method_flags", this.method_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader.have_method_flags_hi());
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from method_flags[" + this.method_flags.length + "]");
        byte[] encodedBand2 = encodeBandInt("method_attr_calls", this.method_attr_calls, Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from method_attr_calls[" + this.method_attr_calls.length + "]");
        byte[] encodedBand3 = encodeBandInt("methodExceptionNumber", this.methodExceptionNumber.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from methodExceptionNumber[" + this.methodExceptionNumber.size() + "]");
        byte[] encodedBand4 = encodeBandInt("methodExceptionClasses", cpEntryListToArray(this.methodExceptionClasses), Codec.UNSIGNED5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from methodExceptionClasses[" + this.methodExceptionClasses.size() + "]");
        byte[] encodedBand5 = encodeBandInt("methodSignature", cpEntryListToArray(this.methodSignature), Codec.UNSIGNED5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from methodSignature[" + this.methodSignature.size() + "]");
        this.method_RVA_bands.pack(out);
        this.method_RIA_bands.pack(out);
        this.method_RVPA_bands.pack(out);
        this.method_RIPA_bands.pack(out);
        this.method_AD_bands.pack(out);
        for (NewAttributeBands bands : this.methodAttributeBands) {
            bands.pack(out);
        }
    }

    private void writeClassAttributeBands(OutputStream out) throws IOException, Pack200Exception {
        byte[] encodedBand = encodeFlags("class_flags", this.class_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader.have_class_flags_hi());
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_flags[" + this.class_flags.length + "]");
        byte[] encodedBand2 = encodeBandInt("class_attr_calls", this.class_attr_calls, Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from class_attr_calls[" + this.class_attr_calls.length + "]");
        byte[] encodedBand3 = encodeBandInt("classSourceFile", cpEntryOrNullListToArray(this.classSourceFile), Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from classSourceFile[" + this.classSourceFile.size() + "]");
        byte[] encodedBand4 = encodeBandInt("class_enclosing_method_RC", cpEntryListToArray(this.classEnclosingMethodClass), Codec.UNSIGNED5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from class_enclosing_method_RC[" + this.classEnclosingMethodClass.size() + "]");
        byte[] encodedBand5 = encodeBandInt("class_EnclosingMethod_RDN", cpEntryOrNullListToArray(this.classEnclosingMethodDesc), Codec.UNSIGNED5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from class_EnclosingMethod_RDN[" + this.classEnclosingMethodDesc.size() + "]");
        byte[] encodedBand6 = encodeBandInt("class_Signature_RS", cpEntryListToArray(this.classSignature), Codec.UNSIGNED5);
        out.write(encodedBand6);
        PackingUtils.log("Wrote " + encodedBand6.length + " bytes from class_Signature_RS[" + this.classSignature.size() + "]");
        this.class_RVA_bands.pack(out);
        this.class_RIA_bands.pack(out);
        byte[] encodedBand7 = encodeBandInt("class_InnerClasses_N", this.class_InnerClasses_N, Codec.UNSIGNED5);
        out.write(encodedBand7);
        PackingUtils.log("Wrote " + encodedBand7.length + " bytes from class_InnerClasses_N[" + this.class_InnerClasses_N.length + "]");
        byte[] encodedBand8 = encodeBandInt("class_InnerClasses_RC", getInts(this.class_InnerClasses_RC), Codec.UNSIGNED5);
        out.write(encodedBand8);
        PackingUtils.log("Wrote " + encodedBand8.length + " bytes from class_InnerClasses_RC[" + this.class_InnerClasses_RC.length + "]");
        byte[] encodedBand9 = encodeBandInt("class_InnerClasses_F", this.class_InnerClasses_F, Codec.UNSIGNED5);
        out.write(encodedBand9);
        PackingUtils.log("Wrote " + encodedBand9.length + " bytes from class_InnerClasses_F[" + this.class_InnerClasses_F.length + "]");
        byte[] encodedBand10 = encodeBandInt("class_InnerClasses_outer_RCN", cpEntryOrNullListToArray(this.classInnerClassesOuterRCN), Codec.UNSIGNED5);
        out.write(encodedBand10);
        PackingUtils.log("Wrote " + encodedBand10.length + " bytes from class_InnerClasses_outer_RCN[" + this.classInnerClassesOuterRCN.size() + "]");
        byte[] encodedBand11 = encodeBandInt("class_InnerClasses_name_RUN", cpEntryOrNullListToArray(this.classInnerClassesNameRUN), Codec.UNSIGNED5);
        out.write(encodedBand11);
        PackingUtils.log("Wrote " + encodedBand11.length + " bytes from class_InnerClasses_name_RUN[" + this.classInnerClassesNameRUN.size() + "]");
        byte[] encodedBand12 = encodeBandInt("classFileVersionMinor", this.classFileVersionMinor.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand12);
        PackingUtils.log("Wrote " + encodedBand12.length + " bytes from classFileVersionMinor[" + this.classFileVersionMinor.size() + "]");
        byte[] encodedBand13 = encodeBandInt("classFileVersionMajor", this.classFileVersionMajor.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand13);
        PackingUtils.log("Wrote " + encodedBand13.length + " bytes from classFileVersionMajor[" + this.classFileVersionMajor.size() + "]");
        for (NewAttributeBands classAttributeBand : this.classAttributeBands) {
            classAttributeBand.pack(out);
        }
    }

    private int[] getInts(CPClass[] cpClasses) {
        int[] ints = new int[cpClasses.length];
        for (int i = 0; i < ints.length; i++) {
            if (cpClasses[i] != null) {
                ints[i] = cpClasses[i].getIndex();
            }
        }
        return ints;
    }

    private void writeCodeBands(OutputStream out) throws IOException, Pack200Exception {
        byte[] encodedBand = encodeBandInt("codeHeaders", this.codeHeaders, Codec.BYTE1);
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHeaders[" + this.codeHeaders.length + "]");
        byte[] encodedBand2 = encodeBandInt("codeMaxStack", this.codeMaxStack.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from codeMaxStack[" + this.codeMaxStack.size() + "]");
        byte[] encodedBand3 = encodeBandInt("codeMaxLocals", this.codeMaxLocals.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from codeMaxLocals[" + this.codeMaxLocals.size() + "]");
        byte[] encodedBand4 = encodeBandInt("codeHandlerCount", this.codeHandlerCount.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from codeHandlerCount[" + this.codeHandlerCount.size() + "]");
        byte[] encodedBand5 = encodeBandInt("codeHandlerStartP", integerListToArray(this.codeHandlerStartP), Codec.BCI5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from codeHandlerStartP[" + this.codeHandlerStartP.size() + "]");
        byte[] encodedBand6 = encodeBandInt("codeHandlerEndPO", integerListToArray(this.codeHandlerEndPO), Codec.BRANCH5);
        out.write(encodedBand6);
        PackingUtils.log("Wrote " + encodedBand6.length + " bytes from codeHandlerEndPO[" + this.codeHandlerEndPO.size() + "]");
        byte[] encodedBand7 = encodeBandInt("codeHandlerCatchPO", integerListToArray(this.codeHandlerCatchPO), Codec.BRANCH5);
        out.write(encodedBand7);
        PackingUtils.log("Wrote " + encodedBand7.length + " bytes from codeHandlerCatchPO[" + this.codeHandlerCatchPO.size() + "]");
        byte[] encodedBand8 = encodeBandInt("codeHandlerClass", cpEntryOrNullListToArray(this.codeHandlerClass), Codec.UNSIGNED5);
        out.write(encodedBand8);
        PackingUtils.log("Wrote " + encodedBand8.length + " bytes from codeHandlerClass[" + this.codeHandlerClass.size() + "]");
        writeCodeAttributeBands(out);
    }

    private void writeCodeAttributeBands(OutputStream out) throws IOException, Pack200Exception {
        byte[] encodedBand = encodeFlags("codeFlags", longListToArray(this.codeFlags), Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader.have_code_flags_hi());
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeFlags[" + this.codeFlags.size() + "]");
        byte[] encodedBand2 = encodeBandInt("code_attr_calls", this.code_attr_calls, Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from code_attr_calls[" + this.code_attr_calls.length + "]");
        byte[] encodedBand3 = encodeBandInt("code_LineNumberTable_N", this.codeLineNumberTableN.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from code_LineNumberTable_N[" + this.codeLineNumberTableN.size() + "]");
        byte[] encodedBand4 = encodeBandInt("code_LineNumberTable_bci_P", integerListToArray(this.codeLineNumberTableBciP), Codec.BCI5);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from code_LineNumberTable_bci_P[" + this.codeLineNumberTableBciP.size() + "]");
        byte[] encodedBand5 = encodeBandInt("code_LineNumberTable_line", this.codeLineNumberTableLine.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from code_LineNumberTable_line[" + this.codeLineNumberTableLine.size() + "]");
        byte[] encodedBand6 = encodeBandInt("code_LocalVariableTable_N", this.codeLocalVariableTableN.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand6);
        PackingUtils.log("Wrote " + encodedBand6.length + " bytes from code_LocalVariableTable_N[" + this.codeLocalVariableTableN.size() + "]");
        byte[] encodedBand7 = encodeBandInt("code_LocalVariableTable_bci_P", integerListToArray(this.codeLocalVariableTableBciP), Codec.BCI5);
        out.write(encodedBand7);
        PackingUtils.log("Wrote " + encodedBand7.length + " bytes from code_LocalVariableTable_bci_P[" + this.codeLocalVariableTableBciP.size() + "]");
        byte[] encodedBand8 = encodeBandInt("code_LocalVariableTable_span_O", integerListToArray(this.codeLocalVariableTableSpanO), Codec.BRANCH5);
        out.write(encodedBand8);
        PackingUtils.log("Wrote " + encodedBand8.length + " bytes from code_LocalVariableTable_span_O[" + this.codeLocalVariableTableSpanO.size() + "]");
        byte[] encodedBand9 = encodeBandInt("code_LocalVariableTable_name_RU", cpEntryListToArray(this.codeLocalVariableTableNameRU), Codec.UNSIGNED5);
        out.write(encodedBand9);
        PackingUtils.log("Wrote " + encodedBand9.length + " bytes from code_LocalVariableTable_name_RU[" + this.codeLocalVariableTableNameRU.size() + "]");
        byte[] encodedBand10 = encodeBandInt("code_LocalVariableTable_type_RS", cpEntryListToArray(this.codeLocalVariableTableTypeRS), Codec.UNSIGNED5);
        out.write(encodedBand10);
        PackingUtils.log("Wrote " + encodedBand10.length + " bytes from code_LocalVariableTable_type_RS[" + this.codeLocalVariableTableTypeRS.size() + "]");
        byte[] encodedBand11 = encodeBandInt("code_LocalVariableTable_slot", this.codeLocalVariableTableSlot.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand11);
        PackingUtils.log("Wrote " + encodedBand11.length + " bytes from code_LocalVariableTable_slot[" + this.codeLocalVariableTableSlot.size() + "]");
        byte[] encodedBand12 = encodeBandInt("code_LocalVariableTypeTable_N", this.codeLocalVariableTypeTableN.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand12);
        PackingUtils.log("Wrote " + encodedBand12.length + " bytes from code_LocalVariableTypeTable_N[" + this.codeLocalVariableTypeTableN.size() + "]");
        byte[] encodedBand13 = encodeBandInt("code_LocalVariableTypeTable_bci_P", integerListToArray(this.codeLocalVariableTypeTableBciP), Codec.BCI5);
        out.write(encodedBand13);
        PackingUtils.log("Wrote " + encodedBand13.length + " bytes from code_LocalVariableTypeTable_bci_P[" + this.codeLocalVariableTypeTableBciP.size() + "]");
        byte[] encodedBand14 = encodeBandInt("code_LocalVariableTypeTable_span_O", integerListToArray(this.codeLocalVariableTypeTableSpanO), Codec.BRANCH5);
        out.write(encodedBand14);
        PackingUtils.log("Wrote " + encodedBand14.length + " bytes from code_LocalVariableTypeTable_span_O[" + this.codeLocalVariableTypeTableSpanO.size() + "]");
        byte[] encodedBand15 = encodeBandInt("code_LocalVariableTypeTable_name_RU", cpEntryListToArray(this.codeLocalVariableTypeTableNameRU), Codec.UNSIGNED5);
        out.write(encodedBand15);
        PackingUtils.log("Wrote " + encodedBand15.length + " bytes from code_LocalVariableTypeTable_name_RU[" + this.codeLocalVariableTypeTableNameRU.size() + "]");
        byte[] encodedBand16 = encodeBandInt("code_LocalVariableTypeTable_type_RS", cpEntryListToArray(this.codeLocalVariableTypeTableTypeRS), Codec.UNSIGNED5);
        out.write(encodedBand16);
        PackingUtils.log("Wrote " + encodedBand16.length + " bytes from code_LocalVariableTypeTable_type_RS[" + this.codeLocalVariableTypeTableTypeRS.size() + "]");
        byte[] encodedBand17 = encodeBandInt("code_LocalVariableTypeTable_slot", this.codeLocalVariableTypeTableSlot.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand17);
        PackingUtils.log("Wrote " + encodedBand17.length + " bytes from code_LocalVariableTypeTable_slot[" + this.codeLocalVariableTypeTableSlot.size() + "]");
        for (NewAttributeBands bands : this.codeAttributeBands) {
            bands.pack(out);
        }
    }

    public void addMethod(int flags, String name, String desc, String signature, String[] exceptions) {
        CPNameAndType nt = this.cpBands.getCPNameAndType(name, desc);
        this.tempMethodDesc.add(nt);
        if (signature != null) {
            this.methodSignature.add(this.cpBands.getCPSignature(signature));
            flags |= 524288;
        }
        if (exceptions != null) {
            this.methodExceptionNumber.add(exceptions.length);
            for (String exception : exceptions) {
                this.methodExceptionClasses.add(this.cpBands.getCPClass(exception));
            }
            flags |= 262144;
        }
        if ((flags & 131072) != 0) {
            flags = (flags & (-131073)) | 1048576;
        }
        this.tempMethodFlags.add(Long.valueOf(flags));
        this.numMethodArgs = countArgs(desc);
        if (!this.anySyntheticMethods && (flags & CpioConstants.C_ISFIFO) != 0 && this.segment.getCurrentClassReader().hasSyntheticAttributes()) {
            this.cpBands.addCPUtf8("Synthetic");
            this.anySyntheticMethods = true;
        }
    }

    public void endOfMethod() {
        if (this.tempMethodRVPA != null) {
            this.method_RVPA_bands.addParameterAnnotation(this.tempMethodRVPA.numParams, this.tempMethodRVPA.annoN, this.tempMethodRVPA.pairN, this.tempMethodRVPA.typeRS, this.tempMethodRVPA.nameRU, this.tempMethodRVPA.tags, this.tempMethodRVPA.values, this.tempMethodRVPA.caseArrayN, this.tempMethodRVPA.nestTypeRS, this.tempMethodRVPA.nestNameRU, this.tempMethodRVPA.nestPairN);
            this.tempMethodRVPA = null;
        }
        if (this.tempMethodRIPA != null) {
            this.method_RIPA_bands.addParameterAnnotation(this.tempMethodRIPA.numParams, this.tempMethodRIPA.annoN, this.tempMethodRIPA.pairN, this.tempMethodRIPA.typeRS, this.tempMethodRIPA.nameRU, this.tempMethodRIPA.tags, this.tempMethodRIPA.values, this.tempMethodRIPA.caseArrayN, this.tempMethodRIPA.nestTypeRS, this.tempMethodRIPA.nestNameRU, this.tempMethodRIPA.nestPairN);
            this.tempMethodRIPA = null;
        }
        if (this.codeFlags.size() > 0) {
            long latestCodeFlag = this.codeFlags.get(this.codeFlags.size() - 1).longValue();
            int latestLocalVariableTableN = this.codeLocalVariableTableN.get(this.codeLocalVariableTableN.size() - 1);
            if (latestCodeFlag == 4 && latestLocalVariableTableN == 0) {
                this.codeLocalVariableTableN.remove(this.codeLocalVariableTableN.size() - 1);
                this.codeFlags.remove(this.codeFlags.size() - 1);
                this.codeFlags.add(0L);
            }
        }
    }

    protected static int countArgs(String descriptor) {
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
                    count += 2;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public void endOfClass() {
        int numFields = this.tempFieldDesc.size();
        this.class_field_count[this.index] = numFields;
        this.field_descr[this.index] = new CPNameAndType[numFields];
        this.field_flags[this.index] = new long[numFields];
        for (int i = 0; i < numFields; i++) {
            this.field_descr[this.index][i] = this.tempFieldDesc.get(i);
            this.field_flags[this.index][i] = this.tempFieldFlags.get(i).longValue();
        }
        int numMethods = this.tempMethodDesc.size();
        this.class_method_count[this.index] = numMethods;
        this.method_descr[this.index] = new CPNameAndType[numMethods];
        this.method_flags[this.index] = new long[numMethods];
        for (int i2 = 0; i2 < numMethods; i2++) {
            this.method_descr[this.index][i2] = this.tempMethodDesc.get(i2);
            this.method_flags[this.index][i2] = this.tempMethodFlags.get(i2).longValue();
        }
        this.tempFieldDesc.clear();
        this.tempFieldFlags.clear();
        this.tempMethodDesc.clear();
        this.tempMethodFlags.clear();
        this.index++;
    }

    public void addSourceFile(String source) {
        String implicitSourceFileName = this.class_this[this.index].toString();
        if (implicitSourceFileName.indexOf(36) != -1) {
            implicitSourceFileName = implicitSourceFileName.substring(0, implicitSourceFileName.indexOf(36));
        }
        if (source.equals(implicitSourceFileName.substring(implicitSourceFileName.lastIndexOf(47) + 1) + ".java")) {
            this.classSourceFile.add(null);
        } else {
            this.classSourceFile.add(this.cpBands.getCPUtf8(source));
        }
        long[] jArr = this.class_flags;
        int i = this.index;
        jArr[i] = jArr[i] | 131072;
    }

    public void addEnclosingMethod(String owner, String name, String desc) {
        long[] jArr = this.class_flags;
        int i = this.index;
        jArr[i] = jArr[i] | 262144;
        this.classEnclosingMethodClass.add(this.cpBands.getCPClass(owner));
        this.classEnclosingMethodDesc.add(name == null ? null : this.cpBands.getCPNameAndType(name, desc));
    }

    public void addClassAttribute(NewAttribute attribute) {
        String attributeName = attribute.type;
        for (NewAttributeBands bands : this.classAttributeBands) {
            if (bands.getAttributeName().equals(attributeName)) {
                bands.addAttribute(attribute);
                int flagIndex = bands.getFlagIndex();
                long[] jArr = this.class_flags;
                int i = this.index;
                jArr[i] = jArr[i] | (1 << flagIndex);
                return;
            }
        }
        throw new IllegalArgumentException("No suitable definition for " + attributeName);
    }

    public void addFieldAttribute(NewAttribute attribute) {
        String attributeName = attribute.type;
        for (NewAttributeBands bands : this.fieldAttributeBands) {
            if (bands.getAttributeName().equals(attributeName)) {
                bands.addAttribute(attribute);
                int flagIndex = bands.getFlagIndex();
                Long flags = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
                this.tempFieldFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
                return;
            }
        }
        throw new IllegalArgumentException("No suitable definition for " + attributeName);
    }

    public void addMethodAttribute(NewAttribute attribute) {
        String attributeName = attribute.type;
        for (NewAttributeBands bands : this.methodAttributeBands) {
            if (bands.getAttributeName().equals(attributeName)) {
                bands.addAttribute(attribute);
                int flagIndex = bands.getFlagIndex();
                Long flags = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
                this.tempMethodFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
                return;
            }
        }
        throw new IllegalArgumentException("No suitable definition for " + attributeName);
    }

    public void addCodeAttribute(NewAttribute attribute) {
        String attributeName = attribute.type;
        for (NewAttributeBands bands : this.codeAttributeBands) {
            if (bands.getAttributeName().equals(attributeName)) {
                bands.addAttribute(attribute);
                int flagIndex = bands.getFlagIndex();
                Long flags = this.codeFlags.remove(this.codeFlags.size() - 1);
                this.codeFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
                return;
            }
        }
        throw new IllegalArgumentException("No suitable definition for " + attributeName);
    }

    public void addMaxStack(int maxStack, int maxLocals) {
        Long latestFlag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
        Long newFlag = Long.valueOf(latestFlag.intValue() | 131072);
        this.tempMethodFlags.add(newFlag);
        this.codeMaxStack.add(maxStack);
        if ((newFlag.longValue() & 8) == 0) {
            maxLocals--;
        }
        this.codeMaxLocals.add(maxLocals - this.numMethodArgs);
    }

    public void addCode() {
        this.codeHandlerCount.add(0);
        if (!this.stripDebug) {
            this.codeFlags.add(4L);
            this.codeLocalVariableTableN.add(0);
        }
    }

    public void addHandler(Label start, Label end, Label handler, String type) {
        int handlers = this.codeHandlerCount.remove(this.codeHandlerCount.size() - 1);
        this.codeHandlerCount.add(handlers + 1);
        this.codeHandlerStartP.add(start);
        this.codeHandlerEndPO.add(end);
        this.codeHandlerCatchPO.add(handler);
        this.codeHandlerClass.add(type == null ? null : this.cpBands.getCPClass(type));
    }

    public void addLineNumber(int line, Label start) {
        Long latestCodeFlag = this.codeFlags.get(this.codeFlags.size() - 1);
        if ((latestCodeFlag.intValue() & 2) == 0) {
            this.codeFlags.remove(this.codeFlags.size() - 1);
            this.codeFlags.add(Long.valueOf(latestCodeFlag.intValue() | 2));
            this.codeLineNumberTableN.add(1);
        } else {
            this.codeLineNumberTableN.increment(this.codeLineNumberTableN.size() - 1);
        }
        this.codeLineNumberTableLine.add(line);
        this.codeLineNumberTableBciP.add(start);
    }

    public void addLocalVariable(String name, String desc, String signature, Label start, Label end, int indx) {
        if (signature != null) {
            Long latestCodeFlag = this.codeFlags.get(this.codeFlags.size() - 1);
            if ((latestCodeFlag.intValue() & 8) == 0) {
                this.codeFlags.remove(this.codeFlags.size() - 1);
                this.codeFlags.add(Long.valueOf(latestCodeFlag.intValue() | 8));
                this.codeLocalVariableTypeTableN.add(1);
            } else {
                this.codeLocalVariableTypeTableN.increment(this.codeLocalVariableTypeTableN.size() - 1);
            }
            this.codeLocalVariableTypeTableBciP.add(start);
            this.codeLocalVariableTypeTableSpanO.add(end);
            this.codeLocalVariableTypeTableNameRU.add(this.cpBands.getCPUtf8(name));
            this.codeLocalVariableTypeTableTypeRS.add(this.cpBands.getCPSignature(signature));
            this.codeLocalVariableTypeTableSlot.add(indx);
        }
        this.codeLocalVariableTableN.increment(this.codeLocalVariableTableN.size() - 1);
        this.codeLocalVariableTableBciP.add(start);
        this.codeLocalVariableTableSpanO.add(end);
        this.codeLocalVariableTableNameRU.add(this.cpBands.getCPUtf8(name));
        this.codeLocalVariableTableTypeRS.add(this.cpBands.getCPSignature(desc));
        this.codeLocalVariableTableSlot.add(indx);
    }

    public void doBciRenumbering(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        renumberBci(this.codeLineNumberTableBciP, bciRenumbering, labelsToOffsets);
        renumberBci(this.codeLocalVariableTableBciP, bciRenumbering, labelsToOffsets);
        renumberOffsetBci(this.codeLocalVariableTableBciP, this.codeLocalVariableTableSpanO, bciRenumbering, labelsToOffsets);
        renumberBci(this.codeLocalVariableTypeTableBciP, bciRenumbering, labelsToOffsets);
        renumberOffsetBci(this.codeLocalVariableTypeTableBciP, this.codeLocalVariableTypeTableSpanO, bciRenumbering, labelsToOffsets);
        renumberBci(this.codeHandlerStartP, bciRenumbering, labelsToOffsets);
        renumberOffsetBci(this.codeHandlerStartP, this.codeHandlerEndPO, bciRenumbering, labelsToOffsets);
        renumberDoubleOffsetBci(this.codeHandlerStartP, this.codeHandlerEndPO, this.codeHandlerCatchPO, bciRenumbering, labelsToOffsets);
        for (NewAttributeBands newAttributeBandSet : this.classAttributeBands) {
            newAttributeBandSet.renumberBci(bciRenumbering, labelsToOffsets);
        }
        for (NewAttributeBands newAttributeBandSet2 : this.methodAttributeBands) {
            newAttributeBandSet2.renumberBci(bciRenumbering, labelsToOffsets);
        }
        for (NewAttributeBands newAttributeBandSet3 : this.fieldAttributeBands) {
            newAttributeBandSet3.renumberBci(bciRenumbering, labelsToOffsets);
        }
        for (NewAttributeBands newAttributeBandSet4 : this.codeAttributeBands) {
            newAttributeBandSet4.renumberBci(bciRenumbering, labelsToOffsets);
        }
    }

    private void renumberBci(List<Integer> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Object label = list.get(i);
            if (!(label instanceof Integer)) {
                if (label instanceof Label) {
                    list.remove(i);
                    Integer bytecodeIndex = labelsToOffsets.get(label);
                    list.add(i, Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue())));
                }
            } else {
                return;
            }
        }
    }

    private void renumberOffsetBci(List<Integer> relative, List<Integer> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Object label = list.get(i);
            if (!(label instanceof Integer)) {
                if (label instanceof Label) {
                    list.remove(i);
                    Integer bytecodeIndex = labelsToOffsets.get(label);
                    Integer renumberedOffset = Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue()) - relative.get(i).intValue());
                    list.add(i, renumberedOffset);
                }
            } else {
                return;
            }
        }
    }

    private void renumberDoubleOffsetBci(List<Integer> relative, List<Integer> firstOffset, List<Object> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Object label = list.get(i);
            if (!(label instanceof Integer)) {
                if (label instanceof Label) {
                    list.remove(i);
                    Integer bytecodeIndex = labelsToOffsets.get(label);
                    Integer renumberedOffset = Integer.valueOf((bciRenumbering.get(bytecodeIndex.intValue()) - relative.get(i).intValue()) - firstOffset.get(i).intValue());
                    list.add(i, renumberedOffset);
                }
            } else {
                return;
            }
        }
    }

    public boolean isAnySyntheticClasses() {
        return this.anySyntheticClasses;
    }

    public boolean isAnySyntheticFields() {
        return this.anySyntheticFields;
    }

    public boolean isAnySyntheticMethods() {
        return this.anySyntheticMethods;
    }

    public void addParameterAnnotation(int parameter, String desc, boolean visible, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
        if (visible) {
            if (this.tempMethodRVPA == null) {
                this.tempMethodRVPA = new TempParamAnnotation(this.numMethodArgs);
                this.tempMethodRVPA.addParameterAnnotation(parameter, desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
            }
            Long flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
            this.tempMethodFlags.add(Long.valueOf(flag.longValue() | 8388608));
            return;
        }
        if (this.tempMethodRIPA == null) {
            this.tempMethodRIPA = new TempParamAnnotation(this.numMethodArgs);
            this.tempMethodRIPA.addParameterAnnotation(parameter, desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
        }
        Long flag2 = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
        this.tempMethodFlags.add(Long.valueOf(flag2.longValue() | 16777216));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/ClassBands$TempParamAnnotation.class */
    public static class TempParamAnnotation {
        int numParams;
        int[] annoN;
        IntList pairN = new IntList();
        List<String> typeRS = new ArrayList();
        List<String> nameRU = new ArrayList();
        List<String> tags = new ArrayList();
        List<Object> values = new ArrayList();
        List<Integer> caseArrayN = new ArrayList();
        List<String> nestTypeRS = new ArrayList();
        List<String> nestNameRU = new ArrayList();
        List<Integer> nestPairN = new ArrayList();

        public TempParamAnnotation(int numParams) {
            this.numParams = numParams;
            this.annoN = new int[numParams];
        }

        public void addParameterAnnotation(int parameter, String desc, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
            int[] iArr = this.annoN;
            iArr[parameter] = iArr[parameter] + 1;
            this.typeRS.add(desc);
            this.pairN.add(nameRU.size());
            this.nameRU.addAll(nameRU);
            this.tags.addAll(tags);
            this.values.addAll(values);
            this.caseArrayN.addAll(caseArrayN);
            this.nestTypeRS.addAll(nestTypeRS);
            this.nestNameRU.addAll(nestNameRU);
            this.nestPairN.addAll(nestPairN);
        }
    }

    public void addAnnotation(int context, String desc, boolean visible, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
        switch (context) {
            case 0:
                if (visible) {
                    this.class_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                    if ((this.class_flags[this.index] & 2097152) != 0) {
                        this.class_RVA_bands.incrementAnnoN();
                        return;
                    }
                    this.class_RVA_bands.newEntryInAnnoN();
                    this.class_flags[this.index] = this.class_flags[this.index] | 2097152;
                    return;
                }
                this.class_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                if ((this.class_flags[this.index] & 4194304) != 0) {
                    this.class_RIA_bands.incrementAnnoN();
                    return;
                }
                this.class_RIA_bands.newEntryInAnnoN();
                this.class_flags[this.index] = this.class_flags[this.index] | 4194304;
                return;
            case 1:
                if (visible) {
                    this.field_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                    Long flag = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
                    if ((flag.intValue() & 2097152) != 0) {
                        this.field_RVA_bands.incrementAnnoN();
                    } else {
                        this.field_RVA_bands.newEntryInAnnoN();
                    }
                    this.tempFieldFlags.add(Long.valueOf(flag.intValue() | 2097152));
                    return;
                }
                this.field_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                Long flag2 = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
                if ((flag2.intValue() & 4194304) != 0) {
                    this.field_RIA_bands.incrementAnnoN();
                } else {
                    this.field_RIA_bands.newEntryInAnnoN();
                }
                this.tempFieldFlags.add(Long.valueOf(flag2.intValue() | 4194304));
                return;
            case 2:
                if (visible) {
                    this.method_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                    Long flag3 = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
                    if ((flag3.intValue() & 2097152) != 0) {
                        this.method_RVA_bands.incrementAnnoN();
                    } else {
                        this.method_RVA_bands.newEntryInAnnoN();
                    }
                    this.tempMethodFlags.add(Long.valueOf(flag3.intValue() | 2097152));
                    return;
                }
                this.method_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
                Long flag4 = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
                if ((flag4.intValue() & 4194304) != 0) {
                    this.method_RIA_bands.incrementAnnoN();
                } else {
                    this.method_RIA_bands.newEntryInAnnoN();
                }
                this.tempMethodFlags.add(Long.valueOf(flag4.intValue() | 4194304));
                return;
            default:
                return;
        }
    }

    public void addAnnotationDefault(List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
        this.method_AD_bands.addAnnotation(null, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
        Long flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
        this.tempMethodFlags.add(Long.valueOf(flag.longValue() | 33554432));
    }

    public void removeCurrentClass() {
        if ((this.class_flags[this.index] & 131072) != 0) {
            this.classSourceFile.remove(this.classSourceFile.size() - 1);
        }
        if ((this.class_flags[this.index] & 262144) != 0) {
            this.classEnclosingMethodClass.remove(this.classEnclosingMethodClass.size() - 1);
            this.classEnclosingMethodDesc.remove(this.classEnclosingMethodDesc.size() - 1);
        }
        if ((this.class_flags[this.index] & 524288) != 0) {
            this.classSignature.remove(this.classSignature.size() - 1);
        }
        if ((this.class_flags[this.index] & 2097152) != 0) {
            this.class_RVA_bands.removeLatest();
        }
        if ((this.class_flags[this.index] & 4194304) != 0) {
            this.class_RIA_bands.removeLatest();
        }
        for (Long flagsL : this.tempFieldFlags) {
            long flags = flagsL.longValue();
            if ((flags & 524288) != 0) {
                this.fieldSignature.remove(this.fieldSignature.size() - 1);
            }
            if ((flags & 131072) != 0) {
                this.fieldConstantValueKQ.remove(this.fieldConstantValueKQ.size() - 1);
            }
            if ((flags & 2097152) != 0) {
                this.field_RVA_bands.removeLatest();
            }
            if ((flags & 4194304) != 0) {
                this.field_RIA_bands.removeLatest();
            }
        }
        for (Long flagsL2 : this.tempMethodFlags) {
            long flags2 = flagsL2.longValue();
            if ((flags2 & 524288) != 0) {
                this.methodSignature.remove(this.methodSignature.size() - 1);
            }
            if ((flags2 & 262144) != 0) {
                int exceptions = this.methodExceptionNumber.remove(this.methodExceptionNumber.size() - 1);
                for (int i = 0; i < exceptions; i++) {
                    this.methodExceptionClasses.remove(this.methodExceptionClasses.size() - 1);
                }
            }
            if ((flags2 & 131072) != 0) {
                this.codeMaxLocals.remove(this.codeMaxLocals.size() - 1);
                this.codeMaxStack.remove(this.codeMaxStack.size() - 1);
                int handlers = this.codeHandlerCount.remove(this.codeHandlerCount.size() - 1);
                for (int i2 = 0; i2 < handlers; i2++) {
                    int index = this.codeHandlerStartP.size() - 1;
                    this.codeHandlerStartP.remove(index);
                    this.codeHandlerEndPO.remove(index);
                    this.codeHandlerCatchPO.remove(index);
                    this.codeHandlerClass.remove(index);
                }
                if (!this.stripDebug) {
                    long cdeFlags = this.codeFlags.remove(this.codeFlags.size() - 1).longValue();
                    int numLocalVariables = this.codeLocalVariableTableN.remove(this.codeLocalVariableTableN.size() - 1);
                    for (int i3 = 0; i3 < numLocalVariables; i3++) {
                        int location = this.codeLocalVariableTableBciP.size() - 1;
                        this.codeLocalVariableTableBciP.remove(location);
                        this.codeLocalVariableTableSpanO.remove(location);
                        this.codeLocalVariableTableNameRU.remove(location);
                        this.codeLocalVariableTableTypeRS.remove(location);
                        this.codeLocalVariableTableSlot.remove(location);
                    }
                    if ((cdeFlags & 8) != 0) {
                        int numLocalVariablesInTypeTable = this.codeLocalVariableTypeTableN.remove(this.codeLocalVariableTypeTableN.size() - 1);
                        for (int i4 = 0; i4 < numLocalVariablesInTypeTable; i4++) {
                            int location2 = this.codeLocalVariableTypeTableBciP.size() - 1;
                            this.codeLocalVariableTypeTableBciP.remove(location2);
                            this.codeLocalVariableTypeTableSpanO.remove(location2);
                            this.codeLocalVariableTypeTableNameRU.remove(location2);
                            this.codeLocalVariableTypeTableTypeRS.remove(location2);
                            this.codeLocalVariableTypeTableSlot.remove(location2);
                        }
                    }
                    if ((cdeFlags & 2) != 0) {
                        int numLineNumbers = this.codeLineNumberTableN.remove(this.codeLineNumberTableN.size() - 1);
                        for (int i5 = 0; i5 < numLineNumbers; i5++) {
                            int location3 = this.codeLineNumberTableBciP.size() - 1;
                            this.codeLineNumberTableBciP.remove(location3);
                            this.codeLineNumberTableLine.remove(location3);
                        }
                    }
                }
            }
            if ((flags2 & 2097152) != 0) {
                this.method_RVA_bands.removeLatest();
            }
            if ((flags2 & 4194304) != 0) {
                this.method_RIA_bands.removeLatest();
            }
            if ((flags2 & 8388608) != 0) {
                this.method_RVPA_bands.removeLatest();
            }
            if ((flags2 & 16777216) != 0) {
                this.method_RIPA_bands.removeLatest();
            }
            if ((flags2 & 33554432) != 0) {
                this.method_AD_bands.removeLatest();
            }
        }
        this.class_this[this.index] = null;
        this.class_super[this.index] = null;
        this.class_interface_count[this.index] = 0;
        this.class_interface[this.index] = null;
        this.major_versions[this.index] = 0;
        this.class_flags[this.index] = 0;
        this.tempFieldDesc.clear();
        this.tempFieldFlags.clear();
        this.tempMethodDesc.clear();
        this.tempMethodFlags.clear();
        if (this.index > 0) {
            this.index--;
        }
    }

    public int numClassesProcessed() {
        return this.index;
    }
}
