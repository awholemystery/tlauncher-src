package org.apache.commons.io;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/StreamIterator.class */
class StreamIterator<E> implements Iterator<E>, Closeable {
    private final Iterator<E> iterator;
    private final Stream<E> stream;

    public static <T> Iterator<T> iterator(Stream<T> stream) {
        return (Iterator<E>) new StreamIterator(stream).iterator;
    }

    private StreamIterator(Stream<E> stream) {
        this.stream = (Stream) Objects.requireNonNull(stream, "stream");
        this.iterator = stream.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        boolean hasNext = this.iterator.hasNext();
        if (!hasNext) {
            close();
        }
        return hasNext;
    }

    @Override // java.util.Iterator
    public E next() {
        E next = this.iterator.next();
        if (next == null) {
            close();
        }
        return next;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.stream.close();
    }
}
