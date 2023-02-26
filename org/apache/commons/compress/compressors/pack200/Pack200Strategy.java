package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/pack200/Pack200Strategy.class */
public enum Pack200Strategy {
    IN_MEMORY { // from class: org.apache.commons.compress.compressors.pack200.Pack200Strategy.1
        @Override // org.apache.commons.compress.compressors.pack200.Pack200Strategy
        StreamBridge newStreamBridge() {
            return new InMemoryCachingStreamBridge();
        }
    },
    TEMP_FILE { // from class: org.apache.commons.compress.compressors.pack200.Pack200Strategy.2
        @Override // org.apache.commons.compress.compressors.pack200.Pack200Strategy
        StreamBridge newStreamBridge() throws IOException {
            return new TempFileCachingStreamBridge();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract StreamBridge newStreamBridge() throws IOException;
}
