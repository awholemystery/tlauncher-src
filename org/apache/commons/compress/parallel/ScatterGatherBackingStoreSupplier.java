package org.apache.commons.compress.parallel;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/parallel/ScatterGatherBackingStoreSupplier.class */
public interface ScatterGatherBackingStoreSupplier {
    ScatterGatherBackingStore get() throws IOException;
}
