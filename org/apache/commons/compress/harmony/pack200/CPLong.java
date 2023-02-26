package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/CPLong.class */
public class CPLong extends CPConstant<CPLong> {
    private final long theLong;

    public CPLong(long theLong) {
        this.theLong = theLong;
    }

    @Override // java.lang.Comparable
    public int compareTo(CPLong obj) {
        return Long.compare(this.theLong, obj.theLong);
    }

    public long getLong() {
        return this.theLong;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + this.theLong;
    }
}
