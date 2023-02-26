package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/UserMigratedException.class */
public class UserMigratedException extends AuthenticatorException {
    private static final long serialVersionUID = 7441756035466353515L;

    public UserMigratedException() {
        super("This user has migrated", "migrated");
    }
}
