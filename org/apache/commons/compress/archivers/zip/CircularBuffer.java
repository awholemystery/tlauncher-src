package org.apache.commons.compress.archivers.zip;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/CircularBuffer.class */
class CircularBuffer {
    private final int size;
    private final byte[] buffer;
    private int readIndex;
    private int writeIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CircularBuffer(int size) {
        this.size = size;
        this.buffer = new byte[size];
    }

    public boolean available() {
        return this.readIndex != this.writeIndex;
    }

    public void put(int value) {
        this.buffer[this.writeIndex] = (byte) value;
        this.writeIndex = (this.writeIndex + 1) % this.size;
    }

    public int get() {
        if (available()) {
            byte b = this.buffer[this.readIndex];
            this.readIndex = (this.readIndex + 1) % this.size;
            return b & 255;
        }
        return -1;
    }

    public void copy(int distance, int length) {
        int pos1 = this.writeIndex - distance;
        int pos2 = pos1 + length;
        for (int i = pos1; i < pos2; i++) {
            this.buffer[this.writeIndex] = this.buffer[(i + this.size) % this.size];
            this.writeIndex = (this.writeIndex + 1) % this.size;
        }
    }
}
