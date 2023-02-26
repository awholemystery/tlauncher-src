package org.apache.commons.compress.harmony.pack200;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPSignature.class */
public class CPSignature extends ConstantPoolEntry implements Comparable {
    private final CPUTF8 signatureForm;
    private final List<CPClass> classes;
    private final String signature;
    private final boolean formStartsWithBracket;

    public CPSignature(String signature, CPUTF8 signatureForm, List<CPClass> classes) {
        this.signature = signature;
        this.signatureForm = signatureForm;
        this.classes = classes;
        this.formStartsWithBracket = signatureForm.toString().startsWith("(");
    }

    @Override // java.lang.Comparable
    public int compareTo(Object arg0) {
        if (this.signature.equals(((CPSignature) arg0).signature)) {
            return 0;
        }
        if (this.formStartsWithBracket && !((CPSignature) arg0).formStartsWithBracket) {
            return 1;
        }
        if (((CPSignature) arg0).formStartsWithBracket && !this.formStartsWithBracket) {
            return -1;
        }
        if (this.classes.size() - ((CPSignature) arg0).classes.size() != 0) {
            return this.classes.size() - ((CPSignature) arg0).classes.size();
        }
        if (this.classes.size() > 0) {
            for (int i = this.classes.size() - 1; i >= 0; i--) {
                CPClass cpClass = this.classes.get(i);
                CPClass compareClass = ((CPSignature) arg0).classes.get(i);
                int classComp = cpClass.compareTo(compareClass);
                if (classComp != 0) {
                    return classComp;
                }
            }
        }
        return this.signature.compareTo(((CPSignature) arg0).signature);
    }

    public int getIndexInCpUtf8() {
        return this.signatureForm.getIndex();
    }

    public List<CPClass> getClasses() {
        return this.classes;
    }

    public String toString() {
        return this.signature;
    }

    public String getUnderlyingString() {
        return this.signature;
    }

    public CPUTF8 getSignatureForm() {
        return this.signatureForm;
    }
}
