package by.gdev.util.os;

import by.gdev.util.OSInfo;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/os/OSExecutorFactoryMethod.class */
public class OSExecutorFactoryMethod {
    private OSInfo.OSType osType = OSInfo.getOSType();

    public void setOsType(OSInfo.OSType osType) {
        this.osType = osType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof OSExecutorFactoryMethod) {
            OSExecutorFactoryMethod other = (OSExecutorFactoryMethod) o;
            if (other.canEqual(this)) {
                Object this$osType = getOsType();
                Object other$osType = other.getOsType();
                return this$osType == null ? other$osType == null : this$osType.equals(other$osType);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OSExecutorFactoryMethod;
    }

    public int hashCode() {
        Object $osType = getOsType();
        int result = (1 * 59) + ($osType == null ? 43 : $osType.hashCode());
        return result;
    }

    public String toString() {
        return "OSExecutorFactoryMethod(osType=" + getOsType() + ")";
    }

    public OSInfo.OSType getOsType() {
        return this.osType;
    }

    public OSExecutor createOsExecutor() {
        switch (this.osType) {
            case WINDOWS:
            default:
                return new WindowsExecutor();
            case LINUX:
                return new LinuxExecutor();
            case MACOSX:
                return new MacExecutor();
        }
    }
}
