package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/ExceptionTableEntry.class */
public class ExceptionTableEntry {
    private final int startPC;
    private final int endPC;
    private final int handlerPC;
    private final CPClass catchType;
    private int startPcRenumbered;
    private int endPcRenumbered;
    private int handlerPcRenumbered;
    private int catchTypeIndex;

    public ExceptionTableEntry(int startPC, int endPC, int handlerPC, CPClass catchType) {
        this.startPC = startPC;
        this.endPC = endPC;
        this.handlerPC = handlerPC;
        this.catchType = catchType;
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeShort(this.startPcRenumbered);
        dos.writeShort(this.endPcRenumbered);
        dos.writeShort(this.handlerPcRenumbered);
        dos.writeShort(this.catchTypeIndex);
    }

    public void renumber(List<Integer> byteCodeOffsets) {
        this.startPcRenumbered = byteCodeOffsets.get(this.startPC).intValue();
        int endPcIndex = this.startPC + this.endPC;
        this.endPcRenumbered = byteCodeOffsets.get(endPcIndex).intValue();
        int handlerPcIndex = endPcIndex + this.handlerPC;
        this.handlerPcRenumbered = byteCodeOffsets.get(handlerPcIndex).intValue();
    }

    public CPClass getCatchType() {
        return this.catchType;
    }

    public void resolve(ClassConstantPool pool) {
        if (this.catchType == null) {
            this.catchTypeIndex = 0;
            return;
        }
        this.catchType.resolve(pool);
        this.catchTypeIndex = pool.indexOf(this.catchType);
    }
}
