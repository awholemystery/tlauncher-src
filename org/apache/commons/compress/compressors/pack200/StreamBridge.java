package org.apache.commons.compress.compressors.pack200;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/pack200/StreamBridge.class */
abstract class StreamBridge extends FilterOutputStream {
    private InputStream input;
    private final Object inputLock;

    abstract InputStream getInputView() throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public StreamBridge(OutputStream out) {
        super(out);
        this.inputLock = new Object();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StreamBridge() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputStream getInput() throws IOException {
        synchronized (this.inputLock) {
            if (this.input == null) {
                this.input = getInputView();
            }
        }
        return this.input;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() throws IOException {
        close();
        synchronized (this.inputLock) {
            if (this.input != null) {
                this.input.close();
                this.input = null;
            }
        }
    }
}
