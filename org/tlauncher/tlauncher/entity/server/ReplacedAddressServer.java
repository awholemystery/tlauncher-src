package org.tlauncher.tlauncher.entity.server;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/server/ReplacedAddressServer.class */
public class ReplacedAddressServer {
    private String oldAddress;
    private String newAddress;

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ReplacedAddressServer) {
            ReplacedAddressServer other = (ReplacedAddressServer) o;
            if (other.canEqual(this)) {
                Object this$oldAddress = getOldAddress();
                Object other$oldAddress = other.getOldAddress();
                if (this$oldAddress == null) {
                    if (other$oldAddress != null) {
                        return false;
                    }
                } else if (!this$oldAddress.equals(other$oldAddress)) {
                    return false;
                }
                Object this$newAddress = getNewAddress();
                Object other$newAddress = other.getNewAddress();
                return this$newAddress == null ? other$newAddress == null : this$newAddress.equals(other$newAddress);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ReplacedAddressServer;
    }

    public int hashCode() {
        Object $oldAddress = getOldAddress();
        int result = (1 * 59) + ($oldAddress == null ? 43 : $oldAddress.hashCode());
        Object $newAddress = getNewAddress();
        return (result * 59) + ($newAddress == null ? 43 : $newAddress.hashCode());
    }

    public String toString() {
        return "ReplacedAddressServer(oldAddress=" + getOldAddress() + ", newAddress=" + getNewAddress() + ")";
    }

    public String getOldAddress() {
        return this.oldAddress;
    }

    public String getNewAddress() {
        return this.newAddress;
    }
}
