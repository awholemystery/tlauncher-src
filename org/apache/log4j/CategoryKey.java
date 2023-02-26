package org.apache.log4j;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/CategoryKey.class */
class CategoryKey {
    String name;
    int hashCache;
    static Class class$org$apache$log4j$CategoryKey;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CategoryKey(String name) {
        this.name = name;
        this.hashCache = name.hashCode();
    }

    public final int hashCode() {
        return this.hashCache;
    }

    public final boolean equals(Object rArg) {
        Class<?> cls;
        if (this == rArg) {
            return true;
        }
        if (rArg != null) {
            if (class$org$apache$log4j$CategoryKey == null) {
                cls = class$("org.apache.log4j.CategoryKey");
                class$org$apache$log4j$CategoryKey = cls;
            } else {
                cls = class$org$apache$log4j$CategoryKey;
            }
            if (cls == rArg.getClass()) {
                return this.name.equals(((CategoryKey) rArg).name);
            }
            return false;
        }
        return false;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }
}
