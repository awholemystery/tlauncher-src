package org.apache.commons.io.output;

import java.io.PrintStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/NullPrintStream.class */
public class NullPrintStream extends PrintStream {
    public static final NullPrintStream NULL_PRINT_STREAM = new NullPrintStream();

    public NullPrintStream() {
        super(NullOutputStream.NULL_OUTPUT_STREAM);
    }
}
