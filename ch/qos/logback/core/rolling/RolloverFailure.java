package ch.qos.logback.core.rolling;

import ch.qos.logback.core.LogbackException;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/rolling/RolloverFailure.class */
public class RolloverFailure extends LogbackException {
    private static final long serialVersionUID = -4407533730831239458L;

    public RolloverFailure(String msg) {
        super(msg);
    }

    public RolloverFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
