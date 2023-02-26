package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/InnerClassesAttribute.class */
public class InnerClassesAttribute extends Attribute {
    private static CPUTF8 attributeName;
    private final List<InnerClassesEntry> innerClasses;
    private final List<ConstantPoolEntry> nestedClassFileEntries;

    public static void setAttributeName(CPUTF8 cpUTF8Value) {
        attributeName = cpUTF8Value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/InnerClassesAttribute$InnerClassesEntry.class */
    public static class InnerClassesEntry {
        CPClass inner_class_info;
        CPClass outer_class_info;
        CPUTF8 inner_class_name;
        int inner_class_info_index = -1;
        int outer_class_info_index = -1;
        int inner_name_index = -1;
        int inner_class_access_flags;

        public InnerClassesEntry(CPClass innerClass, CPClass outerClass, CPUTF8 innerName, int flags) {
            this.inner_class_access_flags = -1;
            this.inner_class_info = innerClass;
            this.outer_class_info = outerClass;
            this.inner_class_name = innerName;
            this.inner_class_access_flags = flags;
        }

        public void resolve(ClassConstantPool pool) {
            if (this.inner_class_info != null) {
                this.inner_class_info.resolve(pool);
                this.inner_class_info_index = pool.indexOf(this.inner_class_info);
            } else {
                this.inner_class_info_index = 0;
            }
            if (this.inner_class_name != null) {
                this.inner_class_name.resolve(pool);
                this.inner_name_index = pool.indexOf(this.inner_class_name);
            } else {
                this.inner_name_index = 0;
            }
            if (this.outer_class_info != null) {
                this.outer_class_info.resolve(pool);
                this.outer_class_info_index = pool.indexOf(this.outer_class_info);
                return;
            }
            this.outer_class_info_index = 0;
        }

        public void write(DataOutputStream dos) throws IOException {
            dos.writeShort(this.inner_class_info_index);
            dos.writeShort(this.outer_class_info_index);
            dos.writeShort(this.inner_name_index);
            dos.writeShort(this.inner_class_access_flags);
        }
    }

    public InnerClassesAttribute(String name) {
        super(attributeName);
        this.innerClasses = new ArrayList();
        this.nestedClassFileEntries = new ArrayList();
        this.nestedClassFileEntries.add(getAttributeName());
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        InnerClassesAttribute other = (InnerClassesAttribute) obj;
        if (getAttributeName() == null) {
            if (other.getAttributeName() != null) {
                return false;
            }
            return true;
        } else if (!getAttributeName().equals(other.getAttributeName())) {
            return false;
        } else {
            return true;
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected int getLength() {
        return 2 + (8 * this.innerClasses.size());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        return (ClassFileEntry[]) this.nestedClassFileEntries.toArray(ClassFileEntry.NONE);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (getAttributeName() == null ? 0 : getAttributeName().hashCode());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        for (InnerClassesEntry entry : this.innerClasses) {
            entry.resolve(pool);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "InnerClasses: " + getAttributeName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void doWrite(DataOutputStream dos) throws IOException {
        super.doWrite(dos);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(this.innerClasses.size());
        for (InnerClassesEntry entry : this.innerClasses) {
            entry.write(dos);
        }
    }

    public void addInnerClassesEntry(CPClass innerClass, CPClass outerClass, CPUTF8 innerName, int flags) {
        if (innerClass != null) {
            this.nestedClassFileEntries.add(innerClass);
        }
        if (outerClass != null) {
            this.nestedClassFileEntries.add(outerClass);
        }
        if (innerName != null) {
            this.nestedClassFileEntries.add(innerName);
        }
        addInnerClassesEntry(new InnerClassesEntry(innerClass, outerClass, innerName, flags));
    }

    private void addInnerClassesEntry(InnerClassesEntry innerClassesEntry) {
        this.innerClasses.add(innerClassesEntry);
    }
}
