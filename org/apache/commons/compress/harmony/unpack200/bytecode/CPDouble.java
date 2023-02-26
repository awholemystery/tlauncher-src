package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPDouble.class */
public class CPDouble extends CPConstantNumber {
    public CPDouble(Double value, int globalIndex) {
        super((byte) 6, value, globalIndex);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeDouble(getNumber().doubleValue());
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "Double: " + getValue();
    }
}
