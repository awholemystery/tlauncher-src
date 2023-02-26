package org.apache.commons.compress.harmony.pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPString.class */
public class CPString extends CPConstant<CPString> {
    private final String string;
    private final CPUTF8 utf8;

    public CPString(CPUTF8 utf8) {
        this.utf8 = utf8;
        this.string = utf8.getUnderlyingString();
    }

    @Override // java.lang.Comparable
    public int compareTo(CPString arg0) {
        return this.string.compareTo(arg0.string);
    }

    public String toString() {
        return this.string;
    }

    public int getIndexInCpUtf8() {
        return this.utf8.getIndex();
    }
}
