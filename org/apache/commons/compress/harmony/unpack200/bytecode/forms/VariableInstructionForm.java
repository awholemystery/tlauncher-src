package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/VariableInstructionForm.class */
public abstract class VariableInstructionForm extends ByteCodeForm {
    public VariableInstructionForm(int opcode, String name) {
        super(opcode, name);
    }

    public void setRewrite4Bytes(int operand, int[] rewrite) {
        int firstOperandPosition = -1;
        int index = 0;
        while (true) {
            if (index < rewrite.length - 3) {
                if (rewrite[index] != -1 || rewrite[index + 1] != -1 || rewrite[index + 2] != -1 || rewrite[index + 3] != -1) {
                    index++;
                } else {
                    firstOperandPosition = index;
                    break;
                }
            } else {
                break;
            }
        }
        setRewrite4Bytes(operand, firstOperandPosition, rewrite);
    }

    public void setRewrite4Bytes(int operand, int absPosition, int[] rewrite) {
        if (absPosition < 0) {
            throw new Error("Trying to rewrite " + this + " but there is no room for 4 bytes");
        }
        int byteCodeRewriteLength = rewrite.length;
        if (absPosition + 3 > byteCodeRewriteLength) {
            throw new Error("Trying to rewrite " + this + " with an int at position " + absPosition + " but this won't fit in the rewrite array");
        }
        rewrite[absPosition] = ((-16777216) & operand) >> 24;
        rewrite[absPosition + 1] = (16711680 & operand) >> 16;
        rewrite[absPosition + 2] = (65280 & operand) >> 8;
        rewrite[absPosition + 3] = 255 & operand;
    }

    public void setRewrite2Bytes(int operand, int absPosition, int[] rewrite) {
        if (absPosition < 0) {
            throw new Error("Trying to rewrite " + this + " but there is no room for 4 bytes");
        }
        int byteCodeRewriteLength = rewrite.length;
        if (absPosition + 1 > byteCodeRewriteLength) {
            throw new Error("Trying to rewrite " + this + " with an int at position " + absPosition + " but this won't fit in the rewrite array");
        }
        rewrite[absPosition] = (65280 & operand) >> 8;
        rewrite[absPosition + 1] = 255 & operand;
    }
}
