package org.apache.log4j.helpers;

import java.util.Hashtable;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/helpers/ThreadLocalMap.class */
public final class ThreadLocalMap extends InheritableThreadLocal {
    @Override // java.lang.InheritableThreadLocal
    public final Object childValue(Object parentValue) {
        Hashtable ht = (Hashtable) parentValue;
        if (ht != null) {
            return ht.clone();
        }
        return null;
    }
}
