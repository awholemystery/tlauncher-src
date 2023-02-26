package org.apache.http;

import java.util.Iterator;

/* loaded from: TLauncher-2.876.jar:org/apache/http/TokenIterator.class */
public interface TokenIterator extends Iterator<Object> {
    @Override // java.util.Iterator
    boolean hasNext();

    String nextToken();
}
