package org.apache.commons.compress.harmony.unpack200.bytecode.forms;

import java.util.Objects;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.SegmentConstantPool;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/forms/ReferenceForm.class */
public abstract class ReferenceForm extends ByteCodeForm {
    protected abstract int getPoolID();

    protected abstract int getOffset(OperandManager operandManager);

    public ReferenceForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v5, types: [int[], int[][]] */
    public void setNestedEntries(ByteCode byteCode, OperandManager operandManager, int offset) throws Pack200Exception {
        SegmentConstantPool globalPool = operandManager.globalConstantPool();
        ClassFileEntry[] nested = {globalPool.getConstantPoolEntry(getPoolID(), offset)};
        Objects.requireNonNull(nested[0], "Null nested entries are not allowed");
        byteCode.setNested(nested);
        byteCode.setNestedPositions(new int[]{new int[]{0, 2}});
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.forms.ByteCodeForm
    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        int offset = getOffset(operandManager);
        try {
            setNestedEntries(byteCode, operandManager, offset);
        } catch (Pack200Exception e) {
            throw new Error("Got a pack200 exception. What to do?");
        }
    }
}
