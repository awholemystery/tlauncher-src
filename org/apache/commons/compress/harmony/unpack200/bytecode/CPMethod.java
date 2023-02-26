package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPMethod.class */
public class CPMethod extends CPMember {
    private boolean hashcodeComputed;
    private int cachedHashCode;

    public CPMethod(CPUTF8 name, CPUTF8 descriptor, long flags, List<Attribute> attributes) {
        super(name, descriptor, flags, attributes);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.CPMember, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "Method: " + this.name + "(" + this.descriptor + ")";
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        int result = (31 * 1) + this.name.hashCode();
        this.cachedHashCode = (31 * result) + this.descriptor.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.CPMember, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }
}
