package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Collection;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/TeeWriter.class */
public class TeeWriter extends ProxyCollectionWriter {
    public TeeWriter(Collection<Writer> writers) {
        super(writers);
    }

    public TeeWriter(Writer... writers) {
        super(writers);
    }
}
