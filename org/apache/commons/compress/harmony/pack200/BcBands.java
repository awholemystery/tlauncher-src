package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.compress.archivers.sevenz.NID;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.objectweb.asm.Label;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/BcBands.class */
public class BcBands extends BandSet {
    private final CpBands cpBands;
    private final Segment segment;
    private final IntList bcCodes;
    private final IntList bcCaseCount;
    private final IntList bcCaseValue;
    private final IntList bcByte;
    private final IntList bcShort;
    private final IntList bcLocal;
    private final List bcLabel;
    private final List<CPInt> bcIntref;
    private final List<CPFloat> bcFloatRef;
    private final List<CPLong> bcLongRef;
    private final List<CPDouble> bcDoubleRef;
    private final List<CPString> bcStringRef;
    private final List<CPClass> bcClassRef;
    private final List<CPMethodOrField> bcFieldRef;
    private final List<CPMethodOrField> bcMethodRef;
    private final List<CPMethodOrField> bcIMethodRef;
    private List bcThisField;
    private final List bcSuperField;
    private List bcThisMethod;
    private List bcSuperMethod;
    private List bcInitRef;
    private String currentClass;
    private String superClass;
    private String currentNewClass;
    private static final int MULTIANEWARRAY = 197;
    private static final int ALOAD_0 = 42;
    private static final int WIDE = 196;
    private static final int INVOKEINTERFACE = 185;
    private static final int TABLESWITCH = 170;
    private static final int IINC = 132;
    private static final int LOOKUPSWITCH = 171;
    private static final int endMarker = 255;
    private final IntList bciRenumbering;
    private final Map<Label, Integer> labelsToOffsets;
    private int byteCodeOffset;
    private int renumberedOffset;
    private final IntList bcLabelRelativeOffsets;

    public BcBands(CpBands cpBands, Segment segment, int effort) {
        super(effort, segment.getSegmentHeader());
        this.bcCodes = new IntList();
        this.bcCaseCount = new IntList();
        this.bcCaseValue = new IntList();
        this.bcByte = new IntList();
        this.bcShort = new IntList();
        this.bcLocal = new IntList();
        this.bcLabel = new ArrayList();
        this.bcIntref = new ArrayList();
        this.bcFloatRef = new ArrayList();
        this.bcLongRef = new ArrayList();
        this.bcDoubleRef = new ArrayList();
        this.bcStringRef = new ArrayList();
        this.bcClassRef = new ArrayList();
        this.bcFieldRef = new ArrayList();
        this.bcMethodRef = new ArrayList();
        this.bcIMethodRef = new ArrayList();
        this.bcThisField = new ArrayList();
        this.bcSuperField = new ArrayList();
        this.bcThisMethod = new ArrayList();
        this.bcSuperMethod = new ArrayList();
        this.bcInitRef = new ArrayList();
        this.bciRenumbering = new IntList();
        this.labelsToOffsets = new HashMap();
        this.bcLabelRelativeOffsets = new IntList();
        this.cpBands = cpBands;
        this.segment = segment;
    }

    public void setCurrentClass(String name, String superName) {
        this.currentClass = name;
        this.superClass = superName;
    }

    public void finaliseBands() {
        this.bcThisField = getIndexInClass(this.bcThisField);
        this.bcThisMethod = getIndexInClass(this.bcThisMethod);
        this.bcSuperMethod = getIndexInClass(this.bcSuperMethod);
        this.bcInitRef = getIndexInClassForConstructor(this.bcInitRef);
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream out) throws IOException, Pack200Exception {
        PackingUtils.log("Writing byte code bands...");
        byte[] encodedBand = encodeBandInt("bcCodes", this.bcCodes.toArray(), Codec.BYTE1);
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcCodes[" + this.bcCodes.size() + "]");
        byte[] encodedBand2 = encodeBandInt("bcCaseCount", this.bcCaseCount.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from bcCaseCount[" + this.bcCaseCount.size() + "]");
        byte[] encodedBand3 = encodeBandInt("bcCaseValue", this.bcCaseValue.toArray(), Codec.DELTA5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from bcCaseValue[" + this.bcCaseValue.size() + "]");
        byte[] encodedBand4 = encodeBandInt("bcByte", this.bcByte.toArray(), Codec.BYTE1);
        out.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from bcByte[" + this.bcByte.size() + "]");
        byte[] encodedBand5 = encodeBandInt("bcShort", this.bcShort.toArray(), Codec.DELTA5);
        out.write(encodedBand5);
        PackingUtils.log("Wrote " + encodedBand5.length + " bytes from bcShort[" + this.bcShort.size() + "]");
        byte[] encodedBand6 = encodeBandInt("bcLocal", this.bcLocal.toArray(), Codec.UNSIGNED5);
        out.write(encodedBand6);
        PackingUtils.log("Wrote " + encodedBand6.length + " bytes from bcLocal[" + this.bcLocal.size() + "]");
        byte[] encodedBand7 = encodeBandInt("bcLabel", integerListToArray(this.bcLabel), Codec.BRANCH5);
        out.write(encodedBand7);
        PackingUtils.log("Wrote " + encodedBand7.length + " bytes from bcLabel[" + this.bcLabel.size() + "]");
        byte[] encodedBand8 = encodeBandInt("bcIntref", cpEntryListToArray(this.bcIntref), Codec.DELTA5);
        out.write(encodedBand8);
        PackingUtils.log("Wrote " + encodedBand8.length + " bytes from bcIntref[" + this.bcIntref.size() + "]");
        byte[] encodedBand9 = encodeBandInt("bcFloatRef", cpEntryListToArray(this.bcFloatRef), Codec.DELTA5);
        out.write(encodedBand9);
        PackingUtils.log("Wrote " + encodedBand9.length + " bytes from bcFloatRef[" + this.bcFloatRef.size() + "]");
        byte[] encodedBand10 = encodeBandInt("bcLongRef", cpEntryListToArray(this.bcLongRef), Codec.DELTA5);
        out.write(encodedBand10);
        PackingUtils.log("Wrote " + encodedBand10.length + " bytes from bcLongRef[" + this.bcLongRef.size() + "]");
        byte[] encodedBand11 = encodeBandInt("bcDoubleRef", cpEntryListToArray(this.bcDoubleRef), Codec.DELTA5);
        out.write(encodedBand11);
        PackingUtils.log("Wrote " + encodedBand11.length + " bytes from bcDoubleRef[" + this.bcDoubleRef.size() + "]");
        byte[] encodedBand12 = encodeBandInt("bcStringRef", cpEntryListToArray(this.bcStringRef), Codec.DELTA5);
        out.write(encodedBand12);
        PackingUtils.log("Wrote " + encodedBand12.length + " bytes from bcStringRef[" + this.bcStringRef.size() + "]");
        byte[] encodedBand13 = encodeBandInt("bcClassRef", cpEntryOrNullListToArray(this.bcClassRef), Codec.UNSIGNED5);
        out.write(encodedBand13);
        PackingUtils.log("Wrote " + encodedBand13.length + " bytes from bcClassRef[" + this.bcClassRef.size() + "]");
        byte[] encodedBand14 = encodeBandInt("bcFieldRef", cpEntryListToArray(this.bcFieldRef), Codec.DELTA5);
        out.write(encodedBand14);
        PackingUtils.log("Wrote " + encodedBand14.length + " bytes from bcFieldRef[" + this.bcFieldRef.size() + "]");
        byte[] encodedBand15 = encodeBandInt("bcMethodRef", cpEntryListToArray(this.bcMethodRef), Codec.UNSIGNED5);
        out.write(encodedBand15);
        PackingUtils.log("Wrote " + encodedBand15.length + " bytes from bcMethodRef[" + this.bcMethodRef.size() + "]");
        byte[] encodedBand16 = encodeBandInt("bcIMethodRef", cpEntryListToArray(this.bcIMethodRef), Codec.DELTA5);
        out.write(encodedBand16);
        PackingUtils.log("Wrote " + encodedBand16.length + " bytes from bcIMethodRef[" + this.bcIMethodRef.size() + "]");
        byte[] encodedBand17 = encodeBandInt("bcThisField", integerListToArray(this.bcThisField), Codec.UNSIGNED5);
        out.write(encodedBand17);
        PackingUtils.log("Wrote " + encodedBand17.length + " bytes from bcThisField[" + this.bcThisField.size() + "]");
        byte[] encodedBand18 = encodeBandInt("bcSuperField", integerListToArray(this.bcSuperField), Codec.UNSIGNED5);
        out.write(encodedBand18);
        PackingUtils.log("Wrote " + encodedBand18.length + " bytes from bcSuperField[" + this.bcSuperField.size() + "]");
        byte[] encodedBand19 = encodeBandInt("bcThisMethod", integerListToArray(this.bcThisMethod), Codec.UNSIGNED5);
        out.write(encodedBand19);
        PackingUtils.log("Wrote " + encodedBand19.length + " bytes from bcThisMethod[" + this.bcThisMethod.size() + "]");
        byte[] encodedBand20 = encodeBandInt("bcSuperMethod", integerListToArray(this.bcSuperMethod), Codec.UNSIGNED5);
        out.write(encodedBand20);
        PackingUtils.log("Wrote " + encodedBand20.length + " bytes from bcSuperMethod[" + this.bcSuperMethod.size() + "]");
        byte[] encodedBand21 = encodeBandInt("bcInitRef", integerListToArray(this.bcInitRef), Codec.UNSIGNED5);
        out.write(encodedBand21);
        PackingUtils.log("Wrote " + encodedBand21.length + " bytes from bcInitRef[" + this.bcInitRef.size() + "]");
    }

    private List<Integer> getIndexInClass(List<CPMethodOrField> cPMethodOrFieldList) {
        return (List) cPMethodOrFieldList.stream().collect(Collectors.mapping((v0) -> {
            return v0.getIndexInClass();
        }, Collectors.toList()));
    }

    private List<Integer> getIndexInClassForConstructor(List<CPMethodOrField> cPMethodList) {
        return (List) cPMethodList.stream().collect(Collectors.mapping((v0) -> {
            return v0.getIndexInClassForConstructor();
        }, Collectors.toList()));
    }

    public void visitEnd() {
        for (int i = 0; i < this.bciRenumbering.size(); i++) {
            if (this.bciRenumbering.get(i) == -1) {
                this.bciRenumbering.remove(i);
                int i2 = this.renumberedOffset + 1;
                this.renumberedOffset = i2;
                this.bciRenumbering.add(i, i2);
            }
        }
        if (this.renumberedOffset != 0) {
            if (this.renumberedOffset + 1 != this.bciRenumbering.size()) {
                throw new IllegalStateException("Mistake made with renumbering");
            }
            for (int i3 = this.bcLabel.size() - 1; i3 >= 0; i3--) {
                Object label = this.bcLabel.get(i3);
                if (label instanceof Integer) {
                    break;
                }
                if (label instanceof Label) {
                    this.bcLabel.remove(i3);
                    Integer offset = this.labelsToOffsets.get(label);
                    int relativeOffset = this.bcLabelRelativeOffsets.get(i3);
                    this.bcLabel.add(i3, Integer.valueOf(this.bciRenumbering.get(offset.intValue()) - this.bciRenumbering.get(relativeOffset)));
                }
            }
            this.bcCodes.add(endMarker);
            this.segment.getClassBands().doBciRenumbering(this.bciRenumbering, this.labelsToOffsets);
            this.bciRenumbering.clear();
            this.labelsToOffsets.clear();
            this.byteCodeOffset = 0;
            this.renumberedOffset = 0;
        }
    }

    public void visitLabel(Label label) {
        this.labelsToOffsets.put(label, Integer.valueOf(this.byteCodeOffset));
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.byteCodeOffset += 3;
        updateRenumbering();
        boolean aload_0 = false;
        if (this.bcCodes.size() > 0 && this.bcCodes.get(this.bcCodes.size() - 1) == ALOAD_0) {
            this.bcCodes.remove(this.bcCodes.size() - 1);
            aload_0 = true;
        }
        CPMethodOrField cpField = this.cpBands.getCPField(owner, name, desc);
        if (aload_0) {
            opcode += 7;
        }
        if (owner.equals(this.currentClass)) {
            opcode += 24;
            this.bcThisField.add(cpField);
        } else {
            if (aload_0) {
                opcode -= 7;
                this.bcCodes.add(ALOAD_0);
            }
            this.bcFieldRef.add(cpField);
        }
        this.bcCodes.add(opcode);
    }

    private void updateRenumbering() {
        if (this.bciRenumbering.isEmpty()) {
            this.bciRenumbering.add(0);
        }
        this.renumberedOffset++;
        for (int i = this.bciRenumbering.size(); i < this.byteCodeOffset; i++) {
            this.bciRenumbering.add(-1);
        }
        this.bciRenumbering.add(this.renumberedOffset);
    }

    public void visitIincInsn(int var, int increment) {
        if (var > endMarker || increment > endMarker) {
            this.byteCodeOffset += 6;
            this.bcCodes.add(WIDE);
            this.bcCodes.add(IINC);
            this.bcLocal.add(var);
            this.bcShort.add(increment);
        } else {
            this.byteCodeOffset += 3;
            this.bcCodes.add(IINC);
            this.bcLocal.add(var);
            this.bcByte.add(increment & endMarker);
        }
        updateRenumbering();
    }

    public void visitInsn(int opcode) {
        if (opcode >= 202) {
            throw new IllegalArgumentException("Non-standard bytecode instructions not supported");
        }
        this.bcCodes.add(opcode);
        this.byteCodeOffset++;
        updateRenumbering();
    }

    public void visitIntInsn(int opcode, int operand) {
        switch (opcode) {
            case 16:
            case 188:
                this.bcCodes.add(opcode);
                this.bcByte.add(operand & endMarker);
                this.byteCodeOffset += 2;
                break;
            case 17:
                this.bcCodes.add(opcode);
                this.bcShort.add(operand);
                this.byteCodeOffset += 3;
                break;
        }
        updateRenumbering();
    }

    public void visitJumpInsn(int opcode, Label label) {
        this.bcCodes.add(opcode);
        this.bcLabel.add(label);
        this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
        this.byteCodeOffset += 3;
        updateRenumbering();
    }

    public void visitLdcInsn(Object cst) {
        CPConstant<?> constant = this.cpBands.getConstant(cst);
        if (this.segment.lastConstantHadWideIndex() || (constant instanceof CPLong) || (constant instanceof CPDouble)) {
            this.byteCodeOffset += 3;
            if (constant instanceof CPInt) {
                this.bcCodes.add(237);
                this.bcIntref.add((CPInt) constant);
            } else if (constant instanceof CPFloat) {
                this.bcCodes.add(238);
                this.bcFloatRef.add((CPFloat) constant);
            } else if (constant instanceof CPLong) {
                this.bcCodes.add(20);
                this.bcLongRef.add((CPLong) constant);
            } else if (constant instanceof CPDouble) {
                this.bcCodes.add(239);
                this.bcDoubleRef.add((CPDouble) constant);
            } else if (constant instanceof CPString) {
                this.bcCodes.add(19);
                this.bcStringRef.add((CPString) constant);
            } else if (constant instanceof CPClass) {
                this.bcCodes.add(236);
                this.bcClassRef.add((CPClass) constant);
            } else {
                throw new IllegalArgumentException("Constant should not be null");
            }
        } else {
            this.byteCodeOffset += 2;
            if (constant instanceof CPInt) {
                this.bcCodes.add(234);
                this.bcIntref.add((CPInt) constant);
            } else if (constant instanceof CPFloat) {
                this.bcCodes.add(235);
                this.bcFloatRef.add((CPFloat) constant);
            } else if (constant instanceof CPString) {
                this.bcCodes.add(18);
                this.bcStringRef.add((CPString) constant);
            } else if (constant instanceof CPClass) {
                this.bcCodes.add(233);
                this.bcClassRef.add((CPClass) constant);
            }
        }
        updateRenumbering();
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.bcCodes.add(LOOKUPSWITCH);
        this.bcLabel.add(dflt);
        this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
        this.bcCaseCount.add(keys.length);
        for (int i = 0; i < labels.length; i++) {
            this.bcCaseValue.add(keys[i]);
            this.bcLabel.add(labels[i]);
            this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
        }
        int padding = (this.byteCodeOffset + 1) % 4 == 0 ? 0 : 4 - ((this.byteCodeOffset + 1) % 4);
        this.byteCodeOffset += 1 + padding + 8 + (8 * keys.length);
        updateRenumbering();
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.byteCodeOffset += 3;
        switch (opcode) {
            case 182:
            case 183:
            case 184:
                boolean aload_0 = false;
                if (this.bcCodes.size() > 0 && this.bcCodes.get(this.bcCodes.size() - 1) == ALOAD_0) {
                    this.bcCodes.remove(this.bcCodes.size() - 1);
                    aload_0 = true;
                    opcode += 7;
                }
                if (owner.equals(this.currentClass)) {
                    opcode += 24;
                    if (name.equals("<init>") && opcode == 207) {
                        opcode = 230;
                        this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
                    } else {
                        this.bcThisMethod.add(this.cpBands.getCPMethod(owner, name, desc));
                    }
                } else if (owner.equals(this.superClass)) {
                    opcode += 38;
                    if (name.equals("<init>") && opcode == 221) {
                        opcode = 231;
                        this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
                    } else {
                        this.bcSuperMethod.add(this.cpBands.getCPMethod(owner, name, desc));
                    }
                } else {
                    if (aload_0) {
                        opcode -= 7;
                        this.bcCodes.add(ALOAD_0);
                    }
                    if (name.equals("<init>") && opcode == 183 && owner.equals(this.currentNewClass)) {
                        opcode = 232;
                        this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
                    } else {
                        this.bcMethodRef.add(this.cpBands.getCPMethod(owner, name, desc));
                    }
                }
                this.bcCodes.add(opcode);
                break;
            case INVOKEINTERFACE /* 185 */:
                this.byteCodeOffset += 2;
                CPMethodOrField cpIMethod = this.cpBands.getCPIMethod(owner, name, desc);
                this.bcIMethodRef.add(cpIMethod);
                this.bcCodes.add(INVOKEINTERFACE);
                break;
        }
        updateRenumbering();
    }

    public void visitMultiANewArrayInsn(String desc, int dimensions) {
        this.byteCodeOffset += 4;
        updateRenumbering();
        this.bcCodes.add(MULTIANEWARRAY);
        this.bcClassRef.add(this.cpBands.getCPClass(desc));
        this.bcByte.add(dimensions & endMarker);
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.bcCodes.add(TABLESWITCH);
        this.bcLabel.add(dflt);
        this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
        this.bcCaseValue.add(min);
        int count = labels.length;
        this.bcCaseCount.add(count);
        for (Label label : labels) {
            this.bcLabel.add(label);
            this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
        }
        int padding = this.byteCodeOffset % 4 == 0 ? 0 : 4 - (this.byteCodeOffset % 4);
        this.byteCodeOffset += padding + 12 + (4 * labels.length);
        updateRenumbering();
    }

    public void visitTypeInsn(int opcode, String type) {
        this.byteCodeOffset += 3;
        updateRenumbering();
        this.bcCodes.add(opcode);
        this.bcClassRef.add(this.cpBands.getCPClass(type));
        if (opcode == 187) {
            this.currentNewClass = type;
        }
    }

    public void visitVarInsn(int opcode, int var) {
        if (var > endMarker) {
            this.byteCodeOffset += 4;
            this.bcCodes.add(WIDE);
            this.bcCodes.add(opcode);
            this.bcLocal.add(var);
        } else if (var > 3 || opcode == 169) {
            this.byteCodeOffset += 2;
            this.bcCodes.add(opcode);
            this.bcLocal.add(var);
        } else {
            this.byteCodeOffset++;
            switch (opcode) {
                case 21:
                case TarConstants.LF_FIFO /* 54 */:
                    this.bcCodes.add(opcode + 5 + var);
                    break;
                case NID.kComment /* 22 */:
                case TarConstants.LF_CONTIG /* 55 */:
                    this.bcCodes.add(opcode + 8 + var);
                    break;
                case 23:
                case 56:
                    this.bcCodes.add(opcode + 11 + var);
                    break;
                case 24:
                case 57:
                    this.bcCodes.add(opcode + 14 + var);
                    break;
                case NID.kDummy /* 25 */:
                case CoreConstants.COLON_CHAR /* 58 */:
                    this.bcCodes.add(opcode + 17 + var);
                    break;
            }
        }
        updateRenumbering();
    }
}
