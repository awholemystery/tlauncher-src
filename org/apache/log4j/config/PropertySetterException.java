package org.apache.log4j.config;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/config/PropertySetterException.class */
public class PropertySetterException extends Exception {
    private static final long serialVersionUID = -1352613734254235861L;
    protected Throwable rootCause;

    public PropertySetterException(String msg) {
        super(msg);
    }

    public PropertySetterException(Throwable rootCause) {
        this.rootCause = rootCause;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null && this.rootCause != null) {
            msg = this.rootCause.getMessage();
        }
        return msg;
    }
}
