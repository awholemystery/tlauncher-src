package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPUTF8.class */
public class CPUTF8 extends ConstantPoolEntry {
    private final String utf8;
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPUTF8(String utf8, int globalIndex) {
        super((byte) 1, globalIndex);
        this.utf8 = (String) Objects.requireNonNull(utf8, "utf8");
    }

    public CPUTF8(String string) {
        this(string, -1);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CPUTF8 other = (CPUTF8) obj;
        return this.utf8.equals(other.utf8);
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        this.cachedHashCode = 31 + this.utf8.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "UTF8: " + this.utf8;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeUTF(this.utf8);
    }

    public String underlyingString() {
        return this.utf8;
    }

    public void setGlobalIndex(int index) {
        this.globalIndex = index;
    }
}
