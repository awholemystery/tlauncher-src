package org.apache.commons.compress.harmony.pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPMethodOrField.class */
public class CPMethodOrField extends ConstantPoolEntry implements Comparable {
    private final CPClass className;
    private final CPNameAndType nameAndType;
    private int indexInClass = -1;
    private int indexInClassForConstructor = -1;

    public CPMethodOrField(CPClass className, CPNameAndType nameAndType) {
        this.className = className;
        this.nameAndType = nameAndType;
    }

    public String toString() {
        return this.className + ": " + this.nameAndType;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        if (obj instanceof CPMethodOrField) {
            CPMethodOrField mof = (CPMethodOrField) obj;
            int compareName = this.className.compareTo(mof.className);
            if (compareName == 0) {
                return this.nameAndType.compareTo(mof.nameAndType);
            }
            return compareName;
        }
        return 0;
    }

    public int getClassIndex() {
        return this.className.getIndex();
    }

    public CPClass getClassName() {
        return this.className;
    }

    public int getDescIndex() {
        return this.nameAndType.getIndex();
    }

    public CPNameAndType getDesc() {
        return this.nameAndType;
    }

    public int getIndexInClass() {
        return this.indexInClass;
    }

    public void setIndexInClass(int index) {
        this.indexInClass = index;
    }

    public int getIndexInClassForConstructor() {
        return this.indexInClassForConstructor;
    }

    public void setIndexInClassForConstructor(int index) {
        this.indexInClassForConstructor = index;
    }
}
