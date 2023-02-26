package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/SwitchForm.class */
public abstract class SwitchForm extends VariableInstructionForm {
    public SwitchForm(int opcode, String name) {
        super(opcode, name);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void fixUpByteCodeTargets(ByteCode byteCode, CodeAttribute codeAttribute) {
        int[] originalTargets = byteCode.getByteCodeTargets();
        int numberOfLabels = originalTargets.length;
        int[] replacementTargets = new int[numberOfLabels];
        int sourceIndex = byteCode.getByteCodeIndex();
        int sourceValue = codeAttribute.byteCodeOffsets.get(sourceIndex).intValue();
        for (int index = 0; index < numberOfLabels; index++) {
            int absoluteInstructionTargetIndex = sourceIndex + originalTargets[index];
            int targetValue = codeAttribute.byteCodeOffsets.get(absoluteInstructionTargetIndex).intValue();
            replacementTargets[index] = targetValue - sourceValue;
        }
        int[] rewriteArray = byteCode.getRewrite();
        for (int index2 = 0; index2 < numberOfLabels; index2++) {
            setRewrite4Bytes(replacementTargets[index2], rewriteArray);
        }
    }
}
