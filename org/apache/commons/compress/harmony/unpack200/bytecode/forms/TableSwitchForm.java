package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import java.util.Arrays;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/TableSwitchForm.class */
public class TableSwitchForm extends SwitchForm {
    public TableSwitchForm(int opcode, String name) {
        super(opcode, name);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int case_count = operandManager.nextCaseCount();
        int default_pc = operandManager.nextLabel();
        int case_value = operandManager.nextCaseValues();
        int[] case_pcs = new int[case_count];
        Arrays.setAll(case_pcs, i -> {
            return operandManager.nextLabel();
        });
        int[] labelsArray = new int[case_count + 1];
        labelsArray[0] = default_pc;
        System.arraycopy(case_pcs, 0, labelsArray, 1, (case_count + 1) - 1);
        byteCode.setByteCodeTargets(labelsArray);
        int highValue = (case_value + case_count) - 1;
        int padLength = 3 - (codeLength % 4);
        int rewriteSize = 1 + padLength + 4 + 4 + 4 + (4 * case_pcs.length);
        int[] newRewrite = new int[rewriteSize];
        int rewriteIndex = 0 + 1;
        newRewrite[0] = byteCode.getOpcode();
        for (int index = 0; index < padLength; index++) {
            int i2 = rewriteIndex;
            rewriteIndex++;
            newRewrite[i2] = 0;
        }
        int i3 = rewriteIndex;
        int rewriteIndex2 = rewriteIndex + 1;
        newRewrite[i3] = -1;
        int rewriteIndex3 = rewriteIndex2 + 1;
        newRewrite[rewriteIndex2] = -1;
        int rewriteIndex4 = rewriteIndex3 + 1;
        newRewrite[rewriteIndex3] = -1;
        int rewriteIndex5 = rewriteIndex4 + 1;
        newRewrite[rewriteIndex4] = -1;
        setRewrite4Bytes(case_value, rewriteIndex5, newRewrite);
        int rewriteIndex6 = rewriteIndex5 + 4;
        setRewrite4Bytes(highValue, rewriteIndex6, newRewrite);
        int rewriteIndex7 = rewriteIndex6 + 4;
        for (int index2 = 0; index2 < case_count; index2++) {
            int i4 = rewriteIndex7;
            int rewriteIndex8 = rewriteIndex7 + 1;
            newRewrite[i4] = -1;
            int rewriteIndex9 = rewriteIndex8 + 1;
            newRewrite[rewriteIndex8] = -1;
            int rewriteIndex10 = rewriteIndex9 + 1;
            newRewrite[rewriteIndex9] = -1;
            rewriteIndex7 = rewriteIndex10 + 1;
            newRewrite[rewriteIndex10] = -1;
        }
        byteCode.setRewrite(newRewrite);
    }
}
