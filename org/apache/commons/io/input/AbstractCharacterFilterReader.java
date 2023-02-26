package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.IntPredicate;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/AbstractCharacterFilterReader.class */
public abstract class AbstractCharacterFilterReader extends FilterReader {
    protected static final IntPredicate SKIP_NONE = ch2 -> {
        return false;
    };
    private final IntPredicate skip;

    protected AbstractCharacterFilterReader(Reader reader) {
        this(reader, SKIP_NONE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractCharacterFilterReader(Reader reader, IntPredicate skip) {
        super(reader);
        this.skip = skip == null ? SKIP_NONE : skip;
    }

    protected boolean filter(int ch2) {
        return this.skip.test(ch2);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch2;
        do {
            ch2 = this.in.read();
            if (ch2 == -1) {
                break;
            }
        } while (filter(ch2));
        return ch2;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = super.read(cbuf, off, len);
        if (read == -1) {
            return -1;
        }
        int pos = off - 1;
        for (int readPos = off; readPos < off + read; readPos++) {
            if (!filter(cbuf[readPos])) {
                pos++;
                if (pos < readPos) {
                    cbuf[pos] = cbuf[readPos];
                }
            }
        }
        return (pos - off) + 1;
    }
}
