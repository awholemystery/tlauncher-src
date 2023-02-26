package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPRef.class */
public abstract class CPRef extends ConstantPoolEntry {
    CPClass className;
    transient int classNameIndex;
    protected CPNameAndType nameAndType;
    transient int nameAndTypeIndex;
    protected String cachedToString;

    public CPRef(byte type, CPClass className, CPNameAndType descriptor, int globalIndex) {
        super(type, globalIndex);
        this.className = (CPClass) Objects.requireNonNull(className, "className");
        this.nameAndType = (CPNameAndType) Objects.requireNonNull(descriptor, "descriptor");
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || hashCode() != obj.hashCode()) {
            return false;
        }
        CPRef other = (CPRef) obj;
        if (!this.className.equals(other.className) || !this.nameAndType.equals(other.nameAndType)) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        ClassFileEntry[] entries = {this.className, this.nameAndType};
        return entries;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        this.nameAndTypeIndex = pool.indexOf(this.nameAndType);
        this.classNameIndex = pool.indexOf(this.className);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        String type;
        if (this.cachedToString == null) {
            if (getTag() == 9) {
                type = "FieldRef";
            } else if (getTag() == 10) {
                type = "MethoddRef";
            } else if (getTag() == 11) {
                type = "InterfaceMethodRef";
            } else {
                type = "unknown";
            }
            this.cachedToString = type + ": " + this.className + "#" + this.nameAndType;
        }
        return this.cachedToString;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(this.classNameIndex);
        dos.writeShort(this.nameAndTypeIndex);
    }
}
