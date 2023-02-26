package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/RuntimeVisibleorInvisibleParameterAnnotationsAttribute.class */
public class RuntimeVisibleorInvisibleParameterAnnotationsAttribute extends AnnotationsAttribute {
    private final int num_parameters;
    private final ParameterAnnotation[] parameter_annotations;

    public RuntimeVisibleorInvisibleParameterAnnotationsAttribute(CPUTF8 name, ParameterAnnotation[] parameter_annotations) {
        super(name);
        this.num_parameters = parameter_annotations.length;
        this.parameter_annotations = parameter_annotations;
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected int getLength() {
        int length = 1;
        for (int i = 0; i < this.num_parameters; i++) {
            length += this.parameter_annotations[i].getLength();
        }
        return length;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public void resolve(ClassConstantPool pool) {
        ParameterAnnotation[] parameterAnnotationArr;
        super.resolve(pool);
        for (ParameterAnnotation parameter_annotation : this.parameter_annotations) {
            parameter_annotation.resolve(pool);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute
    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeByte(this.num_parameters);
        for (int i = 0; i < this.num_parameters; i++) {
            this.parameter_annotations[i].writeBody(dos);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public String toString() {
        return this.attributeName.underlyingString() + ": " + this.num_parameters + " parameter annotations";
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/RuntimeVisibleorInvisibleParameterAnnotationsAttribute$ParameterAnnotation.class */
    public static class ParameterAnnotation {
        private final AnnotationsAttribute.Annotation[] annotations;
        private final int num_annotations;

        public ParameterAnnotation(AnnotationsAttribute.Annotation[] annotations) {
            this.num_annotations = annotations.length;
            this.annotations = annotations;
        }

        public void writeBody(DataOutputStream dos) throws IOException {
            AnnotationsAttribute.Annotation[] annotationArr;
            dos.writeShort(this.num_annotations);
            for (AnnotationsAttribute.Annotation annotation : this.annotations) {
                annotation.writeBody(dos);
            }
        }

        public void resolve(ClassConstantPool pool) {
            AnnotationsAttribute.Annotation[] annotationArr;
            for (AnnotationsAttribute.Annotation annotation : this.annotations) {
                annotation.resolve(pool);
            }
        }

        public int getLength() {
            AnnotationsAttribute.Annotation[] annotationArr;
            int length = 2;
            for (AnnotationsAttribute.Annotation annotation : this.annotations) {
                length += annotation.getLength();
            }
            return length;
        }

        public List<Object> getClassFileEntries() {
            AnnotationsAttribute.Annotation[] annotationArr;
            List<Object> nested = new ArrayList<>();
            for (AnnotationsAttribute.Annotation annotation : this.annotations) {
                nested.addAll(annotation.getClassFileEntries());
            }
            return nested;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.unpack200.bytecode.Attribute, org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry
    public ClassFileEntry[] getNestedClassFileEntries() {
        ParameterAnnotation[] parameterAnnotationArr;
        List<Object> nested = new ArrayList<>();
        nested.add(this.attributeName);
        for (ParameterAnnotation parameter_annotation : this.parameter_annotations) {
            nested.addAll(parameter_annotation.getClassFileEntries());
        }
        return (ClassFileEntry[]) nested.toArray(ClassFileEntry.NONE);
    }
}
