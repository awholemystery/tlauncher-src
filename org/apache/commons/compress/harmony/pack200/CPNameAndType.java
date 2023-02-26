package org.apache.commons.compress.harmony.pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPNameAndType.class */
public class CPNameAndType extends ConstantPoolEntry implements Comparable {
    private final CPUTF8 name;
    private final CPSignature signature;

    public CPNameAndType(CPUTF8 name, CPSignature signature) {
        this.name = name;
        this.signature = signature;
    }

    public String toString() {
        return this.name + ":" + this.signature;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        if (obj instanceof CPNameAndType) {
            CPNameAndType nat = (CPNameAndType) obj;
            int compareSignature = this.signature.compareTo(nat.signature);
            if (compareSignature == 0) {
                return this.name.compareTo(nat.name);
            }
            return compareSignature;
        }
        return 0;
    }

    public int getNameIndex() {
        return this.name.getIndex();
    }

    public String getName() {
        return this.name.getUnderlyingString();
    }

    public int getTypeIndex() {
        return this.signature.getIndex();
    }
}
