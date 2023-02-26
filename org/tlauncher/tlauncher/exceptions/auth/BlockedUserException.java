package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/BlockedUserException.class */
public class BlockedUserException extends AuthenticatorException {
    private String minutes;
    private static final long serialVersionUID = -8707094238355495569L;

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "BlockedUserException(minutes=" + getMinutes() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BlockedUserException) {
            BlockedUserException other = (BlockedUserException) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$minutes = getMinutes();
                Object other$minutes = other.getMinutes();
                return this$minutes == null ? other$minutes == null : this$minutes.equals(other$minutes);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BlockedUserException;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $minutes = getMinutes();
        return (result * 59) + ($minutes == null ? 43 : $minutes.hashCode());
    }

    public String getMinutes() {
        return this.minutes;
    }

    public BlockedUserException(String minutes, String cause) {
        super(cause, "user.blocked");
        this.minutes = minutes;
    }
}
