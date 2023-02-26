package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/ThisFieldRefForm.class */
public class ThisFieldRefForm extends ClassSpecificReferenceForm {
    public ThisFieldRefForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ClassSpecificReferenceForm, org.apache.commons.compress.harmony.unpack200.bytecode.forms.ReferenceForm
    protected int getOffset(OperandManager operandManager) {
        return operandManager.nextThisFieldRef();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ClassSpecificReferenceForm, org.apache.commons.compress.harmony.unpack200.bytecode.forms.ReferenceForm
    protected int getPoolID() {
        return 10;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ClassSpecificReferenceForm
    protected String context(OperandManager operandManager) {
        return operandManager.getCurrentClass();
    }
}
