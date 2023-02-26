package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPConstant.class */
public abstract class CPConstant extends ConstantPoolEntry {
    private final Object value;

    public CPConstant(byte tag, Object value, int globalIndex) {
        super(tag, globalIndex);
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CPConstant other = (CPConstant) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        } else if (!this.value.equals(other.value)) {
            return false;
        } else {
            return true;
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        int result = (31 * 1) + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object getValue() {
        return this.value;
    }
}
