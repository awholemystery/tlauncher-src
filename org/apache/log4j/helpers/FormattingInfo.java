package org.apache.log4j.helpers;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/helpers/FormattingInfo.class */
public class FormattingInfo {
    int min = -1;
    int max = Integer.MAX_VALUE;
    boolean leftAlign = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        this.min = -1;
        this.max = Integer.MAX_VALUE;
        this.leftAlign = false;
    }

    void dump() {
        LogLog.debug(new StringBuffer().append("min=").append(this.min).append(", max=").append(this.max).append(", leftAlign=").append(this.leftAlign).toString());
    }
}
