package org.apache.commons.compress.harmony.pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPClass.class */
public class CPClass extends CPConstant<CPClass> {
    private final String className;
    private final CPUTF8 utf8;
    private final boolean isInnerClass;

    public CPClass(CPUTF8 utf8) {
        this.utf8 = utf8;
        this.className = utf8.getUnderlyingString();
        char[] chars = this.className.toCharArray();
        for (char element : chars) {
            if (element <= '-') {
                this.isInnerClass = true;
                return;
            }
        }
        this.isInnerClass = false;
    }

    @Override // java.lang.Comparable
    public int compareTo(CPClass arg0) {
        return this.className.compareTo(arg0.className);
    }

    public String toString() {
        return this.className;
    }

    public int getIndexInCpUtf8() {
        return this.utf8.getIndex();
    }

    public boolean isInnerClass() {
        return this.isInnerClass;
    }
}
