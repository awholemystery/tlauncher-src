package org.apache.log4j.lf5;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/lf5/PassingLogRecordFilter.class */
public class PassingLogRecordFilter implements LogRecordFilter {
    @Override // org.apache.log4j.lf5.LogRecordFilter
    public boolean passes(LogRecord record) {
        return true;
    }

    public void reset() {
    }
}
