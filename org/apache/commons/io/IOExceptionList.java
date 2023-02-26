package org.apache.commons.io;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/IOExceptionList.class */
public class IOExceptionList extends IOException {
    private static final long serialVersionUID = 1;
    private final List<? extends Throwable> causeList;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public IOExceptionList(java.util.List<? extends java.lang.Throwable> r8) {
        /*
            r7 = this;
            r0 = r7
            java.lang.String r1 = "%,d exceptions: %s"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = r2
            r4 = 0
            r5 = r8
            if (r5 != 0) goto L11
            r5 = 0
            goto L17
        L11:
            r5 = r8
            int r5 = r5.size()
        L17:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r3[r4] = r5
            r3 = r2
            r4 = 1
            r5 = r8
            r3[r4] = r5
            java.lang.String r1 = java.lang.String.format(r1, r2)
            r2 = r8
            r0.<init>(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOExceptionList.<init>(java.util.List):void");
    }

    public IOExceptionList(String message, List<? extends Throwable> causeList) {
        super(message, (causeList == null || causeList.isEmpty()) ? null : causeList.get(0));
        this.causeList = causeList == null ? Collections.emptyList() : causeList;
    }

    public <T extends Throwable> T getCause(int index) {
        return (T) this.causeList.get(index);
    }

    public <T extends Throwable> T getCause(int index, Class<T> clazz) {
        return clazz.cast(this.causeList.get(index));
    }

    public <T extends Throwable> List<T> getCauseList() {
        return (List<T>) this.causeList;
    }

    public <T extends Throwable> List<T> getCauseList(Class<T> clazz) {
        return (List<T>) this.causeList;
    }
}
