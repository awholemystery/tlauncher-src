package org.apache.http;

import java.util.Iterator;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HeaderIterator.class */
public interface HeaderIterator extends Iterator<Object> {
    @Override // java.util.Iterator
    boolean hasNext();

    Header nextHeader();
}
