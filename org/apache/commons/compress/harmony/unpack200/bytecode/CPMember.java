package org.apache.commons.compress.harmony.unpack200.bytecode;

import ch.qos.logback.core.joran.action.Action;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPMember.class */
public class CPMember extends ClassFileEntry {
    List<Attribute> attributes;
    short flags;
    CPUTF8 name;
    transient int nameIndex;
    protected final CPUTF8 descriptor;
    transient int descriptorIndex;

    public CPMember(CPUTF8 name, CPUTF8 descriptor, long flags, List<Attribute> attributes) {
        this.name = (CPUTF8) Objects.requireNonNull(name, Action.NAME_ATTRIBUTE);
        this.descriptor = (CPUTF8) Objects.requireNonNull(descriptor, "descriptor");
        this.flags = (short) flags;
        this.attributes = attributes == null ? Collections.EMPTY_LIST : attributes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        int attributeCount = this.attributes.size();
        ClassFileEntry[] entries = new ClassFileEntry[attributeCount + 2];
        entries[0] = this.name;
        entries[1] = this.descriptor;
        for (int i = 0; i < attributeCount; i++) {
            entries[i + 2] = this.attributes.get(i);
        }
        return entries;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        this.nameIndex = pool.indexOf(this.name);
        this.descriptorIndex = pool.indexOf(this.descriptor);
        this.attributes.forEach(attribute -> {
            attribute.resolve(pool);
        });
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        int result = (31 * 1) + this.attributes.hashCode();
        return (31 * ((31 * ((31 * result) + this.descriptor.hashCode())) + this.flags)) + this.name.hashCode();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "CPMember: " + this.name + "(" + this.descriptor + ")";
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CPMember other = (CPMember) obj;
        if (!this.attributes.equals(other.attributes) || !this.descriptor.equals(other.descriptor) || this.flags != other.flags || !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    protected void doWrite(DataOutputStream dos) throws IOException {
        dos.writeShort(this.flags);
        dos.writeShort(this.nameIndex);
        dos.writeShort(this.descriptorIndex);
        int attributeCount = this.attributes.size();
        dos.writeShort(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            Attribute attribute = this.attributes.get(i);
            attribute.doWrite(dos);
        }
    }
}
