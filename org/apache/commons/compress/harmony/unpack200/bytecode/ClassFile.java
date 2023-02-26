package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/ClassFile.class */
public class ClassFile {
    public int major;
    public int minor;
    private final int magic = -889275714;
    public ClassConstantPool pool = new ClassConstantPool();
    public int accessFlags;
    public int thisClass;
    public int superClass;
    public int[] interfaces;
    public ClassFileEntry[] fields;
    public ClassFileEntry[] methods;
    public Attribute[] attributes;

    public void write(DataOutputStream dos) throws IOException {
        int[] iArr;
        ClassFileEntry[] classFileEntryArr;
        ClassFileEntry[] classFileEntryArr2;
        Attribute[] attributeArr;
        dos.writeInt(-889275714);
        dos.writeShort(this.minor);
        dos.writeShort(this.major);
        dos.writeShort(this.pool.size() + 1);
        int i = 1;
        while (i <= this.pool.size()) {
            ConstantPoolEntry entry = (ConstantPoolEntry) this.pool.get(i);
            entry.doWrite(dos);
            if (entry.getTag() == 6 || entry.getTag() == 5) {
                i++;
            }
            i++;
        }
        dos.writeShort(this.accessFlags);
        dos.writeShort(this.thisClass);
        dos.writeShort(this.superClass);
        dos.writeShort(this.interfaces.length);
        for (int element : this.interfaces) {
            dos.writeShort(element);
        }
        dos.writeShort(this.fields.length);
        for (ClassFileEntry field : this.fields) {
            field.write(dos);
        }
        dos.writeShort(this.methods.length);
        for (ClassFileEntry method : this.methods) {
            method.write(dos);
        }
        dos.writeShort(this.attributes.length);
        for (Attribute attribute : this.attributes) {
            attribute.write(dos);
        }
    }
}
