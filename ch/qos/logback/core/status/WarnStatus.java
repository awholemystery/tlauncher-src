package ch.qos.logback.core.status;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/status/WarnStatus.class */
public class WarnStatus extends StatusBase {
    public WarnStatus(String msg, Object origin) {
        super(1, msg, origin);
    }

    public WarnStatus(String msg, Object origin, Throwable t) {
        super(1, msg, origin, t);
    }
}
