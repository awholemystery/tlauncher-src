package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import org.apache.commons.compress.harmony.pack200.Segment;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttribute.class */
public class NewAttribute extends Attribute {
    private boolean contextClass;
    private boolean contextMethod;
    private boolean contextField;
    private boolean contextCode;
    private final String layout;
    private byte[] contents;
    private int codeOff;
    private Label[] labels;
    private ClassReader classReader;
    private char[] buf;

    public NewAttribute(String type, String layout, int context) {
        super(type);
        this.contextClass = false;
        this.contextMethod = false;
        this.contextField = false;
        this.contextCode = false;
        this.layout = layout;
        addContext(context);
    }

    public NewAttribute(ClassReader classReader, String type, String layout, byte[] contents, char[] buf, int codeOff, Label[] labels) {
        super(type);
        this.contextClass = false;
        this.contextMethod = false;
        this.contextField = false;
        this.contextCode = false;
        this.classReader = classReader;
        this.contents = contents;
        this.layout = layout;
        this.codeOff = codeOff;
        this.labels = labels;
        this.buf = buf;
    }

    public void addContext(int context) {
        switch (context) {
            case 0:
                this.contextClass = true;
                return;
            case 1:
                this.contextField = true;
                return;
            case 2:
                this.contextMethod = true;
                return;
            case 3:
                this.contextCode = true;
                return;
            default:
                return;
        }
    }

    public boolean isContextClass() {
        return this.contextClass;
    }

    public boolean isContextMethod() {
        return this.contextMethod;
    }

    public boolean isContextField() {
        return this.contextField;
    }

    public boolean isContextCode() {
        return this.contextCode;
    }

    public String getLayout() {
        return this.layout;
    }

    public boolean isUnknown() {
        return false;
    }

    public boolean isCodeAttribute() {
        return this.codeOff != -1;
    }

    protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
        byte[] attributeContents = new byte[len];
        System.arraycopy(cr.b, off, attributeContents, 0, len);
        return new NewAttribute(cr, this.type, this.layout, attributeContents, buf, codeOff, labels);
    }

    public boolean isUnknown(int context) {
        switch (context) {
            case 0:
                return !this.contextClass;
            case 1:
                return !this.contextField;
            case 2:
                return !this.contextMethod;
            case 3:
                return !this.contextCode;
            default:
                return false;
        }
    }

    public String readUTF8(int index) {
        return this.classReader.readUTF8(index, this.buf);
    }

    public String readClass(int index) {
        return this.classReader.readClass(index, this.buf);
    }

    public Object readConst(int index) {
        return this.classReader.readConst(index, this.buf);
    }

    public byte[] getBytes() {
        return this.contents;
    }

    public Label getLabel(int index) {
        return this.labels[index];
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttribute$ErrorAttribute.class */
    public static class ErrorAttribute extends NewAttribute {
        public ErrorAttribute(String type, int context) {
            super(type, CoreConstants.EMPTY_STRING, context);
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttribute
        protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
            throw new Error("Attribute " + this.type + " was found");
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttribute$StripAttribute.class */
    public static class StripAttribute extends NewAttribute {
        public StripAttribute(String type, int context) {
            super(type, CoreConstants.EMPTY_STRING, context);
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttribute
        protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
            return null;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttribute$PassAttribute.class */
    public static class PassAttribute extends NewAttribute {
        public PassAttribute(String type, int context) {
            super(type, CoreConstants.EMPTY_STRING, context);
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttribute
        protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
            throw new Segment.PassException();
        }
    }
}
