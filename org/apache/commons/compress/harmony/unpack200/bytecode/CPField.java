package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPField.class */
public class CPField extends CPMember {
    public CPField(CPUTF8 name, CPUTF8 descriptor, long flags, List<Attribute> attributes) {
        super(name, descriptor, flags, attributes);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.CPMember, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "Field: " + this.name + "(" + this.descriptor + ")";
    }
}
