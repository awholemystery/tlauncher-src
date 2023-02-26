package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/BCIRenumberedAttribute.class */
public abstract class BCIRenumberedAttribute extends Attribute {
    protected boolean renumbered;

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected abstract int getLength();

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected abstract void writeBody(DataOutputStream dataOutputStream) throws IOException;

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public abstract String toString();

    protected abstract int[] getStartPCs();

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    public boolean hasBCIRenumbering() {
        return true;
    }

    public BCIRenumberedAttribute(CPUTF8 attributeName) {
        super(attributeName);
    }

    public void renumber(List<Integer> byteCodeOffsets) throws Pack200Exception {
        if (this.renumbered) {
            throw new Error("Trying to renumber a line number table that has already been renumbered");
        }
        this.renumbered = true;
        int[] startPCs = getStartPCs();
        Arrays.setAll(startPCs, i -> {
            return ((Integer) byteCodeOffsets.get(startPCs[i])).intValue();
        });
    }
}
