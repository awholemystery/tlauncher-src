package org.apache.commons.compress.harmony.unpack200.bytecode;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/CPConstantNumber.class */
public abstract class CPConstantNumber extends CPConstant {
    public CPConstantNumber(byte tag, Object value, int globalIndex) {
        super(tag, value, globalIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Number getNumber() {
        return (Number) getValue();
    }
}
