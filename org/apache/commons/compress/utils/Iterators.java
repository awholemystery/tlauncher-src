package org.apache.commons.compress.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/Iterators.class */
public class Iterators {
    public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> iterator) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(iterator);
        boolean z = false;
        while (true) {
            boolean wasModified = z;
            if (iterator.hasNext()) {
                z = wasModified | collection.add(iterator.next());
            } else {
                return wasModified;
            }
        }
    }

    private Iterators() {
    }
}
