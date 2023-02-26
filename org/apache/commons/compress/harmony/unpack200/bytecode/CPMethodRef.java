package org.apache.commons.compress.harmony.unpack200.bytecode;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPMethodRef.class */
public class CPMethodRef extends CPRef {
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPMethodRef(CPClass className, CPNameAndType descriptor, int globalIndex) {
        super((byte) 10, className, descriptor, globalIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.CPRef, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        return new ClassFileEntry[]{this.className, this.nameAndType};
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
