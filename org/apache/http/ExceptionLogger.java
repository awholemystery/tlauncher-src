package org.apache.http;

/* loaded from: TLauncher-2.876.jar:org/apache/http/ExceptionLogger.class */
public interface ExceptionLogger {
    public static final ExceptionLogger NO_OP = new ExceptionLogger() { // from class: org.apache.http.ExceptionLogger.1
        @Override // org.apache.http.ExceptionLogger
        public void log(Exception ex) {
        }
    };
    public static final ExceptionLogger STD_ERR = new ExceptionLogger() { // from class: org.apache.http.ExceptionLogger.2
        @Override // org.apache.http.ExceptionLogger
        public void log(Exception ex) {
            ex.printStackTrace();
        }
    };

    void log(Exception exc);
}
