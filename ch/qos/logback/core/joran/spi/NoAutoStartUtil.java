package ch.qos.logback.core.joran.spi;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/spi/NoAutoStartUtil.class */
public class NoAutoStartUtil {
    public static boolean notMarkedWithNoAutoStart(Object o) {
        if (o == null) {
            return false;
        }
        Class<?> clazz = o.getClass();
        NoAutoStart a = (NoAutoStart) clazz.getAnnotation(NoAutoStart.class);
        return a == null;
    }
}
