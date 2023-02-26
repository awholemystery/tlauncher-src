package org.apache.log4j;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* compiled from: PropertyConfigurator.java */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/SortedKeyEnumeration.class */
class SortedKeyEnumeration implements Enumeration {
    private Enumeration e;

    public SortedKeyEnumeration(Hashtable ht) {
        Enumeration f = ht.keys();
        Vector keys = new Vector(ht.size());
        int last = 0;
        while (f.hasMoreElements()) {
            String key = (String) f.nextElement();
            int i = 0;
            while (i < last) {
                String s = (String) keys.get(i);
                if (key.compareTo(s) <= 0) {
                    break;
                }
                i++;
            }
            keys.add(i, key);
            last++;
        }
        this.e = keys.elements();
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.e.hasMoreElements();
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        return this.e.nextElement();
    }
}
