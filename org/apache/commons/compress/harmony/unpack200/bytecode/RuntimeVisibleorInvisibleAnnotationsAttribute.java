package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/RuntimeVisibleorInvisibleAnnotationsAttribute.class */
public class RuntimeVisibleorInvisibleAnnotationsAttribute extends AnnotationsAttribute {
    private final int num_annotations;
    private final AnnotationsAttribute.Annotation[] annotations;

    public RuntimeVisibleorInvisibleAnnotationsAttribute(CPUTF8 name, AnnotationsAttribute.Annotation[] annotations) {
        super(name);
        this.num_annotations = annotations.length;
        this.annotations = annotations;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected int getLength() {
        int length = 2;
        for (int i = 0; i < this.num_annotations; i++) {
            length += this.annotations[i].getLength();
        }
        return length;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        AnnotationsAttribute.Annotation[] annotationArr;
        super.resolve(pool);
        for (AnnotationsAttribute.Annotation annotation : this.annotations) {
            annotation.resolve(pool);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected void writeBody(DataOutputStream dos) throws IOException {
        int size = dos.size();
        dos.writeShort(this.num_annotations);
        for (int i = 0; i < this.num_annotations; i++) {
            this.annotations[i].writeBody(dos);
        }
        if (dos.size() - size != getLength()) {
            throw new Error();
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return this.attributeName.underlyingString() + ": " + this.num_annotations + " annotations";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        AnnotationsAttribute.Annotation[] annotationArr;
        List<Object> nested = new ArrayList<>();
        nested.add(this.attributeName);
        for (AnnotationsAttribute.Annotation annotation : this.annotations) {
            nested.addAll(annotation.getClassFileEntries());
        }
        return (ClassFileEntry[]) nested.toArray(ClassFileEntry.NONE);
    }
}
