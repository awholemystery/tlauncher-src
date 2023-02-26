package org.apache.http;

import java.util.Iterator;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HeaderElementIterator.class */
public interface HeaderElementIterator extends Iterator<Object> {
    @Override // java.util.Iterator
    boolean hasNext();

    HeaderElement nextElement();
}
