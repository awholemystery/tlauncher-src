package org.tlauncher.tlauncher.ui.login;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginWaitException.class */
public class LoginWaitException extends LoginException {
    private final LoginWaitTask waitTask;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginWaitException$LoginWaitTask.class */
    public interface LoginWaitTask {
        void runTask() throws LoginException;
    }

    public LoginWaitException(String reason, LoginWaitTask waitTask) {
        super(reason);
        if (waitTask == null) {
            throw new NullPointerException("wait task");
        }
        this.waitTask = waitTask;
    }

    public LoginWaitTask getWaitTask() {
        return this.waitTask;
    }
}
