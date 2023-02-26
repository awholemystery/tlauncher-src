package org.tlauncher.tlauncher.ui.converter;

import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/VersionConverter.class */
public class VersionConverter extends LocalizableStringConverter<VersionSyncInfo> {
    private static final VersionSyncInfo LOADING = VersionSyncInfo.createEmpty();
    private static final VersionSyncInfo EMPTY = VersionSyncInfo.createEmpty();
    private final VersionManager vm;

    public VersionConverter(VersionManager vm) {
        super(null);
        if (vm == null) {
            throw new NullPointerException();
        }
        this.vm = vm;
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter, org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toString(VersionSyncInfo from) {
        if (from == null) {
            return null;
        }
        if (from.equals(LOADING)) {
            return Localizable.get("versions.loading");
        }
        if (from.equals(EMPTY)) {
            return Localizable.get("versions.notfound.tip");
        }
        String id = from.getID();
        ReleaseType type = from.getLatestVersion().getReleaseType();
        if (type == null || type.equals(ReleaseType.UNKNOWN)) {
            return id;
        }
        String typeF = type.toString().toLowerCase();
        String formatted = Localizable.get().nget("version." + typeF, id);
        if (formatted == null) {
            return id;
        }
        return formatted;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public VersionSyncInfo fromString(String from) {
        return this.vm.getVersionSyncInfo(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(VersionSyncInfo from) {
        return null;
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(VersionSyncInfo from) {
        return null;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<VersionSyncInfo> getObjectClass() {
        return VersionSyncInfo.class;
    }
}
