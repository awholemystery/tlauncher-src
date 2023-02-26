package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/NewAttribute.class */
public class NewAttribute extends BCIRenumberedAttribute {
    private final List<Integer> lengths;
    private final List<Object> body;
    private ClassConstantPool pool;
    private final int layoutIndex;

    public NewAttribute(CPUTF8 attributeName, int layoutIndex) {
        super(attributeName);
        this.lengths = new ArrayList();
        this.body = new ArrayList();
        this.layoutIndex = layoutIndex;
    }

    public int getLayoutIndex() {
        return this.layoutIndex;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected int getLength() {
        int length = 0;
        for (Integer len : this.lengths) {
            length += len.intValue();
        }
        return length;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected void writeBody(DataOutputStream dos) throws IOException {
        for (int i = 0; i < this.lengths.size(); i++) {
            int length = this.lengths.get(i).intValue();
            Object obj = this.body.get(i);
            long value = 0;
            if (obj instanceof Long) {
                value = ((Long) obj).longValue();
            } else if (obj instanceof ClassFileEntry) {
                value = this.pool.indexOf((ClassFileEntry) obj);
            } else if (obj instanceof BCValue) {
                value = ((BCValue) obj).actualValue;
            }
            if (length == 1) {
                dos.writeByte((int) value);
            } else if (length == 2) {
                dos.writeShort((int) value);
            } else if (length == 4) {
                dos.writeInt((int) value);
            } else if (length == 8) {
                dos.writeLong(value);
            }
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return this.attributeName.underlyingString();
    }

    public void addInteger(int length, long value) {
        this.lengths.add(Integer.valueOf(length));
        this.body.add(Long.valueOf(value));
    }

    public void addBCOffset(int length, int value) {
        this.lengths.add(Integer.valueOf(length));
        this.body.add(new BCOffset(value));
    }

    public void addBCIndex(int length, int value) {
        this.lengths.add(Integer.valueOf(length));
        this.body.add(new BCIndex(value));
    }

    public void addBCLength(int length, int value) {
        this.lengths.add(Integer.valueOf(length));
        this.body.add(new BCLength(value));
    }

    public void addToBody(int length, Object value) {
        this.lengths.add(Integer.valueOf(length));
        this.body.add(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        for (Object element : this.body) {
            if (element instanceof ClassFileEntry) {
                ((ClassFileEntry) element).resolve(pool);
            }
        }
        this.pool = pool;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        int total = 1;
        for (Object element : this.body) {
            if (element instanceof ClassFileEntry) {
                total++;
            }
        }
        ClassFileEntry[] nested = new ClassFileEntry[total];
        nested[0] = getAttributeName();
        int i = 1;
        for (Object element2 : this.body) {
            if (element2 instanceof ClassFileEntry) {
                nested[i] = (ClassFileEntry) element2;
                i++;
            }
        }
        return nested;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/NewAttribute$BCOffset.class */
    private static class BCOffset extends BCValue {
        private final int offset;
        private int index;

        public BCOffset(int offset) {
            super();
            this.offset = offset;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/NewAttribute$BCIndex.class */
    private static class BCIndex extends BCValue {
        private final int index;

        public BCIndex(int index) {
            super();
            this.index = index;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/NewAttribute$BCLength.class */
    private static class BCLength extends BCValue {
        private final int length;

        public BCLength(int length) {
            super();
            this.length = length;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/NewAttribute$BCValue.class */
    private static abstract class BCValue {
        int actualValue;

        private BCValue() {
        }

        public void setActualValue(int value) {
            this.actualValue = value;
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute
    protected int[] getStartPCs() {
        return null;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.BCIRenumberedAttribute
    public void renumber(List<Integer> byteCodeOffsets) {
        if (!this.renumbered) {
            Object previous = null;
            for (Object obj : this.body) {
                if (obj instanceof BCIndex) {
                    BCIndex bcIndex = (BCIndex) obj;
                    bcIndex.setActualValue(byteCodeOffsets.get(bcIndex.index).intValue());
                } else if (obj instanceof BCOffset) {
                    BCOffset bcOffset = (BCOffset) obj;
                    if (previous instanceof BCIndex) {
                        int index = ((BCIndex) previous).index + bcOffset.offset;
                        bcOffset.setIndex(index);
                        bcOffset.setActualValue(byteCodeOffsets.get(index).intValue());
                    } else if (previous instanceof BCOffset) {
                        int index2 = ((BCOffset) previous).index + bcOffset.offset;
                        bcOffset.setIndex(index2);
                        bcOffset.setActualValue(byteCodeOffsets.get(index2).intValue());
                    } else {
                        bcOffset.setActualValue(byteCodeOffsets.get(bcOffset.offset).intValue());
                    }
                } else if (obj instanceof BCLength) {
                    BCLength bcLength = (BCLength) obj;
                    BCIndex prevIndex = (BCIndex) previous;
                    int actualLength = byteCodeOffsets.get(prevIndex.index + bcLength.length).intValue() - prevIndex.actualValue;
                    bcLength.setActualValue(actualLength);
                }
                previous = obj;
            }
            this.renumbered = true;
        }
    }
}
