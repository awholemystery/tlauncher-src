package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.apache.commons.compress.harmony.unpack200.SegmentConstantPool;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/OperandManager.class */
public class OperandManager {
    int[] bcCaseCount;
    int[] bcCaseValue;
    int[] bcByte;
    int[] bcShort;
    int[] bcLocal;
    int[] bcLabel;
    int[] bcIntRef;
    int[] bcFloatRef;
    int[] bcLongRef;
    int[] bcDoubleRef;
    int[] bcStringRef;
    int[] bcClassRef;
    int[] bcFieldRef;
    int[] bcMethodRef;
    int[] bcIMethodRef;
    int[] bcThisField;
    int[] bcSuperField;
    int[] bcThisMethod;
    int[] bcSuperMethod;
    int[] bcInitRef;
    int[] wideByteCodes;
    int bcCaseCountIndex;
    int bcCaseValueIndex;
    int bcByteIndex;
    int bcShortIndex;
    int bcLocalIndex;
    int bcLabelIndex;
    int bcIntRefIndex;
    int bcFloatRefIndex;
    int bcLongRefIndex;
    int bcDoubleRefIndex;
    int bcStringRefIndex;
    int bcClassRefIndex;
    int bcFieldRefIndex;
    int bcMethodRefIndex;
    int bcIMethodRefIndex;
    int bcThisFieldIndex;
    int bcSuperFieldIndex;
    int bcThisMethodIndex;
    int bcSuperMethodIndex;
    int bcInitRefIndex;
    int wideByteCodeIndex;
    Segment segment;
    String currentClass;
    String superClass;
    String newClass;

    public OperandManager(int[] bcCaseCount, int[] bcCaseValue, int[] bcByte, int[] bcShort, int[] bcLocal, int[] bcLabel, int[] bcIntRef, int[] bcFloatRef, int[] bcLongRef, int[] bcDoubleRef, int[] bcStringRef, int[] bcClassRef, int[] bcFieldRef, int[] bcMethodRef, int[] bcIMethodRef, int[] bcThisField, int[] bcSuperField, int[] bcThisMethod, int[] bcSuperMethod, int[] bcInitRef, int[] wideByteCodes) {
        this.bcCaseCount = bcCaseCount;
        this.bcCaseValue = bcCaseValue;
        this.bcByte = bcByte;
        this.bcShort = bcShort;
        this.bcLocal = bcLocal;
        this.bcLabel = bcLabel;
        this.bcIntRef = bcIntRef;
        this.bcFloatRef = bcFloatRef;
        this.bcLongRef = bcLongRef;
        this.bcDoubleRef = bcDoubleRef;
        this.bcStringRef = bcStringRef;
        this.bcClassRef = bcClassRef;
        this.bcFieldRef = bcFieldRef;
        this.bcMethodRef = bcMethodRef;
        this.bcIMethodRef = bcIMethodRef;
        this.bcThisField = bcThisField;
        this.bcSuperField = bcSuperField;
        this.bcThisMethod = bcThisMethod;
        this.bcSuperMethod = bcSuperMethod;
        this.bcInitRef = bcInitRef;
        this.wideByteCodes = wideByteCodes;
    }

    public int nextCaseCount() {
        int[] iArr = this.bcCaseCount;
        int i = this.bcCaseCountIndex;
        this.bcCaseCountIndex = i + 1;
        return iArr[i];
    }

    public int nextCaseValues() {
        int[] iArr = this.bcCaseValue;
        int i = this.bcCaseValueIndex;
        this.bcCaseValueIndex = i + 1;
        return iArr[i];
    }

    public int nextByte() {
        int[] iArr = this.bcByte;
        int i = this.bcByteIndex;
        this.bcByteIndex = i + 1;
        return iArr[i];
    }

    public int nextShort() {
        int[] iArr = this.bcShort;
        int i = this.bcShortIndex;
        this.bcShortIndex = i + 1;
        return iArr[i];
    }

    public int nextLocal() {
        int[] iArr = this.bcLocal;
        int i = this.bcLocalIndex;
        this.bcLocalIndex = i + 1;
        return iArr[i];
    }

    public int nextLabel() {
        int[] iArr = this.bcLabel;
        int i = this.bcLabelIndex;
        this.bcLabelIndex = i + 1;
        return iArr[i];
    }

    public int nextIntRef() {
        int[] iArr = this.bcIntRef;
        int i = this.bcIntRefIndex;
        this.bcIntRefIndex = i + 1;
        return iArr[i];
    }

    public int nextFloatRef() {
        int[] iArr = this.bcFloatRef;
        int i = this.bcFloatRefIndex;
        this.bcFloatRefIndex = i + 1;
        return iArr[i];
    }

    public int nextLongRef() {
        int[] iArr = this.bcLongRef;
        int i = this.bcLongRefIndex;
        this.bcLongRefIndex = i + 1;
        return iArr[i];
    }

    public int nextDoubleRef() {
        int[] iArr = this.bcDoubleRef;
        int i = this.bcDoubleRefIndex;
        this.bcDoubleRefIndex = i + 1;
        return iArr[i];
    }

    public int nextStringRef() {
        int[] iArr = this.bcStringRef;
        int i = this.bcStringRefIndex;
        this.bcStringRefIndex = i + 1;
        return iArr[i];
    }

    public int nextClassRef() {
        int[] iArr = this.bcClassRef;
        int i = this.bcClassRefIndex;
        this.bcClassRefIndex = i + 1;
        return iArr[i];
    }

    public int nextFieldRef() {
        int[] iArr = this.bcFieldRef;
        int i = this.bcFieldRefIndex;
        this.bcFieldRefIndex = i + 1;
        return iArr[i];
    }

    public int nextMethodRef() {
        int[] iArr = this.bcMethodRef;
        int i = this.bcMethodRefIndex;
        this.bcMethodRefIndex = i + 1;
        return iArr[i];
    }

    public int nextIMethodRef() {
        int[] iArr = this.bcIMethodRef;
        int i = this.bcIMethodRefIndex;
        this.bcIMethodRefIndex = i + 1;
        return iArr[i];
    }

    public int nextThisFieldRef() {
        int[] iArr = this.bcThisField;
        int i = this.bcThisFieldIndex;
        this.bcThisFieldIndex = i + 1;
        return iArr[i];
    }

    public int nextSuperFieldRef() {
        int[] iArr = this.bcSuperField;
        int i = this.bcSuperFieldIndex;
        this.bcSuperFieldIndex = i + 1;
        return iArr[i];
    }

    public int nextThisMethodRef() {
        int[] iArr = this.bcThisMethod;
        int i = this.bcThisMethodIndex;
        this.bcThisMethodIndex = i + 1;
        return iArr[i];
    }

    public int nextSuperMethodRef() {
        int[] iArr = this.bcSuperMethod;
        int i = this.bcSuperMethodIndex;
        this.bcSuperMethodIndex = i + 1;
        return iArr[i];
    }

    public int nextInitRef() {
        int[] iArr = this.bcInitRef;
        int i = this.bcInitRefIndex;
        this.bcInitRefIndex = i + 1;
        return iArr[i];
    }

    public int nextWideByteCode() {
        int[] iArr = this.wideByteCodes;
        int i = this.wideByteCodeIndex;
        this.wideByteCodeIndex = i + 1;
        return iArr[i];
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public SegmentConstantPool globalConstantPool() {
        return this.segment.getConstantPool();
    }

    public void setCurrentClass(String string) {
        this.currentClass = string;
    }

    public void setSuperClass(String string) {
        this.superClass = string;
    }

    public void setNewClass(String string) {
        this.newClass = string;
    }

    public String getCurrentClass() {
        if (null == this.currentClass) {
            throw new Error("Current class not set yet");
        }
        return this.currentClass;
    }

    public String getSuperClass() {
        if (null == this.superClass) {
            throw new Error("SuperClass not set yet");
        }
        return this.superClass;
    }

    public String getNewClass() {
        if (null == this.newClass) {
            throw new Error("New class not set yet");
        }
        return this.newClass;
    }
}
