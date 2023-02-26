package org.apache.commons.compress.harmony.unpack200.bytecode;

import ch.qos.logback.core.joran.action.Action;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPClass.class */
public class CPClass extends ConstantPoolEntry {
    private int index;
    public String name;
    private final CPUTF8 utf8;
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPClass(CPUTF8 name, int globalIndex) {
        super((byte) 7, globalIndex);
        this.name = ((CPUTF8) Objects.requireNonNull(name, Action.NAME_ATTRIBUTE)).underlyingString();
        this.utf8 = name;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CPClass other = (CPClass) obj;
        return this.utf8.equals(other.utf8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        return new ClassFileEntry[]{this.utf8};
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        this.cachedHashCode = this.utf8.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        this.index = pool.indexOf(this.utf8);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "Class: " + getName();
    }

    public String getName() {
        return this.name;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(this.index);
    }
}
