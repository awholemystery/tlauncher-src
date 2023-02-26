package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/LabelForm.class */
public class LabelForm extends ByteCodeForm {
    protected boolean widened;

    public LabelForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    public LabelForm(int opcode, String name, int[] rewrite, boolean widened) {
        this(opcode, name, rewrite);
        this.widened = widened;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v7, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v9, types: [int[], int[][]] */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void fixUpByteCodeTargets(ByteCode byteCode, CodeAttribute codeAttribute) {
        int originalTarget = byteCode.getByteCodeTargets()[0];
        int sourceIndex = byteCode.getByteCodeIndex();
        int absoluteInstructionTargetIndex = sourceIndex + originalTarget;
        int targetValue = codeAttribute.byteCodeOffsets.get(absoluteInstructionTargetIndex).intValue();
        int sourceValue = codeAttribute.byteCodeOffsets.get(sourceIndex).intValue();
        byteCode.setOperandSigned2Bytes(targetValue - sourceValue, 0);
        if (this.widened) {
            byteCode.setNestedPositions(new int[]{new int[]{0, 4}});
        } else {
            byteCode.setNestedPositions(new int[]{new int[]{0, 2}});
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        byteCode.setByteCodeTargets(new int[]{operandManager.nextLabel()});
    }
}
