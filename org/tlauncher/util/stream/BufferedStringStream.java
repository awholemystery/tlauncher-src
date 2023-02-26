package org.tlauncher.util.stream;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/stream/BufferedStringStream.class */
public class BufferedStringStream extends StringStream {
    @Override // org.tlauncher.util.stream.StringStream
    public void write(char b) {
        super.write(b);
        if (b == '\n') {
            flush();
        }
    }
}
