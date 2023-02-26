package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/WideForm.class */
public class WideForm extends VariableInstructionForm {
    public WideForm(int opcode, String name) {
        super(opcode, name);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int instruction = operandManager.nextWideByteCode();
        if (instruction == 132) {
            setByteCodeOperandsFormat2(instruction, byteCode, operandManager, codeLength);
        } else {
            setByteCodeOperandsFormat1(instruction, byteCode, operandManager, codeLength);
        }
    }

    protected void setByteCodeOperandsFormat1(int instruction, ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int local = operandManager.nextLocal();
        int[] newRewrite = new int[4];
        int rewriteIndex = 0 + 1;
        newRewrite[0] = byteCode.getOpcode();
        int rewriteIndex2 = rewriteIndex + 1;
        newRewrite[rewriteIndex] = instruction;
        setRewrite2Bytes(local, rewriteIndex2, newRewrite);
        int i = rewriteIndex2 + 2;
        byteCode.setRewrite(newRewrite);
    }

    protected void setByteCodeOperandsFormat2(int instruction, ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int local = operandManager.nextLocal();
        int constWord = operandManager.nextShort();
        int[] newRewrite = new int[6];
        int rewriteIndex = 0 + 1;
        newRewrite[0] = byteCode.getOpcode();
        int rewriteIndex2 = rewriteIndex + 1;
        newRewrite[rewriteIndex] = instruction;
        setRewrite2Bytes(local, rewriteIndex2, newRewrite);
        int rewriteIndex3 = rewriteIndex2 + 2;
        setRewrite2Bytes(constWord, rewriteIndex3, newRewrite);
        int i = rewriteIndex3 + 2;
        byteCode.setRewrite(newRewrite);
    }
}
