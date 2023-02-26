package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.SegmentConstantPool;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/NewClassRefForm.class */
public class NewClassRefForm extends ClassRefForm {
    public NewClassRefForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [int[], int[][]] */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ReferenceForm, org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int offset = getOffset(operandManager);
        if (offset == 0) {
            SegmentConstantPool globalPool = operandManager.globalConstantPool();
            ClassFileEntry[] nested = {globalPool.getClassPoolEntry(operandManager.getCurrentClass())};
            byteCode.setNested(nested);
            byteCode.setNestedPositions(new int[]{new int[]{0, 2}});
        } else {
            try {
                setNestedEntries(byteCode, operandManager, offset);
            } catch (Pack200Exception e) {
                throw new Error("Got a pack200 exception. What to do?");
            }
        }
        operandManager.setNewClass(((CPClass) byteCode.getNestedClassFileEntries()[0]).getName());
    }
}
