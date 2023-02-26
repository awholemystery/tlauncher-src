package ch.qos.logback.core.util;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/util/IncompatibleClassException.class */
public class IncompatibleClassException extends Exception {
    private static final long serialVersionUID = -5823372159561159549L;
    Class<?> requestedClass;
    Class<?> obtainedClass;

    /* JADX INFO: Access modifiers changed from: package-private */
    public IncompatibleClassException(Class<?> requestedClass, Class<?> obtainedClass) {
        this.requestedClass = requestedClass;
        this.obtainedClass = obtainedClass;
    }
}
