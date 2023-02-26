package org.apache.commons.compress.utils;

import java.util.Collections;
import java.util.HashSet;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/Sets.class */
public class Sets {
    private Sets() {
    }

    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);
        return set;
    }
}
