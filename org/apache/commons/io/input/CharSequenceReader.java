package org.apache.commons.io.input;

import ch.qos.logback.core.CoreConstants;
import java.io.Reader;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/CharSequenceReader.class */
public class CharSequenceReader extends Reader implements Serializable {
    private static final long serialVersionUID = 3724187752191401220L;
    private final CharSequence charSequence;
    private int idx;
    private int mark;
    private final int start;
    private final Integer end;

    public CharSequenceReader(CharSequence charSequence) {
        this(charSequence, 0);
    }

    public CharSequenceReader(CharSequence charSequence, int start) {
        this(charSequence, start, Integer.MAX_VALUE);
    }

    public CharSequenceReader(CharSequence charSequence, int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Start index is less than zero: " + start);
        }
        if (end < start) {
            throw new IllegalArgumentException("End index is less than start " + start + ": " + end);
        }
        this.charSequence = charSequence != null ? charSequence : CoreConstants.EMPTY_STRING;
        this.start = start;
        this.end = Integer.valueOf(end);
        this.idx = start;
        this.mark = start;
    }

    private int start() {
        return Math.min(this.charSequence.length(), this.start);
    }

    private int end() {
        return Math.min(this.charSequence.length(), this.end == null ? Integer.MAX_VALUE : this.end.intValue());
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.idx = this.start;
        this.mark = this.start;
    }

    @Override // java.io.Reader
    public boolean ready() {
        return this.idx < end();
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) {
        this.mark = this.idx;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public int read() {
        if (this.idx >= end()) {
            return -1;
        }
        CharSequence charSequence = this.charSequence;
        int i = this.idx;
        this.idx = i + 1;
        return charSequence.charAt(i);
    }

    @Override // java.io.Reader
    public int read(char[] array, int offset, int length) {
        if (this.idx >= end()) {
            return -1;
        }
        Objects.requireNonNull(array, "array");
        if (length < 0 || offset < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
        }
        if (this.charSequence instanceof String) {
            int count = Math.min(length, end() - this.idx);
            ((String) this.charSequence).getChars(this.idx, this.idx + count, array, offset);
            this.idx += count;
            return count;
        } else if (this.charSequence instanceof StringBuilder) {
            int count2 = Math.min(length, end() - this.idx);
            ((StringBuilder) this.charSequence).getChars(this.idx, this.idx + count2, array, offset);
            this.idx += count2;
            return count2;
        } else if (this.charSequence instanceof StringBuffer) {
            int count3 = Math.min(length, end() - this.idx);
            ((StringBuffer) this.charSequence).getChars(this.idx, this.idx + count3, array, offset);
            this.idx += count3;
            return count3;
        } else {
            int count4 = 0;
            for (int i = 0; i < length; i++) {
                int c = read();
                if (c == -1) {
                    return count4;
                }
                array[offset + i] = (char) c;
                count4++;
            }
            return count4;
        }
    }

    @Override // java.io.Reader
    public void reset() {
        this.idx = this.mark;
    }

    @Override // java.io.Reader
    public long skip(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
        }
        if (this.idx >= end()) {
            return 0L;
        }
        int dest = (int) Math.min(end(), this.idx + n);
        int count = dest - this.idx;
        this.idx = dest;
        return count;
    }

    public String toString() {
        CharSequence subSequence = this.charSequence.subSequence(start(), end());
        return subSequence.toString();
    }
}
