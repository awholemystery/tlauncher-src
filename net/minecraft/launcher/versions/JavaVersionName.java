package net.minecraft.launcher.versions;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/JavaVersionName.class */
public class JavaVersionName {
    public static final JavaVersionName JAVA_8_LEGACY = new JavaVersionName("jre-legacy", Double.valueOf(8.0d));
    private String component;
    private Double majorVersion;

    public void setComponent(String component) {
        this.component = component;
    }

    public void setMajorVersion(Double majorVersion) {
        this.majorVersion = majorVersion;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JavaVersionName) {
            JavaVersionName other = (JavaVersionName) o;
            if (other.canEqual(this)) {
                Object this$component = getComponent();
                Object other$component = other.getComponent();
                if (this$component == null) {
                    if (other$component != null) {
                        return false;
                    }
                } else if (!this$component.equals(other$component)) {
                    return false;
                }
                Object this$majorVersion = getMajorVersion();
                Object other$majorVersion = other.getMajorVersion();
                return this$majorVersion == null ? other$majorVersion == null : this$majorVersion.equals(other$majorVersion);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JavaVersionName;
    }

    public int hashCode() {
        Object $component = getComponent();
        int result = (1 * 59) + ($component == null ? 43 : $component.hashCode());
        Object $majorVersion = getMajorVersion();
        return (result * 59) + ($majorVersion == null ? 43 : $majorVersion.hashCode());
    }

    public String toString() {
        return "JavaVersionName(component=" + getComponent() + ", majorVersion=" + getMajorVersion() + ")";
    }

    public JavaVersionName(String component, Double majorVersion) {
        this.component = component;
        this.majorVersion = majorVersion;
    }

    public JavaVersionName() {
    }

    public String getComponent() {
        return this.component;
    }

    public Double getMajorVersion() {
        return this.majorVersion;
    }
}
