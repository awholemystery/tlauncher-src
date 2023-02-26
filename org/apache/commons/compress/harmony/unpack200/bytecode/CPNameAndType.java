package org.apache.commons.compress.harmony.unpack200.bytecode;

import ch.qos.logback.core.joran.action.Action;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.compress.harmony.unpack200.SegmentUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPNameAndType.class */
public class CPNameAndType extends ConstantPoolEntry {
    CPUTF8 descriptor;
    transient int descriptorIndex;
    CPUTF8 name;
    transient int nameIndex;
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPNameAndType(CPUTF8 name, CPUTF8 descriptor, int globalIndex) {
        super((byte) 12, globalIndex);
        this.name = (CPUTF8) Objects.requireNonNull(name, Action.NAME_ATTRIBUTE);
        this.descriptor = (CPUTF8) Objects.requireNonNull(descriptor, "descriptor");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        return new ClassFileEntry[]{this.name, this.descriptor};
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        this.descriptorIndex = pool.indexOf(this.descriptor);
        this.nameIndex = pool.indexOf(this.name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry
    public void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(this.nameIndex);
        dos.writeShort(this.descriptorIndex);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "NameAndType: " + this.name + "(" + this.descriptor + ")";
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        int result = (31 * 1) + this.descriptor.hashCode();
        this.cachedHashCode = (31 * result) + this.name.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CPNameAndType other = (CPNameAndType) obj;
        if (!this.descriptor.equals(other.descriptor) || !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public int invokeInterfaceCount() {
        return 1 + SegmentUtils.countInvokeInterfaceArgs(this.descriptor.underlyingString());
    }
}
