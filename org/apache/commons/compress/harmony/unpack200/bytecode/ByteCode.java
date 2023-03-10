package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/ByteCode.class */
public class ByteCode extends ClassFileEntry {
    private static ByteCode[] noArgByteCodes = new ByteCode[255];
    private final ByteCodeForm byteCodeForm;
    private ClassFileEntry[] nested;
    private int[][] nestedPositions;
    private int[] rewrite;
    private int byteCodeOffset;
    private int[] byteCodeTargets;

    public static ByteCode getByteCode(int opcode) {
        int byteOpcode = 255 & opcode;
        if (ByteCodeForm.get(byteOpcode).hasNoOperand()) {
            if (null == noArgByteCodes[byteOpcode]) {
                noArgByteCodes[byteOpcode] = new ByteCode(byteOpcode);
            }
            return noArgByteCodes[byteOpcode];
        }
        return new ByteCode(byteOpcode);
    }

    protected ByteCode(int opcode) {
        this(opcode, ClassFileEntry.NONE);
    }

    protected ByteCode(int opcode, ClassFileEntry[] nested) {
        this.byteCodeOffset = -1;
        this.byteCodeForm = ByteCodeForm.get(opcode);
        this.rewrite = this.byteCodeForm.getRewriteCopy();
        this.nested = nested;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    protected void doWrite(DataOutputStream dos) throws IOException {
        int[] iArr;
        for (int element : this.rewrite) {
            dos.writeByte(element);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        return this == obj;
    }

    public void extractOperands(OperandManager operandManager, Segment segment, int codeLength) {
        ByteCodeForm currentByteCodeForm = getByteCodeForm();
        currentByteCodeForm.setByteCodeOperands(this, operandManager, codeLength);
    }

    protected ByteCodeForm getByteCodeForm() {
        return this.byteCodeForm;
    }

    public int getLength() {
        return this.rewrite.length;
    }

    public String getName() {
        return getByteCodeForm().getName();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        return this.nested;
    }

    public int getOpcode() {
        return getByteCodeForm().getOpcode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        return objectHashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        if (this.nested.length > 0) {
            for (int index = 0; index < this.nested.length; index++) {
                int argLength = getNestedPosition(index)[1];
                switch (argLength) {
                    case 1:
                        setOperandByte(pool.indexOf(this.nested[index]), getNestedPosition(index)[0]);
                        break;
                    case 2:
                        setOperand2Bytes(pool.indexOf(this.nested[index]), getNestedPosition(index)[0]);
                        break;
                    default:
                        throw new Error("Unhandled resolve " + this);
                }
            }
        }
    }

    public void setOperandBytes(int[] operands) {
        int firstOperandIndex = getByteCodeForm().firstOperandIndex();
        int byteCodeFormLength = getByteCodeForm().operandLength();
        if (firstOperandIndex < 1) {
            throw new Error("Trying to rewrite " + this + " that has no rewrite");
        }
        if (byteCodeFormLength != operands.length) {
            throw new Error("Trying to rewrite " + this + " with " + operands.length + " but bytecode has length " + this.byteCodeForm.operandLength());
        }
        for (int index = 0; index < byteCodeFormLength; index++) {
            this.rewrite[index + firstOperandIndex] = operands[index] & 255;
        }
    }

    public void setOperand2Bytes(int operand, int position) {
        int firstOperandIndex = getByteCodeForm().firstOperandIndex();
        int byteCodeFormLength = getByteCodeForm().getRewrite().length;
        if (firstOperandIndex < 1) {
            throw new Error("Trying to rewrite " + this + " that has no rewrite");
        }
        if (firstOperandIndex + position + 1 > byteCodeFormLength) {
            throw new Error("Trying to rewrite " + this + " with an int at position " + position + " but this won't fit in the rewrite array");
        }
        this.rewrite[firstOperandIndex + position] = (operand & 65280) >> 8;
        this.rewrite[firstOperandIndex + position + 1] = operand & 255;
    }

    public void setOperandSigned2Bytes(int operand, int position) {
        if (operand >= 0) {
            setOperand2Bytes(operand, position);
            return;
        }
        int twosComplementOperand = IcTuple.NESTED_CLASS_FLAG + operand;
        setOperand2Bytes(twosComplementOperand, position);
    }

    public void setOperandByte(int operand, int position) {
        int firstOperandIndex = getByteCodeForm().firstOperandIndex();
        int byteCodeFormLength = getByteCodeForm().operandLength();
        if (firstOperandIndex < 1) {
            throw new Error("Trying to rewrite " + this + " that has no rewrite");
        }
        if (firstOperandIndex + position > byteCodeFormLength) {
            throw new Error("Trying to rewrite " + this + " with an byte at position " + position + " but this won't fit in the rewrite array");
        }
        this.rewrite[firstOperandIndex + position] = operand & 255;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return getByteCodeForm().getName();
    }

    public void setNested(ClassFileEntry[] nested) {
        this.nested = nested;
    }

    public void setNestedPositions(int[][] nestedPositions) {
        this.nestedPositions = nestedPositions;
    }

    public int[][] getNestedPositions() {
        return this.nestedPositions;
    }

    public int[] getNestedPosition(int index) {
        return getNestedPositions()[index];
    }

    public boolean hasMultipleByteCodes() {
        return getByteCodeForm().hasMultipleByteCodes();
    }

    public void setByteCodeIndex(int byteCodeOffset) {
        this.byteCodeOffset = byteCodeOffset;
    }

    public int getByteCodeIndex() {
        return this.byteCodeOffset;
    }

    public void setByteCodeTargets(int[] byteCodeTargets) {
        this.byteCodeTargets = byteCodeTargets;
    }

    public int[] getByteCodeTargets() {
        return this.byteCodeTargets;
    }

    public void applyByteCodeTargetFixup(CodeAttribute codeAttribute) {
        getByteCodeForm().fixUpByteCodeTargets(this, codeAttribute);
    }

    public void setRewrite(int[] rewrite) {
        this.rewrite = rewrite;
    }

    public int[] getRewrite() {
        return this.rewrite;
    }

    public boolean nestedMustStartClassPool() {
        return this.byteCodeForm.nestedMustStartClassPool();
    }
}
