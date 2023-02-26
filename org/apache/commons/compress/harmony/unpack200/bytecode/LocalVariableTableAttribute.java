package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/LocalVariableTableAttribute.class */
public class LocalVariableTableAttribute extends BCIRenumberedAttribute {
    private final int local_variable_table_length;
    private final int[] start_pcs;
    private final int[] lengths;
    private int[] name_indexes;
    private int[] descriptor_indexes;
    private final int[] indexes;
    private final CPUTF8[] names;
    private final CPUTF8[] descriptors;
    private int codeLength;
    private static CPUTF8 attributeName;

    public static void setAttributeName(CPUTF8 cpUTF8Value) {
        attributeName = cpUTF8Value;
    }

    public LocalVariableTableAttribute(int local_variable_table_length, int[] start_pcs, int[] lengths, CPUTF8[] names, CPUTF8[] descriptors, int[] indexes) {
        super(attributeName);
        this.local_variable_table_length = local_variable_table_length;
        this.start_pcs = start_pcs;
        this.lengths = lengths;
        this.names = names;
        this.descriptors = descriptors;
        this.indexes = indexes;
    }

    public void setCodeLength(int length) {
        this.codeLength = length;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected int getLength() {
        return 2 + (10 * this.local_variable_table_length);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(this.local_variable_table_length);
        for (int i = 0; i < this.local_variable_table_length; i++) {
            dos.writeShort(this.start_pcs[i]);
            dos.writeShort(this.lengths[i]);
            dos.writeShort(this.name_indexes[i]);
            dos.writeShort(this.descriptor_indexes[i]);
            dos.writeShort(this.indexes[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        List<CPUTF8> nestedEntries = new ArrayList<>();
        nestedEntries.add(getAttributeName());
        for (int i = 0; i < this.local_variable_table_length; i++) {
            nestedEntries.add(this.names[i]);
            nestedEntries.add(this.descriptors[i]);
        }
        return (ClassFileEntry[]) nestedEntries.toArray(ClassFileEntry.NONE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        this.name_indexes = new int[this.local_variable_table_length];
        this.descriptor_indexes = new int[this.local_variable_table_length];
        for (int i = 0; i < this.local_variable_table_length; i++) {
            this.names[i].resolve(pool);
            this.descriptors[i].resolve(pool);
            this.name_indexes[i] = pool.indexOf(this.names[i]);
            this.descriptor_indexes[i] = pool.indexOf(this.descriptors[i]);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return "LocalVariableTable: " + this.local_variable_table_length + " variables";
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute
    protected int[] getStartPCs() {
        return this.start_pcs;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute
    public void renumber(List<Integer> byteCodeOffsets) throws Pack200Exception {
        int i;
        int[] unrenumbered_start_pcs = new int[this.start_pcs.length];
        System.arraycopy(this.start_pcs, 0, unrenumbered_start_pcs, 0, this.start_pcs.length);
        super.renumber(byteCodeOffsets);
        int maxSize = this.codeLength;
        for (int index = 0; index < this.lengths.length; index++) {
            int start_pc = this.start_pcs[index];
            int encodedLength = this.lengths[index];
            int indexOfStartPC = unrenumbered_start_pcs[index];
            int stopIndex = indexOfStartPC + encodedLength;
            if (stopIndex < 0) {
                throw new Pack200Exception("Error renumbering bytecode indexes");
            }
            if (stopIndex == byteCodeOffsets.size()) {
                i = maxSize;
            } else {
                int stopValue = byteCodeOffsets.get(stopIndex).intValue();
                i = stopValue;
            }
            int revisedLength = i - start_pc;
            this.lengths[index] = revisedLength;
        }
    }
}
