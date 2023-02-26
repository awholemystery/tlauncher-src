package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.compress.archivers.sevenz.NID;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionTableEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.NewAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.modpack.TemlateModpackFrame;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/BcBands.class */
public class BcBands extends BandSet {
    private byte[][][] methodByteCodePacked;
    private int[] bcCaseCount;
    private int[] bcCaseValue;
    private int[] bcByte;
    private int[] bcLocal;
    private int[] bcShort;
    private int[] bcLabel;
    private int[] bcIntRef;
    private int[] bcFloatRef;
    private int[] bcLongRef;
    private int[] bcDoubleRef;
    private int[] bcStringRef;
    private int[] bcClassRef;
    private int[] bcFieldRef;
    private int[] bcMethodRef;
    private int[] bcIMethodRef;
    private int[] bcThisField;
    private int[] bcSuperField;
    private int[] bcThisMethod;
    private int[] bcSuperMethod;
    private int[] bcInitRef;
    private int[] bcEscRef;
    private int[] bcEscRefSize;
    private int[] bcEscSize;
    private int[][] bcEscByte;
    private List<Integer> wideByteCodes;

    public BcBands(Segment segment) {
        super(segment);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [byte[][], byte[][][]] */
    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void read(InputStream in) throws IOException, Pack200Exception {
        AttributeLayoutMap attributeDefinitionMap = this.segment.getAttrDefinitionBands().getAttributeDefinitionMap();
        int classCount = this.header.getClassCount();
        long[][] methodFlags = this.segment.getClassBands().getMethodFlags();
        int bcCaseCountCount = 0;
        int bcByteCount = 0;
        int bcShortCount = 0;
        int bcLocalCount = 0;
        int bcLabelCount = 0;
        int bcIntRefCount = 0;
        int bcFloatRefCount = 0;
        int bcLongRefCount = 0;
        int bcDoubleRefCount = 0;
        int bcStringRefCount = 0;
        int bcClassRefCount = 0;
        int bcFieldRefCount = 0;
        int bcMethodRefCount = 0;
        int bcIMethodRefCount = 0;
        int bcThisFieldCount = 0;
        int bcSuperFieldCount = 0;
        int bcThisMethodCount = 0;
        int bcSuperMethodCount = 0;
        int bcInitRefCount = 0;
        int bcEscCount = 0;
        int bcEscRefCount = 0;
        AttributeLayout abstractModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_ABSTRACT, 2);
        AttributeLayout nativeModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_NATIVE, 2);
        this.methodByteCodePacked = new byte[classCount];
        int bcParsed = 0;
        List<Boolean> switchIsTableSwitch = new ArrayList<>();
        this.wideByteCodes = new ArrayList();
        for (int c = 0; c < classCount; c++) {
            int numberOfMethods = methodFlags[c].length;
            this.methodByteCodePacked[c] = new byte[numberOfMethods];
            for (int m = 0; m < numberOfMethods; m++) {
                long methodFlag = methodFlags[c][m];
                if (!abstractModifier.matches(methodFlag) && !nativeModifier.matches(methodFlag)) {
                    ByteArrayOutputStream codeBytes = new ByteArrayOutputStream();
                    while (true) {
                        byte code = (byte) (255 & in.read());
                        if (code != -1) {
                            codeBytes.write(code);
                        } else {
                            this.methodByteCodePacked[c][m] = codeBytes.toByteArray();
                            bcParsed += this.methodByteCodePacked[c][m].length;
                            int[] codes = new int[this.methodByteCodePacked[c][m].length];
                            for (int i = 0; i < codes.length; i++) {
                                codes[i] = this.methodByteCodePacked[c][m][i] & 255;
                            }
                            int i2 = 0;
                            while (i2 < this.methodByteCodePacked[c][m].length) {
                                int codePacked = 255 & this.methodByteCodePacked[c][m][i2];
                                switch (codePacked) {
                                    case 16:
                                    case 188:
                                        bcByteCount++;
                                        break;
                                    case 17:
                                        bcShortCount++;
                                        break;
                                    case NID.kCTime /* 18 */:
                                    case NID.kATime /* 19 */:
                                        bcStringRefCount++;
                                        break;
                                    case 20:
                                        bcLongRefCount++;
                                        break;
                                    case 21:
                                    case NID.kComment /* 22 */:
                                    case 23:
                                    case 24:
                                    case NID.kDummy /* 25 */:
                                    case 26:
                                    case TemlateModpackFrame.RIGHT_BORDER /* 27 */:
                                    case 28:
                                    case TemlateModpackFrame.LEFT_BORDER /* 29 */:
                                    case 30:
                                    case TarArchiveEntry.MAX_NAMELEN /* 31 */:
                                    case 32:
                                    case 33:
                                    case 34:
                                    case 35:
                                    case CoreConstants.DOLLAR /* 36 */:
                                    case 37:
                                    case 38:
                                    case CoreConstants.SINGLE_QUOTE_CHAR /* 39 */:
                                    case 40:
                                    case CoreConstants.RIGHT_PARENTHESIS_CHAR /* 41 */:
                                    case 42:
                                    case 43:
                                    case CoreConstants.COMMA_CHAR /* 44 */:
                                    case CoreConstants.DASH_CHAR /* 45 */:
                                    case 46:
                                    case IOUtils.DIR_SEPARATOR_UNIX /* 47 */:
                                    case 48:
                                    case TarConstants.LF_LINK /* 49 */:
                                    case 50:
                                    case TarConstants.LF_CHR /* 51 */:
                                    case TarConstants.LF_BLK /* 52 */:
                                    case TarConstants.LF_DIR /* 53 */:
                                    case TarConstants.LF_FIFO /* 54 */:
                                    case TarConstants.LF_CONTIG /* 55 */:
                                    case 56:
                                    case 57:
                                    case CoreConstants.COLON_CHAR /* 58 */:
                                    case 59:
                                    case 60:
                                    case 61:
                                    case 62:
                                    case 63:
                                    case 64:
                                    case 65:
                                    case 66:
                                    case 67:
                                    case 68:
                                    case 69:
                                    case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
                                    case 71:
                                    case 72:
                                    case 73:
                                    case 74:
                                    case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
                                    case 76:
                                    case TarConstants.LF_MULTIVOLUME /* 77 */:
                                    case 78:
                                    case 79:
                                    case 80:
                                    case 81:
                                    case 82:
                                    case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
                                    case 84:
                                    case 85:
                                    case 86:
                                    case 87:
                                    case 88:
                                    case 89:
                                    case 90:
                                    case 91:
                                    case 92:
                                    case 93:
                                    case 94:
                                    case 95:
                                    case 96:
                                    case 97:
                                    case 98:
                                    case 99:
                                    case 100:
                                    case HttpStatus.SC_SWITCHING_PROTOCOLS /* 101 */:
                                    case HttpStatus.SC_PROCESSING /* 102 */:
                                    case TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER /* 103 */:
                                    case SyslogConstants.LOG_AUDIT /* 104 */:
                                    case 105:
                                    case 106:
                                    case 107:
                                    case 108:
                                    case 109:
                                    case 110:
                                    case 111:
                                    case SyslogConstants.LOG_ALERT /* 112 */:
                                    case 113:
                                    case 114:
                                    case 115:
                                    case 116:
                                    case 117:
                                    case 118:
                                    case 119:
                                    case 120:
                                    case 121:
                                    case 122:
                                    case CoreConstants.CURLY_LEFT /* 123 */:
                                    case 124:
                                    case CoreConstants.CURLY_RIGHT /* 125 */:
                                    case 126:
                                    case 127:
                                    case 128:
                                    case 129:
                                    case CompleteSubEntityScene.BUTTON_PANEL_SUB_VIEW /* 130 */:
                                    case TarConstants.PREFIXLEN_XSTAR /* 131 */:
                                    case 133:
                                    case 134:
                                    case 135:
                                    case 136:
                                    case 137:
                                    case 138:
                                    case 139:
                                    case 140:
                                    case 141:
                                    case 142:
                                    case 143:
                                    case 144:
                                    case 145:
                                    case 146:
                                    case 147:
                                    case TarConstants.CHKSUM_OFFSET /* 148 */:
                                    case 149:
                                    case 150:
                                    case 151:
                                    case 152:
                                    case 153:
                                    case 154:
                                    case TarConstants.PREFIXLEN /* 155 */:
                                    case TarConstants.LF_OFFSET /* 156 */:
                                    case 157:
                                    case 158:
                                    case 159:
                                    case 160:
                                    case 161:
                                    case 162:
                                    case 163:
                                    case 164:
                                    case 165:
                                    case 166:
                                    case 172:
                                    case 173:
                                    case 174:
                                    case 175:
                                    case 176:
                                    case 177:
                                    case 186:
                                    case 190:
                                    case 191:
                                    case 194:
                                    case 195:
                                    case 198:
                                    case 199:
                                    case 240:
                                    case 241:
                                    case 242:
                                    case 243:
                                    case 244:
                                    case 245:
                                    case 246:
                                    case 247:
                                    case 248:
                                    case 249:
                                    case 250:
                                    case 251:
                                    case 252:
                                    default:
                                        if (endsWithLoad(codePacked) || endsWithStore(codePacked)) {
                                            bcLocalCount++;
                                            break;
                                        } else if (startsWithIf(codePacked)) {
                                            bcLabelCount++;
                                            break;
                                        } else {
                                            break;
                                        }
                                    case 132:
                                        bcLocalCount++;
                                        bcByteCount++;
                                        break;
                                    case 167:
                                    case 168:
                                    case HttpStatus.SC_OK /* 200 */:
                                    case HttpStatus.SC_CREATED /* 201 */:
                                        bcLabelCount++;
                                        break;
                                    case 169:
                                        bcLocalCount++;
                                        break;
                                    case 170:
                                        switchIsTableSwitch.add(Boolean.TRUE);
                                        bcCaseCountCount++;
                                        bcLabelCount++;
                                        break;
                                    case 171:
                                        switchIsTableSwitch.add(Boolean.FALSE);
                                        bcCaseCountCount++;
                                        bcLabelCount++;
                                        break;
                                    case 178:
                                    case 179:
                                    case 180:
                                    case 181:
                                        bcFieldRefCount++;
                                        break;
                                    case 182:
                                    case 183:
                                    case 184:
                                        bcMethodRefCount++;
                                        break;
                                    case 185:
                                        bcIMethodRefCount++;
                                        break;
                                    case 187:
                                    case 189:
                                    case 192:
                                    case 193:
                                    case 233:
                                    case 236:
                                        bcClassRefCount++;
                                        break;
                                    case 196:
                                        int nextInstruction = 255 & this.methodByteCodePacked[c][m][i2 + 1];
                                        this.wideByteCodes.add(Integer.valueOf(nextInstruction));
                                        if (nextInstruction == 132) {
                                            bcLocalCount++;
                                            bcShortCount++;
                                        } else if (endsWithLoad(nextInstruction) || endsWithStore(nextInstruction) || nextInstruction == 169) {
                                            bcLocalCount++;
                                        } else {
                                            this.segment.log(2, "Found unhandled " + ByteCode.getByteCode(nextInstruction));
                                        }
                                        i2++;
                                        break;
                                    case 197:
                                        bcByteCount++;
                                        bcClassRefCount++;
                                        break;
                                    case HttpStatus.SC_ACCEPTED /* 202 */:
                                    case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION /* 203 */:
                                    case HttpStatus.SC_NO_CONTENT /* 204 */:
                                    case HttpStatus.SC_RESET_CONTENT /* 205 */:
                                    case 209:
                                    case 210:
                                    case 211:
                                    case 212:
                                        bcThisFieldCount++;
                                        break;
                                    case HttpStatus.SC_PARTIAL_CONTENT /* 206 */:
                                    case HttpStatus.SC_MULTI_STATUS /* 207 */:
                                    case 208:
                                    case 213:
                                    case 214:
                                    case ModpackScene.WIDTH_SEARCH_PANEL /* 215 */:
                                        bcThisMethodCount++;
                                        break;
                                    case 216:
                                    case 217:
                                    case 218:
                                    case 219:
                                    case CompleteSubEntityScene.FullGameEntity.CompleteDescriptionGamePanel.SHADOW_PANEL /* 223 */:
                                    case 224:
                                    case 225:
                                    case 226:
                                        bcSuperFieldCount++;
                                        break;
                                    case 220:
                                    case 221:
                                    case 222:
                                    case 227:
                                    case 228:
                                    case 229:
                                        bcSuperMethodCount++;
                                        break;
                                    case 230:
                                    case 231:
                                    case 232:
                                        bcInitRefCount++;
                                        break;
                                    case 234:
                                    case 237:
                                        bcIntRefCount++;
                                        break;
                                    case 235:
                                    case 238:
                                        bcFloatRefCount++;
                                        break;
                                    case 239:
                                        bcDoubleRefCount++;
                                        break;
                                    case 253:
                                        bcEscRefCount++;
                                        break;
                                    case 254:
                                        bcEscCount++;
                                        break;
                                }
                                i2++;
                            }
                        }
                    }
                }
            }
        }
        this.bcCaseCount = decodeBandInt("bc_case_count", in, Codec.UNSIGNED5, bcCaseCountCount);
        int bcCaseValueCount = 0;
        for (int i3 = 0; i3 < this.bcCaseCount.length; i3++) {
            boolean isTableSwitch = switchIsTableSwitch.get(i3).booleanValue();
            if (isTableSwitch) {
                bcCaseValueCount++;
            } else {
                bcCaseValueCount += this.bcCaseCount[i3];
            }
        }
        this.bcCaseValue = decodeBandInt("bc_case_value", in, Codec.DELTA5, bcCaseValueCount);
        for (int index = 0; index < bcCaseCountCount; index++) {
            bcLabelCount += this.bcCaseCount[index];
        }
        this.bcByte = decodeBandInt("bc_byte", in, Codec.BYTE1, bcByteCount);
        this.bcShort = decodeBandInt("bc_short", in, Codec.DELTA5, bcShortCount);
        this.bcLocal = decodeBandInt("bc_local", in, Codec.UNSIGNED5, bcLocalCount);
        this.bcLabel = decodeBandInt("bc_label", in, Codec.BRANCH5, bcLabelCount);
        this.bcIntRef = decodeBandInt("bc_intref", in, Codec.DELTA5, bcIntRefCount);
        this.bcFloatRef = decodeBandInt("bc_floatref", in, Codec.DELTA5, bcFloatRefCount);
        this.bcLongRef = decodeBandInt("bc_longref", in, Codec.DELTA5, bcLongRefCount);
        this.bcDoubleRef = decodeBandInt("bc_doubleref", in, Codec.DELTA5, bcDoubleRefCount);
        this.bcStringRef = decodeBandInt("bc_stringref", in, Codec.DELTA5, bcStringRefCount);
        this.bcClassRef = decodeBandInt("bc_classref", in, Codec.UNSIGNED5, bcClassRefCount);
        this.bcFieldRef = decodeBandInt("bc_fieldref", in, Codec.DELTA5, bcFieldRefCount);
        this.bcMethodRef = decodeBandInt("bc_methodref", in, Codec.UNSIGNED5, bcMethodRefCount);
        this.bcIMethodRef = decodeBandInt("bc_imethodref", in, Codec.DELTA5, bcIMethodRefCount);
        this.bcThisField = decodeBandInt("bc_thisfield", in, Codec.UNSIGNED5, bcThisFieldCount);
        this.bcSuperField = decodeBandInt("bc_superfield", in, Codec.UNSIGNED5, bcSuperFieldCount);
        this.bcThisMethod = decodeBandInt("bc_thismethod", in, Codec.UNSIGNED5, bcThisMethodCount);
        this.bcSuperMethod = decodeBandInt("bc_supermethod", in, Codec.UNSIGNED5, bcSuperMethodCount);
        this.bcInitRef = decodeBandInt("bc_initref", in, Codec.UNSIGNED5, bcInitRefCount);
        this.bcEscRef = decodeBandInt("bc_escref", in, Codec.UNSIGNED5, bcEscRefCount);
        this.bcEscRefSize = decodeBandInt("bc_escrefsize", in, Codec.UNSIGNED5, bcEscRefCount);
        this.bcEscSize = decodeBandInt("bc_escsize", in, Codec.UNSIGNED5, bcEscCount);
        this.bcEscByte = decodeBandInt("bc_escbyte", in, Codec.BYTE1, this.bcEscSize);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void unpack() throws Pack200Exception {
        List<Attribute> currentAttributes;
        int classCount = this.header.getClassCount();
        long[][] methodFlags = this.segment.getClassBands().getMethodFlags();
        int[] codeMaxNALocals = this.segment.getClassBands().getCodeMaxNALocals();
        int[] codeMaxStack = this.segment.getClassBands().getCodeMaxStack();
        List<Attribute>[][] methodAttributes = this.segment.getClassBands().getMethodAttributes();
        String[][] methodDescr = this.segment.getClassBands().getMethodDescr();
        AttributeLayoutMap attributeDefinitionMap = this.segment.getAttrDefinitionBands().getAttributeDefinitionMap();
        AttributeLayout abstractModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_ABSTRACT, 2);
        AttributeLayout nativeModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_NATIVE, 2);
        AttributeLayout staticModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_STATIC, 2);
        int[] wideByteCodeArray = new int[this.wideByteCodes.size()];
        for (int index = 0; index < wideByteCodeArray.length; index++) {
            wideByteCodeArray[index] = this.wideByteCodes.get(index).intValue();
        }
        OperandManager operandManager = new OperandManager(this.bcCaseCount, this.bcCaseValue, this.bcByte, this.bcShort, this.bcLocal, this.bcLabel, this.bcIntRef, this.bcFloatRef, this.bcLongRef, this.bcDoubleRef, this.bcStringRef, this.bcClassRef, this.bcFieldRef, this.bcMethodRef, this.bcIMethodRef, this.bcThisField, this.bcSuperField, this.bcThisMethod, this.bcSuperMethod, this.bcInitRef, wideByteCodeArray);
        operandManager.setSegment(this.segment);
        int i = 0;
        ArrayList<List<Attribute>> orderedCodeAttributes = this.segment.getClassBands().getOrderedCodeAttributes();
        int codeAttributeIndex = 0;
        int[] handlerCount = this.segment.getClassBands().getCodeHandlerCount();
        int[][] handlerStartPCs = this.segment.getClassBands().getCodeHandlerStartP();
        int[][] handlerEndPCs = this.segment.getClassBands().getCodeHandlerEndPO();
        int[][] handlerCatchPCs = this.segment.getClassBands().getCodeHandlerCatchPO();
        int[][] handlerClassTypes = this.segment.getClassBands().getCodeHandlerClassRCN();
        boolean allCodeHasFlags = this.segment.getSegmentHeader().getOptions().hasAllCodeFlags();
        boolean[] codeHasFlags = this.segment.getClassBands().getCodeHasAttributes();
        for (int c = 0; c < classCount; c++) {
            int numberOfMethods = methodFlags[c].length;
            for (int m = 0; m < numberOfMethods; m++) {
                long methodFlag = methodFlags[c][m];
                if (!abstractModifier.matches(methodFlag) && !nativeModifier.matches(methodFlag)) {
                    int maxStack = codeMaxStack[i];
                    int maxLocal = codeMaxNALocals[i];
                    if (!staticModifier.matches(methodFlag)) {
                        maxLocal++;
                    }
                    int maxLocal2 = maxLocal + SegmentUtils.countInvokeInterfaceArgs(methodDescr[c][m]);
                    String[] cpClass = this.segment.getCpBands().getCpClass();
                    operandManager.setCurrentClass(cpClass[this.segment.getClassBands().getClassThisInts()[c]]);
                    operandManager.setSuperClass(cpClass[this.segment.getClassBands().getClassSuperInts()[c]]);
                    List<ExceptionTableEntry> exceptionTable = new ArrayList<>();
                    if (handlerCount != null) {
                        for (int j = 0; j < handlerCount[i]; j++) {
                            int handlerClass = handlerClassTypes[i][j] - 1;
                            CPClass cpHandlerClass = null;
                            if (handlerClass != -1) {
                                cpHandlerClass = this.segment.getCpBands().cpClassValue(handlerClass);
                            }
                            ExceptionTableEntry entry = new ExceptionTableEntry(handlerStartPCs[i][j], handlerEndPCs[i][j], handlerCatchPCs[i][j], cpHandlerClass);
                            exceptionTable.add(entry);
                        }
                    }
                    CodeAttribute codeAttr = new CodeAttribute(maxStack, maxLocal2, this.methodByteCodePacked[c][m], this.segment, operandManager, exceptionTable);
                    List<Attribute> methodAttributesList = methodAttributes[c][m];
                    int indexForCodeAttr = 0;
                    for (Attribute attribute : methodAttributesList) {
                        if (!(attribute instanceof NewAttribute) || ((NewAttribute) attribute).getLayoutIndex() >= 15) {
                            break;
                        }
                        indexForCodeAttr++;
                    }
                    methodAttributesList.add(indexForCodeAttr, codeAttr);
                    codeAttr.renumber(codeAttr.byteCodeOffsets);
                    if (allCodeHasFlags) {
                        currentAttributes = orderedCodeAttributes.get(i);
                    } else if (codeHasFlags[i]) {
                        currentAttributes = orderedCodeAttributes.get(codeAttributeIndex);
                        codeAttributeIndex++;
                    } else {
                        currentAttributes = Collections.EMPTY_LIST;
                    }
                    for (Attribute currentAttribute : currentAttributes) {
                        codeAttr.addAttribute(currentAttribute);
                        if (currentAttribute.hasBCIRenumbering()) {
                            ((BCIRenumberedAttribute) currentAttribute).renumber(codeAttr.byteCodeOffsets);
                        }
                    }
                    i++;
                }
            }
        }
    }

    private boolean startsWithIf(int codePacked) {
        return (codePacked >= 153 && codePacked <= 166) || codePacked == 198 || codePacked == 199;
    }

    private boolean endsWithLoad(int codePacked) {
        return codePacked >= 21 && codePacked <= 25;
    }

    private boolean endsWithStore(int codePacked) {
        return codePacked >= 54 && codePacked <= 58;
    }

    public byte[][][] getMethodByteCodePacked() {
        return this.methodByteCodePacked;
    }

    public int[] getBcCaseCount() {
        return this.bcCaseCount;
    }

    public int[] getBcCaseValue() {
        return this.bcCaseValue;
    }

    public int[] getBcByte() {
        return this.bcByte;
    }

    public int[] getBcClassRef() {
        return this.bcClassRef;
    }

    public int[] getBcDoubleRef() {
        return this.bcDoubleRef;
    }

    public int[] getBcFieldRef() {
        return this.bcFieldRef;
    }

    public int[] getBcFloatRef() {
        return this.bcFloatRef;
    }

    public int[] getBcIMethodRef() {
        return this.bcIMethodRef;
    }

    public int[] getBcInitRef() {
        return this.bcInitRef;
    }

    public int[] getBcIntRef() {
        return this.bcIntRef;
    }

    public int[] getBcLabel() {
        return this.bcLabel;
    }

    public int[] getBcLocal() {
        return this.bcLocal;
    }

    public int[] getBcLongRef() {
        return this.bcLongRef;
    }

    public int[] getBcMethodRef() {
        return this.bcMethodRef;
    }

    public int[] getBcShort() {
        return this.bcShort;
    }

    public int[] getBcStringRef() {
        return this.bcStringRef;
    }

    public int[] getBcSuperField() {
        return this.bcSuperField;
    }

    public int[] getBcSuperMethod() {
        return this.bcSuperMethod;
    }

    public int[] getBcThisField() {
        return this.bcThisField;
    }

    public int[] getBcThisMethod() {
        return this.bcThisMethod;
    }
}
