package org.apache.commons.compress.harmony.unpack200.bytecode;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/AnnotationsAttribute.class */
public abstract class AnnotationsAttribute extends Attribute {

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/AnnotationsAttribute$Annotation.class */
    public static class Annotation {
        private final int num_pairs;
        private final CPUTF8[] element_names;
        private final ElementValue[] element_values;
        private final CPUTF8 type;
        private int type_index;
        private int[] name_indexes;

        public Annotation(int num_pairs, CPUTF8 type, CPUTF8[] element_names, ElementValue[] element_values) {
            this.num_pairs = num_pairs;
            this.type = type;
            this.element_names = element_names;
            this.element_values = element_values;
        }

        public int getLength() {
            int length = 4;
            for (int i = 0; i < this.num_pairs; i++) {
                length = length + 2 + this.element_values[i].getLength();
            }
            return length;
        }

        public void resolve(ClassConstantPool pool) {
            this.type.resolve(pool);
            this.type_index = pool.indexOf(this.type);
            this.name_indexes = new int[this.num_pairs];
            for (int i = 0; i < this.element_names.length; i++) {
                this.element_names[i].resolve(pool);
                this.name_indexes[i] = pool.indexOf(this.element_names[i]);
                this.element_values[i].resolve(pool);
            }
        }

        public void writeBody(DataOutputStream dos) throws IOException {
            dos.writeShort(this.type_index);
            dos.writeShort(this.num_pairs);
            for (int i = 0; i < this.num_pairs; i++) {
                dos.writeShort(this.name_indexes[i]);
                this.element_values[i].writeBody(dos);
            }
        }

        public List<Object> getClassFileEntries() {
            List<Object> entries = new ArrayList<>();
            for (int i = 0; i < this.element_names.length; i++) {
                entries.add(this.element_names[i]);
                entries.addAll(this.element_values[i].getClassFileEntries());
            }
            entries.add(this.type);
            return entries;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/AnnotationsAttribute$ElementValue.class */
    public static class ElementValue {
        private final Object value;
        private final int tag;
        private int constant_value_index = -1;

        public ElementValue(int tag, Object value) {
            this.tag = tag;
            this.value = value;
        }

        public List<Object> getClassFileEntries() {
            List<Object> entries = new ArrayList<>(1);
            if (this.value instanceof CPNameAndType) {
                entries.add(((CPNameAndType) this.value).name);
                entries.add(((CPNameAndType) this.value).descriptor);
            } else if (this.value instanceof ClassFileEntry) {
                entries.add(this.value);
            } else if (this.value instanceof ElementValue[]) {
                ElementValue[] values = (ElementValue[]) this.value;
                for (ElementValue value2 : values) {
                    entries.addAll(value2.getClassFileEntries());
                }
            } else if (this.value instanceof Annotation) {
                entries.addAll(((Annotation) this.value).getClassFileEntries());
            }
            return entries;
        }

        public void resolve(ClassConstantPool pool) {
            if (this.value instanceof CPConstant) {
                ((CPConstant) this.value).resolve(pool);
                this.constant_value_index = pool.indexOf((CPConstant) this.value);
            } else if (this.value instanceof CPClass) {
                ((CPClass) this.value).resolve(pool);
                this.constant_value_index = pool.indexOf((CPClass) this.value);
            } else if (this.value instanceof CPUTF8) {
                ((CPUTF8) this.value).resolve(pool);
                this.constant_value_index = pool.indexOf((CPUTF8) this.value);
            } else if (this.value instanceof CPNameAndType) {
                ((CPNameAndType) this.value).resolve(pool);
            } else if (this.value instanceof Annotation) {
                ((Annotation) this.value).resolve(pool);
            } else if (this.value instanceof ElementValue[]) {
                ElementValue[] nestedValues = (ElementValue[]) this.value;
                for (ElementValue nestedValue : nestedValues) {
                    nestedValue.resolve(pool);
                }
            }
        }

        public void writeBody(DataOutputStream dos) throws IOException {
            dos.writeByte(this.tag);
            if (this.constant_value_index != -1) {
                dos.writeShort(this.constant_value_index);
            } else if (this.value instanceof CPNameAndType) {
                ((CPNameAndType) this.value).writeBody(dos);
            } else if (this.value instanceof Annotation) {
                ((Annotation) this.value).writeBody(dos);
            } else if (this.value instanceof ElementValue[]) {
                ElementValue[] nestedValues = (ElementValue[]) this.value;
                dos.writeShort(nestedValues.length);
                for (ElementValue nestedValue : nestedValues) {
                    nestedValue.writeBody(dos);
                }
            } else {
                throw new Error(CoreConstants.EMPTY_STRING);
            }
        }

        public int getLength() {
            switch (this.tag) {
                case 64:
                    return 1 + ((Annotation) this.value).getLength();
                case 65:
                case 69:
                case 71:
                case 72:
                case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
                case 76:
                case TarConstants.LF_MULTIVOLUME /* 77 */:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 100:
                case HttpStatus.SC_PROCESSING /* 102 */:
                case TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER /* 103 */:
                case SyslogConstants.LOG_AUDIT /* 104 */:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case SyslogConstants.LOG_ALERT /* 112 */:
                case 113:
                case 114:
                default:
                    return 0;
                case 66:
                case 67:
                case 68:
                case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
                case 73:
                case 74:
                case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
                case 90:
                case 99:
                case 115:
                    return 3;
                case 91:
                    int length = 3;
                    ElementValue[] nestedValues = (ElementValue[]) this.value;
                    for (ElementValue nestedValue : nestedValues) {
                        length += nestedValue.getLength();
                    }
                    return length;
                case HttpStatus.SC_SWITCHING_PROTOCOLS /* 101 */:
                    return 5;
            }
        }
    }

    public AnnotationsAttribute(CPUTF8 attributeName) {
        super(attributeName);
    }
}
