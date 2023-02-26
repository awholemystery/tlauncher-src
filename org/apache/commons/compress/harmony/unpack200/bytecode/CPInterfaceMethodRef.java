package org.apache.commons.compress.harmony.unpack200.bytecode;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPInterfaceMethodRef.class */
public class CPInterfaceMethodRef extends CPRef {
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPInterfaceMethodRef(CPClass className, CPNameAndType descriptor, int globalIndex) {
        super((byte) 11, className, descriptor, globalIndex);
    }

    public int invokeInterfaceCount() {
        return this.nameAndType.invokeInterfaceCount();
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        int result = (31 * 1) + this.className.hashCode();
        this.cachedHashCode = (31 * result) + this.nameAndType.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }
}
