package org.apache.commons.io.output;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/NullAppendable.class */
public class NullAppendable implements Appendable {
    public static final NullAppendable INSTANCE = new NullAppendable();

    private NullAppendable() {
    }

    @Override // java.lang.Appendable
    public Appendable append(char c) throws IOException {
        return this;
    }

    @Override // java.lang.Appendable
    public Appendable append(CharSequence csq) throws IOException {
        return this;
    }

    @Override // java.lang.Appendable
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return this;
    }
}
