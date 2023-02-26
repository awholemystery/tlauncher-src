package net.minecraft.launcher.versions;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/AbstractVersion.class */
public abstract class AbstractVersion implements Version {
    private String url;
    private boolean skinVersion;

    @Override // net.minecraft.launcher.versions.Version
    public String getUrl() {
        return this.url;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setUrl(String url) {
        this.url = url;
    }

    @Override // net.minecraft.launcher.versions.Version
    public boolean isSkinVersion() {
        return this.skinVersion;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setSkinVersion(boolean skinVersion) {
        this.skinVersion = skinVersion;
    }
}
