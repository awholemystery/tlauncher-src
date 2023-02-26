package org.apache.log4j.lf5.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogRecord;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/lf5/util/AdapterLogRecord.class */
public class AdapterLogRecord extends LogRecord {
    private static LogLevel severeLevel = null;
    private static StringWriter sw = new StringWriter();
    private static PrintWriter pw = new PrintWriter(sw);

    @Override // org.apache.log4j.lf5.LogRecord
    public void setCategory(String category) {
        super.setCategory(category);
        super.setLocation(getLocationInfo(category));
    }

    @Override // org.apache.log4j.lf5.LogRecord
    public boolean isSevereLevel() {
        if (severeLevel == null) {
            return false;
        }
        return severeLevel.equals(getLevel());
    }

    public static void setSevereLevel(LogLevel level) {
        severeLevel = level;
    }

    public static LogLevel getSevereLevel() {
        return severeLevel;
    }

    protected String getLocationInfo(String category) {
        String stackTrace = stackTraceToString(new Throwable());
        String line = parseLine(stackTrace, category);
        return line;
    }

    protected String stackTraceToString(Throwable t) {
        String s;
        synchronized (sw) {
            t.printStackTrace(pw);
            s = sw.toString();
            sw.getBuffer().setLength(0);
        }
        return s;
    }

    protected String parseLine(String trace, String category) {
        int index = trace.indexOf(category);
        if (index == -1) {
            return null;
        }
        String trace2 = trace.substring(index);
        return trace2.substring(0, trace2.indexOf(")") + 1);
    }
}
