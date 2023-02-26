package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import java.util.Arrays;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/LookupSwitchForm.class */
public class LookupSwitchForm extends SwitchForm {
    public LookupSwitchForm(int opcode, String name) {
        super(opcode, name);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int case_count = operandManager.nextCaseCount();
        int default_pc = operandManager.nextLabel();
        int[] case_values = new int[case_count];
        Arrays.setAll(case_values, i -> {
            return operandManager.nextCaseValues();
        });
        int[] case_pcs = new int[case_count];
        Arrays.setAll(case_pcs, i2 -> {
            return operandManager.nextLabel();
        });
        int[] labelsArray = new int[case_count + 1];
        labelsArray[0] = default_pc;
        System.arraycopy(case_pcs, 0, labelsArray, 1, (case_count + 1) - 1);
        byteCode.setByteCodeTargets(labelsArray);
        int padLength = 3 - (codeLength % 4);
        int rewriteSize = 1 + padLength + 4 + 4 + (4 * case_values.length) + (4 * case_pcs.length);
        int[] newRewrite = new int[rewriteSize];
        int rewriteIndex = 0 + 1;
        newRewrite[0] = byteCode.getOpcode();
        for (int index = 0; index < padLength; index++) {
            int i3 = rewriteIndex;
            rewriteIndex++;
            newRewrite[i3] = 0;
        }
        int i4 = rewriteIndex;
        int rewriteIndex2 = rewriteIndex + 1;
        newRewrite[i4] = -1;
        int rewriteIndex3 = rewriteIndex2 + 1;
        newRewrite[rewriteIndex2] = -1;
        int rewriteIndex4 = rewriteIndex3 + 1;
        newRewrite[rewriteIndex3] = -1;
        int rewriteIndex5 = rewriteIndex4 + 1;
        newRewrite[rewriteIndex4] = -1;
        setRewrite4Bytes(case_values.length, rewriteIndex5, newRewrite);
        int rewriteIndex6 = rewriteIndex5 + 4;
        for (int case_value : case_values) {
            setRewrite4Bytes(case_value, rewriteIndex6, newRewrite);
            int rewriteIndex7 = rewriteIndex6 + 4;
            int rewriteIndex8 = rewriteIndex7 + 1;
            newRewrite[rewriteIndex7] = -1;
            int rewriteIndex9 = rewriteIndex8 + 1;
            newRewrite[rewriteIndex8] = -1;
            int rewriteIndex10 = rewriteIndex9 + 1;
            newRewrite[rewriteIndex9] = -1;
            rewriteIndex6 = rewriteIndex10 + 1;
            newRewrite[rewriteIndex10] = -1;
        }
        byteCode.setRewrite(newRewrite);
    }
}
