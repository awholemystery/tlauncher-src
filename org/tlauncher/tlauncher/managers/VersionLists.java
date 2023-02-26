package org.tlauncher.tlauncher.managers;

import java.io.IOException;
import net.minecraft.launcher.updater.ExtraVersionList;
import net.minecraft.launcher.updater.LocalVersionList;
import net.minecraft.launcher.updater.OfficialVersionList;
import net.minecraft.launcher.updater.RemoteVersionList;
import net.minecraft.launcher.updater.SkinVersionList;
import org.tlauncher.tlauncher.component.LauncherComponent;
import org.tlauncher.util.MinecraftUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionLists.class */
public class VersionLists extends LauncherComponent {
    private final LocalVersionList localList;
    private final RemoteVersionList[] remoteLists;

    public VersionLists(ComponentManager manager) throws Exception {
        super(manager);
        this.localList = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
        OfficialVersionList officialList = new OfficialVersionList();
        ExtraVersionList extraList = new ExtraVersionList();
        SkinVersionList skinVersionList = new SkinVersionList();
        this.remoteLists = new RemoteVersionList[]{officialList, extraList, skinVersionList};
    }

    public LocalVersionList getLocal() {
        return this.localList;
    }

    public void updateLocal() throws IOException {
        this.localList.setBaseDirectory(MinecraftUtil.getWorkingDirectory());
    }

    public RemoteVersionList[] getRemoteLists() {
        return this.remoteLists;
    }
}
